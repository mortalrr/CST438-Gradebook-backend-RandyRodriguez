package com.cst438.controllers;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentDTO;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;

@RestController
@CrossOrigin 
public class AssignmentController {
	
	@Autowired
	AssignmentRepository assignmentRepository;
	
	@Autowired
	CourseRepository courseRepository;
	
	@GetMapping("/assignment")
	public AssignmentDTO[] getAllAssignmentsForInstructor() {
		// get all assignments for this instructor
		String instructorEmail = "dwisneski@csumb.edu";  // user name (should be instructor's email) 
		List<Assignment> assignments = assignmentRepository.findByEmail(instructorEmail);
		AssignmentDTO[] result = new AssignmentDTO[assignments.size()];
		for (int i=0; i<assignments.size(); i++) {
			Assignment as = assignments.get(i);
			AssignmentDTO dto = new AssignmentDTO(
					as.getId(), 
					as.getName(), 
					as.getDueDate().toString(), 
					as.getCourse().getTitle(), 
					as.getCourse().getCourse_id());
			result[i]=dto;
		}
		return result;
	}
	
	
	//create assignment
	@PostMapping("/assignment")
	public void createAssignment(@RequestBody AssignmentDTO asDTO) {
	   Assignment as = new Assignment();
	    as.setName(asDTO.assignmentName());
	    as.setDueDate(Date.valueOf(asDTO.dueDate()));
	   
	   Course course = courseRepository.findById(asDTO.courseId()).orElseThrow (
			   () -> ResponseStatusException(HttpStatus.NOT_FOUND, "Course doesn't exist"));
			   
		as.setCourse(course);
		assignmentRepository.save(as);
			   
	}
	
	
	//update an assignment
	@PutMapping("/assignment/{Id}")
	public void updateAssignment(@PathVariable("id") int as_id, @RequestBody AssignmentDTO asDTO) {
		 Assignment as = assignmentRepository.findById(as_id).orElseThrow (
				   () -> ResponseStatusException(HttpStatus.NOT_FOUND, "Assignment doesn't exist"));
	    
	    as.setName(asDTO.assignmentName());
	    as.setDueDate(Date.valueOf(asDTO.dueDate()));
	    
	    Course course = courseRepository.findById(asDTO.courseId()).orElseThrow (
				   () -> ResponseStatusException(HttpStatus.NOT_FOUND, "Course doesn't exist"));
				   
			as.setCourse(course);
			assignmentRepository.save(as);
	    
	}
	
	
	// delete an assignment
    @DeleteMapping("/assignment/{assignment_id}")
    public void updateAssignment(@PathVariable("assign_id") int as_id, @RequestParam ("force") Optional<String> force) {
    	boolean hasForce = false;
    	Assignment as = assignmentRepository.findById(as_id).orElseThrow (
				   () -> ResponseStatusException(HttpStatus.NOT_FOUND, "Assignment doesn't exist"));
	    
	    if(force.get().equals("true")) {
	    	assignmentRepository.delete(as);
	    	hasForce = true;
	    
    } else {
    	assignmentRepository.findById(as_id).orElseThrow(
    			() -> ResponseStatusException(HttpStatus.NOT_FOUND, "Assignment has grades"));
    };
	
}
}
