package io.interproxy.core.hostproxyloaders;

import io.interproxy.core.binding.Proxies;
import io.interproxy.core.binding.ProxyHost;
import io.interproxy.core.exceptions.InterProxyException;
import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlHostProxyLoader implements HostProxyLoader {
    private static final Logger LOG = LoggerFactory.getLogger(XmlHostProxyLoader.class);

    private String xmlFileName;

    public XmlHostProxyLoader() {}
    public XmlHostProxyLoader(String xmlFileName) {
        this.xmlFileName = xmlFileName;
    }

    /**
     * Load all the hosts items found in XML file passed on the configuration
     * @return {@link List<HttpHost>}
     */
    @Override
    public List<HttpHost> loadHosts() throws InterProxyException {
        LOG.debug("Loading proxies from "+xmlFileName);

        List<HttpHost> hosts = new ArrayList<>();
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(xmlFileName);
            JAXBContext jaxbContext = JAXBContext.newInstance(Proxies.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Object unmarshalledObj = jaxbUnmarshaller.unmarshal(is);

            Proxies proxyHosts = (Proxies) unmarshalledObj;
            for(ProxyHost pHost : proxyHosts.proxies){
                LOG.debug("Adding host "+pHost.hostname);
                hosts.add(new HttpHost(pHost.hostname, pHost.port, pHost.scheme));
            }

        } catch (Exception e){
            throw new InterProxyException("The file "+xmlFileName +" couldn't be loaded."+ e.getMessage());
        }

        return hosts;
    }

    public String getXmlFileName() {
        return xmlFileName;
    }

    public void setXmlFileName(String xmlFileName) {
        this.xmlFileName = xmlFileName;
    }
}
