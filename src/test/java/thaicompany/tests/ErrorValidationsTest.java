package thaicompany.tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import thaicompany.pageobjects.CartPage;
import thaicompany.pageobjects.LandingPage;
import thaicompany.pageobjects.ProductCatalogue;
import thaicompany.testcomponents.BaseTest;
import thaicompany.testcomponents.Retry;

public class ErrorValidationsTest extends BaseTest{

	@Test(groups = {"ErrorHandling"}, retryAnalyzer = Retry.class)
	public void LoginErrorValidation() throws IOException {

		// DATOS DE PRUEBA QUE USAREMOS DURANTE EL FLUJO
		String email = "doquito@gmail.com";
		String password = "cc";
		
		// PODEMOS USAR "landingPage" PARA INVOCAR EL "loginApplication" PORQUE EN EL "BaseTest" CREAMOS EL MÉTODO "launchApplication" QUE MARCAMOS COMO @BeforeMethod QUE RETORNA "landingPage"
		// CON "landingPage" INVOCAMOS EL MÉTODO "loginApplication"
		// "loginApplication" DEVUELVE EL OBJETO "productCatalogue" POR ESO HACEMOS LA SIGNACIÓN, ASI NO TENEMOS QUE CREAR UN OBJETO DE LA CLASE AQUI
		
		LandingPage landingPage = launchApplication();
		landingPage.loginApplication(email, password);
		String errorText = landingPage.getErrorMessage();
		Assert.assertTrue(errorText.equalsIgnoreCase("Incorrect email or password."));

	}
	
	@Test
	public void ProductErrorValidation() throws IOException {

		// DEFINIMOS LOS DATOS DE PRUEBA QUE USAREMOS EN TODO EL FLUJO DE COMPRA
		String productName = "ZARA COAT 3";
		
		// INICIAMOS SESIÓN USANDO EL MÉTODO "loginApplication" DEL OBJETO "landingPage"
		// ESTE MÉTODO RETORNA EL OBJETO "ProductCatalogue" PARA PODER CONTINUAR CON EL FLUJO, 
		// POR ESO HACEMOS LA SIGNACIÓN, ASI NO TENEMOS QUE CREAR UN OBJETO DE LA CLASE AQUI
		LandingPage landingPage = launchApplication();
		ProductCatalogue productCatalogue = landingPage.loginApplication("donpaquito@gmail.com", "Paquito1.");
		
		// AGREGAMOS AL CARRITO EL PRODUCTO ESPECÍFICO QUE QUEREMOS COMPRAR
		// SE LE PASA EL NOMBRE DEL PRODUCTO AL MÉTODO "addProductToCart"
		productCatalogue.addProductToCart(productName);
		
		// NOS DIRIGIMOS AL CARRITO DE COMPRAS
		// "goToCart" RETORNA EL OBJETO  "cartPage" PARA PODER USAR SUS MÉTODOS
		CartPage cartPage = productCatalogue.goToCart();
		
		// VERIFICAMOS QUE EL PRODUCTO "productName" ESTÉ REALMENTE EN EL CARRITO
		// EL MÉTODO "isProductInCart" RETORNA UN BOOLEANO QUE USAREMOS EN EL ASSERT
		Boolean match = cartPage.isProductInCart("ZARA PANTS");
		System.out.println(match);
		Assert.assertFalse(match);
		
	}
	
	
	
	
	
}

