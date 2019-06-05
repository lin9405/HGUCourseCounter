package edu.handong.analysis;

import java.util.*;
import java.io.*;

import edu.handong.analysis.datamodel.Course;
import edu.handong.analysis.datamodel.Student;
import edu.handong.analysis.utils.NotEnoughArgumentException;
import edu.handong.analysis.utils.Utils;

import org.apache.commons.cli.*;

public class HGUCoursePatternAnalyzer {
	String temp;
	String courseName;
	String input;
	String output;
	int analysis;
	String course;
	int startYear;
	int endYear;

	boolean help;

	private HashMap<String, Student> students;

	public void run(String[] args) {

		ArrayList<String> lines;
		Options options = createOptions();

		if (parseOptions(options, args)) {
			if (help) {
				printHelp(options);
				return;
			} else if(analysis ==2 && course ==null){
				printHelp(options);
				return;
			}
			
		//	System.out.println(	input + "\n" + output + "\n" + analysis + "\n" + course + "\n" + startYear + "\n" + endYear);
			if (analysis == 1) {
				lines = Utils.getLines(input, true, startYear, endYear);
				System.out.println(lines);
				students = loadStudentCourseRecords(lines);
				Map<String, Student> sortedStudents = new TreeMap<String, Student>(students);
				ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);
				Utils.writeAFile(linesToBeSaved, output);

			} else if (analysis == 2) {
				lines = Utils.getLines(input, true, startYear, endYear);
				System.out.println(lines);
				students = loadStudentCourseRecords(lines);
				Map<String, Student> sortedStudents = new TreeMap<String, Student>(students);

				ArrayList<String> linesToBeSaved = RateOfStudentTakenCertainCourse(sortedStudents, startYear, endYear,course);

				System.out.println(linesToBeSaved);
				Utils.writeAFile(linesToBeSaved, output);


			}

		}
		// System.out.println(input+"\n"+output+"\n"+analysis+"\n"+course+"\n"+startYear+"\n"+endYear);

	}

	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, args);
			input = cmd.getOptionValue("i");
			output = cmd.getOptionValue("o");
			analysis = Integer.parseInt(cmd.getOptionValue("a"));
			course = cmd.getOptionValue("c");
			startYear = Integer.parseInt(cmd.getOptionValue("s"));
			endYear = Integer.parseInt(cmd.getOptionValue("e"));
			help = cmd.hasOption("h");

		} catch (Exception e) {
			printHelp(options);
			// System.out.println("??");
			return false;
		}

		return true;
	}

	private Options createOptions() {
		Options options = new Options();

		options.addOption(Option.builder("i").longOpt("input").desc("Set an input file path").required().hasArg()
				.argName("input path").build());

		options.addOption(Option.builder("o").longOpt("output").desc("Set an output file path").required().hasArg()
				.argName("output path").build());

		options.addOption(Option.builder("a").longOpt("analysis")
				.desc("1: Count courses per semester, 2: Count per course name and year").required().hasArg()
				.argName("Analysis option").build());

		options.addOption(Option.builder("c").longOpt("coursecode").desc("Course code for 'a 2' option").required(analysis ==2 )
				.hasArg().argName("course code").build());

		options.addOption(Option.builder("s").longOpt("startyear").desc("Set the start year for analysis e.g., -s 2002")
				.required().hasArg().argName("Start year foranalysis").build());

		options.addOption(Option.builder("e").longOpt("endyear").desc("Set the end year for analysis e.g., -e 2005")
				.required().hasArg().argName("End year for analysis").build());

		options.addOption(Option.builder("h").longOpt("help").desc("Show a Help page").argName("Help").build());

		return options;
	}

	/**
	 * This method create HashMap<String,Student> from the data csv file. Key is a
	 * student id and the corresponding object is an instance of Student. The
	 * Student instance have all the Course instances taken by the student.
	 * 
	 * @param lines
	 * @return
	 */
	private HashMap<String, Student> loadStudentCourseRecords(ArrayList<String> lines) {

		HashMap<String, Student> registeredStudent = new HashMap<String, Student>();

		String splitStudent;
		String idStudent;// trimStudent => idStudnet

		for (String line : lines) {
			splitStudent = line.split(",")[0];
			idStudent = splitStudent.trim();
			if (registeredStudent.containsKey(idStudent)) {
				Course cour = new Course(line);
				registeredStudent.get(idStudent).addCourse(cour);
				registeredStudent.get(idStudent).getSemestersByYearAndSemester();
			} else {
				Student stud = new Student(idStudent);
				Course cour = new Course(line);
				stud.addCourse(cour);
				stud.getSemestersByYearAndSemester();
				registeredStudent.put(idStudent, stud);
			}

		}
		return registeredStudent;
		// do not forget to return a proper variable.
	}

	/**
	 * This method generate the number of courses taken by a student in each
	 * semester. The result file look like this: StudentID,
	 * TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester
	 * 0001,14,1,9 0001,14,2,8 ....
	 * 
	 * 0001,14,1,9 => this means, 0001 student registered 14 semeters in total. In
	 * the first semeter (1), the student took 9 courses.
	 * 
	 * 
	 * @param sortedStudents
	 * @return
	 */
	private ArrayList<String> countNumberOfCoursesTakenInEachSemester(Map<String, Student> sortedStudents) {
		ArrayList<String> result = new ArrayList<String>();
		String resultLine;
		String headOfResult = "StudentID, TotalNumberOfSemestersRegistered, Semester,NumCoursesTakenInTheSemester";
		result.add(headOfResult);

		int TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester;
		int totalOfSemester;
		int tempTotalSemester;
		int courseNumOfSemester;

		for (String studentID : sortedStudents.keySet()) {
			totalOfSemester = sortedStudents.get(studentID).getSemestersByYearAndSemester().values().size();

			for (int semester = 1; semester <= totalOfSemester; semester++) {
				int changed = Integer.parseInt(studentID);
				String StudentID = String.format("%04d", changed);
		//		System.out.println(StudentID);
				TotalNumberOfSemestersRegistered = sortedStudents.get(studentID).getSemestersByYearAndSemester().size();
				NumCoursesTakenInTheSemester = sortedStudents.get(studentID).getNumCourseInNthSementer(semester);
				
				resultLine = StudentID + "," + String.valueOf(TotalNumberOfSemestersRegistered) + ","
						+ String.valueOf(semester) + "," + String.valueOf(NumCoursesTakenInTheSemester);
				result.add(resultLine);
			}
		}
		return result;// do not forget to return a proper variable.
	}

	private ArrayList<String> RateOfStudentTakenCertainCourse(Map<String, Student> sortedStudents, int startYear,
			int endYear, String courseCode) {

		ArrayList<String> result = new ArrayList<String>();
		String resultLine;
		String headOfResult = "Year,Semester,CouseCode,CourseName,TotalStudents,StudentsTaken,Rate";
		result.add(headOfResult);

		String yearAndSemester;

		int totalStudent = 0;
		int NumOfStudentTakenCourse = 0;
		float floatRate;
		String stringRate;
	

		for (int start = startYear; start <= endYear; start++) {
			for (int semester = 1; semester < 5; semester++) {
				yearAndSemester = start + "-" + semester;
			//	System.out.println(yearAndSemester);

				totalStudent = 0;
				NumOfStudentTakenCourse = 0;

				for (String studentID : sortedStudents.keySet()) {
					Student student = sortedStudents.get(studentID);
					if(student.getTotalStudentOfyearAndSemester(yearAndSemester)) {
						totalStudent++;
						
						String name = student.getCourseName(courseCode);
						if (name != null) {
							temp = student.getCourseName(courseCode);
						}
					}
					if (student.getStudentTakeSameCourse(yearAndSemester, courseCode)) {
						NumOfStudentTakenCourse++;
					}

				}

				floatRate = (float) ((float) NumOfStudentTakenCourse / (float) totalStudent * 100.0);
				stringRate = String.format("%.1f", floatRate) + "%";
				courseName = temp;
				
				resultLine = yearAndSemester + "," + semester + "," + courseCode + "," + courseName + ","
						+ totalStudent + "," + NumOfStudentTakenCourse + "," + stringRate;

					result.add(resultLine);
				}

			}
	
		return result;
	}

	private void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		String header = "HGU Course Analyzer";
		String footer = ""; // Leave this empty.
		formatter.printHelp("HGUCourseCounter", header, options, footer, true);
	}

}


