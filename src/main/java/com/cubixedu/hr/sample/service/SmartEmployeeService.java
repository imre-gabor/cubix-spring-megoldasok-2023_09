package com.cubixedu.hr.sample.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cubixedu.hr.sample.config.HrConfigProperties;
import com.cubixedu.hr.sample.config.HrConfigProperties.Smart;
import com.cubixedu.hr.sample.model.Employee;

@Service
public class SmartEmployeeService extends AbstractEmployeeService {

	@Autowired
	HrConfigProperties config;

	@Override
	public int getPayRaisePercent(Employee employee) {
		
		double yearsWorked = ChronoUnit.DAYS.between(employee.getDateOfStartWork(), LocalDateTime.now()) / 365.0;
		
		Smart smartConfig = config.getSalary().getSmart();
		
//		if(yearsWorked > smartConfig.getLimit3())
//			return smartConfig.getPercent3();
//		
//		if(yearsWorked > smartConfig.getLimit2())
//			return smartConfig.getPercent2();
//		
//		if(yearsWorked > smartConfig.getLimit1())
//			return smartConfig.getPercent1();
//		
//		return 0;
		
		
		TreeMap<Double, Integer> limits = smartConfig.getLimits();
//opcionális 1. megoldás		
//		Integer maxLimit = null;
//		
//		for(var entry: limits.entrySet()) {
//			if(yearsWorked > entry.getKey())
//				maxLimit = entry.getValue();
//			else
//				break;
//		}
//		
//		return maxLimit == null ? 0 : maxLimit; 
		
//opcionális 2. megoldás		
//		Optional<Double> optionalMax = limits.keySet().stream()
//		.filter(k -> yearsWorked > k)
//		.max(Double::compare);
//		
//		return optionalMax.isEmpty() ? 0 : limits.get(optionalMax.get());
		
		//opcionális 3. megoldás		
		Entry<Double, Integer> floorEntry = limits.floorEntry(yearsWorked);
		return floorEntry == null ? 0 : floorEntry.getValue();
		
	}

}
