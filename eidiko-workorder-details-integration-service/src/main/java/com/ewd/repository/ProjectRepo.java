package com.ewd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ewd.entity.Project;

@Repository
public interface ProjectRepo extends JpaRepository<Project, String> {
	
	@Query("select al from Project al order by al.id desc")
	List<Project> getAllProjects();

}
