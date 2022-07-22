package polinomi;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;



public class PolinomioGUI {
	
	public static void main(String[] args) {
		MyFrame f= new MyFrame();
		f.setVisible(true);
	}//main

}//PolinomioApp


class MyFrame extends JFrame{
	
	private File fileDiSalvataggio=null;
	private Polinomio polinomio= null;
	
	private LinkedList<Polinomio> listaPolinomi= new LinkedList<>();
	private LinkedList<JCheckBox> listaCheck= new LinkedList<>();
	private LinkedList<Polinomio> listSelectedPoli= new LinkedList<>();  // polinomi selezionati nel JCheckBox
	
	private JMenu menuFile, menuComandi, menuHelp;
	private JMenuItem  tipo , tipoAL, tipoLL, tipoMap, tipoSet, apri, salva, exit,
					somma, mul, derivata, valore, elimina,svuota, 
					about;
	private JButton inserisci, cancella;
	private JPanel cBoxPanel, 
					tFieldPanel;
	private JScrollPane sp; 
	private JTextField poliTextField;
	private String inputPoli;
	private int countSelezionati;
	private String tipoPoli= "LinkedList"; //default
	
	//regex
	private String NUM = "[1-9][\\d]*"; //primo numero deve essere solo da 1-9, i successivi va bene che ci siano 0
	private String VAR = "([xX]|[xX][//^]"+NUM+")";
	private String OPER = "[\\+\\-]"; 
	private String MONOMIO = "("+NUM+VAR+"?|"+VAR+")";
	private String POLINOMIO ="[\\+\\-]?"+ MONOMIO+"("+OPER+MONOMIO+")*";
	

