package io.interproxy.core.hostproxyloaders;

import io.interproxy.core.exceptions.InterProxyException;
import org.apache.http.HttpHost;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface HostProxyLoaderScheduler extends HostProxyLoader {

    /**
     * Get the time to be scheduled
     * @return
     */
    Long getTime();

    /**
     * Get the time unit to use for the {@link #getTime()} method.
     * @return
     */
    TimeUnit getTimeUnit();
}
