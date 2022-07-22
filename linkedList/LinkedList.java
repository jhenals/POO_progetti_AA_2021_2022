package linkedList;

import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class LinkedList<T> extends AbstractList<T>{
	 
	private static class Nodo<E>{
		E info;
		Nodo<E> next, prior;
	}//Nodo
	
	private enum Move { UNKNOWN, FORWARD, BACKWARD }
	
	private Nodo<T> first=null, last=null;
	
	private int size=0, modCounter=0;
	
	public int size() { return size; }
	
	public void clear() { 
		first=null; last=null; size=0; 
		modCounter++;
	}//clear
/*
	public void addFirst( T e ) {
		ListIterator<T> lit=this.listIterator();
		lit.add(e);
	}//addFirst
*/
	public void addFirst( T e ) {
		Nodo<T> n=new Nodo<>();
		n.info=e; n.next=first; n.prior=null;
		if( first!=null ) first.prior=n;
		else last=n;
		first=n;
		size++;
		modCounter++;
	}//addFirst
	
	//1.
	public void sort(LinkedList<T> lis, Comparator<? super T> c) {
		if( lis.size()<=1 ) return;
		boolean flag= true;
		int i=1; 
		while( i<size() ) {
			ListIterator<T> lit= lis.listIterator(i);
  			T elemCor= lit.next(), elemPre=lit.previous();
			while( lit.hasPrevious() &&flag ) {
				elemPre=lit.previous();
 				if (c.compare(elemPre, elemCor)>0  ) {
				 	lit.next(); lit.next();
				 	lit.set(elemPre);
				 	lit.previous(); lit.previous();
				}
 				else if (c.compare(elemPre, elemCor)<0 ) {
 					flag= false;
 					lit.next(); lit.next();
 				}
			}
			lit.set(elemCor);  
			flag= true;
			++i;
		}//while		
	}//sort
	
	
	//2. 
	public void addLast( T e ) {
		Nodo<T> n= new Nodo<>();
		n.info= e;
		n.next=null;
		if( first==null) { 
			n.prior=null;
			first=n;last=n;
		}
		else {
			n.prior= last;
			last.next=n;
			last=n;
		}
		size++;
		modCounter++;
	}//addLast
	
	public T getFirst() {
		return first.info;
	}//getFirst
	
	public T getLast() {
		return last.info;
	}//getLast
	
	public T removeFirst() {
		if( first==null ) throw new NoSuchElementException();
		T x= first.info;
		first= first.next;
		size--;
		modCounter++;
		return x;
	}//removeFirst
	
	public T removeLast() {
		if( first==null ) throw new NoSuchElementException();
		T x= last.info;
		last= last.prior;
		last.next= null;
		size--;
		modCounter++;
		return x;
	}//removeLast
	

	public void add( T e ) { 
		 addLast(e);
	}//add

	

	
	public Iterator<T> iterator(){ return new ListIteratore(); }
	
	public ListIterator<T> listIterator(){ return new ListIteratore(); }
	
	public ListIterator<T> listIterator( int pos ) { return new ListIteratore(pos); }
	
	private class ListIteratore implements ListIterator<T>{
		
		private Nodo<T> pre=null, cor=null;
		//la freccia del list iterator e' (logicamente) tra i nodi pre e cor;
		//dopo una next(), il nodo corrente è puntato da pre;
		//dopo una previous(), il nodo corrente è puntato da cor.
		
		private Move lastMove=Move.UNKNOWN;
		private int modCounterMirror=modCounter;
		
		public ListIteratore() {
			pre=null; cor=first;
		}
		public ListIteratore( final int pos ) {
			if( pos<0 || pos>size ) 
				throw new IllegalArgumentException();
			pre=null; cor=first;
			for( int i=0; i<pos; ++i ) {
				pre=cor; 
				cor=cor.next; 
			}
		}
		
		public boolean hasNext() {
			return cor!=null;
		}//hasNext
		
		public T next() {
			if( modCounterMirror!=modCounter ) 
				throw new ConcurrentModificationException();
			if( !hasNext() ) throw new NoSuchElementException();
			lastMove=Move.FORWARD;
			pre=cor; cor=cor.next;
			return pre.info;
		}//next
		
		public void remove() {
			if( modCounterMirror!=modCounter ) 
				throw new ConcurrentModificationException();
			if( lastMove==Move.UNKNOWN ) 
				throw new IllegalStateException();
			Nodo<T> r=null; //nodo da r(imuove
			if( lastMove==Move.FORWARD )
				r=pre;
			else
				r=cor;
			if( r==first ) {
				first=first.next;
				if( first==null ) last=null;
				else first.prior=null;
			}
			else if( r==last ) {
				last=last.prior;
				last.next=null;
			}
			else {//r è un nodo intermedio - occorrono due bypass
				r.prior.next=r.next;
				r.next.prior=r.prior;
			}
			//riposizionare la freccia dell'iterator
			if( lastMove==Move.FORWARD )
				pre=r.prior;
			else 
				cor=r.next;
			size--;
			lastMove=Move.UNKNOWN;
			modCounter++;
			modCounterMirror++;
		}//remove
		
		public boolean hasPrevious() { 
			return pre!=null;
		}//hasPrevious
		
		public T previous() { 
			if( modCounterMirror!=modCounter ) 
				throw new ConcurrentModificationException();
			if( !hasPrevious() ) 
				throw new NoSuchElementException();
			lastMove=Move.BACKWARD;
			cor=pre; pre=pre.prior;
			return cor.info;
		}//previous
		
		public void add( T e ) {
			if( modCounterMirror!=modCounter ) 
				throw new ConcurrentModificationException();
			Nodo<T> n=new Nodo<>();
			n.info=e;
		    //n va messo prima di cor
			n.next=cor;
			//n deve seguire pre
			n.prior=pre; 
			if( cor!=null ) cor.prior=n;
			if( pre!=null ) pre.next=n;
			//aggiustare posizione iteratore
			pre=n;
			//aggiornare eventualmente first e/o last
			if( n.next==first ) first=n;
			if( n.prior==last ) last=n;
			size++;
			lastMove=Move.UNKNOWN;
			modCounter++;
			modCounterMirror++;			
		}//add
		
		public void set( T e ) {
			if( modCounterMirror!=modCounter ) 
				throw new ConcurrentModificationException();
			if( lastMove==Move.UNKNOWN )
				throw new IllegalStateException();
			if( lastMove==Move.FORWARD )
				pre.info=e;
			else
				cor.info=e;
		}//set
	
		//3.
		public int nextIndex() { 
			int i=0;
			if( modCounterMirror!=modCounter ) 
				throw new ConcurrentModificationException();
			if( lastMove==Move.UNKNOWN )
				throw new IllegalStateException();
			if( lastMove== Move.FORWARD ) {
				if( cor== last) return size()-1;
				else { 
					T x= cor.info;
					pre=null; cor=first; 
					while( !(cor.info.equals(x)) ) {
						pre=cor; 
						cor=cor.next; 
						++i; 
					}
				}
			}
			return i;
		}//nextIndex
		
		
		public int previousIndex() { 
			int i =-1;
			if( modCounterMirror!=modCounter ) 
				throw new ConcurrentModificationException();
			if( lastMove==Move.UNKNOWN )
				throw new IllegalStateException();
			if( lastMove== Move.BACKWARD ) {
				if( cor== first) return i;
				else { 
					T x= cor.info;
					pre=null; cor=first; 
					while( !(cor.info.equals(x)) ) {
						pre=cor; 
						cor=cor.next; 
						++i; 
					}
				}
			}
			return i;
		}//previousIndex 
	}//ListIterator
			
	public static void main( String[] args ) {
		LinkedList<Integer> l=new LinkedList<>();
		
		l.addLast(12); l.addLast(2); l.addFirst(30); l.addLast(-1); 
		l.addLast(5); l.add(3); l.add(20); l.addFirst(100);
	
		System.out.println("lista= "+ l);
		System.out.println("size= "+ l.size);
		
		System.out.println("");
		l.sort( l, (i1,i2)->{ 
			return i1<i2?-1:i1>i2?1:0;
		} );
		System.out.println("lista ordinata= "+ l);
		
		
		System.out.println("");
		System.out.println("primo elemento= "+ l.getFirst());
		System.out.println("ultimo elemento= "+ l.getLast());
		System.out.println("elemento rimosso= "+ l.removeFirst() );
		System.out.println("lista aggiornata= "+ l) ;
		System.out.println("elemento rimosso= "+ l.removeLast() );
		System.out.println("lista aggiornata= "+ l) ;
		
		System.out.println("");
		ListIterator<Integer> lit=l.listIterator();
		lit.next();
		lit.next();
		lit.next();
		lit.previous();
		lit.previous();
		System.out.println( "elemento in indice "+ lit.previousIndex()+ " : "+ lit.previous());
		lit.next();
		System.out.println( "elemento in indice "+ lit.nextIndex()+ " : "+ lit.next() );
		
		
		
		
		
/*
		ListIterator<Integer> lit1=l.listIterator();
		int x=lit1.next();
		
		l.addLast(x);
		
		lit1.set(13); //solleva ConcurrentModificationException	 */
		
	}//main

	
}//LinkedList
