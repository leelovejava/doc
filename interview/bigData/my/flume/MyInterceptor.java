package com.atguigu.gmall0808.dw.fi;

import com.google.gson.Gson;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
  * flume 自定义拦截器
 * ETL拦截器和区分类型拦截器
 * 优点，模块化开发和可移植性
 * 缺点，性能会低一些
  */
public class MyInterceptor implements Interceptor {
    private Gson gson =null;

    /**
     * 初始化
     */
    @Override
    public void initialize() {
        gson = new Gson();
    }

    /**
     * 处理单个Event
     * @param event
     * @return
     */
    @Override
    public Event intercept(Event event) {
        // 得到event中的log , 取出日志类型 ，放到header中
        String logString = new String(event.getBody());
        HashMap logMap = gson.fromJson(logString, HashMap.class);
        String logType =(String) logMap.get("type");
        Map<String, String> headers = event.getHeaders();
        headers.put("logType",logType);
        return event;
    }

    /**
     * 处理多个Event，在这个方法中调用Event intercept(Event event)
     * @param events
     * @return
     */
    @Override
    public List<Event> intercept(List<Event> events) {
        for (Event event : events) {
            intercept(  event);
        }

        return events;
    }

    @Override
    public void close() {

    }

    /**
     * 静态内部类
     */
    public static class Builder implements Interceptor.Builder {
        /**
         * 该方法主要用来返回创建的自定义类拦截器对象
         * @return
         */
        @Override
        public Interceptor build() {
            return new MyInterceptor();
        }

        @Override
        public void configure(Context context) {
            //可以通过context得到 flume.conf中设置的参数,传递给Interceptor
        }
    }


}
