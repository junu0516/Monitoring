package kr.co.infotech.monitor.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(("/monitoring"))
public class MonitoringController {

	@GetMapping("/main")
	public String showMainPage(Principal principal, Model model) {
		
		return "view";
	}

	@GetMapping("/failure")
	public String showFailurePage() {
		
		return "failure";
	}
	
	@GetMapping("/loginForm")
	public String login(Principal principal) {

		return "login";
	}
	
	@GetMapping("/nxgate")
	public String showNxGatePage() {
		
		return "nxgate/nxgate";
	}
}
