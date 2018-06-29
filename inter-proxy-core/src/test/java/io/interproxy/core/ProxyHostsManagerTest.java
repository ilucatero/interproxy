package io.interproxy.core;

import io.interproxy.core.exceptions.InterProxyException;
import io.interproxy.core.hostproxyloaders.XmlHostProxyLoader;
import org.apache.http.HttpHost;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyHostsManagerTest {
    private static final Logger LOG = LoggerFactory.getLogger(ProxyHostsManagerTest.class);

    @Test
    public void testProxyHosts() throws InterProxyException {
        ProxyHostsManager proxyHostsMap = ProxyHostsManager.getInstance()
                .addProxyLoader(new XmlHostProxyLoader("xml/proxiesListTest.xml"))
                .loadHosts();
        do{
            HttpHost httpHost = proxyHostsMap.getNextAvailableHttpHost();
            LOG.info(httpHost.toString());
            Assert.assertNotNull(httpHost);
            Assert.assertNotEquals("", httpHost.getHostName());
            Assert.assertNotEquals("", httpHost.getPort());
            Assert.assertNotEquals("", httpHost.getSchemeName());
        }while(!proxyHostsMap.isLast());
    }

    @Test //(expected = InterProxyException.class)
    public void testProxyHostsWithNoLoader() throws InterProxyException {
        ProxyHostsManager proxyHostsMap = ProxyHostsManager.getInstance()
                .addProxyLoader(new XmlHostProxyLoader())
                .loadHosts();

        HttpHost httpHost = proxyHostsMap.getNextAvailableHttpHost();
        Assert.assertNull(httpHost);
        LOG.info("As expected, no hosts where loaded!");
    }
}
