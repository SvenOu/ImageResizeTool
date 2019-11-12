package com.sv.image.tool.manager;
import java.util.HashMap;
import java.util.Map;

public class ServiceManager {
    private static ServiceManager instance;
    public static ServiceManager getInstance() {
        if(null == instance){
            instance = new ServiceManager();
        }
        return instance;
    }

    private Map<String, Service> serviceMap = new HashMap<>();

    /**
     * service 单例管理器
     */
    public <T> T getService(Class<T> cls){
        String key = cls.getName();
        if(serviceMap.containsKey(key)){
            return (T) serviceMap.get(key);
        }else {
            T t = null;
            try {
                t = cls.newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            if(!(t instanceof Service)){
                throw  new RuntimeException("cls: " + cls + " 没有继承Service， 类型错误 ");
            }
            serviceMap.put(key, (Service) t);
            return t;
        }
    }

    public void remove(Class cls){
        serviceMap.remove(cls.getName());
    }

    public void clear(Class cls){
        serviceMap.clear();
    }
}
