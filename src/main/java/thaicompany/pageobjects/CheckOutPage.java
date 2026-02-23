package thaicompany.pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import thaicompany.abstractcomponents.AbstractComponent;

public class CheckOutPage extends AbstractComponent {
	WebDriver driver;

	public CheckOutPage(WebDriver driver) {
		// INICIALIZACIÓN
		super(driver); // ENVIA EL DRIVER AL CONSTRUCTOR DE LA CLASE PADRE
						// CADA CLASE HIJO DEBE ENVIAR EL PARÁMETRO AL PADRE CON "suoer()", ASI TODOS USAN EL MISMO DRIVER

		this.driver = driver; // ASIGNA EL DRIVER RECIBIDO A LA VARIABLE DE ESTA CLASE

		PageFactory.initElements(driver, this); // INICIALIZA TODOS LOS ELEMENTOS DE ESTA CLASE QUE TENGAN @FindBy
												// PAGEFACTORY SE ENCARGA DE DECIRLE A SELENIUM CÓMO ENCONTRARLOS CUANDO SE USEN
	}
	
	@FindBy(xpath = "//input[@placeholder='Select Country']") // LOCATOR PARA ESCRIBIR EL PAIS EN EL INPUT
	WebElement countrySelector;
	
	@FindBy(css = ".action__submit")
	WebElement placeOrderButton;
	
	By countryDopDownBy = By.cssSelector(".ta-results span"); // LOCATOR QUE VOY A USAR EN LA ESPERA DEL DROPDOWN EN EL CHECKOUT
	
	public void selectCountry(String countryLetters, String countryName) {
		// METODO PARA COMPLETAR LA SECCIÓN DE COUNTRY DEL CHECKOUT
		waitForVisibilityOfWebElement(countrySelector);
		countrySelector.sendKeys(countryLetters); // ESCRIBIR LAS LETRAS PARA ACTIVAR LAS SUGERENCIAS
		List <WebElement> countryDopDown = waitForVisibilityOfAllWebElements(countryDopDownBy); // ESPERAR A QUE APAREZCAN LAS OPCIONES DEL DROPDOWN Y GUARDARLAS
		
		WebElement selectCountry = countryDopDown.stream().filter(s -> s.getText().equalsIgnoreCase(countryName)).findFirst().orElse(null); // FILTRAR EL PAIS QUE COINCIDA CON countryName
																																			// findFirst: TOMA EL PRIMER RESULTADO QUE CUMPLA LA CONDICION
																																			// orElse(null): DEVUELVE null SI NO ENCUENTRA COINCIDENCIA
		System.out.println(selectCountry.getText());
		selectCountry.click(); // CLICK EN EL PAIS
	}
	
	public ConfirmationPage placeOrder() {
		// METODO PARA COLOCAR LA ORDEN
		// EL DESTINO DE ESTE MÉTODO ES SIEMPRE LA PÁGINA DE CONFIRMACIÓN
		// POR ESO ENCAPSULAMOS LA CREACION DEL OBJETO DE ESA CLASE AQUI Y LO DEVOLVERMOS PARA NO TENER QUE CREARLO EN LOS TEST
		placeOrderButton.click(); // CLICK EN PLACE ORDER
		return new ConfirmationPage(driver);
	}
	

	
	
	
	
	


	
}
