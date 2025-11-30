package com.kk.LineMappingProject;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


//kulsum khan - 110139964
//phase 1: preprocessing

public class FileNormalization {
	
	//to test the method
	public static void main(String[] args) {
		
		//testing with a sample path
		//can change this to become another file path 
		String codePath = "C:\\Users\\kulsu\\OneDrive\\Documents\\Person.java";
		ArrayList<String> test = normalize(codePath);
		
		
		//print out contents of array list to check:
		for(String s : test) {
			System.out.println("this is the line:" + s + "\n\tthe index is: " + test.indexOf(s));
		}
		
		
		
		
	}
	
	//class will be used to turn code files into standardized text files for processing
	
	/* purpose: remove whitespaces around words, standardize spaces to 1 space (all indents and spaces between
	 * words must become 1 space), make everything lower case
	 * keep empty lines since theyre important for line mapping
	*/
	
	//method normalize(String codeSourceStringPath)
	//input: 1 code file path (as a string)
	//output: returns an array list with each line of the code file being normalized
	
	//example usage: ArrayList<String> normalizedFile = FileNormalization.normalize(/path/to/code.java);
	
	
	public static ArrayList<String> normalize(String codeSourceStringPath) {
		
		ArrayList<String> normalizedLines = new ArrayList<String>();
		
		Path codeSourcePath = Paths.get(codeSourceStringPath);

		if(!Files.isReadable(codeSourcePath)) {
			System.out.println("error");
			System.exit(0);
		}
		
		
		try(BufferedReader reader = Files.newBufferedReader(codeSourcePath)) {
			
			
			//read each line from source code file
			//normalize it
			//write normalized file into normalized text file 
			
			String currentLine;
			String nextLine = reader.readLine();
			
			while(nextLine != null) {
				currentLine = nextLine;
				nextLine = reader.readLine();
				//normalize: 
				
				//1. make it all lower case
				String normalizedLine = currentLine.toLowerCase();
				
				//2. replace all multiple spaces/tabs with a single space
				normalizedLine = normalizedLine.replaceAll("\\h+", " ");
				
				normalizedLines.add(normalizedLine);
				
			}
		}
		catch(IOException e) {
			System.err.println("error opening file " + e.getMessage());
			e.printStackTrace();
			
		}
		
		return normalizedLines;
		
	
		
	}

}
