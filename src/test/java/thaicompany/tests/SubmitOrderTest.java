package thaicompany.tests;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import thaicompany.pageobjects.CartPage;
import thaicompany.pageobjects.CheckOutPage;
import thaicompany.pageobjects.ConfirmationPage;
import thaicompany.pageobjects.OrderPage;
import thaicompany.pageobjects.ProductCatalogue;
import thaicompany.testcomponents.BaseTest;

public class SubmitOrderTest extends BaseTest{
	String productName = "ZARA COAT 3";

	@Test(dataProvider = "getDataHashMap", groups = {"Purchase"})
	public void submitOrder(HashMap<String, String> data) throws IOException {
		// EL TEST RECIBE UN HASHMAP CON LOS DATOS DE UNA EJECUCION DEL DATAPROVIDER
	    // CADA CLAVE DEL HASHMAP REPRESENTA UN DATO DE PRUEBA
		
		// INICIAMOS SESIÓN USANDO EL MÉTODO "loginApplication" DEL OBJETO "landingPage"
		// ESTE MÉTODO RETORNA EL OBJETO "ProductCatalogue" PARA PODER CONTINUAR CON EL FLUJO, 
		// POR ESO HACEMOS LA SIGNACIÓN, ASI NO TENEMOS QUE CREAR UN OBJETO DE LA CLASE AQUI
		ProductCatalogue productCatalogue = landingPage.loginApplication(data.get("email"), data.get("password"));
		
		// AGREGAMOS AL CARRITO EL PRODUCTO ESPECÍFICO QUE QUEREMOS COMPRAR
		// SE LE PASA EL NOMBRE DEL PRODUCTO AL MÉTODO "addProductToCart"
		 // SE OBTIENE EL NOMBRE DEL PRODUCTO DESDE EL HASHMAP USANDO LA CLAVE productName
		productCatalogue.addProductToCart(data.get("productName"));
		
		// NOS DIRIGIMOS AL CARRITO DE COMPRAS
		// "goToCart" RETORNA EL OBJETO  "cartPage" PARA PODER USAR SUS MÉTODOS
		CartPage cartPage = productCatalogue.goToCart();
		
		// VERIFICAMOS QUE EL PRODUCTO "productName" ESTÉ REALMENTE EN EL CARRITO
		// EL MÉTODO "isProductInCart" RETORNA UN BOOLEANO QUE USAREMOS EN EL ASSERT
		Boolean match = cartPage.isProductInCart(data.get("productName"));
		System.out.println(match);
		Assert.assertTrue(match);
		
		// NOS DIRIGIMOS A LA PÁGINA DE CHECKOUT
		CheckOutPage checkOutPage = cartPage.goToCheckOut();
		
		// COMPLETAMOS EL CHECKOUT SELECCIONANDO EL PAÍS EN EL MENÚ AUTOSUGERIBLE
		// SE OBTIENEN LOS DATOS DEL PAIS DESDE EL HASHMAP
	    // countryLetters SE USA PARA FILTRAR Y countryName PARA SELECCIONAR
		 checkOutPage.selectCountry(data.get("countryLetters"), data.get("countryName"));
		 
		// FINALIZAMOS EL PEDIDO HACIENDO CLIC EN "PLACE ORDER"
		 ConfirmationPage confirmationPage = checkOutPage.placeOrder();
		
		// VERIFICAMOS EL MENSAJE DE CONFIRMACIÓN DE LA ORDEN
		String orderTitleText = confirmationPage.getConfirmationMessage();
		Assert.assertTrue(orderTitleText.equalsIgnoreCase("Thankyou for the order."));
	}
	
	@Test (dependsOnMethods = {"submitOrder"})
	public void OrderHistoryTest() {
		
		ProductCatalogue productCatalogue = landingPage.loginApplication("donpaquito@gmail.com", "Paquito1.");
		OrderPage orderPage = productCatalogue.goToOrders();
		boolean productMatch = orderPage.VerifyOrderDisplay(productName);
		Assert.assertTrue(productMatch);
		
	}
	
	@DataProvider
	public Object[][] getData() {
		return new Object[][] {
			{"donpaquito@gmail.com", "Paquito1.", "IPHONE 13 PRO", "ven", "venezuela"},
			{"donpaquito@gmail.com", "Paquito1.", "ZARA COAT 3", "por", "portugal"},
			{"donpaquito@gmail.com", "Paquito1.", "ADIDAS ORIGINAL", "ind", "india"}		
		};
	}
	
	@DataProvider
	public Object[][] getDataHashMap() throws IOException {
		// INVOCAMOS "getJsonDataToHashMap" MANDOLE EL PATH DEL JSON Y GUARDAMOS EL RESULTADO QUE RETORNA EN "dataJson"
		List<HashMap<String, String>> dataJson = getJsonDataToHashMap(System.getProperty("user.dir")+"\\src\\test\\java\\thaicompany\\data\\purchaseOrder.json");
		
		// SE DEVUELVEN LOS TRES HASHMAP
		// CADA HASHMAP SE ENVÍA COMO UNA EJECUCIÓN DEL TEST
		return new Object[][] {{dataJson.get(0)}, {dataJson.get(1)}, {dataJson.get(2)}};
		
		/*CODIGO VIEJO DE HASH MANUAL 
		 * HashMap<String, String> map = new HashMap<String, String>();
		map.put("email", "donpaquito@gmail.com");
		map.put("password", "Paquito1.");
		map.put("productName", "ZARA COAT 3");
		map.put("countryLetters", "por");
		map.put("countryName", "portugal");
		
		 // SE CREA EL SEGUNDO HASHMAP CON OTRO PRODUCTO Y OTRO PAIS
		HashMap<String, String> map2 = new HashMap<String, String>();
		map2.put("email", "donpaquito@gmail.com");
		map2.put("password", "Paquito1.");
		map2.put("productName", "IPHONE 13 PRO");
		map2.put("countryLetters", "ven");
		map2.put("countryName", "venezuela");
		
		// SE CREA EL TERCER HASHMAP CON UN PRODUCTO Y PAIS DIFERENTE
		HashMap<String, String> map3 = new HashMap<String, String>();
		map3.put("email", "donpaquito@gmail.com");
		map3.put("password", "Paquito1.");
		map3.put("productName", "ADIDAS ORIGINAL");
		map3.put("countryLetters", "ind");
		map3.put("countryName", "india");
		*/
	}
}