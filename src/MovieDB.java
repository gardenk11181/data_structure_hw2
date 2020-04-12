import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Genre, Title 을 관리하는 영화 데이터베이스.
 * 
 * MyLinkedList 를 사용해 각각 Genre와 Title에 따라 내부적으로 정렬된 상태를  
 * 유지하는 데이터베이스이다. 
 */
public class MovieDB {
	MyLinkedList<Genre> dbGenres;

    public MovieDB() {
        // FIXME implement this
    	// HINT: MovieDBGenre 클래스를 정렬된 상태로 유지하기 위한 
    	// MyLinkedList 타입의 멤버 변수를 초기화 한다.
		dbGenres = new MyLinkedList<Genre>();
    }

    public void insert(MovieDBItem item) {
        // FIXME implement this
        // Insert the given item to the MovieDB.

    	// Printing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.
//        System.err.printf("[trace] MovieDB: INSERT [%s] [%s]\n", item.getGenre(), item.getTitle());

		Genre itemGenre = new Genre(item.getGenre());
		String itemTitle = item.getTitle();

		int index=0;
		for(Genre genre: dbGenres) {
			if(itemGenre.compareTo(genre)==0) {
				genre.addMovie(itemTitle);
				return;
			} else if(itemGenre.compareTo(genre)<0) {
				itemGenre.addMovie(itemTitle);
				dbGenres.add(index,itemGenre);
				return;
			}
			index++;
		}
		itemGenre.addMovie(itemTitle);
		dbGenres.add(index,itemGenre);

    }

    public void delete(MovieDBItem item) {
        // FIXME implement this
        // Remove the given item from the MovieDB.
    	
    	// Printing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.
//        System.err.printf("[trace] MovieDB: DELETE [%s] [%s]\n", item.getGenre(), item.getTitle());

		Genre itemGenre = new Genre(item.getGenre());
		String itemMovie = item.getTitle();
		for(Genre genre:dbGenres) {
			if(genre.compareTo(itemGenre)==0) {
				genre.removeMovie(itemMovie);
			}
		}

    }

    public MyLinkedList<MovieDBItem> search(String term) {
        // FIXME implement this
        // Search the given term from the MovieDB.
        // You should return a linked list of MovieDBItem.
        // The search command is handled at SearchCmd class.
    	
    	// Printing search results is the responsibility of SearchCmd class. 
    	// So you must not use System.out in this method to achieve specs of the assignment.
    	
        // This tracing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.
//    	System.err.printf("[trace] MovieDB: SEARCH [%s]\n", term);
    	
    	// FIXME remove this code and return an appropriate MyLinkedList<MovieDBItem> instance.
    	// This code is supplied for avoiding compilation error.   
        MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();

        for(Genre genre: dbGenres) {
        	for(String movie: genre.getMovieList()) {
        		if(movie.contains(term)) results.add(new MovieDBItem(genre.getItem(),movie));
			}
		}

        return results;
    }
    
    public MyLinkedList<MovieDBItem> items() {
        // FIXME implement this
        // Search the given term from the MovieDatabase.
        // You should return a linked list of QueryResult.
        // The print command is handled at PrintCmd class.

    	// Printing movie items is the responsibility of PrintCmd class. 
    	// So you must not use System.out in this method to achieve specs of the assignment.

    	// Printing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.
//        System.err.printf("[trace] MovieDB: ITEMS\n");

    	// FIXME remove this code and return an appropriate MyLinkedList<MovieDBItem> instance.
    	// This code is supplied for avoiding compilation error.   
        MyLinkedList<MovieDBItem> results = new MyLinkedList<>();


        for(Genre genre: dbGenres) {
        	for(String title: genre.getMovieList()) {
        		results.add(new MovieDBItem(genre.getItem(),title));
			}
		}
        
    	return results;
    }
}

class Genre extends Node<String> implements Comparable<Genre> {
	private MovieList movieList = new MovieList();

	public MovieList getMovieList() {
		return movieList;
	}

	public void removeMovie(String movie) {
		movieList.remove(movie);
	}

	public void addMovie(String movie) {
		movieList.add(movie);
	}

	public Genre(String name) {
		super(name);
	}
	
	@Override
	public int compareTo(Genre o) {
		return super.getItem().compareTo(o.getItem());
	}

//	@Override
//	public int hashCode() {
//		throw new UnsupportedOperationException("not implemented yet");
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		throw new UnsupportedOperationException("not implemented yet");
//	}
}

class MovieList implements ListInterface<String> {
	Node<String> head;
	int numItems;
	public MovieList() {
		head = new Node<String>(null);
	}

	@Override
	public final Iterator<String> iterator() {
		return new MovieListIterator<String>(this);
	}

	@Override
	public boolean isEmpty() {
		return numItems==0;
	}

	@Override
	public int size() {
		return numItems;
	}

	@Override
	public void add(String item) { // 정렬하며 무비리스트 삽입
		Node<String> last = head;

		for(int i=0; i<numItems; i++) {
			if(last.getNext()!=null && last.getNext().getItem().compareTo(item)==0) return;
			if(last.getNext()==null || item.compareTo(last.getNext().getItem())<0) break;
			last = last.getNext();
		}
		if(last.getNext()==null) {
			last.setNext(new Node<>(item));

		} else {
			Node<String> temp = last.getNext();
			last.setNext(new Node<>(item));
			last.getNext().setNext(temp);
		}


		numItems+=1;
	}

	public void remove(String item) {
		Node<String> last = head;
		for(int i=0; i<numItems; i++) {
			if(last.getNext().getItem().compareTo(item)==0) {
				if(last.getNext().getNext()==null) {
					last.setNext(null);
				} else {
					last.setNext(last.getNext().getNext());
				}
				return;
			}
			last = last.getNext();
		}
	}

	@Override
	public String first() {
		return head.getItem();
	}

	@Override
	public void removeAll() {
		head.setNext(null);
	}
}

class MovieListIterator<T> implements Iterator<String> {
	// FIXME implement this
	// Implement the iterator for MyLinkedList.
	// You have to maintain the current position of the iterator.
	private MovieList list;
	private Node<String> curr;
	private Node<String> prev;

	public MovieListIterator(MovieList list) {
		this.list = list;
		this.curr = list.head;
		this.prev = null;
	}

	@Override
	public boolean hasNext() {
		return curr.getNext() != null;
	}

	@Override
	public String next() {
		if (!hasNext())
			throw new NoSuchElementException();

		prev = curr;
		curr = curr.getNext();

		return curr.getItem();
	}

	@Override
	public void remove() {
		if (prev == null)
			throw new IllegalStateException("next() should be called first");
		if (curr == null)
			throw new NoSuchElementException();
		prev.removeNext();
		list.numItems -= 1;
		curr = prev;
		prev = null;
	}


}