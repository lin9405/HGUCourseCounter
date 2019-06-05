
package edu.handong.analysis.utils;

import java.util.ArrayList;
import java.io.*;
import org.apache.commons.csv.*;

public class Utils {
	String ID;
	String Graduated;
	String firstMajor;
	String secondMajor;
	String courseCode;
	String courseName;
	String courseCredit;
	String year;
	String semesterCourseTaken;

	public static ArrayList<String> getLines(String file, boolean removeHeader, int startYear, int endYear) {
		ArrayList<String> lines = new ArrayList<String>();

		if (removeHeader) {

			try {

				Reader in = new FileReader(file);
				CSVParser csvParser = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
				String line;

				for (CSVRecord csvRecord : csvParser) {

					String ID = csvRecord.get(0).trim();
					String Graduated = csvRecord.get(1).trim();
					String firstMajor = csvRecord.get(2).trim();
					String secondMajor = csvRecord.get(3).trim();
					String courseCode = csvRecord.get(4).trim();
					String courseName = csvRecord.get(5).trim();
					String courseCredit = csvRecord.get(6).trim();
					int yearTake = Integer.parseInt(csvRecord.get(7).trim());
					String semesterCourseTaken = csvRecord.get(8).trim();

					if (yearTake >= startYear && yearTake <= endYear) {
						line = ID + ',' + Graduated + ',' + firstMajor + ',' + secondMajor + ',' + courseCode + ','
								+ courseName + ',' + courseCredit + ',' + String.valueOf(yearTake) + ','
								+ semesterCourseTaken;
						lines.add(line);
					}
				}
				if (removeHeader) {
					lines.remove(0);
				}
			} catch (IOException e) {
				System.out.println("The file path does not exist. Please check your CLI argument!");
				System.exit(0);
			}

		}
		return lines;

	}

	public static void writeAFile(ArrayList<String> lines, String targetFileName) {
		PrintWriter outputStream = null;

		try {
			outputStream = new PrintWriter(targetFileName);
		} catch (FileNotFoundException e) {
			File tmp = new File(targetFileName);
			tmp.getParentFile().mkdirs();
			try {
				tmp.createNewFile();
				outputStream = new PrintWriter(targetFileName);
			} catch (IOException e1) {

			}
		}
		for (String out : lines) {
			outputStream.println(out);
		}
		outputStream.close();
	}
}
