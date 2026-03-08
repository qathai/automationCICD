package cucumber;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

//ANOTACIÓN QUE PERMITE CONFIGURAR LA EJECUCIÓN DE CUCUMBER
@CucumberOptions(
		features = "src/test/java/cucumber", // RUTA DONDE SE ENCUENTRAN LOS ARCHIVOS .FEATURE
		glue = "thaicompany.stepdefinitions", // PAQUETE DONDE ESTÁN LOS STEP DEFINITIONS
		monochrome = true, // HACE QUE LOS RESULTADOS EN CONSOLA SEAN MÁS LEGIBLES
		plugin = {"pretty", "html:target/cucumber.html"}, // SALIDA LEGIBLE EN CONSOLA Y REPORTE HTML EN LA CARPETA TARGET
		tags = "@ExcelValidation") // EJECUTARÁ SOLO FEATURES CON ESE TAG

public class TestNGTestRunner extends AbstractTestNGCucumberTests{
	// ESTA CLASE NO NECESITA MÉTODOS
    // HEREDA LA EJECUCIÓN DESDE AbstractTestNGCucumberTests

}