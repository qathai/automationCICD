package thaicompany.resources;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReporterNG {

	
	public static ExtentReports getReportObject() {
		// MÉTODO PARA CONFIGURAR EXTENT REPORT, ES "static" PARA PODER INVOCARLO DIRECTAMENTE CON LA CLASE
		// SE DEFINE LA RUTA DONDE SE GENERARÁ EL ARCHIVO HTML DEL REPORTE
		// System.getProperty("user.dir") OBTIENE LA RAÍZ DEL PROYECTO
		String path = System.getProperty("user.dir") + "//reports//index.html";

		// SE CREA EL OBJETO ExtentSparkReporter. ESTE OBJETO ES EL ENCARGADO DE GENERAR
		// EL REPORTE EN FORMATO HTML
		// ES OBLIGATORIO PASARLE LA RUTA DONDE SE GUARDARÁ EL ARCHIVO
		ExtentSparkReporter reporter = new ExtentSparkReporter(path);

		// SE CONFIGURA EL NOMBRE GENERAL DEL REPORTE, SE MUESTRA COMO TÍTULO DENTRO DEL
		// HTML
		reporter.config().setReportName("Web Automation Results");

		// SE CONFIGURA EL TÍTULO DEL DOCUMENTO HTML, APARECE EN LA PESTAÑA DEL
		// NAVEGADOR
		reporter.config().setDocumentTitle("Test Results");

		// SE CREA EL OBJETO PRINCIPAL ExtentReports
		// ESTE OBJETO ES EL QUE CENTRALIZA TODA LA INFORMACIÓN DE LOS TESTS
		ExtentReports extent = new ExtentReports();

		// SE ADJUNTA EL REPORTER (HTML) AL OBJETO ExtentReports, SIN ESTA LÍNEA, NO SE
		// GENERA NINGÚN REPORTE
		extent.attachReporter(reporter);
		extent.setSystemInfo("Tester", "Thai"); // SE AGREGA INFORMACIÓN ADICIONAL AL REPORTE
		
		return extent;

	}

}
