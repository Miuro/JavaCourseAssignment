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
	 */
	public void lisaa(Vuokraus vuokraus) {
		alkiot.add(vuokraus);
		muutettu = true;
	}
	
	/**
	 * Etsii pyörän vuokrauksen
	 * @param pyoraID vuokratun pyörän tunnusluku
	 * @return löytynyt vuokraus, tai null jos ei löytynyt
	 */
	public Vuokraus etsi(int pyoraID) {
        for (Vuokraus v : alkiot) {
			if(v.getPyoraId() == pyoraID)
				return v;
		}
     return null;
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
	 * Lukee vuokraukset tiedostosta.
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
			throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei löytynyt. Luodaan uusi.");
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
			for (Vuokraus vuokraus : this) {
				fo.println(vuokraus.toString());
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
	 * @param tunnusNro halutun vuokrauksen id
	 * @return vuokr haluttu vuokraus
	 * @throws SailoException jos ei löydy
	 * @example
	 * <pre name="test">
	 * Vuokraukset t = new Vuokraukset();
	 * v1 = new Vuokraus();
	 * v1.rekisteroi();
	 * v1.testiVuokraus();
	 * t.lisaa(v1);
	 * v2 = new Vuokraus();
	 * v2.testiVuokraus();
	 * v2.rekisteroi();
	 * t.lisaa(v2);
	 * </pre>
	 */
	public Vuokraus anna(int vuokrauksenId){
		for (Vuokraus vuokraus : alkiot) {
			if(vuokraus.getVuokrausId() == vuokrauksenId) {
				return vuokraus;
			}
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
