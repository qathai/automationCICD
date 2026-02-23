package thaicompany.data;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataReader {
	
	public List<HashMap<String, String>> getJsonDataToHashMap() throws IOException {
		
		// LEE EL JSON Y LO CONVIERTE A STRING
		String jsonContent = FileUtils.readFileToString(new File(System.getProperty("user.dir")+"\\src\\test\\java\\thaicompany\\data\\purchaseOrder.json"), StandardCharsets.UTF_8);
		
		// CONVERTIR EL STRING A HASHMAP
		// CON LAS DEPENDENCIAS QUE TENEMOS HASTA AHORA NO ERA POSIBLE HACERLO, DESCARGAMOS LA DEPENDENCIA: "Jackson Databind"
		ObjectMapper mapper =new ObjectMapper();
		List <HashMap <String, String>> dataJson = mapper.readValue(jsonContent, new TypeReference<List<HashMap<String, String>>>(){// ESTA LINEA NO LA ENTIENDO PARA NADA
			
		});
		System.out.println("Estoy usando el método de DataReader");
		return dataJson;
	}

}
