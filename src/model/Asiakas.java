package model;

import java.io.*;

public class Asiakas {
	
	private int 		asiakasId 	= 0;
	private String 		nimi 		= "";
	private String 		sotu 		= "";
	private String 		osoite 		= "";
	private String 		puhnum 		= "";
	private String 		lisaTiedot 	= "";
	
	private static int 	seuraavaId 	= 1;
	
	/**
	 * Oletusasiakkaan konstruktori
	 */
	public Asiakas () {

	}
	
	
	/**
	 * Palauttaa asiakkaan nimen
	 * @return Asiakkaan nimi
	 */
	public String getNimi() {
		return nimi;
	}
	
	
	/**
	 * Tulostaa asiakkaan tiedot
	 * @param out tietovirta johon tulostetaan
	 */
	public void tulosta(PrintStream out) {
		out.println(String.format("%03d", asiakasId, 3) + " " + nimi + " " + sotu);
		out.println(osoite);
		out.println(puhnum);
		out.println(lisaTiedot);
	}
	
	
	/**
	 * Tulostaa asiakkaan tiedot
	 * @param os tietovirta johon tulostetaan
	 */
	public void tulosta(OutputStream os) {
		tulosta(new PrintStream(os));
	}
	
	
	/**
	 * Antaa asiakkaalle seuraavan id numeron
	 * @return asiakkaan uusi idnumero
	 */
	public int rekisteroi() {
		asiakasId = seuraavaId;
		seuraavaId++;
		return asiakasId;
	}
	
	
	/**
	 * Palauttaa asiakkaan tunnusnumeron
	 * @return asiakkaan tunnusnumero
	 */
	public int getAsiakasId() {
		return asiakasId;
	}

	
	/**
	 * Testiohjelma asiakkaalle
	 * @param args ei käytösä
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
