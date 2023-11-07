package com.cubixedu.hr.sample.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.cubixedu.hr.sample.dto.CompanyDto;
import com.cubixedu.hr.sample.dto.EmployeeDto;
import com.cubixedu.hr.sample.model.Company;
import com.cubixedu.hr.sample.model.Employee;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

	public CompanyDto companyToDto(Company company);
	
	public List<CompanyDto> companiesToDtos(List<Company> companies);
	
	public Company dtoToCompany(CompanyDto companyDto);
	
	@Mapping(target = "id", source = "employeeId")
	@Mapping(target = "title", source = "position.name")
	@Mapping(target = "entryDate", source = "dateOfStartWork")
	@Mapping(target = "company", ignore = true)
	EmployeeDto employeeToDto(Employee employee);

	
	@InheritInverseConfiguration
	Employee dtoToEmployee(EmployeeDto employeeDto);
	
	@Mapping(target = "employees", ignore = true)
	@Named("summary")
	public CompanyDto companyToSummaryDto(Company company);

	@IterableMapping(qualifiedByName = "summary")
	public List<CompanyDto> companiesToSummaryDtos(List<Company> companies);

	public List<Employee> dtosToEmployees(List<EmployeeDto> employees);
}
