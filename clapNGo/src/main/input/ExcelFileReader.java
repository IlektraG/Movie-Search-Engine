package main.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelFileReader {
	private static ArrayList<String[]> movies = new ArrayList<String[]>();
	static String[] excelLine = new String[17];
	
	public ExcelFileReader() {
		try {
			movies = parseExcelFile();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public static ArrayList<String[]> parseExcelFile() throws IOException {
		String fileLocation = "";
		fileLocation = "dataset\\movies.xlsx";
		FileInputStream file = new FileInputStream(new File(fileLocation));
	
		@SuppressWarnings("resource")
		org.apache.poi.ss.usermodel.Sheet sheet = (new XSSFWorkbook(file)).getSheetAt(0);
		
		for (Row row : sheet) {
			int i = 0;
			String[] movie = new String[17];
		    for (Cell cell : row) {
		    	
		    	CellType cellType = cell.getCellType();
				if (cellType == CellType.STRING) {
					if (cell.getRichStringCellValue().getString() == "") {
						movie[i] = "----";
					}
					else {
						movie[i] = cell.getRichStringCellValue().getString();
					}
					
					i++;
				} else if (cellType == CellType.NUMERIC) {
					if (DateUtil.isCellDateFormatted(cell)) {
						movie[i] = cell.getDateCellValue().toGMTString() + "";
						i++;
					} else {
						movie[i] = cell.getNumericCellValue() + "";
						i++;
					}
				} 
		    }
		    //Filling empty fields.
			for (int index = 0; index<movie.length; index++) {
				if (movie[index]==null) {
					movie[index] = "----";
				}
			}
		    movies.add(movie);
		}
		excelLine = movies.get(0);
		movies.remove(0);
		movies.remove(3029);
		movies.remove(3364);

		return movies;
		
	}
	public static void printMovie(String[] movie) {
		for (int i = 0; i<movie.length; i++) {
			if (movie[i]==null) {
				movie[i] = "----";
			}
			System.out.println(excelLine[i]+": "+movie[i]);
			
		}
		System.out.println("========================");
	}

	public static void printMovies(ArrayList<String[]> movies) {
		for (int i=0; i< movies.size();i++) {
			printMovie(movies.get(i));
		}
			
		System.out.println("========================");
	}

	public static void setMovies(ArrayList<String[]> movies) {
		ExcelFileReader.movies = movies;
	}

	public static ArrayList<String[]> getMovies() {
		return movies;
	}
}