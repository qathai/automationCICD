package thaicompany.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

	XSSFWorkbook workBook;
	XSSFSheet sheet;
	FileInputStream fis;

	public void getExcelFile(String excelPath, String sheetName) throws IOException {
		// CREAR FLUJO DE ENTRADA PARA LEER EL ARCHIVO EXCEL DESDE LA RUTA INDICADA
		fis = new FileInputStream(excelPath);
		// NIVEL 1: EL ARCHIVO
		workBook = new XSSFWorkbook(fis);
		// CERRAR EL "fis" PORQUE SOLO SIRVE PARA CARGAR EL FILE
		fis.close();
		// NIVEL 2: LA HOJA
		sheet = workBook.getSheet(sheetName);
	}

	public int getColumnNumber(String colName) {
		// RECORRER LAS FILAS DEL SHEET PARA OBTENER LA PRIMERA FILA (HEADER)
		Iterator<Row> rows = sheet.rowIterator();

		Row headerRow = rows.next(); // PRIMERA FILA = ENCABEZADOS

		// RECORRER LAS CELDAS DEL HEADER CON "cellIterator"
		Iterator<Cell> headerCells = headerRow.cellIterator();

		// VALOR POR DEFECTO SI NO SE ENCUENTRA LA COLUMNA
		int colInd = -1;

		// CREAR EL OBJETO FORMATTER UNA SOLA VEZ PARA CONVERTIR CUAQUIER DATO A "String"
		DataFormatter formatter = new DataFormatter();

		while (headerCells.hasNext()) {
			Cell cell = headerCells.next();
			String cellValue = formatter.formatCellValue(cell);
			// COMPARAR EL NOMBRE DE LA CELDA CON EL NOMBRE DE COLUMNA RECIBIDO
			if (cellValue.equalsIgnoreCase(colName)) {
				// OBTENER EL INDICE DE LA COLUMNA
				colInd = cell.getColumnIndex();
				break;
			}
		}
		return colInd; // RETORNAR EL INDICE (O -1 SI NO SE ENCONTRO)
	}

	public int getRowNumber(String colName, String fruitName) {
		int rowInd = -1; // VALOR POR DEFECTO SI NO SE ENCUENTRA LA FILA

		// OBTENER EL INDICE DE LA COLUMNA USANDO EL METODO ANTERIOR
		int colInd = getColumnNumber(colName);
		// RECORRER LAS FILAS DEL SHEET
		Iterator<Row> rows = sheet.rowIterator();
		rows.next(); // OMITIR LA FILA HEADER

		while (rows.hasNext()) {
			Row rowActual = rows.next();
			// OBTENER LA CELDA DE LA COLUMNA ESPECIFICA
			Cell cellActual = rowActual.getCell(colInd);

			// CREAR EL OBJETO FORMATTER UNA SOLA VEZ PARA CONVERTIR CUAQUIER DATO A "String"
			DataFormatter formatter = new DataFormatter();
			String formattedCell = formatter.formatCellValue(cellActual);

			// COMPARAR EL VALOR DE LA CELDA CON EL NOMBRE DE FRUTA RECIBIDO
			if (formattedCell.equalsIgnoreCase(fruitName)) {
				// OBTENER EL INDICE DE LA FILA
				rowInd = cellActual.getRowIndex();
				break;
			}
		}
		return rowInd; // RETORNAR EL INDICE (O -1 SI NO SE ENCONTRO)
	}

	public void updateCell(int row, int column, int updateValue) throws IOException {
		// OBTENER LA FILA Y COLUMNA DEL EXCEL USANDO LOS INDICES RECIBIDOS COMO PARÁMETROS
		Row targetRow = sheet.getRow(row);
		Cell targetCell = targetRow.getCell(column);
		// ACTUALIZAR EL VALOR DE LA CELDA CON EL NUEVO VALOR RECIBIDO
		targetCell.setCellValue(updateValue);
	}
	
	public void saveExcel(String outFilePath) throws IOException {
		// CREAR UN FLUJO DE SALIDA PARA ESCRIBIR EL ARCHIVO EXCEL MODIFICADO EN LA RUTA INDICADA
		FileOutputStream fos = new FileOutputStream(outFilePath);
		// ESCRIBIR LOS CAMBIOS REALIZADOS EN EL WORKBOOK HACIA EL ARCHIVO
		workBook.write(fos);
		fos.close(); // CERRAR EL STREAM
	}
	
	public void closeWorkbook() throws IOException {
		// CERRAR EL WORKBOOK PARA LIBERAR RECURSOS
		workBook.close();
		
	}
	
	

}
