package io.interproxy.core.binding;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement( name = "Proxies" )
public class Proxies {

    @XmlElement( name = "ProxyHost" )
    public List<ProxyHost> proxies;

}
