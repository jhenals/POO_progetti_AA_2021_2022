package polinomi;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class PolinomioLL extends PolinomioAstratto {
	//meglio lavorare con list iterator
	private LinkedList<Monomio> lista= new LinkedList<>(); //non si dimensiona, espanda e contrae come serve
	//non serve più costruttore, mi dà costruttore di default di LinkedList
	
	

	public PolinomioLL factory() {
		return new PolinomioLL(); //covarianza del tipo di ritorno, cambia in base a che tpo di classe concreta
	}//factory
	
	public int size() {
		return lista.size();
	}//size

	public Iterator<Monomio> iterator() {
		return lista.iterator();
	}//iterator

	@Override
	public void add(Monomio m) {
		if( m.getCoeff()==0 ) return;
		//monomi simili, si devono saldare
		ListIterator<Monomio> lit= lista.listIterator();
		boolean flag= false;
		while( lit.hasNext() && !flag ){//se ancora false
			Monomio mc= lit.next();
			if( mc.equals(m) ) {
				Monomio ms= mc.add(m);
				if( ms.getCoeff()==0 ) lit.remove();
				else lit.set(ms);
				flag=true;
			}
			else if( mc.compareTo(m)>0 ) {
				lit.previous();
				lit.add(m);
				flag=true;	
			}
		}
		if( !flag ) lit.add(m);
	}//add
}//PolinomioLL
