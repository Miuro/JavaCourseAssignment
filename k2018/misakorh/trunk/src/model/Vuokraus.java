package model;

import java.io.*;

public class Vuokraus {
	
	private int 		vuokrausId,	 // = 0
						pyoraId,	 // = 0
						vuokraajaId 	= 0;
	private String 		vuokrausPvm, // = ""
						palautusPvm 	= "";
	private double 		hinta 			= 0.;
	private String 		lisaTiedot 		= "";
	
	private static int 	seuraavaId 		= 1;
	
	
	/**
	 * Oletuskonstruktori
	 */
	public Vuokraus() {
		
	}
	
	
	/**
	 * Tulostaa vuokrauksen tiedot
	 * @param out tietovirta johon tulostetaan
	 */
	public void tulosta(PrintStream out) {
		out.println(String.format("%03d", vuokrausId, 3) + " "
					+ String.format("%03d", pyoraId, 3) + " "
					+ String.format("%03d", vuokraajaId, 3));
		out.println(vuokrausPvm + " - " + palautusPvm);
		out.println(hinta);
		out.println(lisaTiedot);
	}
	
	
	/**
	 * Tulostaa vuokrauksen tiedot
	 * @param os tietovirta johon tulostetaan
	 */
	public void tulosta(OutputStream os) {
		tulosta(new PrintStream(os));
	}
	
	
	/**
	 * Palauttaa vuokrauksen tunnusnumeron
	 * @return vuokrauksen tunnusnumero
	 */
	public int rekisteroi() {
		vuokrausId = seuraavaId;
		seuraavaId++;
		return vuokrausId;
	}
	
	
	/**
	 * Palauttaa vuokrauksen tunnusnumeron
	 * @return vuokrauksen tunnusnumero
	 */
	public int getVuokrausId() {
		return vuokrausId;
	}
	

	/**
	 * Testiohjelma vuokraukselle
	 * @param args ei käytösä
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
