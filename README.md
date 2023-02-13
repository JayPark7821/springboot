## 독립 실행이 가능한 서블릿 어플리케이션

![image](https://user-images.githubusercontent.com/60100532/214589136-fc4afd7c-6ffa-4bb6-8c03-b95daea555e6.png)

```java
public class SpringbootApplication {

	public static void main(String[] args) {
		ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
		WebServer webServer = serverFactory.getWebServer();
		webServer.start();
	}

}

```
---

---
![image](https://user-images.githubusercontent.com/60100532/214589291-ddaf717f-a175-4dbd-87ad-5fa37a4d59d2.png)

```java
public class SpringbootApplication {

	public static void main(String[] args) {
		ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
		WebServer webServer = serverFactory.getWebServer(servletContext -> {
			servletContext.addServlet("hello", new HttpServlet(){
				@Override
				protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException {
					String name = req.getParameter("name");

					resp.setStatus(HttpStatus.OK.value());
					resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
					resp.getWriter().print("Hello " + name);
				}
			}).addMapping("/hello");
		});
		webServer.start();
	}

}

```
---

---
![image](https://user-images.githubusercontent.com/60100532/214592448-64935dd3-bc3d-437a-9991-4895d0ab8653.png)
```java
public class SpringbootApplication {

	public static void main(String[] args) {
		ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
		WebServer webServer = serverFactory.getWebServer(servletContext -> {
			servletContext.addServlet("frontcontroller", new HttpServlet(){
				@Override
				protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException {
					// 인증, 보안, 다국어, 공통 기능
					if (req.getRequestURI().equals("/hello") && req.getMethod().equals(HttpMethod.GET.name())) {
						String name = req.getParameter("name");

						resp.setStatus(HttpStatus.OK.value());
						resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
						resp.getWriter().print("Hello " + name);
					} else if (req.getRequestURI().equals("/user")) {

					} else {
						resp.setStatus(HttpStatus.NOT_FOUND.value());
					}

				}
			}).addMapping("/*");
		});
		webServer.start();
	}

}

```
---

---

![image](https://user-images.githubusercontent.com/60100532/214847310-4fc617c6-38ff-47d3-b2e7-2c9b4754a769.png)
```java
public class HelloController {
	public String hello(String name) {
		return "Hello " + name;
	}
}

```
```java
public class SpringbootApplication {

	public static void main(String[] args) {
		ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
		WebServer webServer = serverFactory.getWebServer(servletContext -> {
			HelloController helloController = new HelloController();

			servletContext.addServlet("frontcontroller", new HttpServlet(){
				@Override
				protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException {
					// 인증, 보안, 다국어, 공통 기능
					if (req.getRequestURI().equals("/hello") && req.getMethod().equals(HttpMethod.GET.name())) {
						String name = req.getParameter("name");

						String response = helloController.hello(name);

						resp.setStatus(HttpStatus.OK.value());
						resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
						resp.getWriter().print(response);
					} else if (req.getRequestURI().equals("/user")) {

					} else {
						resp.setStatus(HttpStatus.NOT_FOUND.value());
					}

				}
			}).addMapping("/*");
		});
		webServer.start();
	}

}

```
 
---

---

## 독립 실행형 스프링 어플리케이션
![image](https://user-images.githubusercontent.com/60100532/214847908-adac3438-a1cf-4a49-9a6c-dabad32403f6.png)
* 기존 frontController에서 HelloController를 생성하여 사용하던 방식에서
* HelloController를 스프링 컨테이너에 등록하여 사용하는 방식으로 변경

```java
public class SpringbootApplication {

	public static void main(String[] args) {
		GenericApplicationContext applicationContext = new GenericApplicationContext();
		applicationContext.registerBean(HelloController.class);
		applicationContext.refresh();

		ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
		WebServer webServer = serverFactory.getWebServer(servletContext -> {
			servletContext.addServlet("frontcontroller", new HttpServlet(){
				@Override
				protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException {
					// 인증, 보안, 다국어, 공통 기능
					if (req.getRequestURI().equals("/hello") && req.getMethod().equals(HttpMethod.GET.name())) {
						String name = req.getParameter("name");

						HelloController helloController = applicationContext.getBean(HelloController.class);
						String response = helloController.hello(name);

						resp.setContentType(MediaType.TEXT_PLAIN_VALUE);
						resp.getWriter().print(response);
					} else {
						resp.setStatus(HttpStatus.NOT_FOUND.value());
					}

				}
			}).addMapping("/*");
		});
		webServer.start();
	}

}

```
___

### 의존 오브젝트 추가
![image](https://user-images.githubusercontent.com/60100532/214849964-606eb493-609e-47c7-a93a-263d3f980d06.png)

```java
public class HelloController {
	public String hello(String name) {
		SimpleHelloService helloService = new SimpleHelloService();
		return helloService.sayHello(Objects.requireNonNull(name));
	}
}

```
```java

public class SimpleHelloService {

	String sayHello(String name) {
		return "Hello " + name;
	}
}
```
___

### 의존 오브젝트 DI 적용

![image](https://user-images.githubusercontent.com/60100532/214853715-511a6ba9-86f3-4b7d-8a5c-a2e52eae61b4.png)
```java
public class HelloController {

	private final HelloService helloService;

	public HelloController(HelloService helloService) {
		this.helloService = helloService;
	}

	public String hello(String name) {
		return helloService.sayHello(Objects.requireNonNull(name));
	}
}
```
```java
public interface HelloService {
	String sayHello(String name);
}

```
```java
public class SpringbootApplication {

	public static void main(String[] args) {
		GenericApplicationContext applicationContext = new GenericApplicationContext();
		applicationContext.registerBean(HelloController.class);
		applicationContext.registerBean(SimpleHelloService.class);
		applicationContext.refresh();
        ...
	}
}
```

___

### DispatcherServlet으로 전환
* 어플리케이션과 긴밀하게 연관되어있는 로직이 servlet코드안에 녹아있다.
  * Mapping - 웹 요청이 들어왔을때 그 요청을 처리해줄 controller를 찾는 로직 (url, http method) 
  * 요청의 파라미터

```java
public class SpringbootApplication {

	public static void main(String[] args) {
		GenericWebApplicationContext applicationContext = new GenericWebApplicationContext();
		applicationContext.registerBean(HelloController.class);
		applicationContext.registerBean(SimpleHelloService.class);
		applicationContext.refresh();

		ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
		WebServer webServer = serverFactory.getWebServer(servletContext -> {
			servletContext.addServlet("dispatcherServlet",
					new DispatcherServlet(applicationContext)
				).addMapping("/*");
		});
		webServer.start();
	}

}
```

```java
@RequestMapping
public class HelloController {

	private final HelloService helloService;

	public HelloController(HelloService helloService) {
		this.helloService = helloService;
	}

	@GetMapping("/hello")
	@ResponseBody
	public String hello(String name) {
		return helloService.sayHello(Objects.requireNonNull(name));
	}
}

```

### 스프링 컨테이너로 통합
* 스프링 컨테이너가 생성될 때 DispatcherServlet이 초기화 되도록 변경
```java

public class SpringbootApplication {

	public static void main(String[] args) {
		GenericWebApplicationContext applicationContext = new GenericWebApplicationContext(){
			@Override
			protected void onRefresh() {
				super.onRefresh();

				ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
				WebServer webServer = serverFactory.getWebServer(servletContext -> {
					servletContext.addServlet("dispatcherServlet",
						new DispatcherServlet(this)
					).addMapping("/*");
				});
				webServer.start();
			}
		};
		applicationContext.registerBean(HelloController.class);
		applicationContext.registerBean(SimpleHelloService.class);
		applicationContext.refresh();
	}
}
```


___

### 자바코드 구성정보 사용
```java
@Configuration
public class SpringbootApplication {

	@Bean
	public HelloController helloController(HelloService helloService) {
		return new HelloController(helloService);
	}

	@Bean
	public HelloService helloService() {
		return new SimpleHelloService();
	}

	public static void main(String[] args) {

		AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext(){
			@Override
			protected void onRefresh() {
				super.onRefresh();

				ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
				WebServer webServer = serverFactory.getWebServer(servletContext -> {
					servletContext.addServlet("dispatcherServlet",
						new DispatcherServlet(this)
					).addMapping("/*");
				});
				webServer.start();
			}
		};
		applicationContext.register(SpringbootApplication.class);
		applicationContext.refresh();
	}
}
```

### @Component Scan
```java
@Configuration
@ComponentScan
public class SpringbootApplication {
	public static void main(String[] args) {

		AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext(){
			@Override
			protected void onRefresh() {
				super.onRefresh();

				ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
				WebServer webServer = serverFactory.getWebServer(servletContext -> {
					servletContext.addServlet("dispatcherServlet",
						new DispatcherServlet(this)
					).addMapping("/*");
				});
				webServer.start();
			}
		};
		applicationContext.register(SpringbootApplication.class);
		applicationContext.refresh();
	}
}

```
```java
@RestController
public class HelloController {

	private final HelloService helloService;

	public HelloController(HelloService helloService) {
		this.helloService = helloService;
	}

	@GetMapping("/hello")
	public String hello(String name) {
		return helloService.sayHello(Objects.requireNonNull(name));
	}
}

```
### Bean의 생명주기 메서드
```java
@Configuration
@ComponentScan
public class SpringbootApplication {

  @Bean
  public ServletWebServerFactory servletWebServerFactory() {
    return new TomcatServletWebServerFactory();
  }

  @Bean
  public DispatcherServlet dispatcherServlet() {
    return new DispatcherServlet(new AnnotationConfigWebApplicationContext());
  }


  public static void main(String[] args) {

    AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext(){
      @Override
      protected void onRefresh() {
        super.onRefresh();

        ServletWebServerFactory serverFactory = this.getBean(ServletWebServerFactory.class);
        DispatcherServlet dispatcherServlet = this.getBean(DispatcherServlet.class);
        // dispatcherServlet.setApplicationContext(this);

        WebServer webServer = serverFactory.getWebServer(servletContext -> {
          servletContext.addServlet("dispatcherServlet",dispatcherServlet)
                  .addMapping("/*");
        });
        webServer.start();
      }
    };
    applicationContext.register(SpringbootApplication.class);
    applicationContext.refresh();
  }
}

```
```java
@RestController
public class HelloController implements ApplicationContextAware {

	private final HelloService helloService;
	private ApplicationContext applicationContext;

	public HelloController(HelloService helloService) {
		this.helloService = helloService;
	}

	@GetMapping("/hello")
	public String hello(String name) {
		return helloService.sayHello(Objects.requireNonNull(name));
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		System.out.println("applicationContext = " + applicationContext);
		this.applicationContext = applicationContext;
	}
}

```

## 자동 구성 기반 어플리케이션
### 합성 어노테이션 적용
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Configuration
@ComponentScan
public @interface MySpringBootAnnotation {
}

```
```java
@Configuration
public class Config {
	@Bean
	public ServletWebServerFactory servletWebServerFactory() {
		return new TomcatServletWebServerFactory();
	}

	@Bean
	public DispatcherServlet dispatcherServlet() {
		return new DispatcherServlet();
	}

}

```

```java
@MySpringBootAnnotation
public class SpringbootApplication {


	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class, args);
	}

}

```

### 빈 오브젝트의 역할과 구분
![image](https://user-images.githubusercontent.com/60100532/215331236-f035bdb6-50f9-4187-952d-f1f530e79810.png)
* 스프링 컨테이너가 생성하고 관리하는 빈들은 컨테이너 인프라스트럭처 빈 & 애플리케이션 빈으로 구분한다.

```java
@Configuration
public class DispatcherServletConfig {
	@Bean
	public DispatcherServlet dispatcherServlet() {
		return new DispatcherServlet();
	}

}

@Configuration
public class TomcatWebServerConfig {
  @Bean
  public ServletWebServerFactory servletWebServerFactory() {
    return new TomcatServletWebServerFactory();
  }

}


```

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({DispatcherServletConfig.class, TomcatWebServerConfig.class})
public @interface EnableMyAutoconfiguration {
}

```
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Configuration
@ComponentScan
@EnableMyAutoconfiguration
public @interface MySpringBootApplication {
}

```
![image](https://user-images.githubusercontent.com/60100532/215332338-82581e16-6819-4c12-9c15-a4837127d003.png)


### 동적인 자동 구성 정보 등록
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(MyAutoConfigImportSelector.class)
public @interface EnableMyAutoconfiguration {
}

```
```java
public class MyAutoConfigImportSelector implements DeferredImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		return new String[]{
			"kr.jay.config.autoconfig.DispatcherServletConfig",
			"kr.jay.config.autoconfig.TomcatWebServerConfig"
		};
	}
}

```

### 자동 구성 정보 파일 분리

```java
public class MyAutoConfigImportSelector implements DeferredImportSelector {

	private final ClassLoader classLoader;

	public MyAutoConfigImportSelector(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		Iterable<String> candidates = ImportCandidates.load(MyAutoConfiguration.class, classLoader);
		return StreamSupport.stream(candidates.spliterator(), false).toArray(String[]::new);
	}
}

```
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Configuration
public @interface MyAutoConfiguration {

}

```
```java
//kr.jay.config.MyAutoConfiguration.imports
kr.jay.config.autoconfig.TomcatWebServerConfig
kr.jay.config.autoconfig.DispatcherServletConfig
```

### 조건부 자동 구성
```java
@MyAutoConfiguration
public class JettyWebServerConfig {
	@Bean("jettyWebServerFactory")
	public ServletWebServerFactory servletWebServerFactory() {
		return new JettyServletWebServerFactory();
	}

}

@MyAutoConfiguration
public class TomcatWebServerConfig {
  @Bean("tomcatWebServerFactory")
  public ServletWebServerFactory servletWebServerFactory() {
    return new TomcatServletWebServerFactory();
  }

}


```
```java
// kr.jay.config.MyAutoConfiguration.imports
kr.jay.config.autoconfig.TomcatWebServerConfig
kr.jay.config.autoconfig.JettyWebServerConfig
kr.jay.config.autoconfig.DispatcherServletConfig
```

### @Conditional 과 Condition
```java
@MyAutoConfiguration
@Conditional(JettyWebServerConfig.JettyCondition.class)
public class JettyWebServerConfig {
	@Bean("jettyWebServerFactory")
	public ServletWebServerFactory servletWebServerFactory() {
		return new JettyServletWebServerFactory();
	}

	static class JettyCondition implements Condition {
		@Override
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
			return true;
		}
	}
}

@MyAutoConfiguration
@Conditional(TomcatWebServerConfig.TomcatCondition.class)
public class TomcatWebServerConfig {
  @Bean("tomcatWebServerFactory")
  public ServletWebServerFactory servletWebServerFactory() {
    return new TomcatServletWebServerFactory();
  }

  static class TomcatCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
      return false;
    }
  }
}


```

### Custom @Conditional

![image](https://user-images.githubusercontent.com/60100532/216630280-dd053aa0-af3c-4b2c-a657-83393e1c285f.png)
```java
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

```

```java
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

```

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Conditional(MyOnClassCondition.class)
public @interface ConditionalMyOnClass {
	String value();
}

```
```java
public class MyOnClassCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		Map<String, Object> attributes = metadata.getAnnotationAttributes(
			ConditionalMyOnClass.class.getName());
		String value = (String)attributes.get("value");

		return ClassUtils.isPresent(value, context.getClassLoader());
	}
}

```



## 스프링 부트의 @Conditional
> 스프링 프레임워크의 @Profile도 @Conditional 애노테이션이다.
> @Conditional(ProfileCondition.class)
> public @interface Profile{
> 

* 스프링 부트는 다음과 같은 종류의 @Conditional 애노테이션과 Condition을 제공한다. 
* 스프링 부트의 자동 구성은 이 @Conditional을 이용한다.

### Class Conditions
* @ConditionalOnClass
* @ConditionalOnMissingClass
* 지정한 클래스의 프로젝트내 존재를 확인해서 포함 여부를 결정.
* 주로 @Configuration클래스 레벨에서 사용하지만 @Bean 메소드에도 적용 가능하다. 
* 단 클래스 레벨의 검증 없이 @Bean 메소드에만 적용하면 불필요하게 @Configuration 클래스가 빈으로 등록되기 때문에,
* 클래스 레벨 사용을 우선해야함.

### Bean Conditions
* @ConditionalOnBean
* @ConditionalOnMissingBean
* 빈의 존재 여부를 기준으로 포함여부를 결정함.
* 빈의 타입 또는 이름을 지정할 수 있다. 
* 지정된 빈 정보가 없으면 메소드의 리턴 타입을 기준으로 빈의 존재여부를 체크한다.
* 컨테이너에 등록된 빈 정보를 기준으로 체크하기 때문에 자동 구성 사이에 적용하려면 
* @Configuration 클래스의 적용 순서가 중요하다. 
* 개발자가 직접 정의한 커스텀 빈 구성정보가 자동 구성 정보 처리보다 우선하기 때문에 이 관계에 적용하는 것은 안전하다. 
* 반대로 커스텀 빈 구성 정보에 적용하는 건 피해야 한다. 

> @Configuration 클래스 레벨의 @ConditionalOnClass와 @Bean 메소드 레벨의 
> @ConditionalOnMissingBean 조합은 가장 대표적으로 사용되는 방식이다. 
> 

### Property Conditions
* @ConditionalOnProperty는 스프링의 환경 프로퍼티 정보를 이요함

### Resource Conditions
* @ConditionalOnResource는 클래스패스에 특정 리소스가 존재하는지 확인함.

### Web Application Conditions
* @ConditionalOnWebApplication
* @ConditionalOnNotWebApplication
* 웹 애플리케이션인지 아닌지를 확인함.

### SpEL Expression Conditions
* @ConditionalOnExpression은 스프링 SpEL의 처리 결과를 기준으로 판단함.


### 외부 설정을 활용하는 자동 구성
* Environment Abstraction 

![image](https://user-images.githubusercontent.com/60100532/216989833-fc2b50a4-c0fb-4809-abae-b3bfe53f08fa.png)


![image](https://user-images.githubusercontent.com/60100532/216990927-91cbbf12-3953-48c6-9bfb-79780927be9c.png)

## Spring JDBC 자동 구성 개발
* 자동 구성 클래스와 빈 설계
![image](https://user-images.githubusercontent.com/60100532/218479690-96333730-820a-40ca-9d77-c6c72d0f5d97.png)

