@tag
Feature: Error validation 

@ErrorValidation
Scenario Outline: Login error messsage
Given I landed on ecommerce page 
When Logged in with username <useremail> and password <password>
Then Verify the error message "Incorrect email or password." on the screen

Examples:
|useremail			|password|
|doquito@gmail.com	|cc		 |
