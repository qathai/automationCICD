package thaicompany.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import thaicompany.abstractcomponents.AbstractComponent;

//VAMOS A CONVERTIR ESTA CLASE EN HIJA DE "AbstractComponent", PARA QUE PUEDA UTILIZAR LOS METODOS SIN TENER QUE CREAR OBJETOS
public class LandingPage extends AbstractComponent {
	WebDriver driver;

	// CONSTRUCTOR PARA PODER USAR EL driver
	public LandingPage(WebDriver driver) {
		// INICIALIZACIÓN
		super(driver); // NECESITO ENTENDER ESTO MEJOR, CREO QUE SE AGREGA "super(driver)" PORQUE COMO
						// CONVERTÍ A AbstractComponent EN SU PADRE, Y ESA CLASE TAMBIEN TIENE UN
						// CONSTRUCTO ENTONCES PARA PODER MANDAR UN PARÁMETRO DESDE UN HIJO A UN PADRE SE USA "super()"
						// CADA CLASE HIJO DEBE SERVIR AL PADRE MANDANDO EL PARAMETRO CON "super"
		
		this.driver = driver;
		
		PageFactory.initElements(driver, this); // NECESITO SABER CÓMO FUNCIONA ESTO PARA ENTENDER, LO QUE ENTENDÍ ES QUE SE USA PARA PODER CREAR WebElement DE LA OTRA FORMA

	}

	// WebElements DEL LOGIN

	@FindBy(id = "userEmail") // FORMA DE CREAR EL WebElement USANDO PageFactory PARA REDUCIR LA SINTAXIS
	private WebElement userEmail;

	@FindBy(css = "#userPassword")
	private WebElement userPassword;

	@FindBy(id = "login")
	private WebElement submitButton;
	
	@FindBy(css = "[class *='flyInOut']") // WebElement DEL MENSAJE DE ERROR EN EL LOGIN
	private WebElement errorMessage;
	
	// LOCATORS QUE NO SON WebElements
	
	By submitClick = By.id("login");

	
	/*LOS PARAMETROS DEBEN VENIR DEL TEST Y NO DEL PAGE OBJECT, 
	POR ESO SE PONEN COMO PARÁMETROS EN LOS MÉTODOS.
	EL PAGE OBJECT NO DEBE CONTENER NINGÚN DATO, SOLO DEBE ENFOCARSE EN ELEMENTOS Y ACCIONES*/
	
	
	public ProductCatalogue loginApplication(String email, String password) {
		// METODO PARA HACER LOGIN EN LA APP
		// EL DESTINO DEL LOGIN ES SIEMPRE EL CATALOGO DE PRODUCTOS
		// POR ESO ENCAPSULAMOS LA CREACION DEL OBJETO DE ESA CLASE AQUI Y LO DEVOLVERMOS PARA NO TENER QUE CREARLO EN LOS TEST
		userEmail.sendKeys(email);
		userPassword.sendKeys(password);
		waitForWebElementToBeClickable(submitClick);
		driver.findElement(submitClick).click();
		ProductCatalogue productCatalogue = new ProductCatalogue (driver);
		return productCatalogue;
		
	}

	// METODO PARA NAVEGAR A LA URL
	public void goTo() {
		driver.get("https://rahulshettyacademy.com/client");
	}
	
	// METODOD PARA CAPTURAR EL MENSAJE DE ERROR POR LOGIN INCORRECTO
	public String getErrorMessage() {
		// TENGO QUE ESPERERAR A QUE APAREZCA EL MENSAJE DE ERROR
		// COMO EL MENSAJE DE ERROR ES UN WebElement TENGO QUE USAR UNA ESPERA QUE A LA QUE SE LE PASE UN WebElement
		// Y LUEGO DE OBTENER EL TEXTO DEVOLVERLO
		waitForVisibilityOfWebElement(errorMessage);
		String errorText = errorMessage.getText();
		waitForElementToDesappear(errorMessage);
		return errorText;
		
	}

}
