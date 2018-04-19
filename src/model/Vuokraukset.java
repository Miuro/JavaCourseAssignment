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
	
	private String tiedostonPerusNimi = "vuokraukset";
	private final Collection<Vuokraus> alkiot = new ArrayList<Vuokraus>();
	private boolean muutettu = false;
	
	
	/**
	 * Lisää uuden asiakkaan tietorakenteeseensa. Ottaa vuokrauksen omistukseensa
	 * @param vuokraus lisättävän vuokrauksen viite. Huom. tietorakenne muuttuu omistajaksi
	 */
	public void lisaa(Vuokraus vuokraus) {
		vuokraus.rekisteroi();
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
			throw new SailoException("Tiedostoa " + getTiedostonNimi() + " ei löytynyt.");
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
	 * Vuokraus v1 = new Vuokraus();
	 * v1.rekisteroi();
	 * v1.testiVuokraus(1,5);
	 * t.lisaa(v1);
	 * Vuokraus v2 = new Vuokraus();
	 * v2.testiVuokraus(2,4);
	 * v2.rekisteroi();
	 * t.lisaa(v2);
	 * t.anna(1) === v1;
	 * t.anna(2) === v2;
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
	 * Poistaa vuokrauksen sen pyöräID:n mukaan
	 * @param pyoranID Pyörä, jonka vuokraus poistetaan
	 * @return True, jos poisto onnistui
	 */
	public boolean poista(int pyoranID) {
		for (Vuokraus vuokraus : alkiot) {
			if(vuokraus.getPyoraId() == pyoranID) {
				alkiot.remove(vuokraus);
				muutettu = true;
				return true;
			}
		}
		return false;		
	}
	
	
	/**
	 * Testiohjelma vuokrauksille
	 *
	 * @param args ei käytösä 
	 * @throws SailoException 
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException 
	 * String nimi = "vuokrauksetTesti";
	 * Vuokraukset vuokraukset = new Vuokraukset();
	 * vuokraukset.lueTiedostosta(nimi); #THROWS SailoException 
	 * Vuokraus t1 = new Vuokraus();
	 * Vuokraus t2 = new Vuokraus();
	 * t1.testiVuokraus(1, 2);
	 * t2.testiVuokraus(2, 5);
	 * t1.rekisteroi();
	 * t2.rekisteroi();
	 * vuokraukset.lisaa(t1);
	 * vuokraukset.lisaa(t2);
	 * vuokraukset.tallenna();
	 * vuokraukset = new Vuokraukset();
	 * vuokraukset.lueTiedostosta(nimi);
	 * vuokraukset.getLkm() === 2;
	 * vuokraukset.poista(1);
	 * vuokraukset.getLkm() === 1;
	 * </pre>
	 */
	public static void main(String[] args) throws SailoException {
		Vuokraukset vuokraukset = new Vuokraukset();
		Vuokraus t1 = new Vuokraus();
		Vuokraus t2 = new Vuokraus();
		t1.testiVuokraus(1, 2);
		t2.testiVuokraus(2, 5);
		t1.rekisteroi();
		t2.rekisteroi();
		
		System.out.println("============= Pyörät testi =================");
		String nimi = "vuokrauksetTesti";
		
		try {
			vuokraukset.lueTiedostosta(nimi);
		} catch (SailoException e) {
			//e.printStackTrace();
			System.out.println(e.getMessage());
		}
		vuokraukset.lisaa(t1);
		vuokraukset.lisaa(t2);

		try {
			vuokraukset.tallenna();
		} catch (SailoException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		System.out.println("Vuokrauksia on : " + vuokraukset.getLkm());
		
		Iterator<Vuokraus> iter = vuokraukset.iterator();
		while (iter.hasNext()) System.out.println(iter.next());
		
		System.out.println("Poistetaan vuokraus pyörältä jonka id on 1");
		
		vuokraukset.poista(1);
		System.out.println("Vuokrauksia on : " + vuokraukset.getLkm());
		
		iter = vuokraukset.iterator();		
		while (iter.hasNext()) System.out.println(iter.next());
		
	}
}
