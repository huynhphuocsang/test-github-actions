package com.luv2code.springmvc.controller;

import com.luv2code.springmvc.dao.StudentDao;
import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GradebookController {

	@Autowired
	private Gradebook gradebook;

	@Autowired
	StudentService studentService;

	@Autowired
	StudentDao studentDao;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getStudents(Model m) {
		List<CollegeStudent> students = studentService.getAllStudent();
		m.addAttribute("students",students);
		return "index";
	}

	@RequestMapping(value = "/",method = RequestMethod.POST)
	public String createNewStudent(Model model, @ModelAttribute("student") CollegeStudent student){
		studentService.createNewStudent(student.getFirstname(),student.getLastname(),student.getEmailAddress());
		List<CollegeStudent> students = studentService.getAllStudent();
		model.addAttribute("students",students);
		return "index";
	}

	@RequestMapping(path = "/delete/student/{id}",method = RequestMethod.GET)
	public String deleteStudent(Model model,@PathVariable("id") int id) {
		if(studentService.checkIfStudentIsNull(id)) return "error";

		studentService.deleteStudent(id);
		return "index";
	}


	@GetMapping("/studentInformation/{id}")
		public String studentInformation(@PathVariable int id, Model m) {
		return "studentInformation";
		}

}
