package com.tts.ProjectMockingBird.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tts.ProjectMockingBird.model.Mock;
import com.tts.ProjectMockingBird.model.MockDisplay;
import com.tts.ProjectMockingBird.model.Tag;
import com.tts.ProjectMockingBird.model.User;
import com.tts.ProjectMockingBird.repository.MockRepository;
import com.tts.ProjectMockingBird.repository.TagRepository;

@Service
public class MockService {
	
	@Autowired
    private MockRepository mockRepository;
	@Autowired
    private TagRepository tagRepository;


    public List<MockDisplay> findAll() {
        List<Mock> mocks = mockRepository.findAllByOrderByCreatedAtDesc();
        return formatMocks(mocks);
    }
   
	
    public List<MockDisplay> findAllByUser(User user) {
        List<Mock> mocks = mockRepository.findAllByUserOrderByCreatedAtDesc(user);
        return formatMocks(mocks);
    }
	
    public List<MockDisplay> findAllByUsers(List<User> users){
        List<Mock> mocks = mockRepository.findAllByUserInOrderByCreatedAtDesc(users);
        return formatMocks(mocks);
    }

	public void save(Mock mock) {
		handleTags(mock);
		mockRepository.save(mock);
		
	}
	private void handleTags(Mock mock) {
        List<Tag> tags = new ArrayList<Tag>(); 
        Pattern pattern = Pattern.compile("#\\w+");
        Matcher matcher = pattern.matcher(mock.getMessage());
        while (matcher.find())
        {        
        	String phrase = matcher.group().substring(1).toLowerCase();
        	try {
		        Tag tag = tagRepository.findByPhrase(phrase);
		        if(tag == null) {
		            tag = new Tag();
		            tag.setPhrase(phrase);
		            tagRepository.save(tag);
		        	}
		        tags.add(tag);
        	}
        	catch(Exception e) {
	            Tag tag = new Tag();
	            tag.setPhrase(phrase);
	            tagRepository.save(tag);
	            tags.add(tag);
	        } 
	    }    
	    mock.setTags(tags);
	}

	
	
	private List<MockDisplay> formatMocks(List<Mock> mocks) {
	    shortenLinks(mocks);
	    addTagLinks(mocks);
	    List<MockDisplay> displayMocks = formatTimestamps(mocks);
	    return displayMocks;
	}
	
	private void shortenLinks(List<Mock> mocks) {
	    Pattern pattern = Pattern.compile("https?[^ ]+");
	    for(Mock mock: mocks) {
	    	String message = mock.getMessage();
	    	Matcher matcher = pattern.matcher(message);
	    	while(matcher.find()) {
	    	    String link = matcher.group();
	    	
	    	String shortenedLink = link;
	    	if (link.length() > 23) {
	    	    shortenedLink = link.substring(0,20) + "...";
	    	}
	    	message = message.replace(link, "<a class=\"tag\" href=\"" + link + "\"target=\"_blank\">" + shortenedLink + "</a>");
	    	}
	    mock.setMessage(message);
	    	
	    	}    
	    
	    }    
	    
	private void addTagLinks(List<Mock> mocks) {
	    Pattern pattern = Pattern.compile("#\\w+");
	    for(Mock mock: mocks) {
	    	
	        String message = mock.getMessage();
	        Matcher matcher = pattern.matcher(message);
	        Set<String> tags = new HashSet<String>();
	        while(matcher.find()) {
	            tags.add(matcher.group());
	        }
	        for(String tag : tags) {
	        	
	            message = message.replaceAll(tag, 
	            "<a class=\"tag\" href=\"/mocks/" + tag.substring(1).toLowerCase() + "\">" + tag + "</a>");
	        }
	        mock.setMessage(message);
	    }
	}
	
	public List<MockDisplay> findAllWithTag(String tag){
	    List<Mock> mocks = mockRepository.findByTags_PhraseOrderByCreatedAtDesc(tag);
	    return formatMocks(mocks);
	}
	private List<MockDisplay> formatTimestamps(List<Mock> mocks){
	    List<MockDisplay> response = new ArrayList<>();
	    PrettyTime prettyTime = new PrettyTime();
	    SimpleDateFormat simpleDate = new SimpleDateFormat("M/d/yy");
	    Date now = new Date();
	    for(Mock mock : mocks) {
	        MockDisplay mockDisplay = new MockDisplay();
	        mockDisplay.setUser(mock.getUser());
	        mockDisplay.setMessage(mock.getMessage());
	        mockDisplay.setTags(mock.getTags());
	        long diffInMillies = Math.abs(now.getTime() - mock.getCreatedAt().getTime());
	        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	        if (diff > 3) {
	            mockDisplay.setDate(simpleDate.format(mock.getCreatedAt()));
	        }
	        else {
	            mockDisplay.setDate(prettyTime.format(mock.getCreatedAt()));
	        }
	        response.add(mockDisplay);
	    }
	    return response;
	}
	


}
