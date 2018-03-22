package model;

import java.io.*;

public class Asiakas {
	
	private int 		asiakasId 	= 0;
	private String 		nimi 		= "";
	private String 		sotu 		= "";
	private String 		osoite 		= "";
	private String 		puhnum 		= "";
	private String 		lisatiedot 	= "";
	
	private static int 	seuraavaId 	= 1;
	
	/**
	 * Oletusasiakkaan konstruktori
	 */
	public Asiakas () {

	}
	
	
	public void vastaaHessuHopo() {
		nimi = "Hessu Hopo";
		sotu = "123123-123W";
		osoite = "Aleksanterinkatu 14, Helsinki";
		puhnum = "050 1231231";
		lisatiedot = "Hieno mies";
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
		out.println(lisatiedot);
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
	 * @example
     * <pre name="test">
     *   Asiakas p1 = new Asiakas();
     *   p1.getAsiakasId() === 0;
     *   p1.rekisteroi();
     *   Asiakas p2 = new Asiakas();
     *   p2.rekisteroi();
     *   int n1 = p1.getAsiakasId();
     *   int n2 = p2.getAsiakasId();
     *   n1 === n2-1;
     * </pre>
     */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
