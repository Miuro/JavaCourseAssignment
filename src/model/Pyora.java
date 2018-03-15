package model;

import java.io.*;

/*
 * Pyorä-olio
 * Sisältää konstruktorit uudelle pyörälle.
 * Sisältää metodit pyörän muokkaukselle.
 * TODO: Enum pyörän malleille. esim maastopyörä, mummopyörä, muu jne.
 * TODO: Miten saadaan enumit tulostamaan sisältö kivasti?
 */
public class Pyora {

	private static String[] kunnot = {"Rikki", "Tyydyttävä", "Hyvä", "Erinomainen"};
	private static int seuraavaID = 1;
	
	private int			pyoranID;
	private String 		nimi = "";
	private String		malli = "";
	private int			kunto = 3; // 0-3. 0 = rikki, 1 = tyydyttävä, 2 = hyvä, 3 = erinomainen
	private String 		lisatietoja = "";
	private boolean 	onkoVarattu = false; // FALSE = vapaana, TRUE = vuokrattuna
	private double 		vuokraPerPaiva = 0;
	

	
	/**
	 * Peruskonstruktori pyörälle.
	 */
	public Pyora() {}
	
	
    /**
     * Antaa jäsenelle seuraavan rekisterinumeron.
     * @return jäsenen uusi tunnusNro
     * @example
     * <pre name="test">
     *   Pyora jopo1 = new Pyora();
     *   jopo1.getPyoranID() === 0;
     *   jopo1.rekisteroi();
     *   Pyora jopo2 = new Pyora();
     *   jopo2.rekisteroi();
     *   int n1 = jopo1.getPyoranID();
     *   int n2 = jopo2.getPyoranID();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        pyoranID = seuraavaID;
        seuraavaID++;
        return pyoranID;
    }
    
    public int getPyoranID() {
    	return pyoranID;
    }
	
	/**
	 * Apumetodi testiarvojen tuottamiselle
	 */
	public void vastaaJopo() {
		nimi = "Punainen Jopo";
		malli = "Helkama Jopo";
		kunto = 1;
		onkoVarattu = true;
		vuokraPerPaiva = 12;
		lisatietoja = "Penkki pitää vaihtaa";
	}
		
	
	/**
	 * Tulostetaan pyörän tiedot.
	 * @param out
	 */
	public void tulosta(PrintStream out) {
        out.println(String.format("%03d", pyoranID, 3) + "  " + nimi +  "  " + malli);
        out.println(String.format(kunnot[kunto]));
        out.println("Vuokran määrä: " + String.format("%4.2f", vuokraPerPaiva));
        out.println("Onko vuokrattuna: " + onkoVarattu);
        out.println("Lisätiedot: " + lisatietoja);
	}

	
	   /**
     * Tulostetaan pyörän tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }

	/**
	 * Testataan pyöräluokan toimivuutta.
	 * @param args
	 */
	public static void main(String[] args) {

	}
	
}


