package polinomi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class PolinomioAL extends PolinomioAstratto  {
	private ArrayList<Monomio> lista= new ArrayList<>();
	
	public Polinomio factory() {
		return new PolinomioAL();
	}//factory
	
	public int size() {
		return lista.size();
	}//size
	
	public Iterator<Monomio> iterator() {
		return lista.iterator();
	}//iterator

	
	@Override
	public void add(Monomio m) {
		if (m.getCoeff() != 0) {
			int i = Collections.binarySearch(lista, m);
			if (i >= 0) {
				Monomio somma = lista.get(i).add(m);
				if (somma.getCoeff() != 0) lista.set(i, somma);
				else lista.remove(i);
			} else {
				int pos = 0;
				while (pos < lista.size() && lista.get(pos).compareTo(m) < 0) pos++;
				lista.add(pos, m);
			}
		}
	}
	

}//PolinomioAL
