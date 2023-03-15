package com.hhs.codeboard.blog.config.web;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.ajp.AbstractAjpProtocol;
import org.apache.coyote.ajp.AjpNioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;

@Configuration
public class AJPConfig {

    @Bean
    public ServletWebServerFactory servletWebServerFactory() throws Exception {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createAjpConnector());
        return tomcat;
    }

    private Connector createAjpConnector() throws Exception {
        Connector ajpConnector = new Connector("AJP/1.3");
        ajpConnector.setPort(9099);
        ajpConnector.setSecure(false);
        ajpConnector.setAllowTrace(false);
        ajpConnector.setScheme("http");
        AjpNioProtocol protocol = (AjpNioProtocol)ajpConnector.getProtocolHandler();
        protocol.setSecretRequired(false);
        protocol.setAddress(InetAddress.getByName("0.0.0.0"));
        return ajpConnector;
    }

}
