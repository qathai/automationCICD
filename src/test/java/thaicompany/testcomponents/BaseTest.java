package thaicompany.testcomponents;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.bonigarcia.wdm.WebDriverManager;
import thaicompany.pageobjects.LandingPage;

public class BaseTest {
	
	/* LA IDEA DE ESTA CLASE ES DEFINIR TODOS LOS PASOS QUE SON COMUNES ENTRE LAS DIFERENTES PRUEBAS QUE VAMOS A DESARROLLAR
	 * COMO POR EJEMPLO DECLARAR EL DRIVER Y NAVEGAR HASTA LA URL DE LA APLICACIÓN, EL OBJETIVO DE ESTO ES EVITAR REPETIR PASOS 
	 * EN CADA PRUEBA Y DEJAR LOS CASOS DE PRUEBA LO MAS ENFOCADOS A LA PRUEBA REAL*/
	
	public WebDriver driver; // ESTA VARIABLE GLOBAL ME SIRVE PARA QUE CUALQUIER DRIVER QUE YO CREE EN ESTA CLASE TENGA ACCESO A ESTA VARIABLE
	public LandingPage landingPage; // VARIABLE GLOBAL PARA QUE CUALQUIER CLASE HIJA PUEDA ACCEDER A ELLA Y CUALQUIER MÉTODO DE ESTA CLASE TAMBIEN
	
	public WebDriver initializeDriver() throws IOException {
		// CONFIGURACION DEL DRIVER
		
		// PROPERTIES CLASS - NECESITO MAS EXPLICACIÓN PARA ESTA PARTE Y PARA MIS APUNTES
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//java//thaicompany//resources//GlobalData.properties");
		prop.load(fis);
		// PARA CUALQUIER VARIABLE A NIVEL DE SISTEMA USAMOS "System.getProperty" EN ESTE CASO QUEREMOS LEER
		// ESTAMOS USANDO UN OPERADOR TERNARIO DE JAVA, SI LA EL BROWSER DESDE LA CONSOLA DE MAVEN NO ES NULL USAMOS ESE O SINO USAMOS EL DEL "GlobalProperties"
		// OBTIENE EL VALOR DE LA PROPIEDAD "browser" Y LA GUARDA EN UNA VARIABLE PARA DECIDIR QUE BROWSER USAR
		String browserName = System.getProperty("browser")!= null ? System.getProperty("browser") : prop.getProperty("browser");
		
		//prop.getProperty("browser"); // OBTIENE EL VALOR DE LA PROPIEDAD "browser" Y LA GUARDA EN UNA VARIABLE PARA DECIDIR QUE BROWSER USAR
		
		// CHROME DRIVER
		if (browserName.contains("chrome")) { // SI EL "browserName" CONTIENE "chrome" SE EJECUTA EN CHROME, SE CAMBIO A CONTAINS PORQUE PUEDE VENIR CON EL HEADLESS "chromeheadless"
			ChromeOptions chromeOptions = new ChromeOptions(); // CREAMOS ESTE OBJETO PARA PODER USAR LA PROPIEDAD HEADLESS LUEGO
			WebDriverManager.chromedriver().setup();
			// EVALUA SI EL BROWSER SE EJECUTA EN MODO HEADLESS O NO
			if (browserName.contains("headless")) { // SI AL MOMENTO DE EJECUTAR LOS TEST POR JENKINS O POR LA CONSOLA DE MAVEN LA VARIABLE "browserName" CONTIENE "headless"
				
				chromeOptions.addArguments("headless"); // SE CREA EL ARGUMENTO DE CONFIGURACIÓN PARA LA OPCIÓN HEADLESS
				chromeOptions.addArguments("--window-size=1440,900"); // 
			}
			
			driver = new ChromeDriver(chromeOptions); // SE LE PASA ARGUMENTO DE LA CONFIGURACIÓN AL MOMENTO DE DEFINIR EL DRIVER
													  // EN CASO QUE NO SE CUMPLA LA CONDICIÓN ANTERIOR, LA VARIABLE "chromeOptions" ESTARÁ VACIA Y SE EJECUTARÁ DE FORMA NORMAL SIN MODO HEADLESS
		}
		
		else if (browserName.equalsIgnoreCase("firefox")) {
			
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		
		}
		
		else if (browserName.equalsIgnoreCase("edge")) {
			
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		
		}
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // ESPERA IMPLICITA PARA BUSQUEDAS GENERALES DE ELEMENTOS
		
		if (!browserName.contains("headless")) {
		    driver.manage().window().maximize(); // MAXIMIZAR LA VENTANA CUANDO NO ES MODO HEADLESS PARA EVITAR PROBLEMAS DE RESPONSIVE DESIGN
		}
		
		return driver;
	}
	
