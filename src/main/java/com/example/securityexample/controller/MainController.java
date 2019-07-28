package com.example.securityexample.controller;

import com.example.securityexample.model.User;
import com.example.securityexample.model.UserType;
import com.example.securityexample.repository.UserRepository;
import com.example.securityexample.security.SpringUser;
import com.example.securityexample.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EmailService emailService;

  @PostMapping("/register")
  public String register(@ModelAttribute User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);
    emailService.sendSimpleMessage(user.getEmail(),
      "Բարի Գալուստ " + user.getName(),
      "Դուք Հաջողությամբ գրանցվել եք!");
    return "redirect:/";
  }

  @GetMapping("/login")
  public String loginPage() {
    return "index";
  }

  @GetMapping("/user")
  public String userPage(ModelMap modelMap,
                         @AuthenticationPrincipal
                           SpringUser springUser) {
    modelMap.addAttribute("user", springUser.getUser());
    return "user";
  }

  @GetMapping("/admin")
  public String adminPage() {
    return "admin";
  }

  @GetMapping("/loginSuccess")
  public String loginSuccess(@AuthenticationPrincipal
                               SpringUser springUser) {
    if (springUser.getUser().getUserType() == UserType.ADMIN) {
      return "redirect:/admin";
    }
    return "redirect:/user";

  }


}
