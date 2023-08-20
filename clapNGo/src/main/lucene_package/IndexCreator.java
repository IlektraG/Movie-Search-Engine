package main.lucene_package;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class IndexCreator {
	
	public static void createIndex(String path) throws IOException {
		
		StandardAnalyzer analyzer = new StandardAnalyzer();
        // creating the index 
		
		if (!indexExists()) {
			ArrayList<Document> documents = DocumentCreator.parseInput();
			System.out.println("Creating index!");
			Directory directory = FSDirectory.open(Paths.get(path));       
	        IndexWriterConfig config = new IndexWriterConfig(analyzer);
	        IndexWriter w = new IndexWriter(directory, config);
	        
	        w.addDocuments(documents);
	        w.close();
		}
	}

	private static boolean indexExists() {
		String path = new File("").getAbsolutePath()+ ("//index");
		File file = new File(path);
		
		return file.list().length>0;
	}
}
