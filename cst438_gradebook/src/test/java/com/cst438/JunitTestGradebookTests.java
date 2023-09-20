import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cst438.controllers.AssignmentController;
import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentDTO;
import com.cst438.domain.AssignmentGradeRepository;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;

import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class JunitTestGradebookTests {

    @InjectMocks
    private AssignmentController assignmentController;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private AssignmentGradeRepository assignmentGradeRepository;
    
    
    @Test
    public void listAssignment() throws Exception{
    	MockHttpServletResponse response;
        
        AssignmentDTO asDTO = new AssignmentDTO(4,"Assignment 2", "2023-9-19", "CST 438 - Software Engineering", 31045);
        response = mvc.perform(MockMvcRequestBuilders.post("/assignment").accept(MediaType.APPLICATION_JSON)
        		.content(asJsonString(asDTO)).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        
        Assignment as = assignmentRepository.findById(3).get();
        assertEquals(as.getName().contentEquals("Assignment 2"), true);
    }

    @Test
    public void createAssignment() throws Exception {
        MockHttpServletResponse response;
        
        AssignmentDTO asDTO = new AssignmentDTO(4,"Assignment 2", "2023-9-19", "CST 438 - Software Engineering", 31045);
        response = mvc.perform(MockMvcRequestBuilders.post("/assignment").accept(MediaType.APPLICATION_JSON)
        		.content(asJsonString(asDTO)).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        
        Assignment as = assignmentRepository.findById(3).get();
        assertEquals(as.getName().contentEquals("Assignment 2"), true);
    }
    
    
    @Test
    public void updateAssignment() throws Exception{
    	AssignmentDTO asDTO = new AssignmentDTO(4,"Assignment 2", "2023-9-19", "CST 438 - Software Engineering", 31045);
    	response = mvc.perform(MockMvcRequestBuilders.post("/assignment").accept(MediaType.APPLICATION_JSON)
        		.content(asJsonString(asDTO)).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        
        Assignment as = assignmentRepository.findById(3).get();
        assertEquals(as.getName().contentEquals("Assignment 3"), true);
    }

    @Test
    public void deleteAssignment() throws Exception{
    	MockHttpServletResponse response;
    	
    	response = mvc.perform(MockMvcRequestBuilders.delete("/assignment/2?force=true").accept(MediaType.APPLICATION_JSON))
        		.andReturn().getRespnse();
    	
    	assertEquals(200, response.getStatus());
    	
    	Assignment as = assignmentRepository.findById(2).orElse(null);
    	assertEquals(as, null);
    }

   
}
