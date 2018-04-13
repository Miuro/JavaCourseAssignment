package model;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import fi.jyu.mit.ohj2.Mjonot;

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
	 * Konstruktori vuokrauksella tarvittavilla tiedoilla
	 * @param kestoTunneissa Vuokrauksen kesto
	 * @param vuokraPerPaiva Vuokran m��r� euroissa
	 * @param pyoraID Py�r�n ID
	 * @param asiakasID Asiakkaan ID
	 */
	public Vuokraus(int kestoTunneissa, double vuokraPerPaiva, int pyoraID, int asiakasID) {
		this.pyoraId = pyoraID;
		this.vuokraajaId = asiakasID;
		//TODO aika toimimaan
		this.hinta = vuokraPerPaiva;
	}
	
	public int getPyoraId() {
		return this.pyoraId;
	}
	
	public int getVuokraajaId() {
		return this.vuokraajaId;
	}
	
	public void setVuokrausId(int id) {
		vuokrausId = id;
	}
	
	public void setVuokraajaId(int id) {
		vuokraajaId = id;
	}
	
	public void setPyoraId(int id) {
		pyoraId = id;
	}
	
	
	/**
	 * Palauttaa vuokrauksen kenttien lukum��r�n
	 * @return vuokrauksen kenttien lukum��r�n
	 */
	public int getKenttia() {
		return 7;
	}
	
	/**
	 * Palauttaa ensimm�isen kent�n joka on mielek�s kysytt�v�ksi
	 * @return ensimm�inen j�rkev�sti kysytt�v� kentt�
	 */
	public int ekaKentta() {
		return 3;
	}
	
	
	
	/**
	 * Palauttaa k:n kent�n sis�ll�n merkkijonona
	 * @param k halutun kent�n numero
	 * @return kent�n sis�lt� merkkijonona
	 */
	public String anna(int k) {
		switch (k) {
		case 0:
			return "" + vuokrausId;
		case 1:
			return "" + pyoraId;
		case 2:
			return "" + vuokraajaId;
		case 3:
			return "" + vuokrausAika;
		case 4:
			return "" + palautusAika;
		case 5:
			return "" + hinta;
		case 6:
			return "" + lisatiedot;
		default:
			return "Hupsista";
		}
	}
	
	
	/**
	 * Palauttaa k:tta vuokrauksen kentt�� vastaavan kysymyksen
	 * @param k kuinka monennen kent�n kysymys palutetaan (alkaen 0:sta)
	 * @return k:netta kentt�� vastaava kysymys 
	 */
	public String getKysymys(int k) {
		switch (k) {
		case 0:
			return "Vuokrauksen ID";
		case 1:
			return "Py�r�n ID";
		case 2:
			return "Vuokraajan ID";
		case 3:
			return "Vuokraus p�iv�m��r�";
		case 4:
			return "Palautus p�iv�m��r�";
		case 5:
			return "Hinta";
		case 6:
			return "Lis�tiedot";
		default:
			return "HupsisKupsos";
		}
	}
	
	
	/**
	 * 
	 * @param rivi
	 */
	public void parse(String rivi) {
		String[] osat = rivi.split("\\|");
		for (int k = 0; k < getKenttia(); k++) {
			aseta(k, osat[k]);
		}
	}
	
	
	/**
	 * 
	 * @param k
	 * @param jono
	 * @return
	 */
	public String aseta(int k, String jono) {
		String tjono = jono.trim();
		StringBuffer sb = new StringBuffer(tjono);
		switch (k) {
		case 0:
			setVuokrausId(Mjonot.erota(sb, '|', getVuokrausId()));
			return null;
		case 1:
			setPyoraId(Mjonot.erota(sb, '|', getPyoraId()));;
			return null;
		case 2:
			setVuokraajaId(Mjonot.erota(sb, '|', getVuokraajaId()));;
			return null;
		case 3:
			setVuokrausAika(Mjonot.erota(sb, '|', getVuokrausAika()));;
			return null;
		case 4:
			setPalautusAika(Mjonot.erota(sb, '|', getPalautusAika()));
			return null;
		case 5:
			try {
				hinta = Double.parseDouble(tjono);
			} catch (NumberFormatException e) {
				return "Hinta oli v��rin, anna lukuina esim. 6.8";
			}
			return null;
		case 6:
			lisatiedot = tjono;
			return null;
		default:
			return "Tervetti";
		}
	}
	
	
	/**
	 * Tulostaa vuokrauksen tiedot
	 * @param out tietovirta johon tulostetaan
	 */
	public void tulosta(PrintStream out) {
		out.println(String.format("ID: %03d", vuokrausId, 3) + " "
					+ String.format("Py�r�: %03d", pyoraId, 3) + " "
					+ String.format("Vuokraaja: %03d", vuokraajaId, 3));
		out.println(String.format("Vuokrattu: %s", vuokrausAika));
		out.println(String.format("Palautus: %s", palautusAika));
		out.println("Hinta eur: " + hinta);
		out.println("Lis�tiedot: " + lisatiedot);
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
	 * Luo testattavan ja esim. dataa sis�lt�v�n vuokrauksen.
	 * @param kestoTunneissa Vuokrauksen kesto tunneissa.
	 */
	public void testiVuokraus(int kestoTunneissa) {
		pvm = Calendar.getInstance();
		palautus = Calendar.getInstance();
		palautus.add(Calendar.HOUR, kestoTunneissa);
		vuokrausAika = sdf.format(pvm.getTime());
		palautusAika = sdf.format(palautus.getTime());
		hinta = hinta * kestoTunneissa;
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
	 * @return Palauttaa viitteen vuokrauksen loppumisaikaan, eli milloin py�r�n tulisi olla palautettuna.
	 */
	public String getPalautusAika() {
		return palautusAika;
	}
	
	public String getVuokrausAika() {
		return vuokrausAika;
	}
	
	public void setVuokrausAika(String vuokrAika) {
		vuokrausAika = vuokrAika;
	}
	
	public void setPalautusAika(String palAika) {
		palautusAika = palAika;
	}
	

	/**
	 * Testiohjelma vuokraukselle
	 * @param args ei k�yt�s�
	 */
	public static void main(String[] args) {
		Vuokraus testi = new Vuokraus();
		testi.rekisteroi();
		testi.testiVuokraus(5);
		testi.tulosta(System.out);
		

	}

	public Vuokraus get() {
		return this;
	}

}
