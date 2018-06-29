package io.interproxy.core.binding;

import org.apache.http.HttpHost;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement( name = "ProxyHost" )
public class ProxyHost {

    @XmlElement
    public String hostname;
    @XmlElement
    public Integer port;
    @XmlElement
    public String scheme = HttpHost.DEFAULT_SCHEME_NAME;

    @Override
    public String toString(){
        return "ProxyHost:{"+
                    "hostname:"+hostname + ",port:"+port+ ",scheme:"+scheme
                + "}";
    }

    public boolean isNotEmpty() {
        return !((hostname == null || "".equals(hostname)) && (port == null || "".equals(port)));
    }

    @XmlTransient // to avoid conflicts with getters from xmlElement
    public Integer getPort(){
        return 80;
    }
}
