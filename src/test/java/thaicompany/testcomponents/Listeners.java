package thaicompany.testcomponents;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import thaicompany.resources.ExtentReporterNG;

public class Listeners extends BaseTest implements ITestListener {
	// CON ESTA VARIABLE GLOBAL PODEMOS USAR "extent" y "ExtentTest" EN CUALQUIER MÉTODO DE ESTA CLASE
	ExtentReports extent = ExtentReporterNG.getReportObject();
	ExtentTest test;
	ThreadLocal<ExtentTest> threadLocal = new ThreadLocal<ExtentTest>();

	@Override
	public void onTestStart(ITestResult result) {
		// A PARTIR DE AQUÍ, EXTENT COMIENZA A MONITOREAR ESTE TEST
		// DEVUELVE UN OBJETO QUE REPRESENTA ESE TEST. SE UTILIZA PARA REGISTRAR PASOS, MENSAJES Y RESULTADOS DE ESA EJECUCIÓN.
		test = extent.createTest(result.getMethod().getMethodName());
		threadLocal.set(test); // "threadLocal.set()" MANTIENE UN VALOR INDEPENDIENTE PARA CADA HILO DE EJECUCIÓN, PARA CADA test
	}
	
		
	@Override
	public void onTestSuccess(ITestResult result) {
		// ESTO SE MOSTRARÁ CUANDO EL TEST SEA EXITOSO
		// SE OBTIENE EL TEST ASOCIADO AL HILO ACTUAL CON "threadLocal.get()" Y SE REGISTRA COMO PASS EN EL REPORTE CON ESE TEXTO
		threadLocal.get().log(Status.PASS, "Test Passed");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		// MÉTODO QUE TOMA SCREENSHOT DEL NAVEGADOR AL MOMENTO DEL ERROR
		
		// result.getThrowable() CONTIENE LA EXCEPCIÓN O ERROR LANZADO POR EL TEST
		// threadLocal.get() OBTIENE EL TEST ASOCIADO AL HILO ACTUAL Y REGISTRA EL FALLO EN EXTENT REPORTS
		threadLocal.get().fail(result.getThrowable());
		
		// EL LISTENER NO TIENE ACCESO DIRECTO AL DRIVER DEL TEST,
	    // POR LO QUE NECESITAMOS RECUPERARLO DESDE LA INSTANCIA REAL DEL TEST QUE FALLÓ
		try {
			// OBTENEMOS LA INSTANCIA REAL DEL TEST QUE FALLÓ (POR EJEMPLO SubmitOrderTest)
	        // LUEGO ACCEDEMOS A SU CLASE REAL
	        // BUSCAMOS EL ATRIBUTO LLAMADO "driver"
	        // Y EXTRAEMOS EL VALOR DEL DRIVER QUE ESTABA EN USO DURANTE LA EJECUCIÓN
			driver = (WebDriver) result.getTestClass().getRealClass().getField("driver").get(result.getInstance());
		} catch (Exception e1) {
			e1.printStackTrace(); 
		}
		String filePath = null; // VARIABLE PARA GUARDAR LA RUTA DEL SCREENSHOT
		
		try {
			// LLAMAMOS AL MÉTODO getScreenshot Y CAPTURAMOS EL PATH QUE NOS RETORNA, ESE PATH ES LA IMAGEN
	        // LE PASAMOS EL NOMBRE DEL MÉTODO QUE SERÁ EL NOMBRE DEL ARCHIVO
	        // Y EL DRIVER QUE RECUPERAMOS DESDE EL TEST
			filePath = getScreenshot(result.getMethod().getMethodName(), driver);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ADJUNTAMOS EL SCREENSHOT AL REPORTE DE EXTENT REPORTS
		// SE OBTIENE EL TEST ASOCIADO AL HILO ACTUAL CON "threadLocal.get()" Y SE AGREGA LA IMAGEN DEL FALLO
		threadLocal.get().addScreenCaptureFromPath(filePath, result.getMethod().getMethodName());
	}
	
	@Override
	public void onFinish(ITestContext context) {
		// ESTO HACE QUE SE GENERE EL REPORTE CON TODAS LAS ENTRADAS QUE YA CAPTURÓ
		// SE EJECUTARÁ AL TERMINAR TODA LA EJECUCIÓN DE LOS @Test
		extent.flush();
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		ITestListener.super.onTestSkipped(result);
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
	}

	@Override
	public void onTestFailedWithTimeout(ITestResult result) {
		// TODO Auto-generated method stub
		ITestListener.super.onTestFailedWithTimeout(result);
	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		ITestListener.super.onStart(context);
	}


	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}
	

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return ITestListener.super.isEnabled();
	}
	

}
