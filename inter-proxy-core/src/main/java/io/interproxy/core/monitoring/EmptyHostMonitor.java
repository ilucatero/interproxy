package io.interproxy.core.monitoring;

import io.interproxy.core.monitoring.msg.DefaultHostMonitorMessage;
import io.interproxy.core.monitoring.msg.DefaultHostMonitorMessageBuilder;
import io.interproxy.core.monitoring.msg.HostMonitorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmptyHostMonitor implements HostMonitor{
    private static final Logger LOG = LoggerFactory.getLogger(EmptyHostMonitor.class);

    protected DefaultHostMonitorMessageBuilder defaultHostMonitorMessageBuilder = new DefaultHostMonitorMessageBuilder();

    @Override
    public int sendHostStatus(HostMonitorMessage hostMonitorMessage){
        DefaultHostMonitorMessage defaultHostMonitorMessage = (DefaultHostMonitorMessage) hostMonitorMessage;
        LOG.info("Host:{} / Status:{} / Msg:{}", defaultHostMonitorMessage.host, defaultHostMonitorMessage.status, defaultHostMonitorMessage.message);
        return 0;
    }

    @Override
    public DefaultHostMonitorMessageBuilder getHostMonitorMessageBuilder() {
        return defaultHostMonitorMessageBuilder;
    }

}
