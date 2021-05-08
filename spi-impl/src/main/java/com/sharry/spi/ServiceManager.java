package com.sharry.spi;

import com.sun.istack.internal.NotNull;

import java.util.concurrent.ConcurrentHashMap;

/**
 * SPI(Service Provider Interface) 服务提供入口类
 *
 * @author zhuxiaoyu <a href="zhuxiaoyu.sharry@bytedance.com">Contact me.</a>
 * @version 1.0
 * @since 4/27/21
 */
public class ServiceManager {

    private static final ConcurrentHashMap<Class<?>, ServiceSpec<?>> sServicesCache = new ConcurrentHashMap<>();
    public static boolean sIsStrictMode = false;

    static {
        loadSpiMap();
    }

    public static void setStrictMode(boolean enable) {
        sIsStrictMode = enable;
    }

    @SuppressWarnings({"unchecked", "SynchronizationOnLocalVariableOrMethodParameter"})
    public static <T> T getService(@NotNull final Class<T> clazz) {
        if (null == clazz) {
            throw new RuntimeException("ServiceManager.getService: Null");
        }
        // 获取服务信息
        ServiceSpec<T> serviceSpec = (ServiceSpec<T>) sServicesCache.get(clazz);
        if (null == serviceSpec) {
            if (sIsStrictMode) {
                throw new RuntimeException("Do not found implClass " + clazz);
            }
            return null;
        }

        // 若为单例服务, 并且存在实例, 则直接返回
        if (serviceSpec.singleton && null != serviceSpec.instance) {
            return serviceSpec.instance;
        }

        // 非单例模式，或者没有服务实例
        synchronized (clazz) {
            if (serviceSpec.singleton && null != serviceSpec.instance) {
                return serviceSpec.instance;
            }
            try {
                T res = (T) serviceSpec.implClazz.newInstance();
                if (serviceSpec.singleton) {
                    serviceSpec.instance = res;
                }
                return res;
            } catch (Throwable e) {
                if (sIsStrictMode) {
                    e.printStackTrace();
                    throw new RuntimeException("Do not found implClass " + clazz);
                }
                return null;
            }
        }
    }

    private static void loadSpiMap() {
        // AMS start
        // ......
        // register(...)
        // register(...)
        // AMS end
    }

    public static <T> void register(
            @NotNull Class<? extends T> serviceClass,
            @NotNull Class<? extends T> implClazz,
            boolean delay,
            boolean singleton
    ) {
        ServiceSpec<?> spec = new ServiceSpec<>();
        spec.implClazz = implClazz;
        spec.singleton = singleton;
        spec.delayInstance = delay;
        spec.instance = null;
        sServicesCache.put(serviceClass, spec);
    }

}
