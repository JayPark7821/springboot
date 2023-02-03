package kr.jay.config.autoconfig;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;

import kr.jay.config.ConditionalMyOnClass;
import kr.jay.config.MyAutoConfiguration;

@MyAutoConfiguration
@ConditionalMyOnClass("org.apache.catalina.startup.Tomcat")
// @Conditional(TomcatWebServerConfig.TomcatCondition.class)
public class TomcatWebServerConfig {
	@Bean("tomcatWebServerFactory")
	public ServletWebServerFactory servletWebServerFactory() {
		return new TomcatServletWebServerFactory();
	}

	// static class TomcatCondition implements Condition {
	// 	@Override
	// 	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
	// 		return ClassUtils.isPresent("org.apache.catalina.startup.Tomcat", context.getClassLoader());
	// 	}
	// }
}
