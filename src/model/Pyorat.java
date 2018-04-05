package model;

public class Pyorat {

	private static final int MAX_PYORIA = 5;
	private int lkm = 0;
	private String tiedostonNimi = "";
	private Pyora alkiot[] = new Pyora[MAX_PYORIA];

	/**
	 * Oletusmuodostaja
	 */
	public Pyorat() {
		// Attribuuttien oma alustus riittää
	}

	/**
	 * Lisää uuden pyörän tietorakenteeseen. Ottaa pyörän omistukseensa.
	 * 
	 * @param Pyora
	 *            lisättävän pyörän viite. Huom tietorakenne muuttuu omistajaksi
	 * @throws SailoException
	 *             jos tietorakenne on jo täynnä
	 * @example
	 * 
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
	public void lisaa(Pyora Pyora) {
		if (lkm >= alkiot.length) {
			Pyora[] isompi = new Pyora[alkiot.length + 5];
			for(int i = 0; i < alkiot.length; i++) {
				isompi[i] = alkiot[i];
			}
			alkiot = isompi;
		}
		alkiot[lkm] = Pyora;
		lkm++;
	}

	/**
	 * Palauttaa viitteen i:teen pyörään.
	 * 
	 * @param i monennenko pyörän viite halutaan
	 * @return viite pyörään, jonka indeksi on i
	 * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella
	 */
	public Pyora anna(int i) throws IndexOutOfBoundsException {
		if (i < 0 || lkm <= i)
			throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
		return alkiot[i];
	}

	/**
	 * Lukee jäsenistön tiedostosta. Kesken.
	 * 
	 * @param hakemisto tiedoston hakemisto
	 * @throws SailoException jos lukeminen epäonnistuu
	 */
	public void lueTiedostosta(String hakemisto) throws SailoException {
		tiedostonNimi = hakemisto + "/pyorat.dat";
		throw new SailoException("Ei osata vielä lukea tiedostoa " + tiedostonNimi);
	}

	/**
	 * Tallentaa jäsenistön tiedostoon. Kesken.
	 * 
	 * @throws SailoException
	 *             jos talletus epäonnistuu
	 */
	public void talleta() throws SailoException {
		throw new SailoException("Ei osata vielä tallettaa tiedostoa " + tiedostonNimi);
	}

	/**
	 * Palauttaa kerhon pyörien lukumäärän
	 * 
	 * @return pyörien lukumäärä
	 */
	public int getLkm() {
		return lkm;
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

		System.out.println("============= Pyörät testi =================");

		for (int i = 0; i < pyorat.getLkm(); i++) {
			Pyora jasen = pyorat.anna(i);
			System.out.println("Jäsen nro: " + i);
			jasen.tulosta(System.out);
		}

	}

}
