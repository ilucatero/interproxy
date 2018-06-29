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
package io.interproxy.web.controller;

import io.interproxy.web.model.ProxyHostDto;
import io.interproxy.web.services.RequestProxyService;
import io.interproxy.web.utils.URLUtils;
import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
public class WebController {
    private static final Logger LOG = LoggerFactory.getLogger(WebController.class);

    @Autowired
    private RequestProxyService requestProxyService;

    @GetMapping("/")
    public String load() {
        return "resources/pages/index.html";
    }

    @GetMapping("/proxify")
    public ResponseEntity<String> getPage(@RequestParam(name="url", defaultValue="") String link,
                                          @ModelAttribute ProxyHostDto route, HttpServletResponse response) {
        //response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate, max-age=7200");
        String message = "";
        HttpStatus httpStatus;
        try {
            //validate string is correct url format
           String url = URLUtils.getURI(link).toString();
            LOG.debug("ROUTE to use: " + route.toString());

            // fetch and transfer to client result page
            int status = (route != null && route.isNotEmpty())
                    ? requestProxyService.fetch(url, new HttpHost(route.hostname, route.port, route.scheme), response)
                    : requestProxyService.fetch(url, response);

            httpStatus  = HttpStatus.valueOf(status);
            if(!httpStatus.is2xxSuccessful()){
                message = "Bad or Empty Response from proxy/resource ! >> Status :"+status + ", url:" + url;
            }

        } catch (Exception e) {
            message = "Error while fetching the resource (or empty) of : " + link;
            httpStatus  = HttpStatus.INTERNAL_SERVER_ERROR;
            LOG.error(message, e);
        }
        return new ResponseEntity<>(message, httpStatus);
    }

}