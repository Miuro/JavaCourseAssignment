package model;

public class Asiakkaat {

	private static final int 	MAX_ASIAKKAITA 	= 5;
	private int 				lkm 			= 0;
	private String 				tiedostonNimi 	= "";
	private Asiakas 			alkiot[] 		= new Asiakas[MAX_ASIAKKAITA];
	
	
	/**
	 * Oletusmuodostaja
	 */
	public Asiakkaat() {
		//
	}
	
	
	/**
	 * Lis�� uuden asiakkaan tietorakenteeseensa. Ottaa asiakkaan omistukseensa
	 * @param asiakas lis�tt�v�n asiakkaan viite. Huom. tietorakenne muuttuu omistajaksi
	 * @throws SailoException jos tietorakenne on jo t�ynn�
	 */
	public void lisaa(Asiakas asiakas) /*throws SailoException*/ {
		if(lkm >= alkiot.length) {
			Asiakas[] isompi = new Asiakas[alkiot.length + 5];
			for(int i = 0; i < alkiot.length; i++) {
				isompi[i] = alkiot[i];
			}
			alkiot = isompi;
		}
		//throw new SailoException("Liikaa alkioita");
		alkiot[lkm] = asiakas;
		lkm++;
	}
	
	
	/**
	 * Palauttaa viitteen i:teen asiakkaaseen
	 * @param i monennenko asiakkaan viite halutaan
	 * @return viite asiakkaaseen jonka indeksi on i
	 * @throws IndexOutOfBoundsException jos indeksi i ei ole sallitulla alueella
	 */
	public Asiakas anna(int i) throws IndexOutOfBoundsException {
		if(i < 0 || lkm <= i)
			throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
		return alkiot[i];
	}
	
	
	/**
	 * Lukee asiakkaat tiedostosta. Kesken!
	 * @param hakemisto tiedoston hakemisto
	 * @throws SailoException jos lukeminen ep�onnistuu
	 */
	public void lueTiedostosta(String hakemisto) throws SailoException {
		tiedostonNimi = hakemisto + "/asiakkaat.dat";
		throw new SailoException("Ei osata viel� lukea tiedostoa " + tiedostonNimi);
	}
	
	
	/**
	 * Tallentaa asiakkaat tiedostoon. Kesken!
	 * @throws SailoException jos talletus ep�onnistuu
	 */
	public void talleta() throws SailoException {
		throw new SailoException("Ei osata viel� tallettaa tiedostoa " + tiedostonNimi);
	}
	
	
	/**
	 * Palauttaa asiakkaiden lukum��r�n
	 * @return asiakkaiden lukum��r�
	 */
	public int getLkm() {
		return lkm;
	}
	
	
	/**
	 * Testiohjelma asiakkaille
	 * @param args ei k�yt�s�
	 */
	public static void main(String[] args) {
		// TODO: Everything
	}
	
}