	/*AL COLOCAR LA ANOTACIÓN "@BeforeMethod", ESTE MÉTODO SE EJECUTARÁ ANTES DE CUALQUIER MÉTODO @Test POR LO QUE 
	 * NO ES NECESARIO INVOCAR ESTE MÉTODO DESDE NUESTRO @Test PORQUE YA SE EEJECUTARÁ AUTOMÁTICAMENTE*/
	
	@BeforeMethod(alwaysRun = true)
	public LandingPage launchApplication() throws IOException {
		// METODO PARA INICIALIZAR EL DRIVER E INVOCAR EL "goTo" QUE NAVEGA A LA URL
		driver = initializeDriver();
		landingPage = new LandingPage(driver); // CREACIÓN DEL OBJETO DE LA CLASE LandingPage PARA QUE PUEDA INVOCAR EL MÉTODO "goTo"
		landingPage.goTo();
		return landingPage; // COMO ESTAMOS EVITANDO CREAR OBJETOS DE CLASES EN NUESTROS TEST, NECESITAMOS CREARLOS EN LOS MÉTODOS, PERO
							// PARA USARLO EN EL TEST NO BASTA CON HACER LA CLASE "BaseTest" PADRE DEL "SubmitOrderTest" SINO QUE NECESITAMOS RETORNAR ESE OBJETO PARA USARLO ALLÁ
	}
	
	@AfterMethod(alwaysRun = true)
	public void closeBrowser() {
		// METODO PARA CERRAR EL NAVEGADOR
		driver.quit();
	}
	
	
	public List<HashMap<String, String>> getJsonDataToHashMap(String filePath) throws IOException {
		// METODO PARA CONVERTIR UN JSON A STRING
		// LEE TODO EL CONTENIDO DEL ARCHIVO JSON Y LO CONVIERTE EN UN STRING
		// EN ESTE PUNTO, JAVA SOLO TIENE UN STRING GIGANTE, NADA MÁS
		String jsonContent = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);
		
		// OBJECTMAPPER ES LA CLASE DE JACKSON QUE SABE CONVERTIR JSON A OBJETOS JAVA
		ObjectMapper mapper =new ObjectMapper();
		
		// CONVIERTE EL STRING JSON EN UNA LISTA DE HASHMAP
	    // CADA OBJETO DEL JSON SE CONVIERTE EN UN HASHMAP
		// DECIMOS QUE QUEREMOS CONVERTIR EL JSON EN UNA LISTA DE HASHMAP, DONDE CADA HASHMAP TIENE STRING COMO CLAVE Y STRING COMO VALOR
		List <HashMap <String, String>> dataJson = mapper.readValue(jsonContent, new TypeReference<List<HashMap<String, String>>>(){});
		System.out.println("Estoy usando el método de BaseTest");
		
		return dataJson; // RETORNA LA LISTA COMPLETA DE HASHMAP
	}
	
	public String getScreenshot(String testCaseName, WebDriver driver) throws IOException {
		// METODO PARA TOMAR UNA CAPTURA DE PANTALLA DEL NAVEGADOR ACTUAL
	    // EL DRIVER SE CASTEA A TakesScreenshot PARA PODER USAR getScreenshotAs
		File src =((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		
		// SE COPIA LA IMAGEN GENERADA A LA CARPETA "reports"
	    // EL NOMBRE DEL ARCHIVO SE CONSTRUYE USANDO EL NOMBRE DEL TEST
		FileUtils.copyFile(src, new File(System.getProperty("user.dir")+"//reports//"+ testCaseName + ".png"));
		
		// SE RETORNA LA RUTA COMPLETA DE LA IMAGEN
		return System.getProperty("user.dir")+"//reports//"+ testCaseName + ".png";
	}

}
