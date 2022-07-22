package evalExpr;

import java.util.Iterator;

public abstract class AbstractStack<T> implements Stack<T> {
	
	public String toString() {
		StringBuilder sb=new StringBuilder(200);
		sb.append("[");
		Iterator<T> it=iterator();
		while( it.hasNext() ) {
			T x=it.next();
			sb.append(x);
			if( it.hasNext() ) sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}//toString
	
	
	public int hashCode() {
		int M= 43, h=0;
		for( T e: this ) {
			h= h*M+e.hashCode();		
		}
		return h;
	}//hashCode
	
	public boolean equals( Object x ) {
		if( !(x instanceof Stack) ) return false;
		if( x.equals(this) ) return true;
		Stack<T> s= (Stack<T>) x;
		if( s.size() != this. size() ) return false;
		Iterator<T> it1= iterator(), it2= s.iterator() ;
		while( it1.hasNext() ) {
			if( !(it1.next().equals(it2.next())) ) return false;
		}
		return true;
		
	}//equals
}//AbstractStack
