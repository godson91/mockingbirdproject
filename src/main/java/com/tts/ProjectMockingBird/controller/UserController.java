package com.tts.ProjectMockingBird.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
public class UserController {
	
	@Autowired
    private UserService userService;
    
    @Autowired
    private MockService mockService;
    
    @GetMapping(value = "/users")
    public String getUsers(@RequestParam(value="filter", required=false) String filter, Model model) {
    	List<User> users = new ArrayList<User>();
    	User loggedInUser = userService.getLoggedInUser();
    	List<User> usersFollowing = loggedInUser.getFollowing();
    	List<User> usersFollowers = loggedInUser.getFollowers();
    	if (filter == null) {
    	    filter = "all";
    	} if (filter.equalsIgnoreCase("followers")) {
    	    users = usersFollowers;
    	    model.addAttribute("filter", "followers");
    	} else if (filter.equalsIgnoreCase("following")) {
    	    users = usersFollowing;
    	    model.addAttribute("filter", "following");
    	} else {
    	    users = userService.findAll();
    	    model.addAttribute("filter", "all");
    	} 
        model.addAttribute("users", users);
        SetMockCounts(users, model);
        SetFollowingStatus(users, usersFollowing, model);
        return "users";
    }
    
    @GetMapping(value = "/users/{username}")
    public String getUser(@PathVariable(value="username") String username, Model model) {	
    	
        User loggedInUser = userService.getLoggedInUser();
        List<User> following = loggedInUser.getFollowing();
        boolean isFollowing = false;
        for (User followedUser : following) {
            if (followedUser.getUsername().equals(username)) {
                isFollowing = true;
            }
        }User user = userService.findByUsername(username);
        List<MockDisplay > mocks = mockService.findAllByUser(user);
        model.addAttribute("mockList", mocks);
        model.addAttribute("user", user);
        model.addAttribute("following", isFollowing);        
        boolean isSelfPage = loggedInUser.getUsername().equals(username);
        model.addAttribute("isSelfPage", isSelfPage);
        return "user";
    }




    
    
    private void SetFollowingStatus(List<User> users, List<User> usersFollowing, Model model) {
        HashMap<String,Boolean> followingStatus = new HashMap<>();
        String username = userService.getLoggedInUser().getUsername();
        for (User user : users) {
            if(usersFollowing.contains(user)) {
                followingStatus.put(user.getUsername(), true);
            }else if (!user.getUsername().equals(username)) {
                followingStatus.put(user.getUsername(), false);
        	}
        }
        model.addAttribute("followingStatus", followingStatus);
    }
		
	
	private void SetMockCounts(List<User> users, Model model) {
        HashMap<String,Integer> mockCounts = new HashMap<>();
        for (User user : users) {
            List<MockDisplay> mocks = mockService.findAllByUser(user);
            mockCounts.put(user.getUsername(), mocks.size());
        }
        model.addAttribute("mockCounts", mockCounts);
    }	
    
   
    
   
    


}
