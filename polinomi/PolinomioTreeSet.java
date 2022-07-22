package polinomi;

import java.util.Iterator;
import java.util.TreeSet;

public class PolinomioTreeSet extends PolinomioAstratto{
	private TreeSet<Monomio> lista= new TreeSet<>();

	public Polinomio factory() {
		return new PolinomioTreeSet();
	}
	
	public int size() {
		return lista.size();
	}
	
	@Override
	public void add(Monomio m) {
		if( m.getCoeff()!=0 ) {
			Monomio m1= lista.ceiling(m); //restituisce il più piccolo elemento che è più grande o uguale a m
			if( m1!=null && m1.equals(m) ) {
				lista.remove(m1);
				Monomio somma= m1.add(m);
				if( somma.getCoeff()!=0 )
					lista.add(somma);
			}
			else
				lista.add(m);
		}
	}//add

	@Override
	public Iterator<Monomio> iterator() {
		return lista.iterator();
	}

}//PolinomioTreeSet
