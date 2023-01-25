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