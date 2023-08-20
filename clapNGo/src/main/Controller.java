package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.input.HistoryManager;
import main.lucene_package.IndexCreator;
import main.lucene_package.QueryCreator;

@org.springframework.stereotype.Controller
public class Controller {
	ArrayList<String[]> results = new ArrayList<String[]>();
	String movie;
	String option;
	String order;
	ArrayList<String[]>  order_values = new ArrayList<String[]>();
	
	//RUN localhost:8080
	@RequestMapping(path = "/clapNGo", method = RequestMethod.GET)
	public String getSearchingPage(Model model) throws FileNotFoundException {
		order_values.add(new String[]{"relevance", "Relevance"});
		order_values.add(new String[]{"title", "Title"});
		order_values.add(new String[]{"rating", "Rating"});
		order_values.add(new String[]{"release date", "ReleaseDate"});
		
		
		HistoryManager.loadHistory();
		Searcher searcher = new Searcher();
		model.addAttribute("searcher",searcher);
		return "index";
	}



	@RequestMapping(path = "/results")
	public String getResultsPage(@ModelAttribute("searcher") Searcher searcher, Model model) throws IOException, ParseException {
		String path = new File("").getAbsolutePath() + ("//index");
		IndexCreator.createIndex(path);
		if(searcher.search_movie != null) {
			movie = searcher.search_movie;
			option = searcher.search_options;
		}
		searcher.search_movie = movie;
		searcher.search_options = option;
		
		if(searcher.search_order == null) {
			searcher.search_order = "Relevance";
		}
		
		if(searcher.search_options.equals("None")) {
			results = QueryCreator.searchAllFields(searcher.search_movie, path, searcher.search_order);
		}
		else if(searcher.search_options.equals("Released date")) {
			results = QueryCreator.search("Release_date", searcher.search_movie, path, searcher.search_order);
		}
		else {
			results = QueryCreator.search(searcher.search_options, searcher.search_movie, path, searcher.search_order);
		}

		for(int i = 0; i< results.size(); i++) {
			if(results.get(i)[8].equals("----")) {
				results.get(i)[8] = "/pageNotFound";
			}
		}

		ArrayList<String[]> out = new ArrayList<String[]>();
		for(int i = 0; i< 10; i++) {
			if(results.size() > i) {
				out.add(results.get(i));
			}
			else {
				for(int j = i; j< 10; j++) {
					String[] null_string = { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
					out.add(null_string);
				}
				
				break;
			}
		}
		int movies_index=10;
		if(results.size() < 10) {
			movies_index = 0;
		}
		Searcher newSearcher = new Searcher();
		newSearcher.search_movie = searcher.getSearch_movie();
		newSearcher.search_options = searcher.getSearch_options();
		newSearcher.search_order = searcher.getSearch_order();

		ArrayList<String[]> historySuggestions = QueryCreator.suggestQuery(path);
		if(historySuggestions != null) {
			for(int i = 0; i< historySuggestions.size(); i++) {
				if(historySuggestions.get(i)[8].equals("----")) {
					historySuggestions.get(i)[8] = "/pageNotFound";
				}
			}
		}
		

		
		order = newSearcher.search_order; 
		
		model.addAttribute("order", order_values);
		model.addAttribute("historySuggestion", historySuggestions);
		model.addAttribute("searcher", newSearcher);
		model.addAttribute("results", out);
		model.addAttribute("movies_index", movies_index);
		return "results";
	}

	
	@RequestMapping(path = "/pageNotFound")
	public String getPageNotFound() {
		return "pageNotFound";
		
	}
	
	@RequestMapping(path = "/more_results")
	public String getAnotherResultPage(@RequestParam("searcher") String searcher, @RequestParam("movies_index") int movies_index, Model model) throws IOException, ParseException {
		Searcher newSearcher = new Searcher();
		//newSearcher.search_movie = "bad";
		//newSearcher.search_options = "None";
		newSearcher.search_movie = movie;
		newSearcher.search_options = option;
		newSearcher.search_order = order;
		ArrayList<String[]> out = new ArrayList<String[]>();
		for(int i = movies_index; i< movies_index+10; i++) {
			if(results.size() > i) {
				out.add(results.get(i));
			}
			else {
				for(int j = i; j< movies_index + 10; j++) {
					String[] null_string = { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
					out.add(null_string);
				}
				break;
			}
		}
		String path = new File("").getAbsolutePath() + ("//index");
		ArrayList<String[]> historySuggestions = QueryCreator.suggestQuery(path);
		if(historySuggestions != null) {
			for(int i = 0; i< historySuggestions.size(); i++) {
				if(historySuggestions.get(i)[8].equals("----")) {
					historySuggestions.get(i)[8] = "/pageNotFound";
				}
			}
		}
		order = newSearcher.search_order;
		
		for(int i=0; i<4; i++) {
			if(order_values.get(i)[1].equals(order)) {
				Collections.swap(order_values, 0, i);
				break;
			}
		}
		
		model.addAttribute("order", order_values);
		model.addAttribute("historySuggestion", historySuggestions);
		model.addAttribute("searcher", newSearcher);
		model.addAttribute("results", out);
		model.addAttribute("movies_index", movies_index);
		return "/more_results";
	}


	@RequestMapping(path = "/clearHistory")
	public String getClearHistory(@RequestParam("searcher") String searcher, Model model) throws IOException, ParseException {
		Searcher newSearcher = new Searcher();
		HistoryManager.clearHistory();
		newSearcher.search_movie = movie;
		newSearcher.search_options = option;
		newSearcher.search_order = order;
		ArrayList<String[]> out = new ArrayList<String[]>();
		for(int i = 0; i< 10; i++) {
			if(results.size() > i) {
				out.add(results.get(i));
			}
			else {
				for(int j = i; j< 10; j++) {
					String[] null_string = { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
					out.add(null_string);
				}
				
				break;
			}
		}

		int movies_index=10;
		if(results.size() < 10) {
			movies_index = 0;
		}
		
		model.addAttribute("order", order_values);
		model.addAttribute("searcher", newSearcher);
		model.addAttribute("results", out);
		model.addAttribute("movies_index", movies_index);
		return "/results";
	}

	@RequestMapping(path = "/previous_results")
	public String getPreviousResultPage(@RequestParam("searcher") String searcher, @RequestParam("movies_index") int movies_index, Model model) throws IOException, ParseException {
		Searcher newSearcher = new Searcher();
		//newSearcher.search_movie = "bad";
		newSearcher.search_movie = movie;
		newSearcher.search_options = option;
		newSearcher.search_order = order;
		ArrayList<String[]> out = new ArrayList<String[]>();
		int printed_movies = 10;
		if(results.size() < 10) {
			movies_index = results.size();
			printed_movies = results.size();
		}
		
		for(int i = movies_index-printed_movies; i< movies_index; i++) {
			if(results.size() > i) {
				out.add(results.get(i));
			}
			else {
				for(int j = i; j < movies_index; j++) {
					String[] null_string = { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
					out.add(null_string);
				}
				break;
			}
		}
		if(results.size() < 10) {
			for(int j = results.size(); j < 10; j++) {
				String[] null_string = { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
				out.add(null_string);
			}
			movies_index = 0;
		}
		
		order = newSearcher.search_order; 
		
		model.addAttribute("order", order_values);
		model.addAttribute("searcher", newSearcher);
		model.addAttribute("results", out);
		model.addAttribute("movies_index", movies_index);
		return "/more_results";
	}
	
	@RequestMapping(path = "/documentResults")
	public String getDocumentResults(@RequestParam("document") List<String> document_result, @ModelAttribute("documentResults") Searcher searcher, Model model) {
		String output = "<b><font color='black'><mark>" + "Kalispera" + "</font></mark></b>";

		model.addAttribute("output", output);
		
		model.addAttribute("document", document_result);
		return "document_results";
	}
	
	
	@RequestMapping(path = "/feelingLucky")
	public String getFeelingLuckyPage(Model model) throws IOException {
		String path = new File("").getAbsolutePath() + ("//index");
		String[] luckyResult = QueryCreator.getRandomDocument(path);
		if(luckyResult[8].equals("----")) {
			luckyResult[8] = "/pageNotFound";
		}
		model.addAttribute("output", "Kalispera");
		model.addAttribute("document", luckyResult);
		return "lucky_page";
	}
}