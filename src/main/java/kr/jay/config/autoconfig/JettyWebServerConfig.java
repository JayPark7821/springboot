package kr.jay.config.autoconfig;

import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

import kr.jay.config.MyAutoConfiguration;

@MyAutoConfiguration
public class JettyWebServerConfig {
	@Bean("jettyWebServerFactory")
	public ServletWebServerFactory servletWebServerFactory() {
		return new JettyServletWebServerFactory();
	}

}