package com.cubixedu.hr.sample.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cubixedu.hr.sample.dto.CompanyDto;
import com.cubixedu.hr.sample.dto.EmployeeDto;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

	private Map<Long, CompanyDto> companies = new HashMap<>();
	
	//1. megoldás: CompanyDto lemásolásával, de employees kivételével
	@GetMapping
	public List<CompanyDto> findAll(@RequestParam Optional<Boolean> full){
		return null;
//		if(full.orElse(false)) {
//			return new ArrayList<>(companies.values());
//		} else {
//			return companies.values().stream()
//				.map(this::mapCompanyWithoutEmployeees)
//				.toList();
//		}
	}
	
	
	//2. megoldás: JsonView-val
//	@GetMapping(params = "full=true")
//	public List<CompanyDto> findAllWithEmployees(){
//		return new ArrayList<>(companies.values());
//	}
//	
//	@GetMapping
//	@JsonView(Views.BaseData.class)
//	public List<CompanyDto> findAllWithoutEmployees(){
//		return new ArrayList<>(companies.values());
//	}
//
//	private CompanyDto mapCompanyWithoutEmployeees(CompanyDto c) {
//		return new CompanyDto(c.getId(), c.getRegistrationNumber(), c.getName(), c.getAddress(), null);
//	}
	
	@GetMapping("/{id}")
	public CompanyDto findById(@PathVariable long id, @RequestParam Optional<Boolean> full) {
		return null;
//		CompanyDto companyDto = getCompanyOrThrow(id);
//		if(full.orElse(false)) {
//			return companyDto;
//		} else {
//			return mapCompanyWithoutEmployeees(companyDto);
//		}		
	}
	
	@PostMapping
	public ResponseEntity<CompanyDto> create(@RequestBody CompanyDto companyDto) {
		return null;
//		return companyMapper.companyToDto(companyService.save(companyMapper.dtoToCompany(companyDto)));	
	}
	
	
	@PutMapping("/{id}")
	public ResponseEntity<CompanyDto> update(@PathVariable long id, @RequestBody CompanyDto company) {
		return null;
//      companyDto.setId(id);
//      Company updatedCompany = companyService.update(companyMapper.dtoToCompany(companyDto));
//      if (updatedCompany == null) {
//          throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//      }
//
//      return companyMapper.companyToDto(updatedCompany);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
//      companyService.delete(id);
	}
	
	@PostMapping("/{id}/employees")
	public CompanyDto addNewEmployee(@PathVariable long id, @RequestBody EmployeeDto employeeDto) {
		return null;
//		Company company = companyService.addEmployee(id, companyMapper.dtoToEmployee(employeeDto));
//		return companyMapper.companyToDto(company);
	}
	
	@DeleteMapping("/{id}/employees/{employeeId}")
	public CompanyDto deleteEmployeeFromCompany(@PathVariable long id, @PathVariable long employeeId){
		return null;
//		Company company = companyService.deleteEmployee(id, employeeId);
//		return companyMapper.companyToDto(company);
	}
	
	@PutMapping("/{id}/employees")
	public CompanyDto replaceEmployees(@PathVariable long id, @RequestBody List<EmployeeDto> newEmployees) {
		return null;		
//		Company company = companyService.replaceEmployees(id, companyMapper.dtosToEmployees(newEmployees));
//		return companyMapper.companyToDto(company);
	}
	
	

	private CompanyDto getCompanyOrThrow(long id) {
		CompanyDto company = companies.get(id);
		if(company == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		return company;
	}
	
	
	
	
}
