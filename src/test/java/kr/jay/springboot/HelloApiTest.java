package kr.jay.springboot;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

class HelloApiTest {

	@Test
	void helloApi() {
		TestRestTemplate rest = new TestRestTemplate();

		ResponseEntity<String> response = rest.getForEntity("http://localhost:9090/app/hello?name={name}", String.class, "jay");

		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE)).startsWith(MediaType.TEXT_PLAIN_VALUE);
		Assertions.assertThat(response.getBody()).isEqualTo("*Hello jay*");
	}

	@Test
	void failsHelloApi() {
		TestRestTemplate rest = new TestRestTemplate();

		ResponseEntity<String> response = rest.getForEntity("http://localhost:9090/app/hello?name=", String.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
	}

}