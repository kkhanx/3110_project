# 3110_project
Automated Line Mapping Tool for Versioned Files

# Purpose
Compares two versions of a file and shows how lines from the older version file map to lines in the newer version. Makes it easier to track changes and understand how content moves or gets updated between versions.
The Java programming language was used to create the tool. It reads, normalizes, compares lines in each file, and then generates a clear mapping of where each original line ended up. The tool can be useful for debugging, version tracking, and understanding code updates.

# Authors
- Kulsum Khan khan4n1@uwindsor.ca
- Najiya Ahmad ahmad29@uwindsor.ca
- Yusra Z. Ahmed ahmed2z@uwindsor.ca (shoaib-peg also Yusra but different computer)
- Mahnoz Akhtari akhtari1@uwindsor.ca
- Hajar Alchamih alchamih@uwindsor.ca  

# Installation
## Prerequisites
Java (version 8 or higher)

Maven (version 3.9.11 or higher) -> ensure PATH and Environment variables are set

## Install Project
Clone repository onto local machine.

`git clone https://github.com/kkhanx/3110_project.git `

# Execution Methods

>[!NOTE] 
>- The file path inputs for both files must include an extension signifying type of file (ex: .java, .py etc.)
>- Both files must be readable by the program.


## Method 1.1: Command Prompt/Terminal
1. Use command cd to move into the correct directory (see: Example Usage)
2. Compile the code:
   `javac com/kk/LineMappingProject/*.java`
3. Execute the program:
   `java com.kk.LineMappingProject.Main [path to file version 1] [path to file version 2]`

##### Example Usage of Method 1.1
```bash
> cd .../3110_project/LineMappingProject/src/main/java
> javac com/kk/LineMappingProject/*.java
> java com.kk.LineMappingProject.Main "C:\...\todo-1_v1.html" "C:\...\todo-1_v2.html"

```

   
## Method 1.2: Command Prompt/Terminal
1. Use command cd to move into the directory for LineMappingProject
2. Compile the project:
   `mvn clean compile`
3. Execute the program:
   `java -cp target/classes com.kk.LineMappingProject.Main [path to file version 1] [path to file version 2]`

##### Example Usage of Method 1.2
```bash
> cd .../3110_project/LineMappingProject
> mvn clean compile
> java -cp target/classes com.kk.LineMappingProject.Main "C:\...\todo-1_v1.html" "C:\...\todo-1_v2.html"

```

## Method 2: Eclipse IDE
1. Import the project into Eclipse IDE
   
After pulling the updated project into Eclipse, click the green Run button once to create the default configuration.  
Next, click the small arrow beside the Run button and select Run Configurations.  
In the window that opens, select the configuration named “Main” under Java Application on the left panel.  
Go to the Arguments tab and enter the two file paths you want to test in the Program Arguments box using the format:   
`[path to file version 1].[FILE TYPE] (path to file version 2).[FILE TYPE]`   
Click Apply, then Run.  
Any time you want to test different files, simply return to the Arguments tab and update the two file paths. 
