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
import io.interproxy.core.hostproxyloaders.HostProxyLoaderScheduler;
import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ProxyHostsManager {
    private static final Logger LOG = LoggerFactory.getLogger(ProxyHostsManager.class);

    // map to contain all the ip address
    private final Map<HostProxyLoader, List<HttpHost>> hostsListMapping = new HashMap<>();
    private final List<HttpHost> joinedHostsList = new ArrayList<>();

    private Integer pivot = 0;

    private ProxyHostsManager(){}

    /**
     * Crete a new instance of this class
     * @return {@link ProxyHostsManager}
     */
    public static ProxyHostsManager getInstance(){
        return new ProxyHostsManager();
    }

    /**
     * Create a new instance of this class, adding and loading the passed host loaders.
     * @param newProxyLoaders
     * @return {@link ProxyHostsManager}
     * @throws Exception
     */
    public static ProxyHostsManager getInstance(HostProxyLoader... newProxyLoaders) throws InterProxyException{
        return getInstance()
                .addProxyLoader(newProxyLoaders)
                .loadHosts();
    }

    /**
     * Add the passed host loaders to use.
     * @param newProxyLoaders
     * @return {@link ProxyHostsManager}
     * @throws Exception
     */
    public ProxyHostsManager addProxyLoader(HostProxyLoader... newProxyLoaders) throws InterProxyException {
        if(newProxyLoaders == null || newProxyLoaders.length == 0){
            throw new InterProxyException("The HostProxyLoader list cannot be null or empty. Try to use the getInstance() method instead.");
        }
        if(newProxyLoaders != null && newProxyLoaders.length > 0){
            for(HostProxyLoader proxyLoader : newProxyLoaders) {
                // add proxyloader with default list
                hostsListMapping.put(proxyLoader, new ArrayList<HttpHost>());
            }
        }
        return this;
    }

    /**
     * Run the added host loaders to extract the hosts to use.
     * @return {@link ProxyHostsManager}
     * @throws Exception
     */
    public ProxyHostsManager loadHosts() throws InterProxyException {
        if( hostsListMapping.size() == 0){
            throw new InterProxyException("The HostProxyLoader list is empty. Try to load a HostProxyLoader first.");
        }
        for(final Map.Entry<HostProxyLoader, List<HttpHost>> hplEntry : hostsListMapping.entrySet()) {
            Runnable schedulerHostLoader = new Runnable() {
                @Override
                public void run() {
                    try {
                        LOG.info("Running proxyLoader {}", hplEntry.getKey());

                        List<HttpHost> hostLstTmp = hplEntry.getValue();
                        // clean previous values
                        joinedHostsList.removeAll(hostLstTmp);
                        hostLstTmp.clear();
                        // run loader and fill the joined list
                        hostLstTmp.addAll(hplEntry.getKey().loadHosts());
                        joinedHostsList.addAll(hostLstTmp);

                    } catch (InterProxyException e) {
                        LOG.warn("Loading host with {} was not successful. Will try to load the rest from the list (if any)", hplEntry.getKey().getClass(), e);
                    }
                }
            };

            // set scheduler
            if (hplEntry.getKey() instanceof HostProxyLoaderScheduler) {
                // set scheduler
                HostProxyLoaderScheduler pl = (HostProxyLoaderScheduler) hplEntry.getKey();
                Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(schedulerHostLoader, 0, pl.getTime(), pl.getTimeUnit());
            } else {
                // just run the method to load hsots
                schedulerHostLoader.run();
            }
        }
        return this;
    }


    /**
     * Get last available host in round robin way (if is the last one in the list, it will restart the pivot to index zero)
     * @return {@link HttpHost}
     */
    public synchronized HttpHost getNextAvailableHttpHost(){
        if(joinedHostsList != null && joinedHostsList.size()>0) {
            if (isLast()) {
                pivot = 0;
            }
            return joinedHostsList.get(pivot++);
        }
        return null;
    }

    /**
     * Check of the last used on {@link #getNextAvailableHttpHost()}  is the last element in the list of hosts.
     * @return {@link Boolean#TRUE} if the hosts list size equal to the pivot value
     */
    public boolean isLast(){
        return joinedHostsList.size() == pivot;
    }
}
