package thaicompany.testcomponents;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;


//CLASE QUE IMPLEMENTA IRETRYANALYZER PARA DEFINIR CUÁNTAS VECES SE REEJECUTA UN TEST FALLIDO
public class Retry implements IRetryAnalyzer {
	// CONTADOR QUE LLEVA EL NÚMERO DE VECES QUE EL TEST YA FUE REINTENTADO
	int count = 0;
	// MÁXIMO DE VECES QUE SE PERMITE REINTENTAR UN TEST FALLIDO
	int maxTry = 1;

	@Override
	public boolean retry(ITestResult result) {
		// ESTE MÉTODO SE EJECUTA AUTOMÁTICAMENTE CUANDO UN TEST FALLA
		// SI DEVUELVE TRUE, TESTNG VOLVERÁ A EJECUTAR EL TEST
		// SI DEVUELVE FALSE, EL TEST SE MARCA DEFINITIVAMENTE COMO FALLIDO
		if (count<maxTry) {
			count ++;
			return true;
		}
		return false;
	}

}
