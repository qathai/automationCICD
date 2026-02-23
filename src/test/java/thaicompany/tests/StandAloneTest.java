package thaicompany.tests;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.github.bonigarcia.wdm.WebDriverManager;

public class StandAloneTest {

	public static void main(String[] args) throws InterruptedException {
		// DATOS DE PRUEBA QUE USAREMOS DURANTE EL FLUJO
		String productName = "ZARA COAT 3";
		String countryLetters = "por";
		String countryName = "portugal";
		
		// CONFIGURACION DEL DRIVER
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // ESPERA IMPLICITA PARA BUSQUEDAS GENERALES DE ELEMENTOS
		WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(6)); // ESPERA EXPLICITA PARA CONDICIONES ESPECIFICAS
		
		driver.manage().window().maximize(); // MAXIMIZAR LA VENTANA PARA EVITAR PROBLEMAS DE RESPONSIVE DESIGN

		driver.get("https://rahulshettyacademy.com/client");
		
		// 1. LOGIN
		driver.findElement(By.id("userEmail")).sendKeys("donpaquito@gmail.com");
		driver.findElement(By.cssSelector("#userPassword")).sendKeys("Paquito1.");
		driver.findElement(By.id("login")).click();
		
		// 2. LISTA DE PRODUCTOS Y AGREGAR PRODUCTO ESPECIFICO AL CARRO
		// ESPERAR A QUE TODOS LOS PRODUCTOS SEAN VISIBLES ANTES DE TRABAJAR CON ELLOS
		w.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("div.col-lg-4")));
		List<WebElement> products = driver.findElements(By.cssSelector("div.col-lg-4")); // GUARDAR TODOS LOS PRODUCTOS EN UNA LISTA
		
		// USAMOS STREAM PARA RECORRER LA LISTA
		// filter: SOLO DEJA PASAR EL PRODUCTO CUYO TEXTO COINCIDA CON productName
		// findFirst: DEVUELVE EL PRIMER ELEMENTO QUE CUMPLA LA CONDICION
		// orElse(null): SI NO ENCUENTRA NINGUNO, DEVUELVE null EN VEZ DE LANZAR EXCEPCION
		WebElement filterProduct = products.stream().filter(s -> s.findElement(By.cssSelector("b")).getText().equals(productName)).findFirst().orElse(null);
		
		// VALIDAR QUE EL PRODUCTO FUE ENCONTRADO ANTES DE HACER CLICK
		if (filterProduct!= null) {
			filterProduct.findElement(By.cssSelector(".btn.w-10")).click();
		}
		
		// ESPERAR HASTA QUE APAREZCA EL MENSAJE TOAST DE CONFIRMACION
		w.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#toast-container")));
		
		// ESPERAR A QUE DESAPAREZCA LA ANIMACION PARA EVITAR ERRORES DE INTERACCION (ES CASI IMPOSIBLE DE CAPTURAR, ME PASARON EL LOCATOR DIRECTAMENTE)
		w.until(ExpectedConditions.invisibilityOf(driver.findElement(By.cssSelector(".ng-animating"))));
		
		// 3. IR AL CARRO DE COMPRAS
		driver.findElement(By.cssSelector("[routerlink*= 'cart']")).click();
		
		// 4. VERIFICAR QUE EL PRODUCTO ESTE EN EL CARRO
		// OBTENER LOS NOMBRES DE LOS PRODUCTOS EN EL CARRO 
		List<WebElement> cartProducts = driver.findElements(By.cssSelector(".cartSection h3"));
		
		// anyMatch: DEVUELVE true SI ALGUNO DE LOS ELEMENTOS COINCIDE CON productName
		boolean match = cartProducts.stream().anyMatch(s -> s.getText().equalsIgnoreCase(productName));
		System.out.println(match);
		
		// VERIFICA QUE EL PRODUCTO SI ESTA EN EL CARRO
		Assert.assertTrue(match);
		
		// 5. IR AL CHECKOUT
		driver.findElement(By.cssSelector("li.totalRow button")).click();
		
		// 6. COMPLETAR EL COUNTRY EN EL MENU AUTOSUGERIBLE
		// ESCRIBIR LAS LETRAS PARA ACTIVAR LAS SUGERENCIAS
		w.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//input[@placeholder='Select Country']")))).sendKeys(countryLetters);
		
		// ESPERAR A QUE APAREZCAN LAS OPCIONES DEL DROPDOWN Y GUARDARLAS
		List <WebElement> countryDopDown = w.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".ta-results span")));
		
		// FILTRAR EL PAIS QUE COINCIDA CON countryName
		// findFirst: TOMA EL PRIMER RESULTADO QUE CUMPLA LA CONDICION
		// orElse(null): DEVUELVE null SI NO ENCUENTRA COINCIDENCIA
		WebElement selectCountry = countryDopDown.stream().filter(s -> s.getText().equalsIgnoreCase(countryName)).findFirst().orElse(null);
		System.out.println(selectCountry.getText());
		selectCountry.click(); // HACER CLICK EN EL PAIS
		
		/*// HACE EL MISMO PASO 6 PERO USANDO ACTIONS
		Actions a = new Actions(driver);
		
		w.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//input[@placeholder='Select Country']"))));
		a.sendKeys(driver.findElement(By.xpath("//input[@placeholder='Select Country']")), countryLetters).build().perform();
		
		w.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.cssSelector(".ta-results span"))));
		driver.findElement(By.cssSelector(".ta-item:nth-of-type(1)")).click();*/
		
		// 7. HACER CLICK EN PLACE ORDER
		driver.findElement(By.cssSelector(".action__submit")).click();
		
		// 8. VERIFICAR MENSAJE FINAL DE ORDEN EXITOSA
		// OBTIENE EL TEXTO DEL MESAJJE Y LO COMPARA IGNORANDO MAYUSCULAS CON EL MENSAJE ESPERADO
		String orderMessage = driver.findElement(By.cssSelector(".hero-primary")).getText();
		Assert.assertTrue(orderMessage.equalsIgnoreCase("Thankyou for the order."));
		
	
		driver.findElement(By.cssSelector("[routerlink ='/dashboard/myorders']")).click();
		List <WebElement> columnName = w.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("tr td:nth-child(3)")));
		boolean productMatch = columnName.stream().anyMatch(s -> s.getText().equalsIgnoreCase(productName));
		System.out.println(productMatch);
		Assert.assertTrue(productMatch);
		
		// 9. CERRAR EL NAVEGADOR
		//driver.close();
	}

}
