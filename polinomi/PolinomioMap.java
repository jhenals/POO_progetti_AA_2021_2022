package polinomi;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class PolinomioMap extends PolinomioAstratto {
	private Map<Monomio, Monomio> map= new TreeMap<>(); //come chiave, mi serve per ordine

	public PolinomioMap factory() {
		return new PolinomioMap();
	} //factory
	
	public int size() {
		return map.size();
	}//size
	
	@Override
	public Iterator<Monomio> iterator() {
		return map.values().iterator(); //prendo set di valori perchè vuol dire che prendo i valori aggoirnati (invece Key è esempre quello)
	}//iterator
	
	@Override
	public void add(Monomio m) {
		if( m.getCoeff()==0 ) return; //non faccio nulla
		Monomio m1= map.get(m); //mi ritorna m corrispondente 
		if( m1==null ) map.put(m, m); // non ho monomio simile preesistente
		else {
			map.remove(m); //pessimismo: coeff=0
			m1= m1.add(m); 
			if( m1.getCoeff() !=0 ) map.put(m1, m1); //ho già rimosso prima, ora sto già facendo l'update 
		}
		
	}//PolinomioMap
	

}//PolinomioMap
