package com.tts.ProjectMockingBird.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MockDisplay {

	
	private User user;	
    private String message;
    private String date;
    private List<Tag> tags;
}

