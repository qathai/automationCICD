package thaicompany.testcomponents;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import thaicompany.data.ExcelUtils;
import thaicompany.pageobjects.LandingPage;
import thaicompany.pageobjects.UploadPage;

public class BaseTest {
	
	/* LA IDEA DE ESTA CLASE ES DEFINIR TODOS LOS PASOS QUE SON COMUNES ENTRE LAS DIFERENTES PRUEBAS QUE VAMOS A DESARROLLAR
	 * COMO POR EJEMPLO DECLARAR EL DRIVER Y NAVEGAR HASTA LA URL DE LA APLICACIÓN, EL OBJETIVO DE ESTO ES EVITAR REPETIR PASOS 
	 * EN CADA PRUEBA Y DEJAR LOS CASOS DE PRUEBA LO MAS ENFOCADOS A LA PRUEBA REAL*/
	
	public WebDriver driver; // VARIABLE GLOBAL PARA USAR EL DRIVER EN TODA LA CLASE Y EN CLASES HIJAS
	public LandingPage landingPage; // VARIABLE GLOBAL PARA ACCEDER AL PAGE OBJECT DESDE CLASES HIJAS
	public String downloadPath = "C:\\Users\\Thainays\\Documents\\Curso Selenium Webdriver Java\\test-download-upload"; // VARIEBLE DE CLASE PARA RUTA DE DESCARGA
	public ExcelUtils excelUtils; // VARIABLE GLOBAL PARA USAR LOS METODOS DE EXCEL EN CLASES HIJAS
	public UploadPage uploadPage; // VARIABLE GLOBAL PARA ACCEDER AL PAGE OBJECT DE UPLOAD
	
	public WebDriver initializeDriver() throws IOException {
		// CONFIGURACION DEL DRIVER
		
		// CREAR OBJETO PROPERTIES PARA LEER CONFIGURACIONES DEL ARCHIVO .PROPERTIES
		Properties prop = new Properties();
		// ABRIR EL ARCHIVO DE CONFIGURACIÓN DESDE EL PROYECTO
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//java//thaicompany//resources//GlobalData.properties");
		prop.load(fis); // CARGAR LAS PROPIEDADES DEL ARCHIVO EN MEMORIA
		fis.close(); // CERRAR EL FLUJO DE ENTRADA YA QUE EL ARCHIVO YA FUE LEIDO
		
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
			
			// CREAR MAPA DE PREFERENCIAS
			Map<String, Object> prefs = new HashMap<>();
			
			// ESTABLECER RUTA DE DESCARGA
			prefs.put("download.default_directory", downloadPath);
			prefs.put("download.prompt_for_download", false); // DESACTIVAR PREGUNTA DE CONFIRMACION
			prefs.put("download.directory_upgrade", true); // EVITAR QUE CHROME TENGA PROBLEMAS AL ACTUALIZAR LA CARPETA
			prefs.put("plugins.always_open_pdf_externally", true); // FORZAR DESCARGA DE PDF EN LUGAR DE ABRIR EN EL VISOR
			
			// APLICAR PREFERENCIAS
			chromeOptions.setExperimentalOption("prefs", prefs);
			
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
		System.out.println("HOLA CI/CD");
		return driver;
	}
	
	/*AL COLOCAR LA ANOTACIÓN "@BeforeMethod", ESTE MÉTODO SE EJECUTARÁ ANTES DE CUALQUIER MÉTODO @Test POR LO QUE 
	 * NO ES NECESARIO INVOCAR ESTE MÉTODO DESDE NUESTRO @Test PORQUE YA SE EEJECUTARÁ AUTOMÁTICAMENTE*/
	
	public LandingPage launchApplication() throws IOException {
		// METODO PARA INVOCAR EL "goTo" QUE NAVEGA A LA URL
		landingPage = new LandingPage(driver); // CREACIÓN DEL OBJETO DE LA CLASE LandingPage PARA QUE PUEDA INVOCAR EL MÉTODO "goTo"
		landingPage.goTo();
		return landingPage; // COMO ESTAMOS EVITANDO CREAR OBJETOS DE CLASES EN NUESTROS TEST, NECESITAMOS CREARLOS EN LOS MÉTODOS, PERO
							// PARA USARLO EN EL TEST NO BASTA CON HACER LA CLASE "BaseTest" PADRE DEL "SubmitOrderTest" SINO QUE NECESITAMOS RETORNAR ESE OBJETO PARA USARLO ALLÁ
	}
	
	public UploadPage openExcelPage() {
		uploadPage = new UploadPage(driver); // CREA EL OBJETO UploadPage
		uploadPage.goToExcelPage(); // LLAMA AL MÉTODO DE NAVEGACIÓN
		return uploadPage;
	}
	
	public void prepareExcelTest() throws IOException {
		driver = initializeDriver(); // INICIALIZA EL DRIVER
		uploadPage = openExcelPage(); // CREA UPLOADPAGE Y NAVEGA
		excelUtils = new ExcelUtils(); // INICIALIZA EXCELUTILS
	}
	
	public void prepareEcommerceTest() throws IOException {
		driver = initializeDriver(); // INICIALIZA EL DRIVER
		landingPage = new LandingPage(driver); // CREA OBJETO DE PÁGINA
	}
	
	@BeforeMethod(alwaysRun = true)
	public WebDriver setUpDriver() throws IOException {
		// METODO PARA INICIALIZAR EL DRIVER ANTES DE CADA TEST
		driver = initializeDriver();
		return driver;			
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
