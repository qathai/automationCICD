package thaicompany.pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import thaicompany.abstractcomponents.AbstractComponent;

//VAMOS A CONVERTIR ESTA CLASE EN HIJA DE "AbstractComponent" PARA PODER USAR LOS METODOS DE ESA CLASE SIN VOLVERLOS A CREAR
public class ProductCatalogue extends AbstractComponent {
	WebDriver driver; 

	// CONSTRUCTOR DE LA CLASE, RECIBE EL DRIVER DESDE EL TEST PRINCIPAL "SubmitOrderTest"
	public ProductCatalogue(WebDriver driver) {
		// INICIALIZACIÓN
		super(driver); // ENVIA EL DRIVER AL CONSTRUCTOR DE LA CLASE PADRE
					   // CADA CLASE HIJO DEBE ENVIAR EL PARÁMETRO AL PADRE CON "suoer()", ASI TODOS USAN EL MISMO DRIVER
		
		this.driver = driver; // ASIGNA EL DRIVER RECIBIDO A LA VARIABLE DE ESTA CLASE
		
		PageFactory.initElements(driver, this); // INICIALIZA TODOS LOS ELEMENTOS DE ESTA CLASE QUE TENGAN @FindBy 
												// PAGEFACTORY SE ENCARGA DE DECIRLE A SELENIUM CÓMO ENCONTRARLOS CUANDO SE USEN
	}

	@FindBy(css = "div.col-lg-4") // INDICA A PAGEFACTORY CÓMO ENCONTRAR LOS ELEMENTOS EN LA PÁGINA.
	List<WebElement> products;    // "products" ES LA LISTA QUE LUEGO CONTENDRÁ TODOS LOS ELEMENTOS <div class="col-lg-4">.
								  // POR AHORA SOLO SE DECLARA CÓMO BUSCARLOS, NO SE LLENAN AÚN.
	
	@FindBy(css = ".ng-animating")// LOCATOR QUE VOY A USAR PARA ESPERAR EN  waitForElementToDesappear
	WebElement loader;

	By productsBy = By.cssSelector("div.col-lg-4"); // AQUÍ ESTÁS CREANDO UN LOCATOR PARA USARLO EN LA ESPERA waitForAllElementsToAppear
													// "By" ES UN TIPO DE OBJETO QUE SELENIUM USA PARA DECIR CÓMO ENCONTRAR ELEMENTOS
	
	By addToCartBy = By.cssSelector(".btn.w-10"); // ES EL LOCATOR QUE SE VA A USAR PARA EL "AddToCard"
	
	By toastMessageBy = By.cssSelector("#toast-container"); // ES EL LOCATOR QUE VOY A USAR EN LA ESPERA waitForElementToAppear
	
	
	
	// METODO DE ACCIÓN - METODO PARA OBTENER LA LISTA DE PRODUCTOS
	// ESPERA A QUE TODOS LOS ELEMENTOS QUE COINCIDAN CON “productsBy” ESTÉN VISIBLES EN LA PÁGINA ANTES DE CONTINUAR
	// SE DEVUELVE LA LISTA DE ELEMENTOS QUE PageFactory YA SABE CÓMO ENCONTRAR, LO HACE CON LO QUE DECLARÉ CON “@FindBy”
	public List<WebElement> getProductList() {
		waitForAllElementsToAppear(productsBy); 
		return products; 
	}
	
	public WebElement getProductByName(String productName) {
		WebElement filterProduct = getProductList().stream().filter(s -> s.findElement(By.cssSelector("b")).getText().equals(productName)).findFirst().orElse(null);
		return filterProduct;
		// USAMOS STREAM PARA RECORRER LA LISTA
		// filter: SOLO DEJA PASAR EL PRODUCTO CUYO TEXTO COINCIDA CON productName
		// findFirst: DEVUELVE EL PRIMER ELEMENTO QUE CUMPLA LA CONDICION
		// orElse(null): SI NO ENCUENTRA NINGUNO, DEVUELVE null EN VEZ DE LANZAR EXCEPCION
	}
	
	public void addProductToCart(String productName) { // AQUI SUPUESTAMENTE LO QUE PASA ES QUE EL NOMBRE DEL productName DEBE VENIR DEL TEST
		WebElement filterProduct =  getProductByName(productName); // NECESITO SABER PORQUE NECESITO ESTE PASO Y TAMBIEN NECESITO ENTENDER PORQUE EN EL NOMBRE DEL METODO SE DECLARA EL TIPO DE VARIABLE Y LUEGO DENTRO DEL METODO NO VEO QUE PONGAN EL TIPO
		
		// VALIDAR QUE EL PRODUCTO FUE ENCONTRADO ANTES DE HACER CLICK
		if (filterProduct!= null) {
			filterProduct.findElement(addToCartBy).click();
				}
		
		// ESPERAR HASTA QUE APAREZCA EL MENSAJE TOAST DE CONFIRMACION
		waitForElementToAppear(toastMessageBy);
		
		// ESPERAR A QUE DESAPAREZCA LA ANIMACION PARA EVITAR ERRORES DE INTERACCION
		waitForElementToDesappear(loader);			
	}
	
	
}
