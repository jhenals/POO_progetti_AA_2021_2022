package evalExpr;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class StackConcatenato<T> extends AbstractStack<T> {
	
	private static class Nodo<E>{
		E info;
		Nodo<E> next;
	}//Nodo
	
	private Nodo<T> testa=null;
	private int size=0;
	
	public int size() { return size; }
	public void clear() { testa=null; size=0; }
	public boolean contains( T e ) {
		for( T x: this )
			if( x.equals(e) ) return true;
		return false;
	}//contains
	public void push( T e ) {
		Nodo<T> n=new Nodo<>(); n.info=e;
		n.next=testa;
		testa=n;
		size++;
	}//push
	public T pop() {
		if( testa==null ) throw new NoSuchElementException();
		T x=testa.info;
		testa=testa.next;
		size--;
		return x;
	}//pop
	public T top() {
		if( testa==null ) throw new NoSuchElementException();
		return testa.info;		
	}//top
	
	public Iterator<T> iterator(){
		return new IteratoreStack();
	}//iterator
	
	private class IteratoreStack implements Iterator<T>{
		private Nodo<T> pre=null, cor=null;
		
		public boolean hasNext() {
			if( cor==null ) return testa!=null;
			return cor.next!=null;
		}//hasNext
		
		public T next() {
			 if( !hasNext() ) throw new NoSuchElementException();
			 if( cor==null ) cor=testa;
			 else {
				 pre=cor;
				 cor=cor.next;
			 }
			 //cor punta al nodo corrente
			 return cor.info; //qui c'è il "consumo"
		}//next
		
		public void remove() {
			if( pre==cor ) throw new IllegalStateException();
			if( cor==testa ) testa=testa.next;
			else {//cor punta ad un nodo dal 2 all'ultimo
				pre.next=cor.next;
			}
			size--;
			cor=pre; //arretriamo cor
		}//remove
		
	}//IteratoreStack	
	
}//StackConcatenato
