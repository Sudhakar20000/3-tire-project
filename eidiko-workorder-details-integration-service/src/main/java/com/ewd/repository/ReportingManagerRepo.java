package com.ewd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ewd.entity.ReportingManager;

@Repository  
public interface ReportingManagerRepo extends JpaRepository<ReportingManager, Long> {
}
