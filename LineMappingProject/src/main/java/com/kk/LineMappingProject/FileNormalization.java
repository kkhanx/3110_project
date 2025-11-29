package com.kk.LineMappingProject;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.commons.io.FilenameUtils;


//kulsum khan - 110139964

public class FileNormalization {
	
	//to test the method
	public static void main(String[] args) {
		
		//testing with a sample path
		//can change this to become another file path 
		String codePath = "C:\\Users\\kulsu\\OneDrive\\Documents\\Person.java";
		normalize(codePath);
		
		
		
		
	}
	
	//class will be used to turn code files into standardized text files for processing
	
	/*to do: remove whitespaces around words, standardize spaces to 1 space (all indents and spaces between
	 * words must become 1 space), make everything lower case
	*/
	
	//input: 1 code file path (as a string)
	//output: returns an array list with each line of the code file being normalized
	
	//example usage: ArrayList<String> normalizedFile = FileNormalization.normalize(/path/to/code.java);
	
	
	public static void normalize(String codeSourceStringPath) {
		
		Path codeSourcePath = Paths.get(codeSourceStringPath);
		
		String codeFileExt = codeSourcePath.getFileName().toString();
		String codeFileNoExt = FilenameUtils.getBaseName(codeFileExt);
		
		String normalizedFileName = "normalized" + codeFileNoExt + ".txt";
		
		Path normalizedFilePath = codeSourcePath.getParent().resolve(normalizedFileName);
		
		if(!Files.isReadable(codeSourcePath)) {
			System.out.println("error");
			System.exit(0);
		}
		
		
		try(BufferedReader reader = Files.newBufferedReader(codeSourcePath);
			BufferedWriter writer = Files.newBufferedWriter(normalizedFilePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
			
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
				
				//2. replace all multiple spaces with a single space
				normalizedLine = normalizedLine.replaceAll("\\h+", " ");
				
				
				writer.write(normalizedLine);
				
				if(nextLine != null) {
					writer.newLine(); //dont want to add an extra newLine at the end
					
				}
				
			}
		}
		catch(IOException e) {
			System.err.println("error opening file " + e.getMessage());
			e.printStackTrace();
			
		}
		
	}

}
