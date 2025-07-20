package com.luv2code.springmvc;

import com.luv2code.springmvc.dao.StudentDao;
import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.service.StudentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@TestPropertySource("/application.properties")
@SpringBootTest
class MvcTestingExampleApplicationTest {

    @Autowired
    StudentService studentService;

    @Autowired
    StudentDao studentDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUpEachRun(){
        jdbcTemplate.execute("INSERT INTO STUDENT(id,firstname,lastname,email_address) VALUES(99,'sang','huynh','sang@gmail.com')");
    }

    @AfterEach
    public void cleanUpDb(){
        jdbcTemplate.execute("DELETE FROM student");
    }

    @Test
    @DisplayName("Validate saved information")
    public void testCreateStudentService(){
        CollegeStudent student = studentService.createNewStudent("test","huynh","test@gmail.com");
        CollegeStudent foundStudent = studentDao.findByEmailAddress("test@gmail.com");
        assertEquals(student.getEmailAddress(),foundStudent.getEmailAddress(),"Email must be identical");
    }

    @Test
    @DisplayName("Check if student is null")
    public void testIsStudentNull(){
        assertFalse(studentService.checkIfStudentIsNull(99));
        assertTrue(studentService.checkIfStudentIsNull(0));
    }

    @Test
    @DisplayName("Test delete student with id")
    public void testDeleteStudent(){
        studentService.deleteStudent(99);
        Optional<CollegeStudent> foundStudent = studentDao.findById(99);
        assertFalse(foundStudent.isPresent());
    }


    @Test
    @DisplayName("Could not found student with id: 88")
    public void notFoundStudentWithNewId(){
        Optional<CollegeStudent> student = studentDao.findById(88);
        assertFalse(student.isPresent(),"Student dost not exist");
    }

    @Test
    @Sql("/insertDB.sql")
    @DisplayName("Check all student")
    public void testFindAllStudent(){
        List<CollegeStudent> students = studentService.getAllStudent();
        List<CollegeStudent> foundStudents = studentDao.findAll();

        assertEquals(5,students.size());
        assertEquals(students.size(),foundStudents.size());
    }
}