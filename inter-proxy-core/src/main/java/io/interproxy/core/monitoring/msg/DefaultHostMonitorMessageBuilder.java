package io.interproxy.core.monitoring.msg;

import org.apache.http.HttpHost;

public class DefaultHostMonitorMessageBuilder  {

    /**
     * Using the parameters create a new message to be process by the MonitorManager.
     * @param host
     * @param status
     * @param message
     * @return {@link DefaultHostMonitorMessage}
     */
    public static HostMonitorMessage build(HttpHost host, Integer status, String message){
        DefaultHostMonitorMessage defaultHostMonitorMessage = new DefaultHostMonitorMessage();
        defaultHostMonitorMessage.host = host.getHostName();
        defaultHostMonitorMessage.status = status;
        defaultHostMonitorMessage.message = message;
        return defaultHostMonitorMessage;
    }

}
