package com.example.demo;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class EmployeeResource {
	
	
	final static Logger logger = Logger.getLogger(EmployeeResource.class.getName());
	
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@GetMapping("/employees")
	public List<Employee> retrieveAllEmployees() {
		
		logger.info("employees are retrieved");
		return employeeRepository.findAll();
		
	}

	@GetMapping("/employees/{id}")
	public Employee retrieveEmployee(@PathVariable long id) throws EmployeeNotFoundException {
		Optional<Employee> employee = employeeRepository.findById(id);

		if (!employee.isPresent())
			
		{
			logger.error("Sorry Employee is not found of ID "+id);
			throw new EmployeeNotFoundException("Sorry, we found no employee of ID "+id);
		
		}
		return employee.get();
	}

	@DeleteMapping("/employees/{id}")
	public void deleteEmployee(@PathVariable long id) {
		employeeRepository.deleteById(id);
	}

	@PostMapping("/employees")
	public ResponseEntity<Object> createEmployee(@RequestBody Employee employee) {
		Employee savedStudent = employeeRepository.save(employee);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedStudent.getId()).toUri();

		return ResponseEntity.created(location).build();

	}
	
	@PutMapping("/employees/{id}")
	public ResponseEntity<Object> updateEmployee(@RequestBody Employee employee, @PathVariable long id) {

		Optional<Employee> employeeOptional = employeeRepository.findById(id);

		if (!employeeOptional.isPresent())
			return ResponseEntity.notFound().build();
		employee.setId(id);
		
		employeeRepository.save(employee);

		return ResponseEntity.noContent().build();
	}
}
