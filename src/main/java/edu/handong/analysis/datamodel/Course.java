
package edu.handong.analysis.datamodel;

import java.util.*;

public class Course {
	String studentId;
	String yearMonthGraduated;
	String firstMajor;
	String secondMajor;
	String courseCode;
	String courseName;
	String courseCredit;
	int yearTaken;
	int semesterCourseTaken;

	public Course(String line) {

		ArrayList<String> infoStudent = new ArrayList<String>();

		String[] splitTrimInfo = line.trim().split(", ");

		for (String ele : splitTrimInfo) {
			infoStudent.add(ele);
		}

		studentId = infoStudent.get(0);
		yearMonthGraduated = infoStudent.get(1);
		firstMajor = infoStudent.get(2);
		secondMajor = infoStudent.get(3);
		courseCode = infoStudent.get(4);
		courseName = infoStudent.get(5);
		courseCredit = infoStudent.get(6);
		yearTaken = Integer.parseInt(infoStudent.get(7));
		semesterCourseTaken = Integer.parseInt(infoStudent.get(8));
	}

	public int getYearTaken() {
		return yearTaken;

	}

	public int getSemesterCourseTaken() {
		return semesterCourseTaken;
	}
}
	