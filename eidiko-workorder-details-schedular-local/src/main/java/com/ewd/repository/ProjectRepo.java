package com.ewd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ewd.entity.Project;

public interface ProjectRepo extends JpaRepository<Project, String> {

	@Query(value = "SELECT * FROM project WHERE wo_status = 'Pending' OR po_status = 'Pending' OR sow_status='Pending'", nativeQuery = true)
	List<Project> findALlPendingProjects();

	@Query(value = "SELECT * FROM project WHERE (wo_status = 'Pending' OR po_status = 'Pending' OR sow_status='Pending') AND mashreq_reporting_manager =:mashreqReportingManager", nativeQuery = true)
	List<Project> findPendingProjectByMashreqReporManager(@Param("mashreqReportingManager") String mashreqReportingManager);

	@Query(value = """
    SELECT * FROM project 
    WHERE (
        :mashreqReportingManager IS NULL 
        OR LOWER(:mashreqReportingManager) = 'all' 
        OR LOWER(mashreq_reporting_manager) = LOWER(:mashreqReportingManager)
    )
    AND (
        :projectName IS NULL 
        OR LOWER(:projectName) = 'all' 
        OR LOWER(project_name) = LOWER(:projectName)
    )
    AND (
        :status IS NULL 
        OR LOWER(:status) = 'all' 
        OR LOWER(sow_status) = LOWER(:status) 
        OR LOWER(wo_status) = LOWER(:status) 
        OR LOWER(po_status) = LOWER(:status)
    )
""", nativeQuery = true)
	List<Project> findProjectWithFilters(
			@Param("mashreqReportingManager") String mashreqReportingManager,
			@Param("projectName") String projectName,
			@Param("status") String status
	);



}
