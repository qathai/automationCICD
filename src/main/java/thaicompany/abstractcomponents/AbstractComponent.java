package thaicompany.abstractcomponents;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import thaicompany.pageobjects.CartPage;
import thaicompany.pageobjects.OrderPage;

// VAMOS A CONVERTIR ESTA CLASE EN PADRE DE LAS OTRAS, PORQUE ESTA CLASE VA A CONTENER CÓDIGO QUE SERA REUTILIZADO VARIAS VECES
public class AbstractComponent {
	
	WebDriver driver; // VARIABLE DE CLASE PARA EL DRIVER
	WebDriverWait w; // VARIABLE DE CLASE PARA ESPERAS
	
	public AbstractComponent(WebDriver driver) {
		this.driver = driver;
		w = new WebDriverWait(driver, Duration.ofSeconds(6)); // ESPERA EXPLICITA PARA CONDICIONES ESPECIFICAS
															  // LA DECLARO AQUI PARA USARLA EN TODOS LOS MÉTODOS DE ESTA CLASE 
															  // Y TODAS LAS CLASES QUE HEREDEN DE ESTA PUEDEN USARLA
		PageFactory.initElements(driver, this); // INICIALIZA TODOS LOS ELEMENTOS DE ESTA CLASE QUE TENGAN @FindBy  
	}
	

	public void waitForAllElementsToAppear(By findBy) {
		// ESPERAR A QUE TODOS LOS PRODUCTOS SEAN VISIBLES ANTES DE TRABAJAR CON ELLOS
		w.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(findBy)); // EN LUGAR DE PASARLE EL LOCATOR USANDO By.cssSelector("div.col-lg-4"), LO PARAMETRIZAMOS
																			  // LE PASAMOS UNA VARIABLE DE TIPO By QUE VA A CONTENER EL LOCATOR		
	}
	
	public void waitForElementToAppear(By finBy) {
	// ESPERA A QUE UN SOLO ELEMENTO SEA VISIBLE
		w.until(ExpectedConditions.visibilityOfElementLocated(finBy));
	}
	
	public void waitForElementToDesappear(WebElement elem) {
		// ESPERA A QUE UN SOLO ELEMENTO DESAPARESCA
		w.until(ExpectedConditions.invisibilityOf(elem));
		
	}
	
	public void waitForVisibilityOfAllWebElement(List<WebElement> elem) {
		// ESPERA A QUE UN SOLO ELEMENTO SEA VISIBLE
		w.until(ExpectedConditions.visibilityOfAllElements(elem));
	}
	
	public void waitForVisibilityOfWebElement(WebElement elem) {
		// ESPERA A QUE UN SOLO ELEMENTO SEA VISIBLE
		w.until(ExpectedConditions.visibilityOf(elem));
	}
	
	public void waitForWebElementToBeClickable(By finBy) {
		// ESPERA A QUE UN SOLO ELEMENTO SEA VISIBLE
		w.until(ExpectedConditions.elementToBeClickable(finBy));
	}
	
	public List<WebElement> waitForVisibilityOfAllWebElements(By finBy) {
		// ESPERA A QUE TODOS LOS ELEMENTOS SEAN VISIBLES, REBIDE UN LOCATOR DE TIPO BY Y DEVUELVE UN "List<WebElement>"
		return w.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(finBy));
	}
	
	@FindBy(css = "[routerlink*= 'cart']") // LOCATOR PARA HACER CLICK EN EL CARRO DE COMPRA
	WebElement cartHeaderButton;
	
	@FindBy(css = "[routerlink ='/dashboard/myorders']")
	WebElement ordersHeaderButton;
	
	public CartPage goToCart() {
		//METODO PARA IR AL CARRO DE COMPRAS
		// EL DESTINO DEL "goToCart" ES SIEMPRE EL "CartPage"
		// POR ESO ENCAPSULAMOS LA CREACION DEL OBJETO DE ESA CLASE AQUI Y LO DEVOLVERMOS PARA NO TENER QUE CREARLO EN LOS TEST
		cartHeaderButton.click();
		CartPage cartPage = new CartPage(driver);
		return cartPage;		
	}
	
	public OrderPage goToOrders() {
		ordersHeaderButton.click();
		OrderPage orderPage = new OrderPage(driver);
		return orderPage;
		
		
		
	}
	
	
}
