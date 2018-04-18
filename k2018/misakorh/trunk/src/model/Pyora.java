package model;

import java.io.*;


/*
 * Pyor�-olio
 * Sis�lt�� konstruktorit uudelle py�r�lle.
 * Sis�lt�� metodit py�r�n muokkaukselle.
 * TODO: Enum py�r�n malleille. esim maastopy�r�, mummopy�r�, muu jne.
 * TODO: Miten saadaan enumit tulostamaan sis�lt� kivasti?
 */
public class Pyora implements Cloneable {

	private static String[] kunnot = { "Rikki", "Tyydytt�v�", "Hyv�", "Erinomainen" };
	private static int seuraavaID = 1;

	private int pyoranID;
	private String nimi = "-";
	private String malli = "-";
	private int kunto = 3; // 0-3. 0 = rikki, 1 = tyydytt�v�, 2 = hyv�, 3 = erinomainen
	private String lisatietoja = "-";
	private boolean onkoVarattu = false; // FALSE = vapaana, TRUE = vuokrattuna
	private double vuokraPerTunti = 0;

	
	/**
	 * Asetin py�r�n varauksen tilalle
	 * @param arvo uusi arvo
	 */
	public void setOnkoVarattu(boolean arvo) {
		this.onkoVarattu = arvo;
	}
	
	public boolean getOnkoVarattu() {
		return onkoVarattu;
	}
	
	public void setVuokraPerH(double hinta) {
		vuokraPerTunti = hinta;
	}

	/**
	 * Palauttaa py�r�n kenttien m��r�n
	 * @return kenttien m��r�
	 */
	public int getKenttia() {
		return 7;
	}


	/**
	 * Eka kentt� joka on mielek�s kysytt�v�ksi
	 * @return eknn kent�n indeksi
	 */
	public int ekaKentta() {
		return 1;
	}


	/**
	 * Antaa k:n kent�n sis�ll�n merkkijonona
	 * @param k monenenko kent�n sis�lt� palautetaan
	 * @return kent�n sis�lt� merkkijonona
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
			return "" + kunto;
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
	 * Peruskonstruktori py�r�lle.
	 */
	public Pyora() {
	}


	/**
	 * Antaa j�senelle seuraavan rekisterinumeron.
	 * @return j�senen uusi tunnusNro
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
	 * @return Palauttaa py�r�n ID:n
	 */
	public int getPyoranID() {
		return pyoranID;
	}


	/**
	 * Asettaa py�r�n ID:ksi annetun arvon
	 * Varmistaa, ett� seuraava numero on aina suurempi kuin t�h�n menness� suurin.
	 * @param id Py�r�n uusi ID
	 */
	public void setPyoranID(int id) {
		pyoranID = id;
		if (pyoranID >= seuraavaID) seuraavaID = pyoranID+ 1;
	}


	/**
	 * @return Palauttaa py�r�n nimen.
	 */
	public String getNimi() {
		return nimi;
	}
	
	/**
	 * @return palauttaa py�r�n mallin
	 */
	public String getMalli() {
		return malli;
	}
	
	/**
	 * @return palauttaa py�r�n kunnon
	 */
	public int getKunto() {
		return kunto;
	}


	/**
	 * @return Py�r�n vuokra per p�iv�
	 */
	public double getVuokraPerTunti() {
		return vuokraPerTunti;
	}


