package com.luv2code.springmvc.service;

import com.luv2code.springmvc.dao.StudentDao;
import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.Student;
import com.luv2code.springmvc.models.StudentGrades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
//    private final StudentDao studentDao;
//
//    public StudentService(StudentDao studentDao){
//        this.studentDao = studentDao;
//    }

    @Autowired
    StudentDao studentDao;

    public CollegeStudent createNewStudent(String firstName, String lastName, String emailAddress){
        CollegeStudent student = new CollegeStudent(firstName,lastName,emailAddress);

        return studentDao.save(student);
    }

    public boolean checkIfStudentIsNull(int id){
        Optional<CollegeStudent> student = studentDao.findById(id);
        return student.isEmpty();
    }

    public void deleteStudent(int id){
        studentDao.deleteById(id);
    }
    public List<CollegeStudent> getAllStudent(){
        return studentDao.findAll();
    }
}
