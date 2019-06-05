package edu.handong.analysis;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException  {
		HGUCoursePatternAnalyzer analyzer = new HGUCoursePatternAnalyzer();
		analyzer.run(args);
	}
}