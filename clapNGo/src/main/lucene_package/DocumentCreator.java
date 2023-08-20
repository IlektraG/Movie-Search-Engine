package main.lucene_package;

import java.util.ArrayList;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.util.BytesRef;
import main.input.ExcelFileReader;

public class DocumentCreator {
	static ArrayList<String[]> movies = new ArrayList<String[]>();
	
	public DocumentCreator() {
		//create lucene docs.
	}
	
	private static Document createDocument(String[] movies) {
		Document document = new Document();
		document.add(new SortedDocValuesField("SortedTitle", new BytesRef(movies[0]))); //for sort ,not indexed, not stored
		document.add(new TextField("Title", movies[1], Field.Store.YES));
		document.add(new TextField("Original_title", movies[1], Field.Store.YES));
		document.add(new TextField("Genres", movies[2], Field.Store.YES));
		document.add(new TextField("Keywords", movies[3], Field.Store.YES));
		document.add(new TextField("Tagline", movies[4], Field.Store.YES));
		document.add(new TextField("Overview", movies[5], Field.Store.YES));
		document.add(new StringField("Runtime", movies[6], Field.Store.YES));	//indexed as single token indexed
		document.add(new TextField("Release_date", movies[7], Field.Store.YES));
		
		if (movies[7].split(" ").length>1) {
			document.add(new SortedDocValuesField("SortedReleaseDate", new BytesRef(movies[7].split(" ")[2]))); //for sort ,not indexed, not stored
		}
		
		document.add(new StoredField("Homepage", movies[8]));	//stored, not indexed
		document.add(new TextField("Original_language", movies[9], Field.Store.YES));
		document.add(new TextField("Spoken_language", movies[10], Field.Store.YES));
		document.add(new StoredField("Production_Countries", movies[11])); //stored, not indexed
		document.add(new StoredField("Production_Companies", movies[12])); //stored, not indexed
		document.add(new StoredField("Budget", movies[13])); //only stored not indexed, so it's no searchable
		document.add(new StoredField("Revenue", movies[14])); //stored, not indexed
		document.add(new StoredField("Vote_Count", movies[15])); //stored, not indexed
		document.add(new StoredField("Vote_Average", movies[16])); //stored, not indexed
		document.add(new SortedDocValuesField("SortedRating", new BytesRef(movies[16]))); //for sort ,not indexed, not stored

		return document;
			
	}
	public static Document createDocumentfromString(String field) {
		Document document = new Document();
		document.add(new TextField("Suggestion", field, Field.Store.YES)); //for full text search indexed
		return document;
	}
	
	protected static ArrayList<Document> parseInput() {
		ExcelFileReader excelReader = new ExcelFileReader();
		movies = ExcelFileReader.getMovies();
		ArrayList<Document> documents = new ArrayList<Document>();
		for (int i = 0; i< movies.size(); i++) {
			documents.add(i, createDocument(movies.get(i)));
		}
		
		System.out.println("Documents created: "+documents.size());
		System.out.println("==============================");
		return documents;
	}
	protected static void printDocument(Document document) {
		System.out.println("Title: " +document.get("Title"));
		System.out.println("Original title: " +document.get("Original_title"));
		System.out.println("Genres: " +document.get("Genres"));
		System.out.println("Keywords: " +document.get("Keywords"));
		System.out.println("Tagline: " +document.get("Tagline"));
		System.out.println("Overview: " +document.get("Overview"));
		System.out.println("Runtime: " +document.get("Runtime"));
		System.out.println("Release date: " +document.get("Release_date"));
		System.out.println("Homepage: " +document.get("Homepage"));
		System.out.println("Original language: " +document.get("Original_language"));
		System.out.println("Spoken language: " +document.get("Spoken_language"));
		System.out.println("Production Countries: " +document.get("Production_Countries"));
		System.out.println("Production Companies: " +document.get("Production_Companies"));
		System.out.println("Budget: " +document.get("Budget"));
		System.out.println("Revenue: " +document.get("Revenue"));
		System.out.println("Vote Count: " +document.get("Vote_Count"));
		System.out.println("Vote Average: " +document.get("Vote_Average"));
		System.out.println("=============================================");

	}
	protected static String[] DocumentToArray(Document document) {
		
		String[] movieResult = new String[17];
		movieResult[0] = document.get("Title");
		movieResult[1] = document.get("Original_title");
		movieResult[2] = document.get("Genres");
		movieResult[3] = document.get("Keywords");
		movieResult[4] = document.get("Tagline");
		movieResult[5] = document.get("Overview");
		movieResult[6] = document.get("Runtime");
		movieResult[7] = document.get("Release_date");
		movieResult[8] = document.get("Homepage");
		movieResult[9] = document.get("Original_language");
		movieResult[10] = document.get("Spoken_language");
		movieResult[11] = document.get("Production_Countries");
		movieResult[12] = document.get("Production_Companies");
		movieResult[13] = document.get("Budget");
		movieResult[14] = document.get("Revenue");
		movieResult[15] = document.get("Vote_Count");
		movieResult[16] = document.get("Vote_Average");
		return movieResult;
			
	}
}