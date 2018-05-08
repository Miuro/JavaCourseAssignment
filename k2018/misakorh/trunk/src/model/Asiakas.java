package model;

import java.io.*;

import fi.jyu.mit.ohj2.Mjonot;

public class Asiakas{
	
	private int 		asiakasId;
	private String 		nimi 		= "-";
	private String 		sotu 		= "-";
	private String 		osoite 		= "-";
	private String 		puhnum 		= "-";
	
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
		return 5;
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
	 * <pre name="test">
	 * Asiakas a = new Asiakas();
	 * a.vastaaHessuHopo();
	 * a.anna(1) === "Hessu Hopo";
	 * a.anna(2) === "123123-123W";
	 * a.anna(3) === "Aleksanterinkatu 14, Helsinki";
	 * a.anna(4) === "050 1231231";
	 * </pre>
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
	}
	
	
	/**
	 * Palauttaa asiakkaan nimen
	 * @return Asiakkaan nimi
	 * <pre name="test">
	 * Asiakas a = new Asiakas();
	 * a.vastaaHessuHopo();
	 * a.getNimi() === "Hessu Hopo";
	 * </pre>
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
	 * <pre name="test">
	 * Asiakas a = new Asiakas();
	 * a.vastaaHessuHopo();
	 * a.toString() === "0|Hessu Hopo|123123-123W|Aleksanterinkatu 14, Helsinki|050 1231231|";
	 * </pre>
	 */
	@Override
	public String toString() {
		return "" + 
				getAsiakasId() + "|" +
				nimi + "|" +
				sotu + "|" +
				osoite + "|" +
				puhnum + "|";
	}
	
	
	/**
	 * Jakaa merkkijonon haluttuihin osiin ja kutsuu aseta() funktiota joka asettaa luetut tiedot
	 * asiakkaan tiedoiksi jos mahdollista
	 * @param rivi jaettava merkkijono
	 * <pre name="test">
	 * Asiakas a1 = new Asiakas();
	 * Asiakas a2 = new Asiakas();
	 * a1.parse("1|Frans Bergman|110156-728K|Marjakatu 6 62420 Vuoristo|-");
	 * a1.toString() === "1|Frans Bergman|110156-728K|Marjakatu 6 62420 Vuoristo|-|";
	 * a2.setAsiakasId(2);
	 * a2.toString() === "2|-|-|-|-|";
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
	 * <pre name="test">
	 * Asiakas a = new Asiakas();
	 * a.aseta(1, "Snibs");
	 * a.toString() === "0|Snibs|-|-|-|";
	 * </pre>
	 */
	public String aseta(int k, String jono) {
		String tjono = jono.trim();
		StringBuffer sb = new StringBuffer(tjono);
		switch (k) {
		case 0:
			setAsiakasId(Mjonot.erota(sb, '|', getAsiakasId()));
			return null;
		case 1:
			if (tjono != "")
				nimi = tjono;
			return null;
		case 2:
			if (tjono != "")
				sotu = tjono;
			return null;
		case 3:
			if (tjono != "" && tjono.length() > 1)
				osoite = tjono;
			return null;
		case 4:
			if (tjono != "")
				puhnum = tjono;
			return null;
		default:
			return "Tervetti";
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
	 * <pre name="test">
	 * Asiakas a = new Asiakas();
	 * a.getAsiakasId() === 0;
	 * </pre>
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
		
	}

}