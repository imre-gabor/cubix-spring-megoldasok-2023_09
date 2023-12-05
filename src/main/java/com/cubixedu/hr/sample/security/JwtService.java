package com.cubixedu.hr.sample.security;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cubixedu.hr.sample.config.HrConfigProperties;
import com.cubixedu.hr.sample.config.HrConfigProperties.JwtData;
import com.cubixedu.hr.sample.model.Employee;

import jakarta.annotation.PostConstruct;

@Service
public class JwtService {

	private static final String USERNAME = "username";
	private static final String ID = "id";
	private static final String FULLNAME = "fullname";
	private static final String AUTH = "auth";
	private static final String MANAGED_EMPLOYEES = "managedEmployees";
	private static final String MANAGER = "manager";
	
	@Autowired
	private HrConfigProperties hrConfig;
	
	private String issuer; //NPE-t okozna: = hrConfig.getJwtData().getIssuer();
	private Algorithm algorithm; // = Algorithm.HMAC256("mysecret");

	@PostConstruct
	public void init() {
		JwtData jwtData = hrConfig.getJwtData();
		issuer = jwtData.getIssuer();
		
		try {
			Method algMethod = Algorithm.class.getMethod(hrConfig.getJwtData().getAlg(), String.class);
			algorithm = (Algorithm) algMethod.invoke(null, jwtData.getSecret());
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {			
			e.printStackTrace();
		}
	}
	
	
	public String createJwt(UserDetails userDetails) {
		Employee employee = ((HrUser) userDetails).getEmployee();
		Employee manager = employee.getManager();
		List<Employee> managedEmployees = employee.getManagedEmployees();
		
		Builder jwtBuilder = JWT.create()
			.withSubject(userDetails.getUsername())
			.withArrayClaim(AUTH, userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new))
			.withClaim(FULLNAME, employee.getName())
			.withClaim(ID, employee.getEmployeeId());

		if(manager != null) {
			jwtBuilder.withClaim(MANAGER, createMapFromEmployee(manager));
		}
		if(managedEmployees != null && !managedEmployees.isEmpty()) {
			jwtBuilder.withClaim(MANAGED_EMPLOYEES, managedEmployees.stream()
					.map(this::createMapFromEmployee)
					.toList());
		}
		
		
		return jwtBuilder
			.withExpiresAt(new Date(System.currentTimeMillis() + hrConfig.getJwtData().getDuration().toMillis()))
			.withIssuer(issuer)
			.sign(algorithm);
	}

	private Map<String, Object> createMapFromEmployee(Employee emp) {
		return Map.of(
				ID, emp.getEmployeeId(),
				USERNAME, emp.getUsername()
			);
	}

	public UserDetails parseJwt(String jwtToken) {
		
		DecodedJWT decodedJwt = JWT.require(algorithm)
		.withIssuer(issuer)
		.build()
		.verify(jwtToken);
		
		Employee employee = new Employee();
		employee.setEmployeeId(decodedJwt.getClaim(ID).asLong());
		employee.setName(decodedJwt.getClaim(FULLNAME).asString());
		employee.setUsername(decodedJwt.getSubject());
		
		Claim managerClaim = decodedJwt.getClaim(MANAGER);
		if(managerClaim != null) {
			Map<String, Object> managerData = managerClaim.asMap();
			employee.setManager(parseEmployeeFromMap(managerData));
		}
		
		Claim managedEmployeesClaim = decodedJwt.getClaim(MANAGED_EMPLOYEES);
		if(managedEmployeesClaim != null) {
			employee.setManagedEmployees(new ArrayList<>());
			List<HashMap> managedEmployees = managedEmployeesClaim.asList(HashMap.class);
			if(managedEmployees != null) {
				for(var employeeMap : managedEmployees) {
					Employee managedEmployee = parseEmployeeFromMap(employeeMap);
					if(managedEmployee != null)
						employee.getManagedEmployees().add(employee);
				}
			}
		}
		
		
		return new HrUser(decodedJwt.getSubject(), "dummy", decodedJwt.getClaim(AUTH).asList(String.class).stream()
				.map(SimpleGrantedAuthority::new).toList(), employee);
	}

	private Employee parseEmployeeFromMap(Map<String, Object> employeeMap) {
		if(employeeMap != null) {
			Employee emp = new Employee();
			emp.setEmployeeId(((Integer)employeeMap.get(ID)).longValue());
			emp.setUsername((String) employeeMap.get(USERNAME));
			return emp;
		}
		return null;
	}

}
