package model;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Vuokraus {
	
	private int 		vuokrausId,	 // = 0
						pyoraId,	 // = 0
						vuokraajaId 	= 0;
	private String 		vuokrausAika, // = ""
						palautusAika 	= "";
	private double 		hinta 			= 0.;
	private String 		lisatiedot 		= "";
	
	private static int 	seuraavaId 		= 1;
	
	public static Calendar pvm; // = Calendar.getInstance();
	private Calendar palautus; // = Calendar.getInstance();
	private static final DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	
	
	
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
		out.println(String.format("ID: %03d", vuokrausId, 3) + " "
					+ String.format("Pyörä: %03d", pyoraId, 3) + " "
					+ String.format("Vuokraaja: %03d", vuokraajaId, 3));
		out.println(String.format("Vuokrattu: %s", vuokrausAika));
		out.println(String.format("Palautus: %s", palautusAika));
		out.println("Hinta eur: " + hinta);
		out.println("Lisätiedot: " + lisatiedot);
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
	 * Luo testattavan ja esim. dataa sisältävän vuokrauksen.
	 * @param kestoTunneissa Vuokrauksen kesto tunneissa.
	 */
	public void testiVuokraus(int kestoTunneissa) {
		pyoraId = 7;
		vuokraajaId = 4;
		pvm = Calendar.getInstance();
		palautus = Calendar.getInstance();
		palautus.add(Calendar.HOUR, kestoTunneissa);
		vuokrausAika = sdf.format(pvm.getTime());
		palautusAika = sdf.format(palautus.getTime());
		hinta = 5 * kestoTunneissa; // 50 tilalle kaivettaisiin siis pyoraID:n hinta.
		lisatiedot = "Maksettu luottokortilla";
	}
	
	
	/**
	 * Palauttaa vuokrauksen tunnusnumeron
	 * @return vuokrauksen tunnusnumero
	 */
	public int getVuokrausId() {
		return vuokrausId;
	}
	
	/**
	 * @return Palauttaa viitteen vuokrauksen loppumisaikaan, eli milloin pyörän tulisi olla palautettuna.
	 */
	public Calendar getPalautusAika() {
		return palautus;
	}
	

	/**
	 * Testiohjelma vuokraukselle
	 * @param args ei käytösä
	 */
	public static void main(String[] args) {
		Vuokraus testi = new Vuokraus();
		testi.rekisteroi();
		testi.testiVuokraus(5);
		testi.tulosta(System.out);

	}

}
