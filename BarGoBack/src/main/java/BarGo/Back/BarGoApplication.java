package BarGo.Back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SpringBootApplication
public class BarGoApplication {

	@RequestMapping("/")
	@ResponseBody
	String home() {
		return "Hello World, from Spring Boot!";
	}

	public static void main(String[] args) {
		SpringApplication.run(BarGoApplication.class, args);
	}

}
