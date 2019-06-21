package com.tts.ProjectMockingBird.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tts.ProjectMockingBird.model.Mock;
import com.tts.ProjectMockingBird.model.MockDisplay;
import com.tts.ProjectMockingBird.model.User;
import com.tts.ProjectMockingBird.service.MockService;
import com.tts.ProjectMockingBird.service.UserService;

@Controller
public class MockController {

	
	@Autowired
    private UserService userService;
	
    @Autowired
    private MockService mockService;
    
    @GetMapping(value= {"/mocks", "/"})
    public String getFeed(@RequestParam(value="filter", required=false) 
    String filter, Model model){
    	User loggedInUser = userService.getLoggedInUser();
    	List<MockDisplay> mocks = new ArrayList<>();
    	if (filter == null) {
    	    filter = "all";
    	}
    	if (filter.equalsIgnoreCase("following")) {
    	    List<User> following = loggedInUser.getFollowing();
    	    mocks = mockService.findAllByUsers(following);
    	    model.addAttribute("filter", "following");
    	}
    	else {
    	    mocks = mockService.findAll();
    	    model.addAttribute("filter", "all");
    	}
        model.addAttribute("mockList", mocks);
        return "feed";
    }
    
    @GetMapping(value = "/mocks/new")
    public String getMockForm(Model model) {
        model.addAttribute("mock", new Mock());
        return "newMock";
    }
    
    @PostMapping(value = "/mocks")
    public String submitMockForm(@Valid Mock mock, BindingResult bindingResult, Model model) {
        User user = userService.getLoggedInUser();
        if (!bindingResult.hasErrors()) {
            mock.setUser(user);
            mockService.save(mock);
            model.addAttribute("successMessage", "Mock successfully created!");
            model.addAttribute("mock", new Mock());
        }
        return "newMock";
    }
    @GetMapping(value = "/mocks/{tag}")
    public String getMocksByTag(@PathVariable(value="tag") String tag, Model model) {
        List<MockDisplay> mocks = mockService.findAllWithTag(tag);
        model.addAttribute("mockList", mocks);
        model.addAttribute("tag", tag);
        return "taggedMocks";
    }
    
    
}
