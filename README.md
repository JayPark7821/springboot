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
					resp.setStatus(200);
					resp.setHeader("Content-Type", "text/plain");
					resp.getWriter().print("Hello Servlet");
				}
			}).addMapping("/hello");
		});
		webServer.start();
	}

}

```