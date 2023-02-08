package kr.jay.config.autoconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import kr.jay.config.MyAutoConfiguration;

@MyAutoConfiguration
public class ServerPropertiesConfig {


	@Bean
	public ServerProperties serverProperties(Environment environment) {
		ServerProperties properties = new ServerProperties();
		properties.setContextPath(environment.getProperty("contextPath"));
		properties.setPort(Integer.parseInt(environment.getProperty("port")));

		return properties;
	}
}
