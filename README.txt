# WebService Applicaiton 

1.	GENERAL REQUIREMENTS
-	IntelliJ IDE must be installed, any version that supports Java 1.8
IntelliJ: https://plugins.jetbrains.com/plugin/7212-cucumber-for-java 
o	File -> Settings -> Plugins -> Install JetBrains plugin… -> Right-click ‘Cucumber for Java’ -> ‘Download and Install’.

2.	IMPORTING THE PROJECT
Import the project as a Maven project in Intellij

4.	PROPERTIES FILE
The properties.parameters file is created from the properties.template file at first launch.

The parameters.properties file can be found in
<workspace>/\TestWebservice1/automation_data/

o	properties.parameters
-	This file is where you enter your personal run details 
-   Please ensure you UPDATE your LocationOfDriver and LoggFile point to the project DIR. 

5. To RUN 
Naviagte => <workspace>\WebTestApplication\src\main\java\cucumber\launchers   -> RunFeatures (Right click and Run) 

6. Log File 
Output will be placed in the folder specificed in the Properties file. 



