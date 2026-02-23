package thaicompany.stepdefinitions;

import java.io.IOException;

import org.testng.Assert;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import thaicompany.pageobjects.CartPage;
import thaicompany.pageobjects.CheckOutPage;
import thaicompany.pageobjects.ConfirmationPage;
import thaicompany.pageobjects.LandingPage;
import thaicompany.pageobjects.ProductCatalogue;
import thaicompany.testcomponents.BaseTest;

public class StepDefinitionsImp extends BaseTest{
	// VARIABLES DE LA CLASE, ESTAN VIVAS EN TODA ESTA CLASE PARA USARSE EN TODOS LOS MÉTODOS
	LandingPage landingPage; 
	ProductCatalogue productCatalogue;
	CartPage cartPage;
	CheckOutPage checkOutPage;
	ConfirmationPage confirmationPage;
	
	
	@Given("I landed on ecommerce page")
	public void I_landed_on_ecommerce_page() throws IOException {
		landingPage = launchApplication(); // INICIALIZA EL DRIVER, INVOCA "goTo", DEVUELVE "landingPage"
	}
	
	@Given ("^Logged in with username (.+) and password (.+)$") // SIMBOLO "^" AL INICIO Y "$" AL FIN INDICA QUE ES UNA EXPRESIÓN REGULAR															// LA VARIABLE SE SUSTITUYE CON (.+)
	public void logged_in_username_password(String email, String password) {
		productCatalogue = landingPage.loginApplication(email, password);
	}
	
	@When ("^Add product (.+) to cart$")
	public void Add_product_cart(String productName) {
		productCatalogue.addProductToCart(productName);;
	}
	
	@And ("^Go to checkout verify (.+)$")
	public void Go_to_checkout_verify(String productName) {
		cartPage = productCatalogue.goToCart();
		boolean match = cartPage.isProductInCart(productName);
		Assert.assertTrue(match);
		checkOutPage = cartPage.goToCheckOut();
	}
	
	@And ("^Submit the order with (.+) and (.+)$")
	public void Submit_order(String countryLetters, String countryName) {
		checkOutPage.selectCountry(countryLetters, countryName);
		confirmationPage = checkOutPage.placeOrder();
	}
	
	@Then ("Verify the confirmation message {string} in the screen")
	public void Verify_confirmation_message(String text) {
		String orderTitleText = confirmationPage.getConfirmationMessage();
		Assert.assertTrue(orderTitleText.equalsIgnoreCase(text));
	}
	
	@Then ("Verify the error message {string} on the screen")
	public void Login_error_message(String errorMessage) {
		String errorText = landingPage.getErrorMessage();
		Assert.assertTrue(errorText.equalsIgnoreCase(errorMessage));
	}

}
