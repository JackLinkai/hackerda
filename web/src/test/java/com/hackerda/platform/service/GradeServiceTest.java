package com.hackerda.platform.service;

import com.hackerda.platform.controller.vo.GradeResultVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GradeServiceTest {

    @Autowired
    private GradeService gradeService;

    @Test
    public void getGrade() {

        GradeResultVo grade = gradeService.getGrade(2019025521);

        System.out.println(grade.getOptionalCourseCredit());
    }
}