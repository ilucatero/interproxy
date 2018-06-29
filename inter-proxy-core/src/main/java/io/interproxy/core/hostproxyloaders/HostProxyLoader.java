package io.interproxy.core.hostproxyloaders;

import io.interproxy.core.exceptions.InterProxyException;
import org.apache.http.HttpHost;

import java.util.List;

public interface HostProxyLoader {

    /**
     * Load all the hosts items found in XML file passed on the configuration
     * @return a list of {@link HttpHost}
     */
    List<HttpHost> loadHosts() throws InterProxyException;
}
