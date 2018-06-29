# InterProxy

Web application and client utilities and libraries that allows to proxify URL calls. In other words, it is a proxy between clients & (external) proxies.

##### What it do :
 - Inter-Proxy-Web (JDK9)
   - Contains the the REST service that will process the parametered URL call.
     Based on a configured list of IP proxys, the client use them in a round-robin way to fetch url content.
 - Inter-Proxy-Core (JDK7)
   - Client HTTP that calls a URL using a list of proxies.  
     This is aime to be used from external applications that required to "proxify" all the internal calls, so all 
     application HTTP request are pass through a configured proxy.
   
##### What it does not do :
  - take into account authentication on given proxies.
  - don't provide any blacklisting control nor monitoring from proxies response.
  - don't provide any means to check or remove from queue the down proxies.
  - some proxies only support http request, any https (SSL/TLS) request won't work (e.g. http codes: 5xxs & some 4xx).
  - some sites are only accessible via only one type of scheme (HTTPS or HTTP), any call with non secure scheme will give unsupported http codes (http code: 1xx, 3xx, 4xx or 5xx).
    Also notice that some sites redirect from http to https (therefore 3xx codes).

##### Other actions to consider/do when using :
  - some external (web) proxy add extra content to the response, so make sure to know/provide the correct response
    processing actions (cleaning).
    
    
#### How to run the web server app :
     
Since it is a SpringBoot application, just execute the follow line:  
`java -jar inter-proxy-web/target/inter-proxy.war`  

Any external configuration (properties and/or xml files) should placed on "/config" directory, or passed by parameter (spring.config.location) on execution.


### Open Source Contribution

If you are interested on this project and want to give your sweet knowledge and effor, please check baselines on
[CONTRIBUTING](CONTRIBUTING.md).


### Legal Terms of Use

#### Disclaimer / General Terms of Use : 
  - This work is for research purposes ONLY, any misuse is under your own responsibility.
  - Anyone who use this work is responsible for making sure that its use is in compliance with applicable laws, regulations, and conditions on your country/state or whichever locality it is run.
  - Misuse or neglecting any security measure while using this work, falls into is your own responsibility.
  - This work is open source, but in any case, you should notify the owner/developer of any security issue is found.
  - Any work or commercial use based on InterProxy should be open sourced according to the below detailed license.


#### License

Copyright 2018 Ignacio LUCATERO <https://lucateroblog.wordpress.com/>

Licensed under the GPLv3 (AGPL-3.0-or-later ): https://www.gnu.org/licenses/gpl.txt / http://www.gnu.org/licenses/gpl-3.0.html .

For more details on the license, check out : https://choosealicense.com/licenses/agpl-3.0/#