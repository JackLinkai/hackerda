package com.hackerda.platform.domain.course.timetable;

import com.hackerda.platform.domain.course.timetable.CourseTimeTableOverview;
import com.hackerda.platform.domain.course.timetable.CourseTimetableBO;
import com.hackerda.platform.domain.student.StudentUserBO;

import java.util.List;

public interface CourseTimetableRepository {

    CourseTimeTableOverview getByAccount(StudentUserBO studentUserBO, String termYear, int termOrder);

    CourseTimeTableOverview getByClassId(String classId, String termYear, int termOrder);

    void saveByStudent(List<CourseTimetableBO> tableList, StudentUserBO studentUserBO);

    void saveByClass(List<CourseTimetableBO> tableList, String classId);
}
