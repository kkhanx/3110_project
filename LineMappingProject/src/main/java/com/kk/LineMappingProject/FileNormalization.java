package com.kk.LineMappingProject;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

//kulsum khan - 110139964

public class FileNormalization {
	
	//to test the method
	public static void main(String[] args) {
		
	}
	
	//class will be used to turn code files into standardized text files for processing
	
	/*to do: remove whitepsaces around words, standardize spaces to 1 space (all indents and spaces between
	 * words must become 1 space), make everything lower case, ensure encoding is UTF-8
	*/
	
	//input: 1 code file, and String path of the text file (could be codeName.txt within same folder)
	//output: 1 text file, with everything normalized
	
	//example usage: File normalizedFile_v1 = FileNormalization.normalize(/path/to/codeFile_v1.java, /path/to/normalizedFile.txt);
	
	
	public static void normalize(File codeFile, String pathToNormalizedFile) {
		
		//open the file for processing
		Path codeSourcePath = codeFile.toPath();
		
		File normalizedFile = new File(pathToNormalizedFile);
		Path normalizedFilePath = normalizedFile.toPath();
		
		try(BufferedReader reader = Files.newBufferedReader(codeSourcePath);
			BufferedWriter writer = Files.newBufferedWriter(normalizedFilePath, StandardOpenOption.TRUNCATE_EXISTING)) {
			
			//read each line from source code file
			//normalize it
			//write normalized file into normalized text file 
			
			String lineOfText;
			
			while((lineOfText = reader.readLine()) != null) {
				
				//normalize: 
				
				//1. make it all lower case
				String normalizedLine = lineOfText.toLowerCase();
				
				//2. replace all multiple spaces with a single space
				normalizedLine = normalizedLine.replaceAll("\\s+", " ");
				
			}
		}
		catch(IOException e) {
			System.err.println("error opening file " + e.getMessage());
			e.printStackTrace();
			
		}
		
	}

}
