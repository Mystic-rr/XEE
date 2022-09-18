package XXE.TEST.XXE.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class TestCrossController {
	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}
}
