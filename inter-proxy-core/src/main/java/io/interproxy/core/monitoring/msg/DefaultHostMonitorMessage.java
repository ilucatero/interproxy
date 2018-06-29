package io.interproxy.core.monitoring.msg;

public class DefaultHostMonitorMessage implements HostMonitorMessage{
    public String host;
    public Integer status;
    public String message;

    @Override
    public String toString(){
        return "{DefaultHostMonitorMessage: {"
                + " Host: "+host
                + ", Status:"+status
                + ", Msg:" + message
                + "}}";
    }

}
