package com.kk.LineMappingProject;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

//Phase 1: Preprocessing
//Kulsum Khan - 110139964

public class FileNormalization {
	
	
	//Method normalize(String codeSourceStringPath)

	//Input: 1 code file path (as a string)
	//Output: returns an array list with each line of the code file being normalized
	
	//Example usage: ArrayList<String> normalizedFile = FileNormalization.normalize(/path/to/code.java);
	
	public static ArrayList<String> normalize(String codeSourceStringPath) {
		
		ArrayList<String> normalizedLines = new ArrayList<String>();
		
		Path codeSourcePath = Paths.get(codeSourceStringPath);

		if(!Files.isReadable(codeSourcePath)) {
			System.err.println("File is Not Readable");
		}
		
		
		try(BufferedReader reader = Files.newBufferedReader(codeSourcePath)) {
			
			String currentLine;
			String nextLine = reader.readLine();
			
			while(nextLine != null) { 
				//read each line from source code file
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
			System.err.println("Error opening BufferedReader for File " + e.getMessage());
			e.printStackTrace();
			
		}
		
		return normalizedLines;
		
	
		
	}

}
