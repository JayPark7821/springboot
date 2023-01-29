package kr.jay.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import kr.jay.config.EnableMyAutoconfiguration;
import kr.jay.config.autoconfig.DispatcherServletConfig;
import kr.jay.config.autoconfig.TomcatWebServerConfig;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Configuration
@ComponentScan
@EnableMyAutoconfiguration
public @interface MySpringBootApplication {
}
