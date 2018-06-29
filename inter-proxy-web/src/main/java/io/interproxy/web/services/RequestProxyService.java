/**
 * ====================================================================
 *     Copyright (C) 2018  Ignacio LUCATERO
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * ====================================================================
 */
package io.interproxy.web.services;

import io.interproxy.core.RequestProxyExecutor;
import io.interproxy.core.exceptions.InterProxyException;
import io.interproxy.core.hostproxyloaders.HostProxyLoader;
import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public final class RequestProxyService {
    //private static final Logger LOG = LoggerFactory.getLogger(RequestProxyService.class);

    private RequestProxyExecutor requestProxyExecutor ;

    private List<HostProxyLoader> proxyLoaders = new ArrayList<>(1);

    public void setProxyLoaders(List<HostProxyLoader> proxyLoaders) {
        this.proxyLoaders = proxyLoaders;
    }

    /**
     * Once the class attributes has been set ({@link HostProxyLoader}s defined in the
     * Spring configuration <i>spring-bean-conf.xml</i> file or {@link io.interproxy.web.config.AppConfig})
     * it creates a new instance of the {@link RequestProxyExecutor} which will run itself the loaders.
     * @throws InterProxyException
     */
    public void init() throws InterProxyException {
        requestProxyExecutor = new RequestProxyExecutor(proxyLoaders);
    }

    /**
     * @see RequestProxyExecutor#fetch(String, HttpHost, HttpServletResponse)
     */
    public int fetch(final String url, HttpHost route, final HttpServletResponse sResp) throws Exception {
       return requestProxyExecutor.fetch(url, route, sResp);
    }

    /**
     * @see RequestProxyExecutor#fetch(String, HttpServletResponse)
     */
    public int fetch(final String url, final HttpServletResponse sResp) throws Exception {
        return requestProxyExecutor.fetch(url, sResp);
    }


}
