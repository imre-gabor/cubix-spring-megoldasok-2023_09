package com.cubixedu.hr.sample.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cubixedu.hr.sample.model.Company;
import com.cubixedu.hr.sample.model.Employee;
import com.cubixedu.hr.sample.repository.CompanyRepository;
import com.cubixedu.hr.sample.repository.EmployeeRepository;

@Service
public class CompanyService {

	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private EmployeeService employeeService;
	
	public Company save(Company company) {
		return companyRepository.save(company);
	}

	public Company update(Company company) {
		if(!companyRepository.existsById(company.getId()))
			return null;
		return companyRepository.save(company);
	}

	public List<Company> findAll() {
		return companyRepository.findAll();
	}

	public Optional<Company> findById(long id) {
		return companyRepository.findById(id);
	}

	public void delete(long id) {
		
		//companyRepository.deleteById(id); --> csak akkor megy, ha nincs employee-ja
		Optional<Company> company = companyRepository.findById(id);
		if(company.isPresent()) {
			company.get().getEmployees().forEach(e -> {
				e.setCompany(null);
				employeeService.save(e);
			});
		}
		companyRepository.deleteById(id);
	}
	
	
	@Transactional
	public Company addEmployee(long id, Employee employee) {
		Company company = companyRepository.findByIdWithEmployees(id).get();
		company.addEmployee(employee);
		employeeService.save(employee);
		return company;
	}
	
	@Transactional
	public Company deleteEmployee(long id, long employeeId) {
		Company company = companyRepository.findByIdWithEmployees(id).get();
		Employee employee = employeeService.findById(employeeId).get();
		employee.setCompany(null);
		company.getEmployees().remove(employee);
		//employeeService.save(employee); transactional miatt felesleges
		return company;
	}
	
	@Transactional
	public Company replaceEmployees(long id, List<Employee> employees) {
		Company company = companyRepository.findById(id).get();
		company.getEmployees().forEach(e -> {
			e.setCompany(null);
			//employeeService.save(e);
		});
		company.getEmployees().clear();
		employees.forEach(e -> {
			company.addEmployee(e);
			employeeService.save(e);
		});
		return company;
	}
	
}
