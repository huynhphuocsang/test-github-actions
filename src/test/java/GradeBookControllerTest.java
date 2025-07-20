import com.luv2code.springmvc.dao.StudentDao;
import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.service.StudentService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestPropertySource("/application.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class GradeBookControllerTest {

    private static MockHttpServletRequest request;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Mock
    private StudentService studentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentDao studentDao;

    @BeforeAll
    public static void setUpHttpServletRequest(){
        request = new MockHttpServletRequest();
        request.setParameter("firstname","sang");
        request.setParameter("lastname","huynh");
        request.setParameter("emailAddress","sang@gmail.com");
    }

    @BeforeEach
    public void setUpEachRun(){
        jdbcTemplate.execute("INSERT INTO STUDENT(id,firstname,lastname,email_address) VALUES(99,'sang','huynh','hpsang@gmail.com')");
    }

    @AfterEach
    public void cleanUpDb(){
        jdbcTemplate.execute("DELETE FROM student");
    }

    @Test
    @DisplayName("Test get all student with http GET request")
    public void getStudentsWithHttpRequest() throws Exception {
        CollegeStudent student1 = new CollegeStudent("sang","huynh","sang@gmail.com");
        CollegeStudent student2 = new CollegeStudent("tuyen","nguyen","tuyen@gmail.com");

        List<CollegeStudent> studentList = Arrays.asList(student1,student2);

        when(studentService.getAllStudent()).thenReturn(studentList);

        assertIterableEquals(studentList,studentService.getAllStudent());

        MvcResult mvcResult = mockMvc.perform((MockMvcRequestBuilders.get("/")))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView,"index");
    }

    @Test
    @DisplayName("Test create new student with http POST request")
    public void createNewStudentWithHttPostRequest() throws Exception {
        CollegeStudent student1 = new CollegeStudent("sang","huynh","sang@gmail.com");
        CollegeStudent student2 = new CollegeStudent("tuyen","nguyen","tuyen@gmail.com");

        List<CollegeStudent> studentList = Arrays.asList(student1,student2);

        when(studentService.getAllStudent()).thenReturn(studentList);
        assertIterableEquals(studentList, studentService.getAllStudent());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("firstname",request.getParameter("firstname"))
                        .param("lastname",request.getParameter("lastname"))
                        .param("emailAddress",request.getParameter("emailAddress")))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView,"index");

        CollegeStudent foundStudent = studentDao.findByEmailAddress(request.getParameter("emailAddress"));

        assertNotNull(foundStudent);
    }

    @Test
    @DisplayName("Delete Student success with HTTP request")
    public void deleteStudentSuccess() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/delete/student/{id}",99))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(modelAndView,"index");

        boolean foundStudent = studentDao.findById(99).isPresent();
        assertFalse(foundStudent);
    }

    @Test
    @DisplayName("Delete Student fail with HTTP request")
    public void deleteStudentFail() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/delete/student/{id}",0))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView,"error");

    }

}
