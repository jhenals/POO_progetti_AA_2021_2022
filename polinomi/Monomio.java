package polinomi;

public class Monomio implements Comparable<Monomio> {
	private final int  COEFF, GRADO; 
	
	public Monomio( final int c, final int g ) {
		if( g<0 )throw new IllegalArgumentException();
		this.COEFF=c;
		this.GRADO=g;
	}
	
	public Monomio( Monomio m ) {
		this.COEFF= m.COEFF; 
		this.GRADO= m.GRADO;
	}//Costruttore per coppia
	
	public int getCoeff() { return COEFF; }
	public int getGrado() { return GRADO; }
	
	public boolean equals( Object x ) {
		if( !(x instanceof Monomio) ) return false;
		if( x==this) return true;
		Monomio m= (Monomio)x;
		return GRADO==m.GRADO; //similitudine in Matematica
	}
	
	public int hashCode() {
		return GRADO; //già un intero, se due oggetti sono uguali hanno stesso hashCode
	}
	
	public String toString() {
		StringBuilder sb=new StringBuilder(20);
		if( COEFF==0 ) sb.append(0);
		else {
			if( COEFF<0 ) sb.append( '-' );
			if( Math.abs(COEFF)!=1 ||GRADO==0 ) sb.append(Math.abs(COEFF) );
			if( GRADO>0 ) sb.append('x' );
			if( GRADO>1 ) {
					sb.append('^');
					sb.append(GRADO);
				}	
			}
		return sb.toString();
		}//toString
	
	public Monomio add( Monomio m ) {
		if( !this.equals(m) ) throw new RuntimeException("Monomio non simili");
		return new Monomio(COEFF+m.COEFF, GRADO );
	}//add
	
	public Monomio mul( int s ) {
		return new Monomio( this.COEFF*s, GRADO);
	}//mul
	
	public Monomio mul( Monomio m ) {
		return new Monomio( this.COEFF*m.COEFF, GRADO+m.GRADO );
	}//mul
	
	public int compareTo( Monomio m ) {
		if( GRADO>m.GRADO ) return -1; //m è più piccolo di this
		if( GRADO<m.GRADO ) return 1;
		return 0;
	}//compareTo
	
	public static void main( String[] args ) {
		Monomio m1= new Monomio( 0,7); //monomio nullo
		System.out.println("m1= "+m1);
		Monomio m2= new Monomio( 1,0); //termine noto
		System.out.println("m2= "+m2);
		Monomio m3= new Monomio( 1,1);
		System.out.println("m3= "+m3);
		//Monomio m4= new Monomio( 2,1);
		//System.out.println("m4= "+m4);
		m3= m3.mul(-1);
		System.out.println("m3= "+m3);
		Monomio m4= new Monomio(-3,5);
		System.out.println("m4= "+m4);
		m4= m4.add(new Monomio(3,5) );
		System.out.println("m4= "+m4);
		
	}//main
	
}//Monomio
