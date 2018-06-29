package io.interproxy.web.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class URLUtils {

    private static String[] UTM_PARAMS = new String[]{"utm_source","utm_medium","utm_campaign","utm_term","utm_content"};
    /**
     * Remove google UTM Query params.
     * https://ga-dev-tools.appspot.com/campaign-url-builder/
     * @param url
     * @return
     * @throws java.net.URISyntaxException
     */
    public static URI removeUTMQueryParameter(String url) throws URISyntaxException {
        if(!URLPatterns.WEB_URL.matcher(url).matches()){
            throw new URISyntaxException(url,"The URL doesnt match as a correct web link.");
        }
        URIBuilder uriBuilder = new URIBuilder(url);
        List<NameValuePair> queryParameters = uriBuilder.getQueryParams()
                .parallelStream()
                .filter(p -> Arrays.stream(UTM_PARAMS).parallel().noneMatch(s -> s.equalsIgnoreCase(p.getValue())))
                .collect(Collectors.toList());
        if (queryParameters.isEmpty()) {
            uriBuilder.removeQuery();
        } else {
            uriBuilder.setParameters(queryParameters);
        }
        return uriBuilder.build();
    }

    /**
     * Check that the scheme exist and remove all google adwords UTM parameters
     * @param link
     * @return {@Link URI}
     * @throws URISyntaxException
     */
    public static URI getURI(final String link)throws URISyntaxException{
        String url = !link.matches("(http(s)?|ftp|file|rtsp)://.*") ? ("http://"+link) : link;
        return removeUTMQueryParameter(url);
    }
}
