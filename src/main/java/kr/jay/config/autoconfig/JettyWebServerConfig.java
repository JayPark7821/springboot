package kr.jay.config.autoconfig;

import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;

import kr.jay.config.ConditionalMyOnClass;
import kr.jay.config.MyAutoConfiguration;

@MyAutoConfiguration
// @Conditional(JettyWebServerConfig.JettyCondition.class)
@ConditionalMyOnClass("org.eclipse.jetty.server.Server")
public class JettyWebServerConfig {
	@Bean("jettyWebServerFactory")
	public ServletWebServerFactory servletWebServerFactory() {
		return new JettyServletWebServerFactory();
	}

	// static class JettyCondition implements Condition {
	// 	@Override
	// 	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
	// 		return ClassUtils.isPresent("org.eclipse.jetty.server.Server", context.getClassLoader());
	// 	}
	// }
}
