package io.interproxy.core.hostproxyloaders;

import io.interproxy.core.exceptions.InterProxyException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class XmlHostProxyLoaderTest extends BaseHostProxyLoaderTest {
    private static final Logger LOG = LoggerFactory.getLogger(XmlHostProxyLoaderTest.class);

    private String fileName = "xml/proxiesListTest.xml";

    @Test
    public void testLoadHosts() throws InterProxyException  {
        XmlHostProxyLoader xmlHostProxyLoader = new XmlHostProxyLoader(fileName);

        checkValues(xmlHostProxyLoader);
    }

    @Test
    public void testLoadHostsWithDefaultConstructor() throws InterProxyException {
        XmlHostProxyLoader xmlHostProxyLoader = new XmlHostProxyLoader();
        xmlHostProxyLoader.setXmlFileName(fileName);

        checkValues(xmlHostProxyLoader);
    }

    @Test(expected = InterProxyException.class)
    public void testLoadHostsWithNoFile() throws InterProxyException {
        XmlHostProxyLoader xmlHostProxyLoader = new XmlHostProxyLoader();
        checkValues(xmlHostProxyLoader);
    }

}