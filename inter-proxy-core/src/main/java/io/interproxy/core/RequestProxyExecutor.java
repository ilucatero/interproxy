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
package io.interproxy.core;

import io.interproxy.core.exceptions.InterProxyException;
import io.interproxy.core.hostproxyloaders.HostProxyLoader;
import io.interproxy.core.monitoring.HostMonitor;
import io.interproxy.core.monitoring.HostMonitorManager;
import io.interproxy.core.exceptions.HostMonitoringException;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RequestProxyExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(RequestProxyExecutor.class);

    private static final int MAX_RETRY = 3;

    private ProxyHostsManager proxyHostsManager = ProxyHostsManager.getInstance();
    private HostMonitorManager hostMonitorManager;

    /**
     * Create a new instance of this class. It also add the passed {@link HostProxyLoader}s, and set the default {@link HostMonitor}
     * @param newProxyLoaders List of {@link HostProxyLoader}s
     * @throws InterProxyException
     */
    public RequestProxyExecutor(List<HostProxyLoader> newProxyLoaders) throws InterProxyException {
        this(newProxyLoaders, null);
    }

    /**
     * Create a new instance of this class. It also add the passed {@link HostProxyLoader}s,
     * and set the passed {@link HostMonitor} (if null it set the default {@link io.interproxy.core.monitoring.EmptyHostMonitor})
     * @param newProxyLoaders List of {@link HostProxyLoader}s
     * @throws InterProxyException
     */
    public RequestProxyExecutor(List<HostProxyLoader> newProxyLoaders, HostMonitor hostMonitor) throws InterProxyException {
        proxyHostsManager.addProxyLoader(newProxyLoaders.toArray(new HostProxyLoader[0])).loadHosts();
        hostMonitorManager = hostMonitor != null ? new HostMonitorManager(hostMonitor) : new HostMonitorManager();
    }

    /**
     * It uses the passed route on the {@link RequestProxyExecutor#get} method.
     *
     * @see RequestProxyExecutor#get(String, HttpHost, HttpServletResponse, int)
     */
    public int fetch(String url, HttpHost route, HttpServletResponse sResp) throws IOException  {
       return get(url, route, sResp, 0);
    }
    /**
     * It uses the next available route on {@link RequestProxyExecutor#get} method.
     *
     * @see RequestProxyExecutor#get(String, HttpHost, HttpServletResponse, int)
     */
    public int fetch(String url, HttpServletResponse sResp) throws IOException  {
        return get(url, proxyHostsManager.getNextAvailableHttpHost(), sResp, 0);
    }

    private int fetchWithRetry(String url, HttpServletResponse sResp, int retry) throws IOException  {
        return get(url, proxyHostsManager.getNextAvailableHttpHost(), sResp, retry);
    }

    /**
     * Send the url with httpRequest GET method using a specified route host(proxy)
     * @param url url to submit as GET method
     * @param route the host-proxy to use
     * @param sResp the ServletResponse object where the content & headers will be copied
     * @return the response status code
     * @throws IOException
     */
    private int get(final String url, final HttpHost route, final HttpServletResponse sResp, int retry) throws IOException {
        //get httpClient with round robin planned route/proxy
        CloseableHttpClient httpclient = HttpClients.custom()
                .setRoutePlanner(new DefaultProxyRoutePlanner(route))
                .build();
        int status = 500;
        String message = "";

        try{
            HttpGet httpget = new HttpGet(url);

            LOG.info("Executing request " + httpget.getRequestLine() + " via " + route);

            // get page
            HttpResponse responseHttp = httpclient.execute(httpget);
            status = responseHttp.getStatusLine().getStatusCode();

            if (status >= 200 && status < 300) {
                message = "OK";
                // transfer all headers into the caller response
                for(Header header : responseHttp.getAllHeaders()){
                    if (!"Transfer-Encoding".equalsIgnoreCase(header.getName())) {
                        // ask not to cache any call (if call is from browser)
                        if ("Cache-Control".equalsIgnoreCase(header.getName())) {
                            sResp.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
                        } else {
                            sResp.addHeader(header.getName(), header.getValue());

                        }
                    }
                }

                HttpEntity entity = responseHttp.getEntity();
                try{
                    IOUtils.copy(entity.getContent(), sResp.getOutputStream());
                }catch(IOException e){
                    LOG.error("Error while processing the response",e);
                    message = e.getMessage();
                }finally {
                    EntityUtils.consume(entity);
                }

            }else if((status == 503 || status == 403) && retry < MAX_RETRY){
                //retry with another proxy
                LOG.info("Status {}, Retrying with another proxy!", status);
                return fetchWithRetry(url, sResp, retry++);
            } else if(status == 301 && retry < MAX_RETRY){
                //retry with HTTPS
                if(url.startsWith("http:")) {
                    LOG.info("Retrying as HTTPS!");
                    String httpsUrl = url.replaceAll("http:", "https:");
                    return get(httpsUrl, route, sResp, retry++);
                }
            }

        } catch (Exception e){
            LOG.error("Error while processing the response", e);
        } finally{
            httpclient.close();
        }

        // send monitoring status in new thread
        try {
            hostMonitorManager.exec(route, status, message);
        } catch (Exception e) { /* do nothing */
            LOG.error("Error on sending monitorig host message", e);
        }

        return status;
    }

}
