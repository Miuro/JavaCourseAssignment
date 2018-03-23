package model;

import java.io.*;

/*
 * Pyor�-olio
 * Sis�lt�� konstruktorit uudelle py�r�lle.
 * Sis�lt�� metodit py�r�n muokkaukselle.
 * TODO: Enum py�r�n malleille. esim maastopy�r�, mummopy�r�, muu jne.
 * TODO: Miten saadaan enumit tulostamaan sis�lt� kivasti?
 */
public class Pyora {

	private static String[] kunnot = {"Rikki", "Tyydytt�v�", "Hyv�", "Erinomainen"};
	private static int seuraavaID = 1;
	
	private int			pyoranID;
	private String 		nimi = "";
	private String		malli = "";
	private int			kunto = 3; // 0-3. 0 = rikki, 1 = tyydytt�v�, 2 = hyv�, 3 = erinomainen
	private String 		lisatietoja = "";
	private boolean 	onkoVarattu = false; // FALSE = vapaana, TRUE = vuokrattuna
	private double 		vuokraPerPaiva = 0; //TODO: muuta vuokraPerTunti
	

	
	/**
	 * Peruskonstruktori py�r�lle.
	 */
	public Pyora() {}
	
	
    /**
     * Antaa j�senelle seuraavan rekisterinumeron.
     * @return j�senen uusi tunnusNro
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
    
    /**
     * @return Palauttaa py�r�n ID:n
     */
    public int getPyoranID() {
    	return pyoranID;
    }
	
    /**
     * @return Palauttaa py�r�n nimen.
     */
    public String getNimi() {
    	return nimi;
    }
    
    
	/**
	 * @return Py�r�n vuokra per p�iv�
	 */
	public double getVuokraPerPaiva() {
		return vuokraPerPaiva;
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
		lisatietoja = "Penkki pit�� vaihtaa";
	}
		
	
	/**
	 * Tulostetaan py�r�n tiedot.
	 * @param out
	 */
	public void tulosta(PrintStream out) {
        out.println(String.format("%03d", pyoranID, 3) + "  " + nimi +  "  " + malli);
        out.println(String.format(kunnot[kunto]));
        out.println("Vuokran m��r�: " + String.format("%4.2f", vuokraPerPaiva));
        out.println("Onko vuokrattuna: " + onkoVarattu);
        out.println("Lis�tiedot: " + lisatietoja);
	}

	


	/**
     * Tulostetaan py�r�n tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }

	/**
	 * Testataan py�r�luokan toimivuutta.
	 * @param args
	 */
	public static void main(String[] args) {

	}
	
}

