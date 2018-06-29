package io.interproxy.core.hostproxyloaders;

import io.interproxy.core.exceptions.InterProxyException;
import org.apache.http.HttpHost;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class WebSiteFreeProxyListHostProxyLoaderTest extends BaseHostProxyLoaderTest {
    private static final Logger LOG = LoggerFactory.getLogger(WebSiteFreeProxyListHostProxyLoaderTest.class);

    private String fileName = "xml/FreeProxyList.html";


    @Test
    @Ignore
    public void testLoadHostsWithDefaultConstructor() throws InterProxyException {
        // test usign inner url : "https://free-proxy-list.net/";
        WebSiteFreeProxyListHostProxyLoader xmlHostProxyLoader = new WebSiteFreeProxyListHostProxyLoader();

        checkValues(xmlHostProxyLoader);
    }

    @Test //(expected = InterProxyException.class)
    public void testLoadHostsWithHtmlFile() throws InterProxyException {
        WebSiteFreeProxyListHostProxyLoader xmlHostProxyLoader = new WebSiteFreeProxyListHostProxyLoader(fileName);

        checkValues(xmlHostProxyLoader);
    }

}