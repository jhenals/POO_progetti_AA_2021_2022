package polinomi;

import java.util.Iterator;

public interface Polinomio extends Iterable<Monomio>{
	default int size() {
		int c=0;
		for( Iterator<Monomio> i=iterator(); i.hasNext(); i.next(), c++ );
		return c;
	}//size
	
	void add( Monomio m );
	
	Polinomio factory();  //crea oggetto nell'interfaccia
	
	default Polinomio add( Polinomio p ) { //devono differenziare per il numero e tipo di parametri
		Polinomio somma= factory();
		for( Monomio m: this) somma.add(m);
		for( Monomio m: p ) somma.add(m);
		//unione di monomi tanto add mette insieme i monomi simili
		return somma;
	}//add 
	
	default Polinomio mul( Monomio m ) {
		Polinomio prod= factory();
		for( Monomio m1: this ) prod.add(m.mul(m1));
		return prod;
	}//mul
	
	default Polinomio mul( Polinomio p ) {
		Polinomio prod= factory();
		for( Monomio m: this) 
			prod=prod.add( p.mul(m) );
		return prod;
	}//mul
	
	default Polinomio derivata() {
		Polinomio d= factory();
		for( Monomio m: this ) 
			if( m.getGrado()>0 ) 
				d.add( new Monomio(m.getCoeff()*m.getGrado(), m.getGrado()-1) );
		return d;
	}//derivata

	default double valore( double x ) {
		double v=0.0D;
		for( Monomio m: this )
			v=v+m.getCoeff()*Math.pow(x, m.getGrado() );
		return v;
	}//valore
}//Polinomio
