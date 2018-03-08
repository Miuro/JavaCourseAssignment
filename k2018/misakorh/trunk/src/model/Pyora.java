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
	 * Konstruktori pyörälle.
	 * Ei vissiin pidä olla valmiissa ohjelmassa, mutta lisätään testimielessä
	 * @param nimi Nimi
	 * @param malli Malli
	 * @param kunto Kunto
	 * @param vuokraPerPaiva Vuokraperpäivä euroissa.
	 */
	public Pyora(String nimi, String malli, PyoranKunto kunto, double vuokraPerPaiva) {
		this.nimi = nimi;
		this.malli = malli;
		this.kunto = kunto;
		this.vuokraPerPaiva = vuokraPerPaiva;
	}
	
	

	/**
	 * @return Pyorän malli
	 */
	private String getMalli() {
		return malli;
	}



	/**
	 * @return Pyorän kunto
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
	 * @return Lisätiedot
	 */
	private String getLisatietoja() {
		return lisatietoja;
	}



	/**
	 * @param lisatietoja Asetettava lisätieto
	 */
	private void setLisatietoja(String lisatietoja) {
		this.lisatietoja = lisatietoja;
	}

	
	/**
	 * Tulostetaan pyörän tiedot.
	 * TODO: enumerable pyoranKunto sillein että se tulostaa kivasti
	 * @param out
	 */
	public void tulosta(PrintStream out) {
        out.println(String.format("%03d", pyoranID, 3) + "  " + nimi +  "  " + malli);
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
		Pyora testi = new Pyora("Punainen Jopo", "Maastopyörä", PyoranKunto.HYVA, 100);
		testi.setLisatietoja("Tarvitsee uudet ketjut");
		testi.setVuokrauksenTila(false);
		testi.tulosta(System.out);
	}
	
}


