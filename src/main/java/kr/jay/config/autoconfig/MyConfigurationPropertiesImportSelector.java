package kr.jay.config.autoconfig;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.MultiValueMap;

public class MyConfigurationPropertiesImportSelector implements DeferredImportSelector {
	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		MultiValueMap<String, Object> attr = importingClassMetadata.getAllAnnotationAttributes(
			EnableMyConfigurationProperties.class.getName());

		Class clazz = (Class)attr.getFirst("value");
		return new String[] {clazz.getName()};
	}
}
