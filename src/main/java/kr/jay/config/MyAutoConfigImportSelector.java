package kr.jay.config;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MyAutoConfigImportSelector implements DeferredImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		return new String[]{
			"kr.jay.config.autoconfig.DispatcherServletConfig",
			"kr.jay.config.autoconfig.TomcatWebServerConfig"
		};
	}
}
