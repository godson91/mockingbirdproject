package com.tts.ProjectMockingBird.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tts.ProjectMockingBird.model.Tag;

@Repository
public interface TagRepository extends CrudRepository <Tag, Long> {
	Tag findByPhrase(String phrase);
}
