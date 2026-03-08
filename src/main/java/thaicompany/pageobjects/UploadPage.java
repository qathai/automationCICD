package thaicompany.pageobjects;

import java.io.File;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import thaicompany.abstractcomponents.AbstractComponent;

public class UploadPage extends AbstractComponent {
	WebDriver driver; // VARIABLE DE LA CLASE TODOS LOS MÉTODOS PUEDEN USARLA

	// CONSTRUCTOR QUE RECIBE EL DRIVER DESDE EL TEST
	public UploadPage(WebDriver driver) {
		super(driver); 							// ENVÍA EL DRIVER A LA CLASE PADRE
		this.driver = driver; 					// ASIGNA EL DRIVER A ESTA CLASE
		PageFactory.initElements(driver, this); // INICIALIZA LOS ELEMENTOS DE LA PÁGINA CON PAGEFACTORY
	}

	@FindBy(id = "downloadButton")
	WebElement downloadButton;

	@FindBy(css = "input[type='file']")
	WebElement uploadButton;

	By successMessage = By.xpath("//div[contains(text(),'Updated Excel')]");

	@FindBy(css = ".rdt_TableBody")
	WebElement table;

	By fruitCol = By.xpath(".//div[@data-column-id='2']");
	By fruitPrice = By.xpath("./following-sibling::div[2]");

	public boolean downLoadFile(String downloadPath) throws InterruptedException {
		// MÉTODO PARA DESCARGAR EL EXCEL DE LA PÁGINA
		downloadButton.click();

		// ESPERAR QUE EL ARCHIVO SE DESCARGUE ANTES DE PODER ACCEDER A ÉL
		// CREAR UN OBJETO "file" PARA PODER USAR EL METODO "exists"
		File file = new File(downloadPath + "\\download.xlsx");

		int timeoutSeconds = 10;
		int waited = 0;

		while (!file.exists() && waited < timeoutSeconds) {
			Thread.sleep(1000);
			waited++;
		}
		return file.exists();
	}

	public String uploadFile(String uploadFilePath) {
		// CARGAR ARCHIVO - SELENIUM NO PUEDE CONTROLAR EL EXPLORADOR DE ARCHIVOS DE WINDOWS
		// POR ESO SE ENVÍA DIRECTAMENTE EL PATH DEL ARCHIVO AL INPUT TYPE="FILE"
		uploadButton.sendKeys(uploadFilePath);
		waitForElementToAppear(successMessage);
		String tText = driver.findElement(successMessage).getText();
		waitForElementToDesappearByLocator(successMessage);
		return tText;
	}

	public WebElement getWebFruitRow(String fruitName) {
		// MÉTODO QUE BUSCA Y RETORNA LA FILA DE LA FRUTA EN LA TABLA DE LA WEB
		List<WebElement> fruitsColumn = table.findElements(fruitCol);
		WebElement fruit = fruitsColumn.stream().filter(s -> s.getText().equalsIgnoreCase(fruitName)).findFirst()
				.orElseThrow(() -> new RuntimeException("FRUIT NOT FOUND"));
		System.out.println(fruit.getText());
		return fruit;

	}

	public String getWebFruitPrice(String fruitName) {
		// OBTIENE EL PRECIO DE LA FRUTA DESDE LA FILA ENCONTRADA EN LA TABLA
		WebElement fruit = getWebFruitRow(fruitName);
		String fPrice = fruit.findElement(fruitPrice).getText();
		return fPrice;
	}
	
	public void goToExcelPage() {
		driver.get("https://rahulshettyacademy.com/upload-download-test/index.html");
	}

}