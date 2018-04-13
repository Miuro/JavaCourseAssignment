package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Vuokraukset implements Iterable<Vuokraus> {
	
	//private static final int MAX_VUOKRAUKSIA = 5; // does this even make sense?
	//private int lkm = 0;
	private String tiedostonPerusNimi = "vuokraukset";
	private final Collection<Vuokraus> alkiot = new ArrayList<Vuokraus>();
	private boolean muutettu = false;
	
	
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
	
	public void etsi(int pyoraID) {
		Collection<Pyora> loytyneet = new ArrayList<>(); 
        for (Vuokraus v: alkiot) { 
            loytyneet.add(v);  
        } 
        return loytyneet;
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
	 * Lukee vuokraukset tiedostosta. Kesken.
	 * 
	 * @param tied tiedoston nimi
	 * @throws FileNotFoundException jos ei aukea
	 * @throws IOException jos ongelmia tiedoston kanssa
	 */
	public void lueTiedostosta(String tied) throws SailoException {
		setTiedostonPerusNimi(tied);
		
		try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
			String rivi;
			while ((rivi = fi.readLine()) != null) {
				rivi = rivi.trim();
				if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
				Vuokraus vuokraus = new Vuokraus();
				vuokraus.parse(rivi);
				lisaa(vuokraus);
			}
			muutettu = false;
			
		} catch (FileNotFoundException e) {
			throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
		} catch (IOException e) {
			throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
		}
	}
	
	
	/**
	 * Tallettaa vuokraukset tiedostoon
	 * 
	 * @throws SailoException 
	 */
	public void tallenna() throws SailoException {
		if (!muutettu) return;
		
		File fbak = new File(getBakNimi());
		File ftied = new File(getTiedostonNimi());
		fbak.delete();
		ftied.renameTo(fbak);
		
		try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
			for(int i = 0; i < getLkm(); i++) {
				fo.println(annaVuokraus(i).toString());
			}
		} catch (FileNotFoundException e) {
			throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
		} catch (IOException e) {
			throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
		}
		
		muutettu = false;
		
	}

	
	/**
	 * Palauttaa vuokrausten lukumäärän
	 * 
	 * @return vuokrausten lukumäärä
	 */
	public int getLkm() {
		return alkiot.size();
	}
	
	
	/**
	 * Luetaan aikaisemmin annetusta tiedostosta
	 * 
	 * @throws SailoException jos tulee poikkeus
	 */
	public void lueTiedostosta() throws SailoException {
		lueTiedostosta(getTiedostonPerusNimi());
	}
	
	
	/**
	 * Asettaa tiedoston perusnimen ilman tarkenninta
	 * 
	 * @param tied tallennustiedoston nimi
	 */
	public void setTiedostonPerusNimi(String tied) {
		this.tiedostonPerusNimi = tied;
	}
	
	
	/**
	 * Palauttaa tiedoston nimen jota käytetään tallennukseen ilman päätettä
	 * 
	 * @return tallennustiedoston nimi
	 */
	public String getTiedostonPerusNimi() {
		return this.tiedostonPerusNimi;
	}
	
	
	/**
	 * Palauttaa tiedoston nimen jota käytetään tallennukseen (.dat)
	 * 
	 * @return tiedoston nimi + .dat
	 */
	public String getTiedostonNimi() {
		return this.tiedostonPerusNimi + ".dat";
	}
	
	
	/**
	 * Palauttaa varakopiotiedoston nimen
	 * 
	 * @return varakopiotiedoston nimi
	 */
	public String getBakNimi() {
		return this.tiedostonPerusNimi + ".bak";
	}
	
	
	/**
	 * Iteraattori
	 */
	@Override
	public Iterator<Vuokraus> iterator(){
		return alkiot.iterator();
	}
	
	
	/**
	 * Palauttaa halutun vuokrauksen
	 * 
	 * @param tunnusNro halutun vuokrauksen pyörän tunnusnumero
	 * @return vuokr haluttu vuokraus
	 * @throws SailoException jos ei löydy
	 */
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
	 *
	 * @param args ei käytösä 
	 */
	public static void main(String[] args) {
		
	}
}
