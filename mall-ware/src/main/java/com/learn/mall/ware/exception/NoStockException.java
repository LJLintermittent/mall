package com.learn.mall.ware.exception;

/**
 * Description:
 * date: 2021/5/17 19:50
 * Package: com.learn.mall.ware.exception
 *
 * @author 李佳乐
 * @version 1.0
 */
public class NoStockException extends RuntimeException {

    private Long skuId;

    public NoStockException(Long skuId) {
        super("商品ID：" + skuId + ";没有足够的库存了");
    }

    public NoStockException(String message){
        super(message);
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }
}
