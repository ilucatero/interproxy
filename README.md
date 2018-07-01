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
    
### Clone, Compile and Running

#### How to build it
Firs of all, you have to get Interproxy project into your local machine.

```
cd /home/user/worspaces/ # or on windows > cd /c/user/worspaces/
git clone git@github.com:ilucatero/interproxy.git
cd interproxy
```

As soon as you see the pom.xml file, you can get that it is a Maven project. So to compile it just go to root directory of
 project workspace and do
 
 ```
 mvn clean install
 ```

#### How to run the web server app :
     
Since it is a SpringBoot application, just execute the follow line:  
`java -jar inter-proxy-web/target/inter-proxy.war`  

Any external configuration (properties and/or xml files) should placed on "/config" directory, or passed by parameter (spring.config.location) on execution.

#### How to use the client dependency

If you have the web server up and running and you need your applications to call the middleware proxy server. You just
 need to add the below dependency on your *pom.xml* file of Maven project.
 
**TODO**: once the Maven repository is accepted and created, put here the dependency.
```
<dependency>
    <groupId>interproxy</groupId>
    <artifactId>interproxy-client</artifactId>
    <version>1.0</version>
</dependency>
```

In case you are not using Maven as building tool for your project, you will need to compile it, extract the *jar* file
 and set it as an external jar (putting it manually on the */lib* directory of your application).

### Docker containers

From previous lines, you know how to compile and run the *interproxy* application war file locally. But since the world 
  is not perfect, and is worthless most of the time to run it on the grandma laptop, we prefer use a modern aproeach : Docker.
  
For instance, check the cheat sheet to know the general commands you need to use on docker [here](https://docs.docker.com/get-started/part2/#recap-and-cheat-sheet-optional).

Please notice that the image and other docker generated items are aimed to contain the springboot web application, which
 will run on a linux container.

#### Build the base linux image

As the web application requires java 10 installed. Also, we have decided to mount the application on a ubuntu linux image.
 And to avoid to build this image every time the application need to be build, a base image containing all configuration is created here.

Anyhow, if you don't need to add any details on the Dockerfile, and hence don't need to build the image, you can freely
 use the one we created and pushed on Docker-Hub: *ilucatero/interproxy:latest*. 
 
Note: This image is already configured on the interproxy-web Dockerfile, so if you use the provided base image , you don't need to do anything here.

#### Build the interproxy-web image
The application uses the *sprotify-docker* maven plugin (than you !), so to compile you just need to run the below line :
```
mvn clean package docker:build
```
*Don't forget to do a* mvn clean install *on the application's root directory, so the interproxy-client dependency could be generated.*

#### Run the image
To run the image just do:
```
docker run -d -p 8080:<8080 or app port> interproxy-image
```


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