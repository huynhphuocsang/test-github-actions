package com.luv2code.springmvc.dao;

import com.luv2code.springmvc.models.CollegeStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentDao extends JpaRepository<CollegeStudent,Integer> {
    public CollegeStudent findByEmailAddress(String email);
}
