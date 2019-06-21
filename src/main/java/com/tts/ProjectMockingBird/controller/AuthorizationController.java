package com.tts.ProjectMockingBird.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.tts.ProjectMockingBird.model.User;
import com.tts.ProjectMockingBird.service.UserService;

@Controller
public class AuthorizationController {

	@Autowired
    private UserService userService;

    @GetMapping(value="/login")
    public String login(){
        return "authentication/login";
    }
    @GetMapping(value="/registration")
    public String registration(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "authentication/registration";
    }

    @PostMapping(value = "/registration")
    public String createNewUser(@Valid User user, BindingResult bindingResult, Model model) {
        User userExists = userService.findByUsername(user.getUsername());
        if(bindingResult.hasErrors()) {
        	return "authentication/registration";
        }
        if (userExists != null) {
            bindingResult.rejectValue("username", "error.user", "Username is already taken");
        	return "authentication/registration";

        }
        
        
        if (!bindingResult.hasErrors()) {
            userService.saveNewUser(user);
            model.addAttribute("success", "Sign up successful!");
            model.addAttribute("user", new User());
        }
        return "authentication/login";
    }
    
}
