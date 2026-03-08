package thaicompany.tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import thaicompany.data.ExcelUtils;
import thaicompany.pageobjects.UploadPage;
import thaicompany.testcomponents.BaseTest;

public class ExcelValidationTest extends BaseTest {
	// DECLARA LOS OBJETOS QUE SE INICIALIZAN EN EL @BeforeMethod
	// UTILIZAN EL DRIVER CREADO EN BaseTest
	ExcelUtils excelUtils;
	UploadPage uploadPage;

	
	String dPath = "C:\\Users\\Thainays\\Documents\\Curso Selenium Webdriver Java\\test-download-upload";


	@BeforeMethod
	public void pageSetup() {
		uploadPage = new UploadPage(driver); // CREA EL PAGE OBJECT USANDO EL DRIVER INICIALIZADO EN BaseTest
											 // LOS @Before DE LA CLASE PADRE SE EJECUTAN ANTES DEL TEST
		excelUtils = new ExcelUtils();		 // CREA LA UTILIDAD DE EXCEL PARA USARLA EN LOS TESTS
	}

	@Test(enabled = false)
	public void downLoadWebFile() throws InterruptedException {
		// TEST VERIFICA LA DESCARGA DEL ARCHIVO
		uploadPage = openExcelPage();
		boolean flag = uploadPage.downLoadFile(dPath);
		Assert.assertTrue(flag);
	}

	@Test(enabled = false, dataProvider = "dataEditExcelFile")
	public void editExcelFile(String excelPath, String sheetName, String priceCol, String colName, String fruitName,
			int updateValue, String updatedFilePath) throws IOException {
		// TEST QUE EDITA UN ARCHIVO EXCEL BUSCANDO UNA FILA Y COLUMNA ESPECÍFICA
		// LUEGO ACTUALIZA EL VALOR DE LA CELDA Y GUARDA EL ARCHIVO MODIFICADO
		excelUtils.getExcelFile(excelPath, sheetName);
		int column = excelUtils.getColumnNumber(priceCol);
		int row = excelUtils.getRowNumber(colName, fruitName);
		excelUtils.updateCell(row, column, updateValue);
		excelUtils.saveExcel(updatedFilePath);
		excelUtils.closeWorkbook();

	}

	@Test(enabled = true, dataProvider = "dataEndToEndExcel")
	public void endToEndExcelTest(String dPath, String excelPath, String sheetName, String priceCol, String colName,
			String fruitName, int updateValue, String updatedFilePath) throws InterruptedException, IOException {
		// TEST END TO END QUE DESCARGA UN EXCEL, MODIFICA UN VALOR,
		// LO SUBE NUEVAMENTE Y VERIFICA QUE EL CAMBIO SE REFLEJE EN LA WEB
		uploadPage = openExcelPage();
		boolean flag = uploadPage.downLoadFile(dPath);
		Assert.assertTrue(flag);
		excelUtils.getExcelFile(excelPath, sheetName);
		int column = excelUtils.getColumnNumber(priceCol);
		int row = excelUtils.getRowNumber(colName, fruitName);
		excelUtils.updateCell(row, column, updateValue);
		excelUtils.saveExcel(updatedFilePath);
		excelUtils.closeWorkbook();
		String text = uploadPage.uploadFile(updatedFilePath);
		Assert.assertEquals(text, "Updated Excel Data Successfully.");
		uploadPage.getWebFruitRow(fruitName);
		String fPrice = uploadPage.getWebFruitPrice(fruitName);
		Assert.assertEquals(fPrice, String.valueOf(updateValue));

	}

	@DataProvider
	public Object[][] dataEndToEndExcel() {
		return new Object[][] { { "C:\\Users\\Thainays\\Documents\\Curso Selenium Webdriver Java\\test-download-upload",
				"C:\\Users\\Thainays\\Documents\\Curso Selenium Webdriver Java\\test-download-upload\\download.xlsx",
				"Sheet1", "price", "fruit_name", "Mango", 200,
				"C:\\Users\\Thainays\\Documents\\Curso Selenium Webdriver Java\\test-download-upload\\excel_modificado.xlsx" } };
	}

	@DataProvider
	public Object[][] dataEditExcelFile() {
		return new Object[][] { {
				"C:\\Users\\Thainays\\Documents\\Curso Selenium Webdriver Java\\test-download-upload\\download.xlsx",
				"Sheet1", "price", "fruit_name", "Apple", 150,
				"C:\\Users\\Thainays\\Documents\\Curso Selenium Webdriver Java\\test-download-upload\\excel_modificado.xlsx" } };
	}
}
