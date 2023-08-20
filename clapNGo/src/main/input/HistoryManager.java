package main.input;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class HistoryManager {

	private static ArrayList<String> searchedTerms = new ArrayList<String>();
	
	public static void loadHistory() throws FileNotFoundException {
		String historyPath = new File("").getAbsolutePath() + ("//history");
		File historyFile = new File(historyPath+"\\history.txt");
		Scanner scanner = new Scanner(historyFile);
		while (scanner.hasNextLine()) {
			String term = scanner.nextLine();
			searchedTerms.add(term);
		}
		scanner.close();
	}
	
	public static void saveHistory(String term) throws IOException {
		String historyPath = new File("").getAbsolutePath() + ("//history");
		File historyFile = new File(historyPath+"\\history.txt");
		FileWriter fileWriter = new FileWriter(historyFile, true);
		if (term.split(" ").length>0) {
			for (int i =0;i<term.split(" ").length;i++) {
				searchedTerms.add(term.split(" ")[i]);
				fileWriter.write(term.split(" ")[i]+"\n");
			}
		}
		else {
			searchedTerms.add(term);
			fileWriter.write(term+"\n");
			
		}
		
		fileWriter.close();
	}
	public static ArrayList<String> getHistoryList() {
		return searchedTerms;
	}
	
	public static void clearHistory() throws IOException {

        String historyPath = new File("").getAbsolutePath() + ("//history");
        File historyFile = new File(historyPath + "\\history.txt");
        PrintWriter writer = new PrintWriter(historyFile);
        writer.print("");
        writer.close();
        searchedTerms.clear();                                 //TO UPDATE ON DELETE RUNTIME
    }
}