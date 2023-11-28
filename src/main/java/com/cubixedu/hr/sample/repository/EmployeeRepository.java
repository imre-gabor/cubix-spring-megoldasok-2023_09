package com.cubixedu.hr.sample.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cubixedu.hr.sample.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee>{

	Page<Employee> findBySalaryGreaterThan(Integer minSalary, Pageable pageable);
	
	List<Employee> findByPositionName(String jobTitle);
	List<Employee> findByNameStartingWithIgnoreCase(String namePrefix);
	List<Employee> findByDateOfStartWorkBetween(LocalDateTime start, LocalDateTime end);

	
	@Query("UPDATE Employee e SET e.salary = :minSalary "
			+ "WHERE e.position.name=:position "
			+ "AND e.salary < :minSalary "
			+ "AND e.company.id = :companyId")
	@Modifying
	public void updateSalaries(String position, int minSalary, long companyId);

	Optional<Employee> findByUsername(String username);
}
