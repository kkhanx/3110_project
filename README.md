# 3110_project
Automated Line Mapping Tool for Versioned Files

# Introduction
This project focuses on building a simple tool that compares two versions of a text file and shows how lines from the old file map to lines in the new one. The goal was to make it easier to track changes and understand how content moves or gets updated between versions. To do this, we used Java to read, normalize, and compare files, and then generated a clear mapping of where each original line ended up. The results show that the tool can efficiently identify matching lines, highlight changes, and give a straightforward view of edits, which can be useful for debugging, version tracking, or understanding code updates

# Authors
Kulsum Khan khan4n1@uwindsor.ca   
Najiya Ahmad ahmad29@uwindsor.ca   
Yusra Ahmed ahmed2z@uwindsor.ca (shoaib-peg also Yusra but different computer)  
Mahnoz Akhtari akhtari1@uwindsor.ca  
Hajar Alchamih alchamih@uwindsor.ca  

# Running Tests
Method 1: Command Line
To test the program first make sure you are in the correct directery then compile code by prompting this in terminal:  
  `javac con/kk/LineMappingProject/*.java`  
Then run the program by this prompt:  
  `java.kk.LineMapping.project.Main [path to file version 1].[FILE TYPE] (path to file version 2).[FILE TYPE]`  
Method 2: 
Method 3: Eclipse
After pulling the updated project into Eclipse, click the green Run button once to create the default configuration. 
Next, click the small arrow beside the Run button and select Run Configurations.
In the window that opens, select the configuration named “Main” under Java Application on the left panel.
Go to the Arguments tab and enter the two file paths you want to test in the Program Arguments box using the format: 
`[path to file version 1].[FILE TYPE] (path to file version 2).[FILE TYPE]` 
Click Apply, then Run.
Any time you want to test different files, simply return to the Arguments tab and update the two file paths.
