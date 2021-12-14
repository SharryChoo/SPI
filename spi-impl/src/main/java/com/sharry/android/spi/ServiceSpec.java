package com.sharry.android.spi;

/**
 * 服务的属性
 */
public class ServiceSpec<T> {

    /**
     * Service class.
     */
    public Class<?> implClazz;

    /**
     * is singleton
     */
    public boolean singleton;

    /**
     * if true is instantiate when invoke.
     */
    public boolean delayInstance;

    /**
     * if is singleton, will hold instance.
     */
    public T instance = null;

}
