package thaicompany.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import thaicompany.abstractcomponents.AbstractComponent;

public class ConfirmationPage extends AbstractComponent {
	
	WebDriver driver;

	public ConfirmationPage(WebDriver driver) {
		// INICIALIZACIÓN
		super(driver); // ENVIA EL DRIVER AL CONSTRUCTOR DE LA CLASE PADRE
					   // CADA CLASE HIJO DEBE ENVIAR EL PARÁMETRO AL PADRE CON "suoer()", ASI TODOS USAN EL MISMO DRIVER

		this.driver = driver; // ASIGNA EL DRIVER RECIBIDO A LA VARIABLE DE ESTA CLASE

		PageFactory.initElements(driver, this); // INICIALIZA TODOS LOS ELEMENTOS DE ESTA CLASE QUE TENGAN @FindBy
														// PAGEFACTORY SE ENCARGA DE DECIRLE A SELENIUM CÓMO ENCONTRARLOS CUANDO SE USEN
	}
	
	@FindBy(css = ".hero-primary")
	WebElement orderMessage;
	
	public String getConfirmationMessage() {
		// METODO PARA VERIFICAR MENSAJE FINAL DE ORDEN
		// OBTIENE EL TEXTO DEL MESAJJE Y LO COMPARA CON EL MENSAJE ESPERADO, IGNORANDO MAYUSCULAS 
		String orderTitleText = orderMessage.getText();
		return orderTitleText;
	}

}
