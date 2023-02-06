package kr.jay.config.autoconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import kr.jay.config.MyAutoConfiguration;

@MyAutoConfiguration
public class PropertyPlaceholderConfig {
	@Bean
	PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
