package edu.handong.analysis.datamodel;

import java.util.ArrayList;
import java.util.HashMap;

public class Student {
	private String studentId;
	private ArrayList<Course> coursesTaken; 
	private HashMap<String, Integer> semestersByYearAndSemester; 

	public Student(String studentld) {
		studentId = studentld;
		coursesTaken = new ArrayList<Course>();
		semestersByYearAndSemester = new HashMap<String, Integer>();
	}

	public void addCourse(Course newRecord) {
		coursesTaken.add(newRecord);
	}

	
	public HashMap<String, Integer> getSemestersByYearAndSemester() {
	int year, semester, coursenumOfSemester; 
	int tempCoursenum;
		String yearAndSemester;
		
		coursenumOfSemester = coursesTaken.size();
		
		ArrayList<Course> cloneCourses = (ArrayList<Course>) coursesTaken.clone();
		
		Course selectedCourse = cloneCourses.get(coursenumOfSemester - 1);
		year = selectedCourse.getYearTaken();
		semester = selectedCourse.getSemesterCourseTaken();
		yearAndSemester = String.valueOf(year) + "-" + String.valueOf(semester);
		
		if (!semestersByYearAndSemester.containsKey(yearAndSemester)) {
			tempCoursenum = semestersByYearAndSemester.size();
			semestersByYearAndSemester.put(yearAndSemester, tempCoursenum + 1);
		}
		return semestersByYearAndSemester;

	}

	public int getNumCourseInNthSementer(int semester) {
		
		int year, semesterCourse, NumCourseInNthSementer = 0;
		String yearToSemester;

		for (Course cour : coursesTaken) {
			year = cour.getYearTaken();
			semesterCourse = cour.getSemesterCourseTaken();
			yearToSemester = String.valueOf(year) + "-" + String.valueOf(semesterCourse);
			if (semestersByYearAndSemester.get(yearToSemester) == semester) {
				NumCourseInNthSementer++;
			}
		}
		return NumCourseInNthSementer;
	}
}