	AscoltatoreEventi listener = new AscoltatoreEventi();
	
	
	public MyFrame() {
		
	setTitle( "RISOLUTORE POLINOMI");
	setSize(500,400);
	setLocation( 100,100 );
	setResizable(false);
	setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	
	addWindowListener( new WindowAdapter() {
        public void windowClosing(WindowEvent e){
        	if( consentExit() ) System.exit(0);
        }
	});
	
	//menuBar
	JMenuBar menuBar= new JMenuBar();
	setJMenuBar(menuBar);
	
	//in menuBar- File
	menuFile= new JMenu("File"); 
	menuBar.add (menuFile);
	tipo= new JMenu("Tipo");
	menuFile.add(tipo);
	
	
	//in File-Nuova
	tipoAL= new JMenuItem("ArrayList");
	tipoLL= new JMenuItem("LinkedList");
	tipoMap= new JMenuItem("Map");
	tipoSet= new JMenuItem("TreeSet");
	tipo.add(tipoAL);
	tipo.add(tipoLL);
	tipo.add(tipoMap);
	tipo.add(tipoSet);
	tipoAL.addActionListener(listener);
	tipoLL.addActionListener(listener);
	tipoMap.addActionListener(listener);
	tipoSet.addActionListener(listener); 
	
	menuFile.addSeparator();
	apri= new JMenuItem("Open"); 
	menuFile.add(apri);
	salva= new JMenuItem("Salva");
	menuFile.add(salva);
	exit= new JMenuItem("Exit");
	menuFile.add(exit);
	tipo.addActionListener(listener);
	apri.addActionListener(listener);
	salva.addActionListener(listener);
	exit.addActionListener(listener);
	
	
	
	
	
	//in menuBar- Comandi
	menuComandi= new JMenu( "Comandi" );
	menuBar.add(menuComandi);
	somma= new JMenuItem("Somma"); 
	mul= new JMenuItem("Prodotto");
	derivata= new JMenuItem("Derivata");
	valore= new JMenuItem("Valore");
	elimina= new JMenuItem( "Elimina");
	svuota= new JMenuItem("Svuota");
	menuComandi.add(somma);
	menuComandi.add(mul);
	menuComandi.add(derivata);
	menuComandi.add(valore);
	menuComandi.addSeparator();
	menuComandi.add(elimina);
	menuComandi.add(svuota);
	somma.addActionListener(listener);
	mul.addActionListener(listener);
	derivata.addActionListener(listener);
	valore.addActionListener(listener);
	elimina.addActionListener(listener);
	svuota.addActionListener(listener);
	
			
	
	
	 //in menuBar- Help
	 menuHelp= new JMenu("Help"); 
	 menuBar.add(menuHelp);
	 about= new JMenuItem("About");
	 menuHelp.add(about);	 
	 about.addActionListener(listener);
	
	
	
	//panello di Jcheckbox
	cBoxPanel= new JPanel();
	JLabel listLabel= new JLabel("POLINOMI:");
	listLabel.setFont(new Font("Verdana", Font.BOLD, 15));
	cBoxPanel.add(listLabel);
	cBoxPanel.setLayout( new BoxLayout(cBoxPanel, BoxLayout.Y_AXIS));
	cBoxPanel.setBackground(Color.YELLOW);
	cBoxPanel.setBorder(new LineBorder(Color.BLACK,2));
	sp= new JScrollPane(cBoxPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	add(sp, BorderLayout.CENTER);
	
	
	//panello di JTextField
	tFieldPanel= new JPanel();
	tFieldPanel.setPreferredSize (new Dimension (500, 100));
	tFieldPanel.setLayout(new FlowLayout());
	tFieldPanel.setBackground(Color.MAGENTA);
	tFieldPanel.setBorder(new LineBorder(Color.BLACK,2));
	JLabel tFieldLabel= new JLabel ("Inserisci Polinomio: ");
	tFieldLabel.setFont(new Font("Verdana", Font.PLAIN,15));
	poliTextField= new JTextField(25);
	poliTextField.addActionListener(listener);
	poliTextField.setFont(new Font("Verdana", Font.PLAIN, 15));
	tFieldPanel.add(tFieldLabel);
	tFieldPanel.add(poliTextField);
	inserisci= new JButton("Inserisci");
	cancella= new JButton("Cancella");
	tFieldPanel.add(inserisci);
	tFieldPanel.add(cancella);
	add(tFieldPanel, BorderLayout.SOUTH);
	inserisci.addActionListener(listener);
	cancella.addActionListener(listener);
	
	inizializza();
	//pack();
	}//Costruttore-FrameGUI
	
	
	private void aggiorna() {
		 for( JCheckBox c: listaCheck ) 
			 c.setSelected(false);		
		 listSelectedPoli.clear();
		 polinomio=new PolinomioLL();
		 countSelezionati=0;
		 inizializza(); //so, if jcheckbox list is not empty, svuota is visible
	}//deselect
	
	private void inizializza() {
		if(!listaCheck.isEmpty())
			svuota.setEnabled(true);
		else {
			somma.setEnabled(false);
			mul.setEnabled(false);
			derivata.setEnabled(false);
			valore.setEnabled(false);
			elimina.setEnabled(false);
			svuota.setEnabled(false);
		}
	}//inizializza

	private Monomio parseMonomio(String mon ) {
		StringTokenizer st= new StringTokenizer(mon,"xX^",true);
		String token;
		int coef=1, deg=0;
		while(st.hasMoreTokens()) {
			token = st.nextToken();
			if(token.equalsIgnoreCase("x")) { 
				if(st.hasMoreTokens()) {
					st.nextToken(); //^
					deg=Integer.parseInt(st.nextToken()); //grado
				}
				else {deg=1;break;}
			}
			else {
				if(st.hasMoreTokens()) {
					st.nextToken(); //xX
					if(st.hasMoreTokens()) {
						st.nextToken(); //^
						coef=Integer.parseInt(token);deg=Integer.parseInt(st.nextToken());break;
					}
					else { coef=Integer.parseInt(token);deg=1;}
				}
				else coef=Integer.parseInt(token);
			}
		}//while
		Monomio m= new Monomio(coef,deg);
		return m;
	}//parseMonomio
	
	
	private Polinomio parsePolinomio( String str ) {
		//polinomio=new PolinomioLL();
		
		switch ( tipoPoli ) {
		case "ArrayList": polinomio= new PolinomioAL(); break;
		case "Map": polinomio= new PolinomioMap(); break;
		case "TreeSet": polinomio= new PolinomioTreeSet(); break;
		default: polinomio=new PolinomioLL();
		}
		
		Pattern pattern = Pattern.compile(POLINOMIO);
		Matcher matcher = pattern.matcher(str);
		if( !matcher.matches() )
			 JOptionPane.showMessageDialog(null,"Inserisci un polinomio valido!");
		else {
			StringTokenizer st= new StringTokenizer(str, "+-",true);
			String segno;
			Monomio monomio;
			String token;
			while(st.hasMoreTokens()) {
				token= st.nextToken();
				if(token.equals("-") || token.equals("+") ) {
					segno=token;
					token=st.nextToken();
					monomio= parseMonomio(token);
					if(segno.equals("-")) 
						polinomio.add(monomio.mul(-1));
					else polinomio.add(monomio);
				}
				else{
					monomio= parseMonomio(token);
					polinomio.add(monomio);
				}
			}
		}
		return polinomio;
	}//parsePolinomio
	
	private void creaPoli( Polinomio p) {
		polinomio= parsePolinomio(p.toString() );
		listaPolinomi.add(polinomio);
		JCheckBox poliCheck= new JCheckBox( polinomio.toString() ); //elemento da inserire in JCheckBox
		poliCheck.setFont(new Font("Verdana", Font.PLAIN, 15));
		poliCheck.addActionListener(listener);
		poliCheck.setBackground(null);
		listaCheck.add(poliCheck); //insert in jcheckbox
		cBoxPanel.add(poliCheck);
		cBoxPanel.validate();
		validate();
		inizializza();
	}//creaPolinomio
	
	private void hideBinaryOperations(boolean visible) {
		if( visible ) {
			somma.setEnabled(false);
			mul.setEnabled(false);
			derivata.setEnabled(true);
			valore.setEnabled(true);
			elimina.setEnabled(true);
		}
		else {
			somma.setEnabled(true);
			mul.setEnabled(true);
			derivata.setEnabled(false);
			valore.setEnabled(false);
			elimina.setEnabled(false);
		}
	}//hideBinaryOperations
	

		
	 private boolean consentExit(){
		   int option=JOptionPane.showConfirmDialog(
				   null,  "Are you sure you want to exit the program", "Exit?",
				   JOptionPane.YES_NO_OPTION);
		   return option==JOptionPane.YES_OPTION;
	 }//consentExit
	 
	 class AboutFrame extends JFrame{
		 public AboutFrame() {
			 setTitle("Help");
			 setSize(430,280);
			 setLocation(710,300);
			 setResizable(false);
			 JPanel aboutPanel= new JPanel();
			 aboutPanel.setOpaque(true);
			 add(aboutPanel);
			 JTextArea txtArea= new JTextArea();
			 txtArea.setBackground(Color.LIGHT_GRAY);
			 txtArea.setFont( new Font("Consolas", Font.PLAIN, 10));
			 txtArea.setPreferredSize((new Dimension(450, 200)));
			 txtArea.setEditable(false);
			 txtArea.append("\n"
					 +" This is a simple Polynomial Calculator that solves basic operations \n"
			 		+ " such as addition, multiplication, derivatives. \n"
			 		+ " It can also evaluate the polynomial for a given \n"
			 		+ " value of the variable x. \n"
			 		+ "\n"
			 		+ " User can modify which data structure to use in creating the object \n"
			 		+ " polynomial: LinkedList(default), ArrayList, Map, TreeSet. \n"
			 		+ "\n"
			 		+ "\n"
			 		+ "\n"
			 		+ " -Jhenalyn Subol,213485");
			 aboutPanel.add(txtArea);
			 pack();
		 }//costruttore
	 }//AboutFrame

	 
	 class DialogResult extends JDialog{
		 public DialogResult() {
			 setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			 setTitle("Risultato");
			 setBounds(100, 100, 359, 174);
		     getContentPane().setLayout(null);
		     JLabel label = new JLabel("Il risultato è \n"
				 		+ polinomio.toString());
		     label.setBounds(86, 37, 175, 29);
		     getContentPane().add(label);
		 }
	 }//DialogResult
	 
	 
	 private class AscoltatoreEventi implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			 
			 for( JCheckBox c: listaCheck) {
				 if( e.getSource()==c ) {
					 if( c.isSelected() ) { 
						listSelectedPoli.add(parsePolinomio(c.getText()));
						countSelezionati++;
					 }  //elemento selezionato
					 else {
						listSelectedPoli.remove(parsePolinomio(c.getText()));
						 countSelezionati--;
					 }
					 if( countSelezionati==1 )
						 hideBinaryOperations(true);
					 else if( countSelezionati>=2 )
						 hideBinaryOperations(false);
					 else
						 inizializza(); //se non ho selezionato nessun elementi oppure se selezione più di 2
				 }//e==c
			 }//for JCheckBox c 
			 
			 
			 if( e.getSource()==tipoAL ) {
				 tipoPoli= "ArrayList";
				  JOptionPane.showMessageDialog( null, "Polinomio ArrayList");
			 }//e==tipoAL
			 
			 else if( e.getSource()==tipoLL ) {
				  tipoPoli= "LinkedList";
				  JOptionPane.showMessageDialog( null, "Polinomio LinkedList");
			 }//e==tipoLL
			 
			 else if( e.getSource()==tipoMap ) {
				  tipoPoli= "Map";
				  JOptionPane.showMessageDialog( null, "Polinomio Map");
			 }//e==tipoMap
			 
			 else if( e.getSource()==tipoSet ) {
				  tipoPoli= "TreeSet";
				  JOptionPane.showMessageDialog( null, "Polinomio TreeSet");
			 }//e==tipoSet
			 
			 
			if( e.getSource()==apri ) {
				 JFileChooser jFC = new JFileChooser( FileSystemView.getFileSystemView().getHomeDirectory() );
				
				aggiorna();
				 try{
	  				   if( jFC.showOpenDialog(null)==JFileChooser.APPROVE_OPTION ){
	  					   if( !jFC.getSelectedFile().exists() ){
	  						   JOptionPane.showMessageDialog(null,"File inesistente!"); 
	  					   }
	  					   else{
	  						
	  						   fileDiSalvataggio=jFC.getSelectedFile();
	  						   
	  						 if(!listaCheck.isEmpty()) {
	  							 for( JCheckBox c: listaCheck )
	  									cBoxPanel.remove(c);
	  								cBoxPanel.revalidate();
	  								cBoxPanel.repaint();
	  								listaCheck.removeAll(listaCheck);
	  								listSelectedPoli.clear();
	  								countSelezionati=0;
	  						 }
	  						
	  						 
	  						   try{ //ripristino il file 
	  							 BufferedReader br=new BufferedReader( new FileReader(fileDiSalvataggio.getAbsolutePath()) );
	  							  String linea= null;
	  							  for(;;) {
	  								  linea=br.readLine();
		  								if(linea==null) break;
		  								creaPoli(parsePolinomio(linea));	  	  
	  							  }br.close();
	  						   }catch(IOException ioe){
	  							   JOptionPane.showMessageDialog(null,"Fallimento apertura. File malformato!");
	  							   ioe.printStackTrace();
	  						   }
	  					   }
	  				   }
	  				   else
	  					   JOptionPane.showMessageDialog(null,"Nessuna apertura!");
	  			   }catch( Exception exc ){
	  				   exc.printStackTrace();
	  			   }
				 
			 }//e==apri
			 
		
			 else if( e.getSource()==salva) {
				 JFileChooser jFC = new JFileChooser( FileSystemView.getFileSystemView().getHomeDirectory() );
				 FileNameExtensionFilter filtro = new FileNameExtensionFilter("File TXT", "txt");
				 jFC.setFileFilter(filtro);
				
				 PrintWriter pw;
				 try{
	  				   if( fileDiSalvataggio!=null ){
	  					   int ans=JOptionPane.showConfirmDialog(null,"Sovrascrivere "+fileDiSalvataggio.getAbsolutePath()+" ?");
						   if( ans==0 /*SI*/) {
							   pw=new PrintWriter( new FileWriter(fileDiSalvataggio.getAbsolutePath()) );
							   for( Polinomio p: listaPolinomi ) pw.println(p);
							   pw.close();
						   }
						   else
							   JOptionPane.showMessageDialog(null,"Nessun salvataggio!");
						   return;
					   }
	  				   if( jFC.showSaveDialog(null)==JFileChooser.APPROVE_OPTION )
	  					   fileDiSalvataggio=jFC.getSelectedFile();
	  					   
	  				   if( fileDiSalvataggio!=null ){
	  					   pw=new PrintWriter( new FileWriter(fileDiSalvataggio.getAbsolutePath()) );
						   for( Polinomio p: listaPolinomi ) pw.println(p);
						   pw.close();
	  				   }
	  				   else
	  					   JOptionPane.showMessageDialog(null,"Nessun Salvataggio!");
	  			   }catch( Exception exc ){
	  				   exc.printStackTrace();
	  			   }
			 }//e==salva
			 
			 
			 else if( e.getSource()==exit ) {
				 if( consentExit() ) System.exit(0);
			 }//e==exit
			 
			 else if( e.getSource()==about ) {
				 JFrame abF= new AboutFrame();
				 abF.setVisible(true);
			 }//e==about
			 
			 else if( e.getSource()== inserisci ) {
				 inputPoli=poliTextField.getText().toString();
				
				 if( inputPoli.matches(POLINOMIO) ) {
					JCheckBox box= new JCheckBox(inputPoli);
					listaCheck.add(box);
					polinomio= parsePolinomio(inputPoli);
					creaPoli(polinomio); //ho aggiunto automaticamente il polinomio creato in listaPolinomi
					poliTextField.setText("");
					poliTextField.setEditable(true);
				 }
				 else {
					 JOptionPane.showMessageDialog(null, "INSERIRE UN POLINOMIO VALIDO!");
					 poliTextField.setText("");
					 poliTextField.setEditable(true); 
				 }
			 }//e==ins
			 
			 
			 else if( e.getSource()== cancella ) {
				 poliTextField.setText(" ");
				 inputPoli=null;
				 poliTextField.setEditable(true);
			 }//e==cancella
			
			
			 else if( e.getSource()==somma  ) {
				
				 try { 
					switch ( tipoPoli ) {
						case "ArrayList": polinomio= new PolinomioAL(); break;
						case "Map": polinomio= new PolinomioMap(); break;
						case "TreeSet": polinomio= new PolinomioTreeSet(); break;
						default:polinomio=new PolinomioLL();
					}
					
					for( Polinomio p: listSelectedPoli)
						polinomio= polinomio.add(p);
					creaPoli(polinomio);
					DialogResult res= new DialogResult();
					res.setVisible(true);
				} finally {
					aggiorna();
				}
			 }//e==somma
		
		 
			else if( e.getSource()== mul) {
					try { 
						switch ( tipoPoli ) {
						case "ArrayList": polinomio= new PolinomioAL(); break;
						case "Map": polinomio= new PolinomioMap(); break;
						case "TreeSet": polinomio= new PolinomioTreeSet(); break;
						default: polinomio=new PolinomioLL();
					}
					
					polinomio= listSelectedPoli.get(0).mul(listSelectedPoli.get(1));
					creaPoli(polinomio);
					DialogResult res= new DialogResult();
					res.setVisible(true);
				} finally {
					aggiorna();
				}	 
			}//e==mul
						 
				 
			else if( e.getSource()== derivata ) {
				try { 
					polinomio=new PolinomioLL();
					polinomio= listSelectedPoli.get(0).derivata();
					creaPoli(polinomio);
					DialogResult res= new DialogResult();
					res.setVisible(true);
				} finally {
					aggiorna();
				}	 
			}//e==derivata
		
			else if( e.getSource()==valore ) {
				Double v= 0.0D;
				boolean flag= false;
				Polinomio p=listSelectedPoli.get(0);
				try {
					loop:while( !flag ){
						String x= JOptionPane.showInputDialog("Inserisci il valore di x: ");
						String regexDbl= "([\\-]?[0-9]+|[0-9]+[.][0-9]+)";
						if( x.matches(regexDbl) ) {
							v=p.valore(Double.parseDouble(x));
							JOptionPane.showMessageDialog(null, "Il valore del polinomio "+ p.toString()+
									"\n"
									+ "quando x="+x+" è \n"
											+v);
							flag=true;
						}
						else {
							 JOptionPane.showMessageDialog( null, "Inserisci un valore valido di x per favore..");
							 continue loop;
						}
					}
				}
				finally {
						aggiorna();
				}
				
			 }//e==valore 
			 
			
			else if( e.getSource()==elimina ) {
				int i=0;
				int opt= JOptionPane.showConfirmDialog(cBoxPanel, "Sei sicuro di voler eliminare questo polinomio?");
				if( opt== JOptionPane.YES_OPTION) {
				
					try { 
						for( JCheckBox c: listaCheck )
							if( c.isSelected() ) {
								i= listaCheck.indexOf(c);
								cBoxPanel.remove(c);
							}
						listaCheck.remove(i);
						listaPolinomi.remove(i);
						cBoxPanel.revalidate();
						cBoxPanel.repaint();
					} finally {
						aggiorna();
					}	 	
				}
				else aggiorna();	
				
			}//e==elimina
		
			else if( e.getSource()==svuota ) {
				for( JCheckBox c: listaCheck )
					cBoxPanel.remove(c);
				cBoxPanel.revalidate();
				cBoxPanel.repaint();
				listaCheck.removeAll(listaCheck);
				listSelectedPoli.clear();
				countSelezionati=0;
				aggiorna();
			}//e==svuota

		 }//actionPerformed 
				
	 }//Ascoltatore
	 
}//MyFrame

