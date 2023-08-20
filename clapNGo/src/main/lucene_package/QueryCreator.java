package main.lucene_package;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import main.input.HistoryManager;

public class QueryCreator {
	private static String[] searchableFields = {"Title", "Original_title", "Genres", "Keywords", "Tagline", "Overview", "Runtime",
												"Release_date", "Original_language", "Spoken_language"};
	private static ArrayList<String> stopwords = new ArrayList<String>(Arrays.asList("a", "an", "and", "are", "as", "at", "be", "but", "by",
    		"for", "if", "in", "into", "is", "it",
    		"no", "not", "of", "on", "or", "such",
    		"that", "the", "their", "then", "there", "these",
    		"they", "this", "to", "was", "will", "with"));
	private static CharArraySet set = new CharArraySet(stopwords, true);
	private static StandardAnalyzer stopAnalyzer = new StandardAnalyzer(set);
	private static StandardAnalyzer analyzer = new StandardAnalyzer();
	
	public static ArrayList<String[]> search(String searchField, String searchTerm, String path, String orderBy) throws IOException, ParseException  {

        Directory directory = FSDirectory.open(Paths.get(path));
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);

        QueryParser parser = new QueryParser(searchField, analyzer);    //stopAnalyzer is also available!
        Query query = parser.parse(searchTerm);
        TopFieldDocs hits = null;
        HistoryManager.saveHistory(searchTerm);
        
        if (orderBy.equals("Title")) {
            hits =isearcher.search(query, 30, new Sort(new SortField("SortedTitle", SortField.Type.STRING)));
            //printResults(hits.scoreDocs, isearcher);
            return getResults(hits.scoreDocs, isearcher);
        }
        else if (orderBy.equals("ReleaseDate")) {
            //sortByDate, true means reverse sort!
            hits =isearcher.search(query, 200, new Sort(new SortField("SortedReleaseDate", SortField.Type.STRING, true)));
            //printResults(hits.scoreDocs, isearcher);
            return getResults(hits.scoreDocs, isearcher);
        }
        else if (orderBy.equals("Relevance")){
            TopDocs hitsByRelevance =  isearcher.search(query, 30);
            //printResults(hitsByRelevance.scoreDocs, isearcher);
            return getResults(hitsByRelevance.scoreDocs, isearcher);
        }
        else {
            hits =isearcher.search(query, 200, new Sort(new SortField("SortedRating", SortField.Type.STRING, true)));
            //printResults(hits.scoreDocs, isearcher);
            return getResults(hits.scoreDocs, isearcher);
        }
    }

	public static ArrayList<String[]> searchAllFields(String searchTerm, String path, String orderBy) throws IOException, ParseException {

		Directory directory = FSDirectory.open(Paths.get(path));
		DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);
        
        MultiFieldQueryParser multiParser = new MultiFieldQueryParser(searchableFields, analyzer); 	//stopAnalyzer is also available!
        Query query = multiParser.parse(searchTerm);	
        
        TopFieldDocs hits = null;
        HistoryManager.saveHistory(searchTerm);
        if (orderBy.equals("Title")) {
            hits =isearcher.search(query, 30, new Sort(new SortField("SortedTitle", SortField.Type.STRING)));
            //printResults(hits.scoreDocs, isearcher);
            return getResults(hits.scoreDocs, isearcher);
        }																					
        else if (orderBy.equals("ReleaseDate")) {
        	hits =isearcher.search(query, 200, new Sort(new SortField("SortedReleaseDate", SortField.Type.STRING, true)));
        	//printResults(hits.scoreDocs, isearcher);
        	return getResults(hits.scoreDocs, isearcher);
        }
        else if (orderBy.equals("Relevance")){
        	TopDocs hitsByRelevance =  isearcher.search(query, 30);
        	//printResults(hitsByRelevance.scoreDocs, isearcher);
        	return getResults(hitsByRelevance.scoreDocs, isearcher);
        }
        else {
        	hits = isearcher.search(query, 200, new Sort(new SortField("SortedRating", SortField.Type.STRING, true)));
        	//printResults(hits.scoreDocs, isearcher);
        	return getResults(hits.scoreDocs, isearcher);

        }
       
	}

	public static ArrayList<String[]> suggestQuery(String path) throws IOException, ParseException {
        Directory directory = FSDirectory.open(Paths.get(path));
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);
        ArrayList <String> searchedTerms = HistoryManager.getHistoryList();            //get History
        if (searchedTerms.size()>=5) {
            Random rand = new Random();

            int randomId = rand.nextInt(searchedTerms.size()-1);
            String searchTerm = searchedTerms.get(randomId);
            MultiFieldQueryParser multiParser = new MultiFieldQueryParser(searchableFields, analyzer); 	//stopAnalyzer is also available!
            Query query = multiParser.parse(searchTerm);	
            TopDocs hits =  isearcher.search(query, 5);
            /*if (hits.scoreDocs.length > 0) {
                printResults(hits.scoreDocs, isearcher);
            }*/


            return getResults(hits.scoreDocs, isearcher);
        }
        return null;

    }
	
	private static void printResults(ScoreDoc[] hits, IndexSearcher isearcher) throws IOException {
		System.out.println("Found: "+ hits.length+" movies");
        for (int i=0; i<hits.length;i++) {
        	DocumentCreator.printDocument(isearcher.doc(hits[i].doc));
        }
	}
	
	private static ArrayList<String[]> getResults(ScoreDoc[] hits, IndexSearcher isearcher) throws IOException{
        ArrayList<String[]> results = new ArrayList<String[]>();
         for (int i=0; i<hits.length;i++) {
             results.add(DocumentCreator.DocumentToArray(isearcher.doc(hits[i].doc)));
         }

        return results;

    }

	public static String[] getRandomDocument(String path) throws IOException {

        Directory directory = FSDirectory.open(Paths.get(path));
        DirectoryReader ireader = DirectoryReader.open(directory);

        Random rand = new Random();
        int randomId = rand.nextInt(4795);
        Document document = ireader.document(randomId);
        return DocumentCreator.DocumentToArray(document);
    }	
}
