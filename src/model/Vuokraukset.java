package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Vuokraukset implements Iterable<Vuokraus> {
	
	//private static final int MAX_VUOKRAUKSIA = 5; // does this even make sense?
	//private int lkm = 0;
	private String tiedostonNimi = "";
	private final Collection<Vuokraus> alkiot = new ArrayList<Vuokraus>();
	
	
	/**
	 * Lisää uuden asiakkaan tietorakenteeseensa. Ottaa vuokrauksen omistukseensa
	 * @param vuokraus lisättävän vuokrauksen viite. Huom. tietorakenne muuttuu omistajaksi
	 * @throws SailoException jos tietorakenne on jo täynnä
	 */
	public void lisaa(Vuokraus vuokraus) throws SailoException {
		alkiot.add(vuokraus);
		/*if(lkm >= alkiot.length) throw new SailoException("Liikaa alkioita");
		alkiot[lkm] = vuokraus;
		lkm++;*/
	}
	
	/*
	/**
	 * Palauttaa viitteen i:teen vuokraukseen
	 * @param i monennenko asiakkaan viite halutaan
	 * @return viite vuokraukseen jonka indeksi on i
	 * @throws IndexOutOfBoundsException jos indeksi i ei ole sallitulla alueella
	 */
	/*public Vuokraus anna(int i) throws IndexOutOfBoundsException {
		if(i < 0 || lkm <= i)
			throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
		return alkiot[i];
	}
	*/
	
	
	/**
	 * Lukee vuokraukset tiedostosta. Kesken!
	 * @param hakemisto tiedoston hakemisto
	 * @throws SailoException jos lukeminen epäonnistuu
	 */
	public void lueTiedostosta(String hakemisto) throws SailoException {
		tiedostonNimi = hakemisto + "/vuokraukset.dat";
		throw new SailoException("Ei osata vielä lukea tiedostoa " + tiedostonNimi);
	}
	
	
	/**
	 * Tallentaa vuokraukset tiedostoon. Kesken!
	 * @throws SailoException jos talletus epäonnistuu
	 */
	public void talleta() throws SailoException {
		throw new SailoException("Ei osata vielä tallettaa tiedostoa " + tiedostonNimi);
	}

	
	/**
	 * Palauttaa vuokrausten lukumäärän
	 * @return vuokrausten lukumäärä
	 */
	public int getLkm() {
		return alkiot.size();
	}
	
	@Override
	public Iterator<Vuokraus> iterator(){
		return alkiot.iterator();
	}
	
	public Vuokraus annaVuokraus(int tunnusNro) throws SailoException{
		try {
			for(Vuokraus vuokr : alkiot)
				if(vuokr.getPyoraId() == tunnusNro) return vuokr;
		} catch (Exception e) {
			throw new SailoException("Vuokrausta ei löytynyt, pyöräid: " + tunnusNro);
		}
		return null;
	}
	
	
	
	
	/**
	 * Testiohjelma vuokrauksille
	 * @param args ei käytösä
	 * @throws SailoException 
	 */
	public static void main(String[] args) {
		
	}
}
