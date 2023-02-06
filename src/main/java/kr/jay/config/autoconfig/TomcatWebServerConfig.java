package kr.jay.config.autoconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

import kr.jay.config.ConditionalMyOnClass;
import kr.jay.config.MyAutoConfiguration;

@MyAutoConfiguration
@ConditionalMyOnClass("org.apache.catalina.startup.Tomcat")
// @Conditional(TomcatWebServerConfig.TomcatCondition.class)
public class TomcatWebServerConfig {

	@Value("${contextPath}")
	String contextPath;

	@Bean("tomcatWebServerFactory")
	@ConditionalOnMissingBean
	public ServletWebServerFactory servletWebServerFactory() {
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
		factory.setContextPath(this.contextPath);
		return factory;
	}

	// static class TomcatCondition implements Condition {
	// 	@Override
	// 	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
	// 		return ClassUtils.isPresent("org.apache.catalina.startup.Tomcat", context.getClassLoader());
	// 	}
	// }
}
