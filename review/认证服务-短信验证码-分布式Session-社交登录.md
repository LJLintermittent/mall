### 短信验证码注册业务逻辑

首先注册的请求统一打到一个独立的微服务，一般都会有这种独立的微服务做专门的认证中心

前端在注册页面填入手机号以后，点击发送验证码，会给后端服务发来请求，这个请求带的参数就是手机号，根据不同的手机号区分不同的注册用户，认证服务首先做参数校验，校验手机号是否符合手机号的正则以及常规的非空校验，后端校验的主要目的就是防止postman这种可以绕过前端校验的接口请求工具。认证服务校验完毕后还会先从redis中查出来这个key，这个key的名字可以使用一个规定好的redis前缀，比如sms:code:后面在跟个手机号，那么在服务中直接get获取就ok了，然后校验是否在一分钟内发送过，如果发送过直接抛出一个异常，这个校验一分钟的逻辑也是后端校验，前端一般也会做短信防刷，比如发送完以后按钮变灰等。

认证服务拿到电话号已经随机生成一个六位的数字作为验证码，发送给第三方服务，另外往redis存的时候一定要记录当前时间，用来在获取的时候做防刷校验。

接着第三方服务就调用阿里云的短信验证码服务接口发送就行了

这时候用户的手机就可以收到短信验证码了，然后填入表单封装成注册信息，发送给注册的controller

注意，观察其他系统的实现，一个验证码的有效期一般都在十分钟左右，但是前端发完以后一般会有一个一分钟左右的倒计时，表示这个验证码可以在一分钟后重新发送，但是验证码的有效期是是十分钟，并不是说一分钟之内没有填入就过期了，这个业务点要注意

思路就是redis存的code要设置十分钟的过期时间，但是一分钟内这个防刷校验需要通过获取当前系统时间减去生成时候获取的系统时间，看是否小于一分钟，如果小于一分钟，抛异常，不发送短信

上面是发送短信验证码的逻辑，接下来梳理用户的整体注册逻辑

1.首先从redis中取出验证码， 与用户传进来的验证码进行对比，如果比对成功了，直接将验证码从redis中删除掉

2.前面还需要对用户输入的注册数据进行JSR303校验，当用户名密码验证码等信息全部校验通过以后，接下来认证服务就要调用用户服务去注册写库了

3.写库之前还有一步非常重要的就是判断当前注册的用户名和电话号码是否已经存在了，如果已经存在，抛出相应的异常，前端接收，做一个友好的提示，让用户重新发送请求来注册，这块也能看到验证码设置有效期长一点的原因，因为用户很有可能验证码输的没问题，但是在用户名这块会卡很久，因为有可能会重复

另外写库的时候，密码一定不能用明文，本项目采用的是spring提供的BCryptPasswordEncoder.encode()方法进行加密。

它的原理是sha-256+随机盐+密钥的方式对密码进行加密，sha系列是hash算法，不是加密算法，使用加密算法意味着不管多复杂的密码都能解密，而哈希算法是不可逆的。注册的时候根据用户输入的密码，进行hash处理，得到密码的hash值，然后将其存入数据库，登录的时候将用户输入的密码进行相同的hash运算，得到hash值，与数据库中的进行比对，如果一样，那么密码正确

登录的逻辑就比较简单了，还是拿BCryptPasswordEncoder.matchs()进行密码匹配，拿用户名去查库，如果数据都查不到，那就证明没有这个用户，应该先去注册，查到数据以后才开始进行密码匹配

### 社交登录

OAuth2.0是一个开放标准，允许用户授权第三方应用访问他们存储在另一个服务提供者里面的数据，而不需要提供用户名或密码给当前应用，也就是说用户使用我的电商系统，可以不注册，直接使用社交登录等那些流量大的平台，流量大的平台意味着已经注册过的几率大嘛，在当前系统中就不需要再来进行一次比较麻烦的注册逻辑

社交登录的整体逻辑如下：

