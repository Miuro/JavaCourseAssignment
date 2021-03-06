package model;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Vuokrausolio. Sis�lt�� metodit vuokrauksen muokkaukselle ja tiedon hakemiselle.
 * @author Jouko Sirkka, Miro Korhonen
 * @version 1.1, 23.5.2018
 */
public class Vuokraus implements Cloneable {
	
	private static int 	seuraavaID 		= 1;
	private int 		vuokrausId;
	private int			pyoraId;	 
	private int			asiakasId;
	private String 		vuokrausAika    = "-";
	private String		palautusAika 	= "-";
	private double 		hinta 			= 0.;
	private String 		lisatiedot 		= "-";
	
	
	/**
	 * K�ytet��n samaa kalenteria kaikille oliolle.
	 */
	public static Calendar pvm; // = Calendar.getInstance();
	private Calendar palautus; // = Calendar.getInstance();
	private static final DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	
	
	/**
	 * Palauttaa py�r�n string muodossa ominaisuudet erotettuna "|"-merkill�
	 * @return py�r� stringmuodossa jossa ominaisuudet erotettuna "|"-merkill�
	 */
	@Override
	public String toString() {
		return vuokrausId + "|" +
				pyoraId + "|" +
				asiakasId + "|" +
				vuokrausAika + "|" +
				palautusAika + "|" +
				hinta + "|"	+
				lisatiedot;
	}
	
	
	/**
	 * Oletuskonstruktori
	 */
	public Vuokraus() {
		
	}
	
	
	/**
	 * Konstruktori vuokrauksella tarvittavilla tiedoilla
	 * @param vuokraPerPaiva Vuokran m��r� euroissa
	 * @param pyoraID Py�r�n ID
	 * @param asiakasID Asiakkaan ID
	 */
	public Vuokraus(double vuokraPerPaiva, int pyoraID, int asiakasID) {
		this.pyoraId = pyoraID;
		this.asiakasId = asiakasID;
		this.hinta = vuokraPerPaiva;
	}
	
	/**
	 * Antaa py�r�n tunnusluvun
	 * @return py�r�n tunnusluku
	 */
	public int getPyoraId() {
		return this.pyoraId;
	}

	/**
	 * Asettaa vuokratun py�r�n tunnusluvun
	 * @param id haluttu tunnusluku
	 */
	public void setPyoraId(int id) {
		pyoraId = id;
	}
	
	
	/**
	 * Antaa vuokraajan tunnusluvun
	 * @return vuokraajan tunnusluku
	 */
	public int getAsiakasId() {
		return this.asiakasId;
	}
	
	/**
	 * Asettaa vuokrauksen tunnusluvun
	 * @param id haluttu tunnusluku
	 */
	public void setVuokrausId(int id) {
		vuokrausId = id;
		if (vuokrausId >= seuraavaID) seuraavaID = vuokrausId+ 1;
	}
	
