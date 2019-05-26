package edu.handong.analysis.utils;

import java.util.ArrayList;
import java.io.*;

public class Utils {
	
	
	public static ArrayList<String> getLines(String file, boolean removeHeader) {
		BufferedReader br;
		String line;
		ArrayList<String> lines = new ArrayList();
		
		try {
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}br.close();
		} catch (IOException e) {
			System.out.println("The file path does not exist. Please check your CLI argument!");
			
		} 
		if(removeHeader) {
			lines.remove(0);
		}
		return lines;
	}
	
	public static void writeAFile(ArrayList<String> lines, String targetFileName){
		try {
			File file= new File(targetFileName);
			FileOutputStream fileStream = new FileOutputStream(file);
			DataOutputStream dataStream=new DataOutputStream(fileStream);
			
			for(String line:lines){
				dataStream.write((line+"\n").getBytes());
			}
			dataStream.close();
		fileStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

}
