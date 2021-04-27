package com.sharry.spi

/**
 * 拓展方法, 快捷获取服务对象
 */
fun <T> Class<T>.getService(): T? {
    return ServiceManager.getService(this)
}