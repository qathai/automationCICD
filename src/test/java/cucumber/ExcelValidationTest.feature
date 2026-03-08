Feature: Update fruit price using Excel file

  @ExcelValidation
  Scenario Outline: End to end test for downloading, editing, uploading an Excel file and validating the change in the web table
    Given I am on the web table page
    And I download the file to <dPath>
    When I edit the file using the new ExcelUtils <excelPath>, <sheetName>, <priceCol>, <colName>, <fruitName>, <updateValue>, <updatedFilePath>
    And I upload the edited file <updatedFilePath>
    Then The <success_message> should be displayed
    And The updated price <updateValue> of <fruitName> should be displayed on the table

    Examples:
      | dPath                                                                               | excelPath                                                                                          | sheetName | priceCol | colName    | fruitName | updateValue | success_message                  | updatedFilePath                                                                                            |
      | C:\\Users\\Thainays\\Documents\\Curso Selenium Webdriver Java\\test-download-upload | C:\\Users\\Thainays\\Documents\\Curso Selenium Webdriver Java\\test-download-upload\\download.xlsx | Sheet1    | price    | fruit_name | Apple     | 250         | Updated Excel Data Successfully. | C:\\Users\\Thainays\\Documents\\Curso Selenium Webdriver Java\\test-download-upload\\excel_modificado.xlsx |
