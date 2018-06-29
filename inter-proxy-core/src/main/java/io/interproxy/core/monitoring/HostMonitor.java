package io.interproxy.core.monitoring;

import io.interproxy.core.monitoring.msg.DefaultHostMonitorMessage;
import io.interproxy.core.monitoring.msg.DefaultHostMonitorMessageBuilder;
import io.interproxy.core.monitoring.msg.HostMonitorMessage;

public interface HostMonitor {

    /**
     * Defines the monitoring message builder to use. <br/>
     * Any implementation should extends DefaultHostMonitorMessageBuilder class.
     *
     * @return {@link DefaultHostMonitorMessageBuilder}
     */
    DefaultHostMonitorMessageBuilder getHostMonitorMessageBuilder();

    /**
     * Do run the monitoring notification
     * @param defHostMonitorMessageBuilder
     * @return  the sent status
     */
    int sendHostStatus(HostMonitorMessage defHostMonitorMessageBuilder);

}
