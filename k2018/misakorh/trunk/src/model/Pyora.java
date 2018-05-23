package model;

import java.io.*;


/*
 * Pyorä-olio
 * Sisältää konstruktorit uudelle pyörälle.
 * Sisältää metodit pyörän muokkaukselle.
 * @author Jouko Sirkka, Miro Korhonen
 * @version 1.1, 15.5.2018
 */
public class Pyora implements Cloneable {

	private static String[] kunnot = { "Rikki", "Tyydyttävä", "Hyvä", "Erinomainen" };
	public static int seuraavaID = 1;

	private int pyoranID;
	private String nimi = "-";
	private String malli = "-";
	private int kunto = 3; // 0-3. 0 = rikki, 1 = tyydyttävä, 2 = hyvä, 3 = erinomainen
	private String lisatietoja = "-";
	private boolean onkoVarattu = false; // FALSE = vapaana, TRUE = vuokrattuna
	private double vuokraPerTunti = 0;

	
	/**
	 * Asetin pyörän varauksen tilalle
	 * @param arvo uusi arvo
	 * <pre name="test">
	 * Pyora p = new Pyora();
	 * p.getOnkoVarattu() === false;
	 * p.setOnkoVarattu(true);
	 * p.getOnkoVarattu() === true; 
	 * </pre>
	 */
	public void setOnkoVarattu(boolean arvo) {
		this.onkoVarattu = arvo;
	}
	
	/**
	 * Palauttaa tiedon, onko pyörä varattu
	 * @return True, jos pyörä on varattu
	 */
	public boolean getOnkoVarattu() {
		return onkoVarattu;
	}
	
	
	/**
	 * Asettaa pyörän tuntivuokran määrän
	 * @param hinta Hinta euroissa per tunti
	 */
	public void setVuokraPerH(double hinta) {
		vuokraPerTunti = hinta;
	}

	/**
	 * Palauttaa pyörän kenttien määrän
	 * @return kenttien määrä
	 */
	public int getKenttia() {
		return 7;
	}


	/**
	 * Eka kenttä joka on mielekäs kysyttäväksi
	 * @return eknn kentän indeksi
	 */
	public int ekaKentta() {
		return 1;
	}


	/**
	 * Antaa k:n kentän sisällön merkkijonona
	 * @param k monenenko kentän sisältö palautetaan
	 * @return kentän sisältö merkkijonona
	 * @example
	 * <pre name="test">
	 * Pyora p1 = new Pyora();
	 * p1.vastaaJopo();
	 * p1.anna(1) === "Punainen Jopo";
	 * p1.anna(4) === "Penkki pitää vaihtaa";
	 * </pre>
	 */
	public String anna(int k) {
		switch (k) {
		case 0:
			return "" + pyoranID;
		case 1:
			return "" + nimi;
		case 2:
			return "" + malli;
		case 3:
			return "" + kunnot[kunto];
		case 4:
			return "" + lisatietoja;
		case 5:
			return "" + onkoVarattu;
		case 6:
			return "" + vuokraPerTunti;
		default:
			return "Hupsista";
		}
	}


	/**
	 * Peruskonstruktori pyörälle.
	 */
	public Pyora() {
	}


	/**
	 * Antaa jäsenelle seuraavan rekisterinumeron.
	 * @return jäsenen uusi tunnusNro
	 * @example <pre name="test">
	 *   Pyora jopo1 = new Pyora();
	 *   jopo1.getPyoranID() === 0;
	 *   jopo1.rekisteroi();
	 *   Pyora jopo2 = new Pyora();
	 *   jopo2.rekisteroi();
	 *   int n1 = jopo1.getPyoranID();
	 *   int n2 = jopo2.getPyoranID();
	 *   n1 === n2-1;
	 * </pre>
	 */
	public int rekisteroi() {
		pyoranID = seuraavaID;
		seuraavaID++;
		return pyoranID;
	}


	/**
	 * @return Palauttaa pyörän ID:n
	 */
	public int getPyoranID() {
		return pyoranID;
	}


	/**
	 * Asettaa pyörän ID:ksi annetun arvon
	 * Varmistaa, että seuraava numero on aina suurempi kuin tähän mennessä suurin.
	 * @param id Pyörän uusi ID
	 */
	public void setPyoranID(int id) {
		pyoranID = id;
		if (pyoranID >= seuraavaID) seuraavaID = pyoranID+ 1;
	}


	/**
	 * @return Palauttaa pyörän nimen.
	 */
	public String getNimi() {
		return nimi;
	}
	
	/**
	 * @return palauttaa pyörän mallin
	 */
	public String getMalli() {
		return malli;
	}
	
	
	/*
	 * @return palauttaa pyörän kunnon integer arvona. 0 = rikki ja 3 = erinomainen
	 */
	
	public int getKunto() {
		return kunto;
	}


	/**
	 * @return Pyörän vuokra per päivä
	 */
	public double getVuokraPerTunti() {
		return vuokraPerTunti;
	}


