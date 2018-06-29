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
package io.interproxy.core.monitoring;

import io.interproxy.core.exceptions.HostMonitoringException;
import io.interproxy.core.exceptions.InterProxyException;
import io.interproxy.core.monitoring.msg.HostMonitorMessage;
import org.apache.http.HttpHost;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HostMonitorManager {

    private HostMonitor defHostMonitor;

    /**
     * Constructor which create a new instance of this class and set the default HostMonitor ({@link EmptyHostMonitor})
     * @throws InterProxyException
     */
    public HostMonitorManager() throws InterProxyException {
        this(new EmptyHostMonitor());
    }

    /**
     * Constructor which create a new instance of this class and set the passed {@link HostMonitor}
     * @param hostMonitor
     * @throws HostMonitoringException
     */
    public HostMonitorManager(HostMonitor hostMonitor) throws HostMonitoringException {
        if(hostMonitor == null) {
            throw new HostMonitoringException("The HostMonitor  must not be null");
        }
        defHostMonitor = hostMonitor;
    }

    /**
     * Using the passed parameters, it will create the message to send. Then using the defined {@link HostMonitor},
     * it runs in a new thread the {@link HostMonitor#sendHostStatus} method.
     * @param httpHost  to be added to the message sent
     * @param status  to be added to the message sent
     * @param message  to be added to the message sent
     */
    public void exec(HttpHost httpHost, Integer status, String message){
        final HostMonitorMessage hostMonitorMessage = defHostMonitor.getHostMonitorMessageBuilder().build(httpHost, status, message);

        // fire & forget
        ExecutorService x = Executors.newSingleThreadExecutor();
        x.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    defHostMonitor.sendHostStatus(hostMonitorMessage);
                }catch (Exception e){ /** nothing to do */}
            }
        });
        x.shutdown();
    }


}
