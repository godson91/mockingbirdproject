package com.tts.ProjectMockingBird.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tts.ProjectMockingBird.model.Mock;
import com.tts.ProjectMockingBird.model.User;

@Repository
public interface MockRepository extends CrudRepository<Mock, Long> {

	    List<Mock> findAllByOrderByCreatedAtDesc();
	    List<Mock> findAllByUserOrderByCreatedAtDesc(User user);
	    List<Mock> findAllByUserInOrderByCreatedAtDesc(List<User> users);
	    List<Mock> findByTags_PhraseOrderByCreatedAtDesc(String phrase);
	
}
