package kr.jay.springboot;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class HelloServiceTest {

	@Test
	void simpleHelloService() {
		SimpleHelloService simpleHelloService = new SimpleHelloService();

		String response = simpleHelloService.sayHello("jay");
		Assertions.assertThat(response).isEqualTo("Hello jay");
	}
}
