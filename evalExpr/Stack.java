package evalExpr;

import java.util.Iterator;
import java.util.NoSuchElementException;

public interface Stack<T> extends Iterable<T>{
	
	default int size() {
		int c=0;
		for( Iterator<T> it= iterator(); it.hasNext(); it.next(), c++ );
		return c;
	}//size
	
	default void clear() {
		Iterator<T> it=iterator();
		while( it.hasNext() ) {
			it.next(); it.remove();
		}
	}//clear
	
	default boolean contains( T e ) {
		for( T x: this )
			if( x.equals(e) ) return true;
		return false;
	}//contains
	
	void push( T e );
	
	default T pop() {
		Iterator<T> it=iterator();
		if( !it.hasNext() ) throw new NoSuchElementException();		
		T x=it.next();
		it.remove();
		return x;
	}//pop
	
	default T top() {
		Iterator<T> it=iterator();
		if( !it.hasNext() ) throw new NoSuchElementException();
		return it.next();
	}//top
	
	default T peek() {
		return top();
	}//peek
	
	default boolean isEmpty() {
		return !iterator().hasNext();
	}//isEmpty
	
}//Stack
