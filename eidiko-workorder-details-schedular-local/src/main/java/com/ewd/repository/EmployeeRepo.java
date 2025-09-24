package com.ewd.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ewd.entity.Employee;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, String> {

	Optional<Employee> findById(String employeeId);

	@Query(value = "SELECT * FROM employee", nativeQuery = true)
	List<Employee> findAllPendingEmployees();

	@Query(value = "SELECT * FROM employee WHERE (wo_status = 'Pending' OR po_status = 'Pending' OR sow_status='Pending') AND mashreq_reporting_manager =:mashreqReportingManager", nativeQuery = true)
	List<Employee> findPendingEmpByMashreqReporManager(@Param("mashreqReportingManager") String mashreqReportingManager);

	@Query(value = """
    SELECT * FROM employee 
    WHERE (
        :mashreqReportingManager IS NULL 
        OR LOWER(:mashreqReportingManager) = 'all' 
        OR LOWER(mashreq_reporting_manager) = LOWER(:mashreqReportingManager)
    )
    AND (
        :employeeName IS NULL 
        OR LOWER(:employeeName) = 'all' 
        OR LOWER(employee_name) = LOWER(:employeeName)
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
	List<Employee> findEmployeesWithFilters(
			@Param("mashreqReportingManager") String mashreqReportingManager,
			@Param("employeeName") String employeeName,
			@Param("projectName") String projectName,
			@Param("status") String status
	);



}