	/**
	 * Asettaa vuokraajan tunnusluvun
	 * @param id haluttu tunnusluku
	 */
	public void setAsiakasId(int id) {
		asiakasId = id;
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
	 * @example
	 * <pre name=""test>
	 * Vuokraus v1 = new Vuokraus();
	 * v1.testiVuokraus(1, 2);
	 * v1.anna(1) === 1;
	 * v1.anna(3) === 2;
	 * </pre>
	 */
	public String anna(int k) {
		switch (k) {
		case 0:
			return "" + vuokrausId;
		case 1:
			return "" + pyoraId;
		case 2:
			return "" + asiakasId;
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
	 * Pilkkoo rivin ja kutsuu aseta -funktiota
	 * @param rivi pilkottava rivi
	 * <pre name="test">
	 * #THROWS SailoException
	 * Vuokraus v1 = new Vuokraus();
	 * Vuokraus v2 = new Vuokraus();
	 * v1.parse("1|1|1|13.04.2018 23:48|14.04.2018 04:48|60.0|Maksettu luottokortilla");
	 * v1.toString() === "1|1|1|13.04.2018 23:48|14.04.2018 04:48|60.0|Maksettu luottokortilla";
	 * v2.toString() === "0|0|0|-|-|0.0|-";
	 * </pre>
	 * @throws SailoException jos parsiminen ep�onnistuu.
	 */
	public void parse(String rivi) throws SailoException {
		String[] osat = rivi.split("\\|");
		for (int k = 0; k < getKenttia(); k++) {
			aseta(k, osat[k]);
		}
	}
	
	
	/**
	 * Asettaa vuokraukseen merkkijonon sis�lt�v�n tiedon jos se on sopiva.
	 * @param k kent�n indeksi
	 * @param jono asetettava merkkijono
	 * @return null jos kaikki ok, muu merkkijono jos jotain ongelmaa
	 * @throws SailoException  jos sy�tet��n v��r�� tietoa.
	 */
	public String aseta(int k, String jono) throws SailoException {
		String tjono = jono.trim();
		switch (k) {
		case 0:		
			setVuokrausId(Integer.parseInt(tjono));
			return null;
		case 1:
			setPyoraId(Integer.parseInt(tjono));
			return null;
		case 2:
			asiakasId = Integer.parseInt(tjono);
			return null;
		case 3:
			vuokrausAika = tjono;
			return null;
		case 4:
			palautusAika = tjono;
			return null;
		case 5:
			try {
				hinta = Double.parseDouble(tjono);
			} catch (NumberFormatException e) {
				throw new SailoException("Hinta oli v��rin, anna lukuina esim. 6.8");
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
		out.println(String.format("%03d", vuokrausId, 3) + " "
					+ String.format("Py�r�: %03d", pyoraId, 3) + " "
					+ String.format("Vuokraaja: %03d", asiakasId, 3));
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
		vuokrausId = seuraavaID;
		seuraavaID++;
		return vuokrausId;
	}
	
	/**
	 * Luo testattavan ja esim. dataa sis�lt�v�n vuokrauksen.
	 * @param pyoraId1 Mik� Id annetaan py�r�lle.
	 * @param kestoTunneissa Vuokrauksen kesto tunneissa.
	 */
	public void testiVuokraus(int pyoraId1, int kestoTunneissa) {
		this.pyoraId = pyoraId1;
		pvm = Calendar.getInstance();
		palautus = Calendar.getInstance();
		palautus.add(Calendar.HOUR, kestoTunneissa);
		vuokrausAika = Integer.toString(kestoTunneissa);
		palautusAika = sdf.format(palautus.getTime());
		hinta = 10;
		hinta = hinta * kestoTunneissa;
		lisatiedot = "Maksettu luottokortilla";
	}
	
	/**
	 * Asetin palautusajalle. Lis�� palautusajaksi t�m�n hetkisen kellonajan + keston tunneissa
	 * @param kestoTunneissa Kesto tunneissa
	 */
	public void setPalautusAika(int kestoTunneissa) {
		palautus = Calendar.getInstance();
		palautus.add(Calendar.HOUR, kestoTunneissa);
		palautusAika = sdf.format(palautus.getTime());
	}
	
	/**
	 * Palauttaa vuokrauksen tunnusnumeron
	 * @return vuokrauksen tunnusnumero
	 */
	public int getVuokrausId() {
		return vuokrausId;
	}
	
	/**
	 * Antaa palautusajan
	 * @return Palauttaa viitteen vuokrauksen loppumisaikaan, eli milloin py�r�n tulisi olla palautettuna.
	 */
	public String getPalautusAika() {
		return palautusAika;
	}
	
	/**
	 * Antaa vuokrausajanhetken
	 * @return viite vuokrauksen aloitusajankohtaan, milloin py�r� vuokrattiin
	 */
	public String getVuokrausAika() {
		return vuokrausAika;
	}
	
	/**
	 * Asettaa vuokrausajan
	 * @param vuokrAika vuokrausajankohta
	 */
	public void setVuokrausAika(String vuokrAika) {
		vuokrausAika = vuokrAika;
	}
	
	/**
	 * Asettaa palautusajankohdan
	 * @param palAika palautusaika
	 */
	public void setPalautusAika(String palAika) {
		palautusAika = palAika;
	}
	
	/**
	 * Antaa vuokrauksen viitteen
	 * @return vuokraus
	 */
	public Vuokraus get() {
		return this;
	}

	/**
	 * Testiohjelma vuokraukselle
	 * @param args ei k�yt�s�
	 */
	public static void main(String[] args) {
		Vuokraus testi = new Vuokraus(1,1,1);
		testi.rekisteroi();
		testi.testiVuokraus(1,5);
		testi.tulosta(System.out);
		Vuokraus testi2 = new Vuokraus(2,2,2);
		testi2.rekisteroi();
		testi2.testiVuokraus(2,5);
		System.out.println();
		testi2.tulosta(System.out);
		System.out.println(testi.toString());
		System.out.println(testi2.toString());
	}

}
