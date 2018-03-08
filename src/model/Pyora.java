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
	private enum PyoranKunto {
		RIKKI, TYYDYTTAVA, HYVA, ERINOMAINEN
	}
	
	private int pyoranID;
	private String nimi = "";
	private String malli = "";
	private PyoranKunto kunto = PyoranKunto.ERINOMAINEN;
	private double vuokraPerPaiva = 0;
	private boolean onkoVarattu; // FALSE = vapaana, TRUE = vuokrattuna
	private String lisatietoja = "";
	
	private static int seuraavaPyoranID = 1;

	
	/**
	 * Konstruktori py�r�lle.
	 * Ei vissiin pid� olla valmiissa ohjelmassa, mutta lis�t��n testimieless�
	 * @param nimi Nimi
	 * @param malli Malli
	 * @param kunto Kunto
	 * @param vuokraPerPaiva Vuokraperp�iv� euroissa.
	 */
	public Pyora(String nimi, String malli, PyoranKunto kunto, double vuokraPerPaiva) {
		this.nimi = nimi;
		this.malli = malli;
		this.kunto = kunto;
		this.vuokraPerPaiva = vuokraPerPaiva;
	}
	
	

	/**
	 * @return Pyor�n malli
	 */
	private String getMalli() {
		return malli;
	}



	/**
	 * @return Pyor�n kunto
	 */
	private PyoranKunto getKunto() {
		return kunto;
	}



	/**
	 * @param kunto Asetettava kunto
	 */
	private void setKunto(PyoranKunto kunto) {
		this.kunto = kunto;
	}



	/**
	 * @return the vuokraPerPaiva
	 */
	private double getVuokraPerPaiva() {
		return vuokraPerPaiva;
	}



	/**
	 * @param vuokraPerPaiva Asetettava vuokra
	 */
	private void setVuokraPerPaiva(double vuokraPerPaiva) {
		this.vuokraPerPaiva = vuokraPerPaiva;
	}



	/**
	 * @return Vuokrauksen tila. True = Varattu vai vapaana
	 */
	private boolean isVuokrauksenTila() {
		return onkoVarattu;
	}



	/**
	 * @param vuokrauksenTila Asetettava vuokrauksen tila
	 */
	private void setVuokrauksenTila(boolean vuokrauksenTila) {
		this.onkoVarattu = vuokrauksenTila;
	}



	/**
	 * @return Lis�tiedot
	 */
	private String getLisatietoja() {
		return lisatietoja;
	}



	/**
	 * @param lisatietoja Asetettava lis�tieto
	 */
	private void setLisatietoja(String lisatietoja) {
		this.lisatietoja = lisatietoja;
	}

	
	/**
	 * Tulostetaan py�r�n tiedot.
	 * TODO: enumerable pyoranKunto sillein ett� se tulostaa kivasti
	 * @param out
	 */
	public void tulosta(PrintStream out) {
        out.println(String.format("%03d", pyoranID, 3) + "  " + nimi +  "  " + malli);
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
		Pyora testi = new Pyora("Punainen Jopo", "Maastopy�r�", PyoranKunto.HYVA, 100);
		testi.setLisatietoja("Tarvitsee uudet ketjut");
		testi.setVuokrauksenTila(false);
		testi.tulosta(System.out);
	}
	
}


