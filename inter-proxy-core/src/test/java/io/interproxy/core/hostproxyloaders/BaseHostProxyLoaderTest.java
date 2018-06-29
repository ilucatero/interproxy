package io.interproxy.core.hostproxyloaders;

import io.interproxy.core.exceptions.InterProxyException;
import org.apache.http.HttpHost;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BaseHostProxyLoaderTest {
    private static final Logger LOG = LoggerFactory.getLogger(BaseHostProxyLoaderTest.class);

    protected void checkValues( HostProxyLoader hostProxyLoader ) throws InterProxyException  {
        List<HttpHost> httpHosts = hostProxyLoader.loadHosts();
        Assert.assertNotNull(httpHosts);
        LOG.info("Total Host loaded :{}", httpHosts.size());
        HttpHost hH = httpHosts.get(0);
        Assert.assertNotEquals("", hH.getHostName());
        Assert.assertNotEquals("", hH.getPort());
        Assert.assertNotEquals("", hH.getSchemeName());
    }


}