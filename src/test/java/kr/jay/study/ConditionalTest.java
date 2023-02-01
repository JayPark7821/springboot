package kr.jay.study;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class ConditionalTest {

	@Test
	void conditional() {
		// true
		new ApplicationContextRunner().withUserConfiguration(Config1.class)
			.run(context -> {
				Assertions.assertThat(context).hasSingleBean(MyBean.class);
				Assertions.assertThat(context).hasSingleBean(Config1.class);
			});



		//false
		new ApplicationContextRunner().withUserConfiguration(Config2.class)
			.run(context -> {
			Assertions.assertThat(context).doesNotHaveBean(MyBean.class);
			Assertions.assertThat(context).doesNotHaveBean(Config1.class);
		});


	}

	@Retention(RetentionPolicy.RUNTIME)
	@Conditional(TrueCondition.class)
	@interface TrueConditional{}

	@Configuration
	@TrueConditional
	static class Config1 {
		@Bean
		MyBean myBean() {
			return new MyBean();
		}
	}


	@Retention(RetentionPolicy.RUNTIME)
	@Conditional(FalseCondition.class)
	@interface FalseConditional{}


	@Configuration
	@FalseConditional
	static class Config2 {
		@Bean
		MyBean myBean() {
			return new MyBean();
		}

	}

	static class MyBean {

	}

	static class TrueCondition implements Condition {
		@Override
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
			return true;
		}
	}

	static class FalseCondition implements Condition {
		@Override
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
			return false;
		}
	}
}
