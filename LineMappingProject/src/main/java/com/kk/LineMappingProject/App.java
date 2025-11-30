package com.kk.LineMappingProject;

import java.util.ArrayList;

/**
 * Hello world!
 *
 */

//testing the project

public class App
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        // Adding DiffAlgorithm to main
        if (args.length < 2) {
            System.out.println("Usage: java TestDiffWithPaths <file1_path> <file2_path>");
            System.out.println("Example: java TestDiffWithPaths /path/to/version1.java /path/to/version2.java");
            return;
        }
        
        String file1 = args[0];
        String file2 = args[1];
        
        // Call the separate test class
        TestDiffWithPaths.DiffTester.testFiles(file1, file2);
    	
    	
//    	if(args.length < 2) {
//    		System.out.println("java App /path/to/version1.java /path/to/version2.java");
//    		return;
//    	}
//    	
//    	//phase 1:
//    	ArrayList<String> normalizedFile1 = FileNormalization.normalize(args[0]);
//    	ArrayList<String> normalizedFile2 = FileNormalization.normalize(args[1]);
//    	
//    	
//    	//phase 2:
    }
}

//author name: kulsum khan
//Author: Hajer
//Author: Yusra Ahmed, ID: 110106816
//Author: Najiya Ahmad