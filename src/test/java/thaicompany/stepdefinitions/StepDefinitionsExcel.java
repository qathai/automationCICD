package thaicompany.stepdefinitions;

import java.io.IOException;

import org.testng.Assert;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import thaicompany.testcomponents.BaseTest;

public class StepDefinitionsExcel extends BaseTest {

	String actualMessage;
	
	@After
	public void closeDriver() {
		if (driver != null) {
			driver.close(); // CIERRA EL NAVEGADOR AL FINALIZAR EL SCENARIO
		}
		
	}

	@Given("I am on the web table page")
	public void go_to_the_web_table_page() throws IOException {
		prepareExcelTest();
	}

	@And("^I download the file to (.+)$")
	public void download_file_to(String dPath) throws InterruptedException {
		boolean flag = uploadPage.downLoadFile(dPath);
		Assert.assertTrue(flag);
	}

	@When("^I edit the file using the new ExcelUtils (.+), (.+), (.+), (.+), (.+), (.+), (.+)$")
	public void edit_file_with_excelUtils(String excelPath, String sheetName, String priceCol, String colName,
			String fruitName, int updateValue, String updatedFilePath) throws IOException {
		excelUtils.getExcelFile(excelPath, sheetName);
		int column = excelUtils.getColumnNumber(priceCol);
		int row = excelUtils.getRowNumber(colName, fruitName);
		excelUtils.updateCell(row, column, updateValue);
		excelUtils.saveExcel(updatedFilePath);
		excelUtils.closeWorkbook();
	}

	@And("^I upload the edited file (.+)$")
	public void upload_edited_file(String updatedFilePath) {
		actualMessage = uploadPage.uploadFile(updatedFilePath);

	}

	@Then("^The (.+) should be displayed$")
	public void success_message_displayed(String success_message) {
		Assert.assertEquals(actualMessage, success_message);
	}
	
	@And ("^The updated price (.+) of (.+) should be displayed on the table$")
	public void updated_price_displayed_on_table(String updateValue, String fruitName) {
		uploadPage.getWebFruitRow(fruitName);
		String fPrice = uploadPage.getWebFruitPrice(fruitName);
		Assert.assertEquals(fPrice, String.valueOf(updateValue));
	}

}
