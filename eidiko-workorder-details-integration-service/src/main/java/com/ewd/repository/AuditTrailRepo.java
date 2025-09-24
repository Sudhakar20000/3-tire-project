package com.ewd.repository;

import com.ewd.entity.AuditTrail;
import com.ewd.entity.Employee;
import com.ewd.entity.Project;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface AuditTrailRepo extends JpaRepository<AuditTrail, Long> {

    // Find by Employee
    Optional<AuditTrail> findByEmployee(Employee employee);

    // Find by Project
    Optional<AuditTrail> findByProject(Project project);

    // Delete by Employee ID (custom Oracle SQL)
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM AUDIT_TRAIL WHERE EMPLOYEE_ID = :employeeId", nativeQuery = true)
    void deleteByEmployeeId(String employeeId);

    // Delete by Project ID (custom Oracle SQL)
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM AUDIT_TRAIL WHERE PROJECT_ID = :projectId", nativeQuery = true)
    void deleteByProjectId(String projectId);
}


