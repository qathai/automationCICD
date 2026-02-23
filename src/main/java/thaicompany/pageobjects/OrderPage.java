package thaicompany.pageobjects;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import thaicompany.abstractcomponents.AbstractComponent;

public class OrderPage extends AbstractComponent {
	WebDriver driver;

	public OrderPage(WebDriver driver) {
		// INICIALIZACIÓN
		super(driver); // ENVIA EL DRIVER AL CONSTRUCTOR DE LA CLASE PADRE
						// CADA CLASE HIJO DEBE ENVIAR EL PARÁMETRO AL PADRE CON "suoer()", ASI TODOS USAN EL MISMO DRIVER

		this.driver = driver; // ASIGNA EL DRIVER RECIBIDO A LA VARIABLE DE ESTA CLASE

		PageFactory.initElements(driver, this); // INICIALIZA TODOS LOS ELEMENTOS DE ESTA CLASE QUE TENGAN @FindBy
												// PAGEFACTORY SE ENCARGA DE DECIRLE A SELENIUM CÓMO ENCONTRARLOS CUANDO SE USEN
	}
	
	@FindBy(css = "tr td:nth-child(3)")
	List<WebElement> columnName;
	
	
	public boolean VerifyOrderDisplay(String productName) {
		// METODO PARA VERIFICAR QUE EL PRODUCTO COMPRADO APAREZCA EN LA TABLA DE ORDERS
		waitForVisibilityOfAllWebElement(columnName);
		boolean productMatch = columnName.stream().anyMatch(s -> s.getText().equalsIgnoreCase(productName));
		return productMatch;
		
	}
	
	
	

	
	
	
	
}
