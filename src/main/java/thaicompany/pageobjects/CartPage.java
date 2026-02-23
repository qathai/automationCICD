package thaicompany.pageobjects;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import thaicompany.abstractcomponents.AbstractComponent;

public class CartPage extends AbstractComponent {
	WebDriver driver;

	public CartPage(WebDriver driver) {
		// INICIALIZACIÓN
		super(driver); // ENVIA EL DRIVER AL CONSTRUCTOR DE LA CLASE PADRE
						// CADA CLASE HIJO DEBE ENVIAR EL PARÁMETRO AL PADRE CON "suoer()", ASI TODOS USAN EL MISMO DRIVER

		this.driver = driver; // ASIGNA EL DRIVER RECIBIDO A LA VARIABLE DE ESTA CLASE

		PageFactory.initElements(driver, this); // INICIALIZA TODOS LOS ELEMENTOS DE ESTA CLASE QUE TENGAN @FindBy
												// PAGEFACTORY SE ENCARGA DE DECIRLE A SELENIUM CÓMO ENCONTRARLOS CUANDO SE USEN
	}
	
	@FindBy(css = ".cartSection h3") // LOCATOR PARA OBTENER LOS NOMBRES DE LOS PRODUCTOS EN EL CARRO 
	List<WebElement> cartProducts;
	
	@FindBy(css = "li.totalRow button") // LOCATOR PARA HACER CLICK EN EL BOTON DE IR AL CHECKOUT
	WebElement checkOutButton;
	
	public boolean isProductInCart(String productName) {
		// METODO PARA VERIFICAR QUE EL PRODUCTO ESTE EN EL CARRO
		// anyMatch: DEVUELVE true SI ALGUNO DE LOS ELEMENTOS COINCIDE CON "productName"
		// "productName" HAY QUE ENVÍARLO CUANDO SE LLAME A ESTE MÉTODO
		boolean match = cartProducts.stream().anyMatch(s -> s.getText().equalsIgnoreCase(productName));
		return match;
	}
	
	public CheckOutPage goToCheckOut() {
		// METODO PARA IR AL CHECKOUT
		// EL DESTINO DE ESTE CLIC ES SIEMPRE LA PÁGINA DE CHECKOUT
		// POR ESO ENCAPSULAMOS LA CREACION DEL OBJETO DE ESA CLASE AQUI Y LO DEVOLVERMOS PARA NO TENER QUE CREARLO EN LOS TEST
		checkOutButton.click();
		CheckOutPage checkOutPage = new CheckOutPage(driver);
		return checkOutPage;
		
		
	}
	

	
	
	
	
}
