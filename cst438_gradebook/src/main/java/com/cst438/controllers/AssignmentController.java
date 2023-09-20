package com.cst438.controllers;

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
	
	@PostMapping("/assignment")
	public ResponseEntity<Assignment> createAssignment(@RequestBody AssignmentDTO assignmentDTO) {
	    // Retrieve the course based on course ID
	    Optional<Course> optionalCourse = courseRepository.findById(assignmentDTO.courseId());
	    if (optionalCourse.isPresent()) {
	        Course course = optionalCourse.get();
	        Assignment newAssignment = new Assignment(assignmentDTO.assignmentName(), assignmentDTO.dueDate(), course);
	        assignmentRepository.save(newAssignment);
	        return new ResponseEntity<>(newAssignment, HttpStatus.CREATED);
	    } else {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course is not found");
	    }
	}
	
	@PutMapping("/assignment/{assignmentId}")
	public ResponseEntity<Assignment> updateAssignment(
	        @PathVariable int assignmentId,
	        @RequestBody AssignmentDTO updatedAssignmentDTO) {
	    Optional<Assignment> optionalAssignment = assignmentRepository.findById(assignmentId);
	    if (optionalAssignment.isPresent()) {
	        Assignment assignment = optionalAssignment.get();
	        assignment.setName(updatedAssignmentDTO.assignmentName());
	        assignment.setDueDate(updatedAssignmentDTO.dueDate());
	        // Update other assignment properties as needed
	        assignmentRepository.save(assignment);
	        return new ResponseEntity<>(assignment, HttpStatus.OK);
	    } else {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Assignment not found");
	    }
	}
	
	
	// Delete an assignment by ID
    @DeleteMapping("/assignment/{assignmentId}")
    public ResponseEntity<String> deleteAssignment(
            @PathVariable int assignmentId,
            @RequestParam(value = "force", required = false, defaultValue = "false") boolean force) {
        Optional<Assignment> optionalAssignment = assignmentRepository.findById(assignmentId);
        if (optionalAssignment.isPresent()) {
            Assignment assignment = optionalAssignment.get();

            // Check if the assignment has grades
            if (!force && assignment.hasGrades()) {
                return new ResponseEntity<>("Assignment has grades. Use force=true to delete.", HttpStatus.BAD_REQUEST);
            }

            assignmentRepository.delete(assignment);
            return new ResponseEntity<>("The assignment is deleted successfully", HttpStatus.NO_CONTENT);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The assignment not found");
        }
    }
	
}
