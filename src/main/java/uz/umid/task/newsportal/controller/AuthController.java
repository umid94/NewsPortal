package uz.umid.task.newsportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.umid.task.newsportal.model.User;
import uz.umid.task.newsportal.security.UserDetailsServiceImpl;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserDetailsServiceImpl userService;
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AuthController(UserDetailsServiceImpl userService, PasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";

    }

    @PostMapping("/registration")
    public String registrationNewUser(@Valid @ModelAttribute("user") User user, BindingResult theBindingResult, Model model) {
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("message", "user with this username exists");
            return "registration";
        }

        if (theBindingResult.hasErrors()) {
            return "registration";
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userService.save(user);
            model.addAttribute("message", "congratulations you can log in");
            return "login";
        }
    }
}
