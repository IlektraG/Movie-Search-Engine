package main;

public class Searcher {
	String search_movie;
	String search_options;
	String search_order;
	
	public String getSearch_options() {
		return search_options;
	}

	public void setSearch_options(String search_options) {
		this.search_options = search_options;
	}

	public String getSearch_movie() {
		return search_movie;
	}


	public void setSearch_order(String search_order) {
		this.search_order = search_order;
	}

	public String getSearch_order() {
		return search_order;
	}
	
	@Override
	public String toString() {
		return "Searcher [search_movie=" + search_movie + ", search_options=" + search_options + ", search_order=" + search_order  + "]";
	}

	public void setSearch_movie(String search_movie) {
		this.search_movie = search_movie;
	}
}