1.用户在当前系统，比如我的电商系统中，点击微博登录，那么会跳转到微博的认证页面，用户输入自己的账号和密码，相当于让微博给电商授权，输入完以后这个请求会发送给微博的服务器，微博的服务器认证成功以后会返回一个访问令牌，然后电商服务拿到令牌以后请求微博服务器获取开放的信息，微博认证了令牌以后返回你需要的信息，或者叫我可以给你的信息，安全的信息，然后就相当于电商系统拿到了你在微博中的昵称，头像等信息，可以在电商系统中做一个展示

2.如果用户是第一次使用社交登录，那么认证成功以后需要将这些信息注册在电商服务中，让电商系统的用户数据库中存在记录，以后如果用户再来使用社交登录，就不需要入库了，每次拿着这个账号买东西，也是对应的这个用户账号数据

其实准确点来说微博这块是先给你一个code码，你拿着这个code去换取一个access token，然后再拿着access token去调用微博的API，获取用户头像昵称等信息

### 分布式Session与Cookie

session存在服务端，jsessionId存在客户端，每次通过jsessionId取出保存的数据

1.浏览器第一次访问服务器（进行登录）

2.登录成功，保存用户信息到Session中

3.命令浏览器保存一个jsessionId=123的cookie

4.以后再次访问的时候，会带上这个cookie，里面保存有jsessionId

Session使用的第一个问题就是不能跨服务，对于项目来说就是跨域名

Session不仅要在不同的服务之间共享，并且也要在同服务的不同机器中，也就是服务集群模式下做共享

首先了解一下分布式Session共享的解决方案，可以直接复制，但是很不好，任意一台服务器都存储了其他所有服务器的session数据，并且复制会产生大量的网络io消耗

然后是一致性哈希，让某个用户请求只会哈希到一台固定的机器，但是扩容缩容还是会有影响，并且session依然在服务器中保存着，服务器重启就没了

那么最终采用redis中间件作为session的载体，这样业务机器重启，session还在，也可以很方便的扩容缩容

那么使用了redis以后确实解决了不同的项目存储session的问题，接下来还有一个点就是cookie的作用域问题，domian，需要使用CookieSerializer将session的作用域扩大至*.mall.com

只有一个jsessionid就可以知道这个用户登没登陆，但是还是要往session中存放user信息，为了其他服务可以获取到这个用户信息然后去查库做相应的业务逻辑

### 登录拦截器问题

有些服务必须要登录以后才可以使用的，比如订单服务等

那么在调用订单服务的接口之前（当然有些接口也不用拦截，那么可以先获取uri，进行匹配放行），进行登录拦截

~~~java
@Component
public class LoginUserInterceptor implements HandlerInterceptor {

    public static ThreadLocal<MemberRespVo> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 库存服务远程调用这个订单服务的接口，会出现登录拦截问题
         */
        String uri = request.getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean match1 = antPathMatcher.match("/order/order/status/**", uri);
        boolean match2 = antPathMatcher.match("/payed/notify", uri);
        if (match1 || match2) {
            return true;
        }
        MemberRespVo memberRespVo = (MemberRespVo) request.getSession().getAttribute(AuthConstant.LOGIN_USER);
        if (memberRespVo != null) {
            threadLocal.set(memberRespVo);
            return true;
        } else {
            //没登录就去登录
            request.getSession().setAttribute("msg", "请先进行登录");
            response.sendRedirect("http://auth.mall.com/auth/login.html");
            return false;
        }
    }
}
~~~

往容器里面放一个拦截器，重写preHandle方法，在这个方法里面可以使用antpathmatcher来对一些不需要登录的请求做一个放行，需要登录的请求那么先从redis中获取session，然后获取它的属性，也就是用户的登录信息，如果确实登录过，那么肯定可以从redis中拿到登录信息，然后这里这里放到threadlocal中，做一个线程内的共享与数据传递，后面的业务中如果需要用户信息，从threadlocal中拿就行了，就不需要每次都请求redis中的session来拿用户信息，如果获取不到session，证明没有登录，那么重定向到登录页面去登录

