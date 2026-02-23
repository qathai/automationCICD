@tag
Feature: Purchase order from ecommerce website

Background: 
Given I landed on ecommerce page


@Regression
Scenario Outline: Positive test for submitting the order
Given Logged in with username <useremail> and password <password>
When Add product <productName> to cart
And Go to checkout verify <productName>
And Submit the order with <countryLetters> and <countryName>
Then Verify the confirmation message "Thankyou for the order." in the screen

Examples:
|useremail	            |password    |productName  |countryLetters|countryName|
|donpaquito@gmail.com	|Paquito1.	 |ZARA COAT 3  |por           |portugal   |