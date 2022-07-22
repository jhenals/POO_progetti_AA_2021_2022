package evalExpr;

import java.util.Scanner;
import java.util.StringTokenizer;

public class EvalExpr {
	
	
	public static void main(String[] args) {
		
		String pattern=  "(\\d+|[\\+\\-\\/*\\^\\(\\)])+";
		Scanner sc= new Scanner(System.in);
		
		System.out.println("Fornisci l'espressione: ");
		String expr= sc.nextLine();
		if( !expr.matches(pattern) ) 
			throw new IllegalArgumentException( "Espressione malformata!" ); 
		
		int ris= evaluate(expr);
		System.out.println( ris );
		sc.close();
	}//main
	
	static int evaluate( String expr ) {
		Stack<Integer> oprnd= new StackConcatenato<>();
		Stack<String> oprtr= new StackConcatenato<>();
		
		
		StringTokenizer st= new StringTokenizer( expr, "+-*%/^()", true);
		
		
		while (st.hasMoreTokens()) {
			String tk= st.nextToken();
			//se è cifra
			if(isDigit(tk)) {
				int num= Integer.parseInt(tk);
				oprnd.push(num);
			}
			
			else if( tk.equals("("))
				oprtr.push(tk);
			
			else if( tk.equals(")")) {
				while( !(oprtr.peek().equals("(")) ){
					int output= valutaEspressione(oprnd, oprtr);
					oprnd.push(output);
				}
				if( oprtr.peek().equals("(") )
					oprtr.pop(); //tolgo parentesi (
			}
			
			else if( isOperator(tk)){
				String opc= tk;
				while( !oprtr.isEmpty() && precedence(opc)<=precedence(oprtr.peek()) ) {
					int output= valutaEspressione( oprnd, oprtr);
					oprnd.push(output);
				}
				oprtr.push(opc);
			}	
			
		}//for
		
		while( !oprtr.isEmpty() ) {
			int output= valutaEspressione(oprnd,oprtr);
			oprnd.push(output);
		}  
		return oprnd.pop();

	}//evaluate

	static boolean isDigit(String tk) {
		try {
			Integer.parseInt(tk);
			return true;
		}catch(NumberFormatException nfe){ return false;}
	}

	static int precedence(String tk) {
		switch(tk) {
		case"+": case"-": return 1;
		case"%": case"/": case"*": return 2; 
		case"^": return 3; 
		}
		return -1;
	}

	static boolean isOperator(String tk) {
		return (tk.equals("+")||tk.equals("-")||
				tk.equals("/")||tk.equals("*")||
				tk.equals("%")||tk.equals("^"));
	}

	static int valutaEspressione(Stack<Integer> oprnd, Stack<String> oprtr) {
		int o1= oprnd.pop();
		int o2= oprnd.pop();
		String op= oprtr.pop();
		switch(op) { 
		case"+": return o1 + o2; 
		case"-": return o2 - o1; 
		case"*": return o1 * o2; 
		case"%": return o2 % o1; 
		case"^": return (int) Math.pow(o2, o1);
		case"/": 
			if( o1==0) {
				System.out.println("Divisione con 0 non valido");
				return 0;
			}
			return o2/o1; 
		}
		return 0;
	}//valutaEspressione
	
	//Prova:
	// (14+8)*(8-4)/((6-2*2)*(1+2)) =14
	//"2*(5*(3+6))/5-2" =16
	//"(2^2)+15" =19

}//EvalExpr
