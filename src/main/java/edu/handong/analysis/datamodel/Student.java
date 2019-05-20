package edu.handong.analysis.datamodel;
import java.util.*;

public class Student {
String studentId;
ArrayList<Course> coursesTaken;
HashMap<String,Integer> semestersByYearAndSemester;

public Student(String studentId) {
	
}
public void addCourse(Course newRecord) {
	
}
public HashMap<String,Integer> getSemestersByYearAndSemester(){
	return semestersByYearAndSemester;
}
public int getNumberCourseInNthsementer(int semester) {
	return semester;
	
}

}
