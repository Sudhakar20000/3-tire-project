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

	// Optional is already provided by JpaRepository, but you can override if needed
	Optional<Employee> findById(String employeeId);

	// JPQL query to get all employees ordered by id descending
	@Query("SELECT e FROM Employee e ORDER BY e.id DESC")
	List<Employee> findAllEmployeesDesc();

	// Oracle-compatible native SQL using CASE WHEN inside COUNT
	@Query(value = """
        SELECT
            COUNT(CASE WHEN sow_status = :status THEN 1 ELSE NULL END) AS sowPending,
            COUNT(CASE WHEN po_status = :status THEN 1 ELSE NULL END) AS poPending,
            COUNT(CASE WHEN wo_status = :status THEN 1 ELSE NULL END) AS woPending
        FROM employee
        """, nativeQuery = true)
	List<Object[]> getStatusAllEmployee(String status);

	// Oracle-compatible with FROM DUAL for scalar values
	@Query(value = """
        SELECT
            (SELECT COUNT(*) FROM employee WHERE sow_status = 'Pending') +
            (SELECT COUNT(*) FROM project WHERE sow_status = 'Pending') AS sowPending,

            (SELECT COUNT(*) FROM employee WHERE sow_status = 'Sent') +
            (SELECT COUNT(*) FROM project WHERE sow_status = 'Sent') AS sowSent
        FROM dual
        """, nativeQuery = true)
	List<Object[]> getSowPendingAndSentCount();

	// Oracle-compatible with FROM DUAL for scalar values
//	@Query(value = """
//        SELECT 
//            (SELECT COUNT(*) FROM employee WHERE wo_status = 'Pending' OR po_status = 'Pending') +
//            (SELECT COUNT(*) FROM project WHERE wo_status = 'Pending' OR po_status = 'Pending') AS pendingCount,
//
//            (SELECT COUNT(*) FROM employee WHERE wo_status = 'Received' AND po_status = 'Received') +
//            (SELECT COUNT(*) FROM project WHERE wo_status = 'Received' AND po_status = 'Received') AS receivedCount
//        FROM dual
//        """, nativeQuery = true)
//	List<Object[]> getWoOrPoStatus();
	
	@Query(value = """
		    SELECT 
		        -- Pending count: any status = 'Pending'
		        (SELECT COUNT(*) FROM employee 
		         WHERE wo_status = 'Pending' OR po_status = 'Pending') +
		        (SELECT COUNT(*) FROM project 
		         WHERE wo_status = 'Pending' OR po_status = 'Pending') AS pendingCount,

		        -- Received count:
		        (SELECT COUNT(*) FROM employee 
		         WHERE (wo_status = 'Received' AND po_status = 'Received') 
		            OR (wo_status = 'Received' AND po_status = 'NA') 
		            OR (wo_status = 'NA' AND po_status = 'Received')) +
		        (SELECT COUNT(*) FROM project 
		         WHERE (wo_status = 'Received' AND po_status = 'Received') 
		            OR (wo_status = 'Received' AND po_status = 'NA') 
		            OR (wo_status = 'NA' AND po_status = 'Received')) AS receivedCount
		    FROM dual
		    """, nativeQuery = true)
		List<Object[]> getWoOrPoStatus();

}
