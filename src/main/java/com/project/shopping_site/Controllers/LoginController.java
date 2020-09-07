package com.project.shopping_site.Controllers;

import com.project.shopping_site.Entities.User;
import com.project.shopping_site.Forms.UserForm;
import com.project.shopping_site.Repositories.UserRepository;
import com.project.shopping_site.Validation.SignUpValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    final UserRepository userRepository;
    final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public LoginController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }
    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute("user", new UserForm());
        return "signup";
    }
    @PostMapping("/signup")
    public String signUp(@Validated({SignUpValidation.class}) @ModelAttribute("user") UserForm submittedUser, BindingResult result, Model model) {
        if (result.hasErrors()) {
            result.getAllErrors().forEach(System.out::println);
            return "signup";
        }
        User existedUser = userRepository.findByName(submittedUser.getName());
        if (existedUser != null) {
            model.addAttribute("userNameTaken", "user name is already taken");
            return "signup";
        }
        User user = new User();
        user.setName(submittedUser.getName());
        user.setTel(submittedUser.getTel());
        user.setAddress(submittedUser.getAddress());
        user.setPassword(bCryptPasswordEncoder.encode(submittedUser.getPassword()));
        userRepository.save(user);
        return "redirect:/";
    }
}
