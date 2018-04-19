package model;

import java.io.*;

import fi.jyu.mit.ohj2.Mjonot;

public class Asiakas{
	
	private int 		asiakasId;
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
	
	
	/**
	 * Palauttaa asiakkaan kenttien lukumäärän
	 * @return asiakkaan kenttien lukumäärän
	 */
	public int getKenttia() {
		return 6;
	}
	
	
	
	/**
	 * Palauttaa ensimmäisen kentän joka on mielekäs kysyttäväksi
	 * @return ensimmäinen järkevästi kysyttävä kenttä
	 */
	public int ekaKentta() {
		return 1;
	}
	
	
	/**
	 * Palauttaa k:n kentän sisällön merkkijonona
	 * @param k halutun kentän numero
	 * @return kentän sisältö merkkijonona
	 */
	public String anna(int k) {
		switch (k) {
		case 0:
			return "" + asiakasId;
		case 1:
			return "" + nimi;
		case 2:
			return "" + sotu;
		case 3:
			return "" + osoite;
		case 4:
			return "" + puhnum;
		case 5:
			return "" + lisatiedot;
		default:
			return "Hupsista";
		}
	}
	
	
	/**
	 * Täyttää asiakkaan testitiedoillas
	 */
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
	 * Asettaa asiakkaan tunnusluvun
	 * @param id Asiakkaan haluttu tunnusluku
	 */
	public void setAsiakasId(int id) {
		asiakasId = id;
	}
	
	
	/**
	 * Palauttaa jäsenen tiedot muodossa, jonka voi tallentaa tiedostoon.
	 * @return Jäsenen tiedot tolppaeroteltuna jonona.
	 */
	@Override
	public String toString() {
		return "" + 
				getAsiakasId() + "|" +
				nimi + "|" +
				sotu + "|" +
				osoite + "|" +
				puhnum + "|" +
				lisatiedot;
	}
	
	
	/**
	 * Jakaa merkkijonon haluttuihin osiin ja kutsuu aseta() funktiota joka asettaa luetut tiedot
	 * asiakkaan tiedoiksi jos mahdollista
	 * @param rivi jaettava merkkijono
	 * <pre name="test">
	 * Asiakas a1 = new Asiakas();
	 * Asiakas a2 = new Asiakas();
	 * a1.parse("1|Frans Bergman|110156-728K|Marjakatu 6 62420 Vuoristo|-|Herkku-ukko");
	 * a1.toString() === "1|Frans Bergman|110156-728K|Marjakatu 6 62420 Vuoristo|-|Herkku-ukko";
	 * a2.setAsiakasId(2);
	 * a2.toString() === "2|||||";
	 * </pre>
	 */
	public void parse(String rivi) {
		String[] osat = rivi.split("\\|");
		for (int k = 0; k < getKenttia(); k++) {
			aseta(k, osat[k]);
		}
	}
	
	/**
	 * Asettaa asiakkaan tietoihin merkkijonon tiedot
	 * @param k mikä case kyseessä
	 * @param jono merkkijono jota sovitetaan tiedoksi
	 * @return null mjos kaikki hyvin, "Tervetti" jos ei sovi mihinkään kentään
	 */
	public String aseta(int k, String jono) {
		String tjono = jono.trim();
		StringBuffer sb = new StringBuffer(tjono);
		switch (k) {
		case 0:
			setAsiakasId(Mjonot.erota(sb, '|', getAsiakasId()));
			return null;
		case 1:
			nimi = tjono;
			return null;
		case 2:
			sotu = tjono;
			return null;
		case 3:
			osoite = tjono;
			return null;
		case 4:
			puhnum = tjono;
			return null;
		case 5:
			lisatiedot = tjono;
			return null;
		default:
			return "Tervetti";
		}
	}
	
	
	
	/**
	 * Palauttaa k:tta asiakkaan kenttää vastaavan kysymyksen
	 * @param k kuinka monennen kentän kysymys palutetaan (alkaen 0:sta)
	 * @return k:netta kenttää vastaava kysymys 
	 */
	public String getKysymys(int k) {
		switch (k) {
		case 0:
			return "Asiakkaan ID";
		case 1:
			return "Nimi";
		case 2:
			return "Sotu";
		case 3:
			return "Osoite";
		case 4:
			return "Puhelinnumero";
		case 5:
			return "Lisätiedot";
		default:
			return "HupsisKupsos";
		}
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