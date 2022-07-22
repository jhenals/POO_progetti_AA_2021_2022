 package evalExpr;
 
import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.event.*;


public class EvalExprGUI {

	public static void main(String[] args) {
		EvalExprFrame f= new EvalExprFrame();
		f.setVisible(true);
	}//main
	
}//EvalExprGUI


class EvalExprFrame extends JFrame implements ActionListener {
	private JButton vai, cancella;
	private JTextField expr, res; 
	private JLabel lblExpr, lblRes;
	private JMenuItem exit, about;
	
	public EvalExprFrame() {
		setTitle("Risolutore Espressione Aritmetica");
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocation(400,400);
	    setSize(400,200);
	    
	    JMenuBar mb= new JMenuBar();
	    setJMenuBar(mb);
	    JMenu file= new JMenu("File");
	    JMenu help= new JMenu( "Help");
	    file.addActionListener(this);
	    help.addActionListener(this);
	    mb.add(file);
	    mb.add(help);
	    
	    exit= new JMenuItem("Exit");
	    about= new JMenuItem( "About");
	    
	    exit.addActionListener(this);
	    about.addActionListener(this);
	    
	    file.add(exit);
	    help.add(about);    
	    
		JPanel p= new JPanel();
		p.setBackground(Color.MAGENTA);
		p.setBorder(new LineBorder(Color.BLACK, 1));
		lblExpr= new JLabel("Fornisci l'espressione: ");
		lblExpr.setFont(new Font("Verdana", Font.PLAIN, 15));
		expr= new JTextField(23);
		expr.setEditable(true);
		expr.setFont(new Font("Verdana", Font.PLAIN, 15));
		
		vai= new JButton( "Vai");
		cancella= new JButton( "Cancella" );
		
		expr.addActionListener(this);
		vai.addActionListener(this);
		cancella.addActionListener(this);
		
		p.add(lblExpr);
		p.add(expr);
		p.add(vai);
		p.add(cancella);
		add(p, BorderLayout.CENTER);
		
		JPanel q= new JPanel();
		q.setBackground(Color.CYAN);
		q.setBorder(new LineBorder(Color.BLACK, 2));
		lblRes= new JLabel("Risultato: ");
		res= new JTextField(15);
		res.setEditable(false);
		res.addActionListener(this);
		
		q.add(lblRes);
		q.add(res);
		add(q, BorderLayout.SOUTH);
		
	}//costruttore                 
	
	public void actionPerformed(ActionEvent evt) {
		if( evt.getSource()== vai) {
			String s= expr.getText();
			String pattern= "(\\d+|[\\+\\-\\/*\\^\\(\\)])+";
			JOptionPane e=  new JOptionPane
					("Espressione Malformata.", JOptionPane.ERROR_MESSAGE) ;
			
			if( !s.matches(pattern)) {
				JOptionPane.showMessageDialog(null, "Espressione malformata!",
						"ERROR", JOptionPane.ERROR_MESSAGE);
				expr.setText("");
				expr.setEditable(true);
				
			}
			else {
				int ris= EvalExpr.evaluate(s);
				res.setText(Integer.toString(ris));
				return; 
			}
			
		}//vai
		
		else if( evt.getSource()== cancella ) {
			expr.setText("");
			expr.setEditable(true);
			res.setText("");
			res.setEditable(false);
		}//cancella
		
		 else if( evt.getSource()==exit ) {
			 System.exit(0);
		 }//exit
		
		 else if( evt.getSource()== about ) {
			 JFrame abF= new AboutFrame();
			 abF.setVisible(true);
		 }//about
		
		
	}//actionPerformed
	
	 class AboutFrame extends JFrame{
		 public AboutFrame() {
			 setTitle("Help");
			 setSize(500,300);
			 setLocation(710,300);
			 setResizable(false);
			 JPanel aboutPanel= new JPanel();
			 aboutPanel.setOpaque(true);
			 add(aboutPanel);
			 JTextArea txtArea= new JTextArea();
			 txtArea.setBackground(Color.LIGHT_GRAY);
			 txtArea.setFont( new Font("Consolas", Font.PLAIN, 10));
			 txtArea.setPreferredSize((new Dimension(250,200)));
			 txtArea.setEditable(false);
			 txtArea.append("\n"
					 +" This simple program \n"
					 + " evaluates an arithmetic expression \n"
					 + " using the PEMDAS rule: \n"
					 + "\n"
					 +" P- Parenthesis \n"
					 +" E- Exponent \n"
					 +" MD- Multiplication,Modulo,Division \n"
					 +" AS- Addition, Subtraction \n"
					 + "\n" 
			 		+ "-Jhenalyn Subol,213485");
			 aboutPanel.add(txtArea);
			 pack();
		 }//costruttore
	 }//AboutFrame
	 
}//EvalExprFrame