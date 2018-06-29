package io.interproxy.core.hostproxyloaders;

import io.interproxy.core.binding.Proxies;
import io.interproxy.core.binding.ProxyHost;
import io.interproxy.core.exceptions.InterProxyException;
import org.apache.http.HttpHost;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WebSiteFreeProxyListHostProxyLoader implements HostProxyLoaderScheduler {
    private static final Logger LOG = LoggerFactory.getLogger(WebSiteFreeProxyListHostProxyLoader.class);

    private String freeProxyListUrl = "https://free-proxy-list.net/";
    private String htmlFileName;

    public WebSiteFreeProxyListHostProxyLoader() {}
    public WebSiteFreeProxyListHostProxyLoader(String htmlFileName) {
        this.htmlFileName = htmlFileName;
    }

    /**
     * Load all the hosts items found in XML file passed on the configuration
     * @return {@link List<HttpHost>}
     */
    @Override
    public List<HttpHost> loadHosts() throws InterProxyException {
        LOG.debug("Loading proxies from "+freeProxyListUrl);

        List<HttpHost> hosts = new ArrayList<>();
        try {

            Document document = loadDocument();

            Elements odd = document.select("tbody").first().select("tr");

            for (Element line : odd) {
                Elements hostElements = line.getAllElements();
                if (hostElements.get(0).getElementById("th") == null) {
                    String hostname = hostElements.get(1).text();
                    Integer port = Integer.valueOf(hostElements.get(2).text());

                    HttpHost host = new HttpHost(hostname, port, HttpHost.DEFAULT_SCHEME_NAME);
                    LOG.debug("Adding proxy host:{}", host.toHostString());
                    hosts.add(host);
                }
            }

        } catch (Exception e){
            throw new InterProxyException("The file "+freeProxyListUrl +" couldn't be loaded."+ e.getMessage());
        }

        return hosts;
    }

    private Document loadDocument() throws IOException {
        Document document;
        if(htmlFileName == null) {
            document = Jsoup.connect(freeProxyListUrl).get();
        }else {
            InputStream is = getClass().getClassLoader().getResourceAsStream(htmlFileName);
            document = Jsoup.parse(is, Charset.defaultCharset().name(), "");
        }
        return document;
    }

    @Override
    public Long getTime() {
        return 60L;
    }

    @Override
    public TimeUnit getTimeUnit() {
        return TimeUnit.MINUTES;
    }
}
