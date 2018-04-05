package model;

import java.util.ArrayList;

public class Pyorat {
	
	private ArrayList<Pyora> alkiot = new ArrayList<>();

	private String tiedostonNimi = "";

	/**
	 * Oletusmuodostaja
	 */
	public Pyorat() {
		// Attribuuttien oma alustus riitt��
	}

	/**
	 * Lis�� uuden py�r�n tietorakenteeseen. Ottaa py�r�n omistukseensa.
	 * 
	 * @param Pyora lis�tt�v�n py�r�n viite. Huom tietorakenne muuttuu omistajaksi
	 * @throws SailoException  jos tietorakenne on jo t�ynn�
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException 
	 * Pyorat pyorat = new Pyorat();
	 * Pyora jopo1 = new Pyora(), jopo2 = new Pyora();
	 * pyorat.getLkm() === 0;
	 * pyorat.lisaa(jopo1); pyorat.getLkm() === 1;
	 * pyorat.lisaa(jopo2); pyorat.getLkm() === 2;
	 * pyorat.lisaa(jopo1); pyorat.getLkm() === 3;
	 * pyorat.anna(0) === jopo1;
	 * pyorat.anna(1) === jopo2;
	 * pyorat.anna(2) === jopo1;
	 * pyorat.anna(1) == jopo1 === false;
	 * pyorat.anna(1) == jopo2 === true;
	 * pyorat.lisaa(jopo1); pyorat.getLkm() === 4;
	 * pyorat.lisaa(jopo1); pyorat.getLkm() === 5;
	 * pyorat.lisaa(jopo1);
	 *          </pre>
	 */
	public void lisaa(Pyora pyora) {
		alkiot.add(pyora);
	}

	/**
	 * Palauttaa viitteen i:teen py�r��n.
	 * 
	 * @param i monennenko py�r�n viite halutaan
	 * @return viite py�r��n, jonka indeksi on i
	 * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella
	 */
	public Pyora anna(int i) throws IndexOutOfBoundsException {
		if (i < 0 || alkiot.size() <= i)
			throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
		return alkiot.get(i);
	}

	/**
	 * Lukee j�senist�n tiedostosta. Kesken.
	 * 
	 * @param hakemisto tiedoston hakemisto
	 * @throws SailoException jos lukeminen ep�onnistuu
	 */
	public void lueTiedostosta(String hakemisto) throws SailoException {
		tiedostonNimi = hakemisto + "/pyorat.dat";
		throw new SailoException("Ei osata viel� lukea tiedostoa " + tiedostonNimi);
	}

	/**
	 * Tallentaa j�senist�n tiedostoon. Kesken.
	 * @throws SailoException jos talletus ep�onnistuu
	 */
	public void talleta() throws SailoException {
		throw new SailoException("Ei osata viel� tallettaa tiedostoa " + tiedostonNimi);
	}

	/**
	 * Palauttaa kerhon py�rien lukum��r�n
	 * 
	 * @return py�rien lukum��r�
	 */
	public int getLkm() {
		return alkiot.size();
	}

	/**
	 * Testataan toimivuutta
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Pyorat pyorat = new Pyorat();

		Pyora jopo1 = new Pyora(), jopo2 = new Pyora();
		jopo1.rekisteroi();
		jopo1.vastaaJopo();
		jopo2.rekisteroi();
		jopo2.vastaaJopo();
		pyorat.lisaa(jopo1);
		pyorat.lisaa(jopo2);

		System.out.println("============= Py�r�t testi =================");

		for (int i = 0; i < pyorat.getLkm(); i++) {
			Pyora jasen = pyorat.anna(i);
			System.out.println("J�sen nro: " + i);
			jasen.tulosta(System.out);
		}

	}

}
