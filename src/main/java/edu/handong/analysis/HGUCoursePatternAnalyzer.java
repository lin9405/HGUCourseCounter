package edu.handong.analysis;

import java.util.*;
import java.io.*;



import edu.handong.analysis.datamodel.Course;
import edu.handong.analysis.datamodel.Student;
import edu.handong.analysis.utils.NotEnoughArgumentException;
import edu.handong.analysis.utils.Utils;


import org.apache.commons.cli.CommandLine;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;





public class HGUCoursePatternAnalyzer {
	String path;
	boolean verbose;
	boolean help;
	boolean full;
	
	private HashMap<String, Student> students;

	/**
	 * This method runs our analysis logic to save the number courses taken by each
	 * student per semester in a result file. Run method must not be changed!!
	 * 
	 * @param args
	 */
	public void run(String[] args) {
		
		


		Options options = createOptions();
		if(parseOptions(options, args)){
			if (help){
				System.out.println("!!!");
				printHelp(options);
				return;
			}
			
			// path is required (necessary) data so no need to have a branch.
			System.out.println("You provided \"" + path + "\" as the value of the option p");
			
			// TODO show the number of files in the path
			
			if(verbose) {
		
				System.out.println("");
				System.out.println("Your program is terminated. (This message is shown because you turned on -v option!");
			}
		
		}
		
	/*
		try {
			// when there are not enough arguments from CLI, it throws the
			// NotEnoughArgmentException which must be defined by you.
			if (args.length < 2)
				throw new NotEnoughArgumentException();
		} catch (NotEnoughArgumentException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}

*/
		String dataPath = args[0]; // csv file to be analyzed
		String resultPath = args[1]; // the file path where the results are saved.
		ArrayList<String> lines = Utils.getLines(dataPath, true);

		students = loadStudentCourseRecords(lines);

		// To sort HashMap entries by key values so that we can save the results by
		// student ids in ascending order.
		Map<String, Student> sortedStudents = new TreeMap<String, Student>(students);

		// Generate result lines to be saved.
		ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);

		// Write a file (named like the value of resultPath) with linesTobeSaved.
		Utils.writeAFile(linesToBeSaved, resultPath);
	}

	


	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, args);

			path = cmd.getOptionValue("p");
			verbose = cmd.hasOption("v");
			help = cmd.hasOption("h");

		} catch (Exception e) {
			printHelp(options);
			System.out.println("??");
			return false;
		}

		return true;
	}

	
private Options createOptions() {
	Options options = new Options();

	// add options by using OptionBuilder
	options.addOption(Option.builder("p").longOpt("path")
			.desc("Set a path of a directory or a file to display")
			.hasArg()
			.argName("Path name to display")
			.required()
			.build());

	// add options by using OptionBuilder
	options.addOption(Option.builder("v").longOpt("verbose")
			.desc("Display detailed messages!")
			//.hasArg()     // this option is intended not to have an option value but just an option
			.argName("verbose option")
			//.required() // this is an optional option. So disabled required().
			.build());
	
	// add options by using OptionBuilder
	options.addOption(Option.builder("h").longOpt("help")
	        .desc("Help")
	        .build());
	

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
		String headOfResult = "StudentID, TotalNumberOfSemestersRegistered, Semester,NumCoursesTakenInTheSemester";
		int TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester;
		int totalOfSemester;
		result.add(headOfResult);
		int tempTotalSemester;
		int courseNumOfSemester;
		for (String studentID : sortedStudents.keySet()) 
		{
			totalOfSemester = sortedStudents.get(studentID).getSemestersByYearAndSemester().values().size();

			for (int semester = 1; semester <= totalOfSemester; semester++) 
			{
				int changed = Integer.parseInt(studentID);
				String StudentID = String.format("%04d", changed);
				System.out.println(StudentID);
				TotalNumberOfSemestersRegistered = sortedStudents.get(studentID).getSemestersByYearAndSemester().size();
				NumCoursesTakenInTheSemester = sortedStudents.get(studentID).getNumCourseInNthSementer(semester);
				resultLine = StudentID + "," + String.valueOf(TotalNumberOfSemestersRegistered) + ","
						+ String.valueOf(semester) + "," + String.valueOf(NumCoursesTakenInTheSemester);
				result.add(resultLine);
			}
		}
		return result;// do not forget to return a proper variable.
	}
	
	
	
	
	private void printHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		String header = "CLI test program";
		String footer ="\nPlease report issues at https://github.com/lifove/CLIExample/issues";
		formatter.printHelp("CLIExample", header, options, footer, true);
	}

}