	/**
	 * Palauttaa pyörän string muodossa ominaisuudet erotettuna "|"-merkillä
	 * @return pyörä stringmuodossa jossa ominaisuudet erotettuna "|"-merkillä
	 */
	@Override
	public String toString() {
		return pyoranID + "|" +
				nimi + "|" +
				malli + "|" +
				kunto + "|" +
				vuokraPerTunti + "|" +
				onkoVarattu + "|"
				+ lisatietoja;
	}
	
	/**
	 * Palauttaa pyörän string muodossa ominaisuudet erotettuna "|"-merkillä poislukien Id kenttä 
	 * @return pyörä stringmuodossa jossa ominaisuudet erotettuna "|"-merkillä ilman id:tä
	 */
	public String toStringNOID() {
		return 	nimi + "|" +
				malli + "|" +
				kunto + "|" +
				vuokraPerTunti + "|" +
				onkoVarattu + "|"
				+ lisatietoja;
	}

	/**
	 * Vertailu kahdelle pyörälle. Vertaa pyörien hintoja.
	 */
	public int compareTo(Pyora p) {
		if(this.getVuokraPerTunti() < p.getVuokraPerTunti()) return -1;
		if(this.getVuokraPerTunti() > p.getVuokraPerTunti()) return 1;
		return 0;
	}

	
	/**
	 * Selvitää pyörän tiedot | erotellusta merkkijonosta
	 * @param rivi josta pyörän tiedot otetaan
	 * <pre name="test">
	 * #THROWS SailoException
	 * Pyora p1 = new Pyora();
	 * Pyora p2 = new Pyora();
	 * p1.parse("1|Mountainer 6X|Maastopyörä|3|50|true|Jee");
	 * p2.setPyoranID(2);
	 * p1.toString() === "1|Mountainer 6X|Maastopyörä|3|50.0|true|Jee";
	 * p2.toString() === "2|-|-|3|0.0|false|-";
	 * </pre>
	 * @throws SailoException 
	 */
	public void parse(String rivi) throws SailoException {
		String[] osat = rivi.split("\\|");
		for (int k = 0; k < getKenttia(); k++) {
			aseta(k, osat[k]);
		}
		
	}


	/**
	 * Asettaa k:n kentän arvoksi parametrina tuodun merkkijonon arvon
	 * @param k kuinka monennen kentän arvo asetetaan
	 * @param jono jonoa joka asetetaan kentän arvoksi
	 * @return null jos asettaminen onnistuu.
	 * @throws SailoException 
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException
	 * Pyora jasen = new Pyora();
	 * jasen.aseta(1,"Jopo") === null;
	 * jasen.aseta(3,"Terve"); #THROWS SailoException
	 * jasen.aseta(4, "Moi"); #THROWS SailoException
	 * </pre>
	 */
	public String aseta(int k, String jono) throws SailoException {
		String tjono = jono.trim();
		if(tjono.equals("")) tjono = "-";
		switch (k) {
		case 0:
			try {
				setPyoranID(Integer.parseInt(tjono));
			} catch (NumberFormatException e) {
				throw new SailoException("ID:n on oltava numero");
			}
			return null;
		case 1:
			nimi = tjono;
			return null;
		case 2:
			malli = tjono;
			return null;
		case 3:
			try {
				kunto = Integer.parseInt(tjono);
			} catch (NumberFormatException e) {
				throw new SailoException("Kunto oli väärin, anna arvo 0-3");
			}
			return null;
		case 4:
			try {
				vuokraPerTunti = Double.parseDouble(tjono);
			} catch (NumberFormatException e) {
				throw new SailoException("Vuokran luku epäonnistui, anna numeroita.");
			}
		case 5:
			try {
				onkoVarattu = Boolean.parseBoolean(tjono);
			} catch (Exception e) {
				throw new SailoException("Tilan luku epäonnistui. Kirjoita false tai true");
			}
		case 6:
			lisatietoja = tjono;
			return null;
		default:
			return "Tervetti";
		}
	}


	/**
	 * Apumetodi testiarvojen tuottamiselle
	 */
	public void vastaaJopo() {
		nimi = "Punainen Jopo";
		malli = "Helkama Jopo";
		kunto = 1;
		vuokraPerTunti = 12;
		lisatietoja = "Penkki pitää vaihtaa";
	}

	/**
	 * Apumetodi testiarvojen tuottamiselle
	 */
	public void vastaaJopo2() {
		nimi = "Punainen Jopo";
		malli = "Helkama Jopo";
		onkoVarattu = true;
		kunto = 1;
		vuokraPerTunti = 10;
		lisatietoja = "Penkki pitää vaihtaa";
	}

	/**
	 * Tulostetaan pyörän tiedot.
	 * @param out
	 */
	public void tulosta(PrintStream out) {
		out.println(String.format("%03d", pyoranID, 3) + "  " + nimi + "  " + malli);
		out.println(String.format(kunnot[kunto]));
		out.println("Vuokran määrä: " + String.format("%4.2f", vuokraPerTunti));
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

	}

}