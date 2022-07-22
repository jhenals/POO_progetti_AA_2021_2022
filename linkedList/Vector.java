package linkedList;

import java.util.Iterator;

public interface Vector<T> extends Iterable<T> {
	
	default int size() {
		int c=0; 
		//for ( T x : this ) c++;
		for( Iterator<T> it= iterator(); it.hasNext(); it.next() ) c++;
		return c;
	}//size 
	
	default boolean contains( T x ) {
		for( T y: this )
			if( y.equals(x) )
				return true;
		return false;
	}//contains
	
	default void clear() {
		Iterator<T> it= iterator();
		while( it.hasNext() ) {
			it.next();
			it.remove();
		}
	}//clear
	
	default int indexOf( T x ) {
		int i=-1;
		Iterator<T> it= iterator();
		while( it.hasNext() ) {
			i++;
			T y= it.next();
			if( y.equals(x) ) return i;
		}
		return -1;
	}//indexOf
	
	default T get( int indice ) {
		if( indice<0 || indice>=size() ) throw new IndexOutOfBoundsException();
		int i=-1;
		Iterator<T> it= iterator();
		while( it.hasNext() ) {
			i++;
			T x= it.next();
			if( i==indice ) return x;
		}
		return null; //return fittizia- non verrà mai eseguita, o un eccezione o l'indice è buono
	}//get
	
	T set( int indice, T x ); //NON SI PUO' SCRIVERE QUI!
	void add( T x );
	void add( int indice, T x );
	
	default T remove( int indice ) {
		if( indice<0 || indice>=size() ) throw new IndexOutOfBoundsException();
		int i=-1;
		T elem= null;
		Iterator<T> it= iterator();
		while( it.hasNext() ) {
			++i;
			elem= it.next(); 
			if( i== indice ) { 
				it.remove(); //toglie elemento in this(nella collezione) 
				break; 			
			}
		} 
		return elem;
	}//remove
	

	default void remove( T x ) {
		Iterator<T> it= iterator();
		while( it.hasNext() ) {
			T elem= it.next(); 
			if( elem.equals(x) ) { 
				it.remove(); //toglie elemento in this(nella collezione) 
				break; 			
			}
		}
	}//remove- prima elemento x
	
	default boolean isEmpty() {
		Iterator<T> it= iterator();
		return !it.hasNext(); //0 cicli- complessita 0(1)
		//return size()==0;
	}//isEmpty
	
	Vector<T> subVector( int da, int a );
}//Vector
