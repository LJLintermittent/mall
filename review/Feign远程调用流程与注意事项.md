### Feign远程调用原理

远程调用的步骤：

1.基于面向接口的动态代理方式生成实现类

2.基于RequestBean，动态生成Request

3.使用encoder将bean转换成Http报文

4.拦截器负责对请求和响应进行装饰处理

优化点：

1.GZIP压缩

2.替换为httpclient客户端，使用http连接池提高性能

feign是一个http请求调用的框架，可以以java接口和注解的方式进行http调用，并且springcloud为feign引入了ribbon实现客户端的负载均衡

Feign的调用逻辑源码：

ReflectiveFeign

~~~java 
  @Override
  public <T> T newInstance(Target<T> target) {
    Map<String, MethodHandler> nameToHandler = targetToHandlersByName.apply(target);
    Map<Method, MethodHandler> methodToHandler = new LinkedHashMap<Method, MethodHandler>();
    List<DefaultMethodHandler> defaultMethodHandlers = new LinkedList<DefaultMethodHandler>();

    for (Method method : target.type().getMethods()) {
      if (method.getDeclaringClass() == Object.class) {
        continue;
      } else if (Util.isDefault(method)) {
        DefaultMethodHandler handler = new DefaultMethodHandler(method);
        defaultMethodHandlers.add(handler);
        methodToHandler.put(method, handler);
      } else {
        methodToHandler.put(method, nameToHandler.get(Feign.configKey(target.type(), method)));
      }
    }
    InvocationHandler handler = factory.create(target, methodToHandler);
    T proxy = (T) Proxy.newProxyInstance(target.type().getClassLoader(),
        new Class<?>[] {target.type()}, handler);

    for (DefaultMethodHandler defaultMethodHandler : defaultMethodHandlers) {
      defaultMethodHandler.bindTo(proxy);
    }
    return proxy;
  }
~~~

可以看到通过jdk动态代理生成feignclient接口的代理类

那么执行代理对象的方法时实际上执行的是InvocationHandler接口的实现类的invoke方法

~~~java
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      if ("equals".equals(method.getName())) {
        try {
          Object otherHandler =
              args.length > 0 && args[0] != null ? Proxy.getInvocationHandler(args[0]) : null;
          return equals(otherHandler);
        } catch (IllegalArgumentException e) {
          return false;
        }
      } else if ("hashCode".equals(method.getName())) {
        return hashCode();
      } else if ("toString".equals(method.getName())) {
        return toString();
      }

      return dispatch.get(method).invoke(args);
    }
~~~

上面是FeignInvocationHandler类中的invoke方法，FeignInvocationHandler是一个实现了InvocationHandler接口的内部类，是ReflectiveFeign的一个内部类，他肯定实现了invoke方法，就是上面的代码，可以看到如果是equals，hashcode和tostring等这些方法，直接调用这些方法就ok了，如果不是，通过dispatch.get(method).invoke(args);来进行一个转发

~~~java
  interface MethodHandler {

    Object invoke(Object[] argv) throws Throwable;
  }
~~~

这个方法是MethodHandler接口中定义的方法，然后看它的实现类SynchronousMethodHandler

~~~java
 @Override
  public Object invoke(Object[] argv) throws Throwable {
    RequestTemplate template = buildTemplateFromArgs.create(argv);
    Retryer retryer = this.retryer.clone();
    while (true) {
      try {
        return executeAndDecode(template);
      } catch (RetryableException e) {
        try {
          retryer.continueOrPropagate(e);
        } catch (RetryableException th) {
          Throwable cause = th.getCause();
          if (propagationPolicy == UNWRAP && cause != null) {
            throw cause;
          } else {
            throw th;
          }
        }
        if (logLevel != Logger.Level.NONE) {
          logger.logRetry(metadata.configKey(), logLevel);
        }
        continue;
      }
    }
  }

~~~

这就是真正调用的方法，可以看到先构建模板请求，然后拿到一个重试器，后面如果调用出现异常了会进行重试

executeAndDecode(template)是真正执行模板请求的方法

~~~java
 Object executeAndDecode(RequestTemplate template) throws Throwable {
    Request request = targetRequest(template);

    if (logLevel != Logger.Level.NONE) {
      logger.logRequest(metadata.configKey(), logLevel, request);
    }

    Response response;
    long start = System.nanoTime();
    try {
      response = client.execute(request, options);
    } catch (IOException e) {
      if (logLevel != Logger.Level.NONE) {
        logger.logIOException(metadata.configKey(), logLevel, e, elapsedTime(start));
      }
      throw errorExecuting(request, e);
    }
    long elapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
     
         。
         。
         。
         
~~~

这个方法就是执行请求并解码相应，在执行请求之前，有一个方法targetRequest（template）

~~~java
  Request targetRequest(RequestTemplate template) {
    for (RequestInterceptor interceptor : requestInterceptors) {
      interceptor.apply(template);
    }
    return target.apply(template);
  }
~~~

可以看到它会遍历所有的他自己的一个拦截器接口requestInterceptors

然后执行拦截方法apply，那么我们就可以自己往容器中放一个RequestInterceptor，重写 apply方法，将请求头等信息设置进去来解决feign远程调用丢失请求头的问题