	/**
	 * Palauttaa py�r�n string muodossa ominaisuudet erotettuna "|"-merkill�
	 * @return py�r� stringmuodossa jossa ominaisuudet erotettuna "|"-merkill�
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
	 * Palauttaa py�r�n string muodossa ominaisuudet erotettuna "|"-merkill� poislukien Id kentt� 
	 * @return py�r� stringmuodossa jossa ominaisuudet erotettuna "|"-merkill� ilman id:t�
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
	 * 
	 */
	public int compareTo(Pyora p) {
		if(this.getVuokraPerTunti() < p.getVuokraPerTunti()) return -1;
		if(this.getVuokraPerTunti() > p.getVuokraPerTunti()) return 1;
		return 0;
	}

	
	/**
	 * Selvit�� py�r�n tiedot | erotellusta merkkijonosta
	 * @param rivi josta py�r�n tiedot otetaan
	 * <pre name="test">
	 * Pyora p1 = new Pyora();
	 * Pyora p2 = new Pyora();
	 * p1.parse("1|Mountainer 6X|Maastopy�r�|3|50|true|Jee");
	 * p2.setPyoranID(2);
	 * p1.toString() === "1|Mountainer 6X|Maastopy�r�|3|50.0|true|Jee";
	 * p2.toString() === "2|||3|0.0|false|";
	 * </pre>
	 */
	public void parse(String rivi) {
		String[] osat = rivi.split("\\|");
		for (int k = 0; k < getKenttia(); k++) {
			aseta(k, osat[k]);
		}
				
				/*
		StringBuilder sb = new StringBuilder();
		int kohta = 0;
		for (int k = 0; k < getKenttia(); k++) {
			aseta(k, Mjonot.erota(sb, '|'));
			aseta(k, sb.subSequence(kohta, sb.indexOf("|"))
			*/
		
	}


	/**
	 * Asettaa k:n kent�n arvoksi parametrina tuodun merkkijonon arvon
	 * @param k kuinka monennen kent�n arvo asetetaan
	 * @param jono jonoa joka asetetaan kent�n arvoksi
	 * @return null jos asettaminen onnistuu.
	 * @example <pre name="test">
	 * Pyora jasen = new Pyora();
	 * jasen.aseta(1,"Jopo") === null;
	 * jasen.aseta(3,"Terve") === "Kunto oli v��rin, anna arvo 0-3";
	 * jasen.aseta(4, "Moi") === "Vuokra oli v��rin";
	 * </pre>
	 */
	public String aseta(int k, String jono) {
		String tjono = jono.trim();
		if(tjono.equals("")) tjono = "-";
		switch (k) {
		case 0:
			setPyoranID(Integer.parseInt(tjono));
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
				return "Kunto oli v��rin, anna arvo 0-3";
			}
			return null;
		case 4:
			try {
				vuokraPerTunti = Double.parseDouble(tjono);
			} catch (NumberFormatException e) {
				return "Vuokra oli v��rin";
			}
			return null;
		case 5:
			onkoVarattu = Boolean.parseBoolean(tjono);
			return null;
		case 6:
			lisatietoja = tjono;
			return null;
		default:
			return "Tervetti";
		}
	}


	/**
	 * Palauttaa k:tta j�senen kentt�� vastaavan kysymyksen
	 * @param k kuinka monennen kent�n kysymys palautetaan (0-alkuinen)
	 * @return k:netta kentt�� vastaava kysymys
	 */
	public String getKysymys(int k) {
		switch (k) {
		case 0:
			return "Py�r�n ID";
		case 1:
			return "Nimi";
		case 2:
			return "Malli";
		case 3:
			return "Kunto 0-3";
		case 4:
			return "Vuokra per tunti";
		case 5:
			return "Onko varattuna";
		case 6:
			return "Lis�tietoja";
		default:
			return "Hupsista";
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
		lisatietoja = "Penkki pit�� vaihtaa";
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
		lisatietoja = "Penkki pit�� vaihtaa";
	}

	/**
	 * Tulostetaan py�r�n tiedot.
	 * @param out
	 */
	public void tulosta(PrintStream out) {
		out.println(String.format("%03d", pyoranID, 3) + "  " + nimi + "  " + malli);
		out.println(String.format(kunnot[kunto]));
		out.println("Vuokran m��r�: " + String.format("%4.2f", vuokraPerTunti));
		out.println("Onko vuokrattuna: " + onkoVarattu);
		out.println("Lis�tiedot: " + lisatietoja);
	}


	/**
	 * Tulostetaan py�r�n tiedot
	 * @param os tietovirta johon tulostetaan
	 */
	public void tulosta(OutputStream os) {
		tulosta(new PrintStream(os));
	}


	/**
	 * Testataan py�r�luokan toimivuutta.
	 * @param args
	 */
	public static void main(String[] args) {

	}

}