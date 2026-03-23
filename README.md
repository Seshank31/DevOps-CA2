# DevOps-CA2

Task Title: Develop a Student Feedback Registration Form (5 sub tasks)

Create a Student Feedback Registration Form using HTML, CSS, JavaScript, Selenium, and Jenkins. The objective is to design a web form, validate user input, test it automatically and then run those tests through Jenkins.

Sub Task 1: Create the Registration Form using HTML

Design a Student Feedback Registration Form with following input fields:

Student Name
Email ID
Mobile Number
Department
Gender
Feedback Comments
Submit button
Reset button
The form should be properly structured and easy to use.

Sub Task 2: Use of CSS

Use CSS to make the form visually attractive and user-friendly. Students are instructed to use Internal and External CSS.

Sub Task 3: Use of JavaScript

Use JavaScript for form validation. The form should check whether the user has entered correct data before submission. Perform validation:

Student Name should not be empty
Email should be in proper format
Mobile Number should contain valid digits only
At least one gender option should be selected
Department should be selected
Feedback Comments should not be blank and should meet minimum length of 10 words

Sub Task 4: Use of Selenium

Students have to use Selenium to test whether the form is working correctly.

Using Selenium, create test cases:

Check whether the form page opens successfully
Enter valid data and verify successful submission
Leave mandatory fields blank and check error messages
Enter invalid email format and verify validation
Enter invalid mobile number and verify validation
Check whether dropdown selection works properly
Check whether buttons such as Submit and Reset work correctly

Sub Task 5: Use of Jenkins (Additional)

Students have to use Jenkins to automate the execution of Selenium test cases. Students are instructed to perform following task in Jenkins:

Install Jenkins
Create a simple Jenkins job
Connect the project folder or GitHub repository
Configure the job to run Selenium test scripts
Execute the job
Observe whether the build is successful or failed

Folder Structure:
Devops Lab/
|
|-- index.html
|-- style.css
|-- script.js
|-- lib/
|   |-- selenium-server-4.40.0.jar
|-- com/
|   |-- selenium/
|   |   |-- test/
|   |   |   |-- TestPro.java
|-- screenshot

Selenium Testing

Selenium is used to automate the testing of the Student Feedback Form.
The test script is written in Java and executed using ChromeDriver.

Jenkins Integration

Jenkins is used to automate the execution of Selenium test cases.
A Freestyle Job was created and configured with a build step using shell commands to compile and run the Selenium script.
