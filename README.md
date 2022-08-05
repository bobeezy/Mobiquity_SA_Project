# Mobiquity Automation Framework #

## FEATURES ##

The **Mobiquity Automation Framework** is a simple API testing framework that integrates with CircleCI pipeline.

## TOOLS USED ##

Mac OS, IntelliJ IDE(version 11.0.11), Java(version 11.0.9), Gradle(version 5.2.1), RestAssured, Allure Report(version 2.14.0), TestNG, CircleCI.

---

## FRAMEWORK STRUCTURE ##

### build.gradle file ###
The build.gradle file found on the **Automation framework root folder** contains all the common dependencies that are used in the project

### engine ###
Contains functionality that form the backbone of the framework

**DriverFactory.java**
- contains configurations to setup base url, setup excel data reader, logger and environments.

### APICommonMethods.java ###
- contains configurations to read/edit Json files.
- contains configurations for different Http Methods.

### CustomHeaders.java ###
- contains configurations to customise headers.

### CommentMethods.java ###
- contains configurations to update payloads and do response validations for COMMENT test cases.

### PostMethods.java ###
- contains configurations to update payloads and do response validations for POST test cases.

### UserMethods.java ###
- contains configurations to update payloads and do response validations for USER test cases.

### GlobalEnums.java ###
- contains API configurations to update COMMENT, POST & USER different endpoints.

### utils ###
Contains logic or functionality needed to run tests with efficiency:

**PropertyFileReader.java** - reads element Json path, file path and other identifier types that are specified in the ".properties" files

**ExcelUtility.java** - contains configurations to read spreadsheet and store in memory

**SetEnvironmentDataUtility.java** - contains configurations to setup test environment

### resources ###
Contains different resource files used throughout the project:

**API.properties** - contains configurations URIs, file path, Json path

**UserData.xlsx** - external test data file that contains different test case's data

**Mobiquity Automation Test Coverage.xlsx** - Automation Test Coverage

**testSuites** - contains different xml Test Suites used in the project

### MobiquityTest.java ###
- contains different Test cases used for the Mobiquity project.

---

## EXECUTING TEST SUITE ##
1. Go to top menu bar and navigate to Run > Edit Configurations...
2. On the 'Run/Debug Configurations' window, 'Add New Configuration' by clicking the (+) at the top-left corner
3. Select 'Gradle'
4. Then fill in the fields below:
    * Gradle projects: Mobiquity_SA_Project (Navigate to the project location)
    * Tasks: clean run_mobiquity_test
    * Arguments: --stacktrace
5. Press 'Apply', then 'Ok' button
6. Finally to run the test
    * Press the Green Play button
    * Or navigate to Run > Run 'Mobiquity_SA_Project [clean run_mobiquity_test]'

   //OR
   
7. Execute command from terminal: 

   $ ./gradlew run_mobiquity_test


**Screenshot**

[<img src="https://res.cloudinary.com/bobystore/image/upload/v1628445614/Screenshots/8.png" width="250"/>](https://res.cloudinary.com/bobystore/image/upload/v1628445614/Screenshots/8.png)

## GENERATE ALLURE REPORT on Mac OS ##
Reference: https://docs.qameta.io/allure/

1. Execute a test

2. From the terminal, navigate to the project root:

   e.g. $ cd ~/Documents/Projects/Java/mobiquity_project

3. Execute the terminal command:

   $ allure serve ./build/allure-results

   //OR

4. Execute the terminal command:

   a.1. Generate allure report :

        $ allure generate allure-results --clean -o allure-report

   a.2. wait for output:

   'Report successfully generated to allure-report'


    a.3. View report (report will open automatically)
    
        $ allure serve allure-results

    //OR
		
    b.1. Generate allure report : 
   
        $ allure generate allure-results --clean -o allure-report


    b.2. wait for output:

         'Report successfully generated to allure-report'


    b.3. Navigate to '/allure-report/index.html' in the project, then open index.html with any browser

---

## CircleCI Setup ##

**.circleci** - contains config.yml file with the configurations required for CI execution on CircleCI.

---
