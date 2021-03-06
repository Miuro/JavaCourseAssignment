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

/**
 * Tietorakenne vuokrauksille. Sis�lt�� metodit mm. vuokraukset.dat-tiedoston tallenukselle.
 * @author Jouko Sirkka, Miro Korhonen
 * @version 1.0, 15.5.2018
 */
public class Vuokraukset implements Iterable<Vuokraus> {
	
	private String tiedostonPerusNimi = "vuokraukset";
	private final Collection<Vuokraus> alkiot = new ArrayList<>();
	private boolean muutettu = false;
	
	
	/**
	 * Lis�� uuden asiakkaan tietorakenteeseensa. Ottaa vuokrauksen omistukseensa
	 * @param vuokraus lis�tt�v�n vuokrauksen viite. Huom. tietorakenne muuttuu omistajaksi
	 * @example
	 * <pre name="test">
     * #THROWS SailoException
     * Vuokraukset vuokraukset = new Vuokraukset();
     * Vuokraus v1 = new Vuokraus(), v2 = new Vuokraus();
     * v1.rekisteroi();
     * v2.rekisteroi();
     * vuokraukset.lisaa(v1);
     * vuokraukset.lisaa(v2);
     * vuokraukset.tallenna();
     * int a = v1.getVuokrausId();
     * int b = v2.getVuokrausId();
     * b - a === 1;
	 * </pre>
	 */
	public void lisaa(Vuokraus vuokraus) {
		//vuokraus.rekisteroi();
		alkiot.add(vuokraus);
		muutettu = true;
	}
	
	/**
	 * Etsii py�r�n vuokrauksen
	 * @param pyoraID vuokratun py�r�n tunnusluku
	 * @return l�ytynyt vuokraus, tai null jos ei l�ytynyt
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
	 * @throws SailoException jos luku ep�onnistuu.
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
			throw new SailoException("Tiedostoa " + getTiedostonNimi() + " ei l�ytynyt.");
		} catch (IOException e) {
			throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
		}
	}
	
	
	/**
	 * Tallettaa vuokraukset tiedostoon
	 * 
	 * @throws SailoException jos tiedoston luku tai kirjoitus ep�onnistuu.
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
	 * Palauttaa vuokrausten lukum��r�n
	 * 
	 * @return vuokrausten lukum��r�
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
	 * Palauttaa tiedoston nimen jota k�ytet��n tallennukseen ilman p��tett�
	 * 
	 * @return tallennustiedoston nimi
	 */
	public String getTiedostonPerusNimi() {
		return this.tiedostonPerusNimi;
	}
	
	
	/**
	 * Palauttaa tiedoston nimen jota k�ytet��n tallennukseen (.dat)
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
	 * @param vuokrauksenId Halutun vuokrauksen id
	 * @return vuokr haluttu vuokraus
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
	 * t.anna(v1.getVuokrausId()) === v1;
	 * t.anna(v2.getVuokrausId()) === v2;
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
	 * Poistaa vuokrauksen sen py�r�ID:n mukaan
	 * @param pyoranID Py�r�, jonka vuokraus poistetaan
	 * @return True, jos poisto onnistui
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException
     * Vuokraukset vuokraukset = new Vuokraukset();
     * Vuokraus v1 = new Vuokraus(), v2 = new Vuokraus();
     * v1.rekisteroi();
     * v1.testiVuokraus(3,1000);
     * v2.rekisteroi();
     * vuokraukset.lisaa(v1);
     * vuokraukset.lisaa(v2);
     * vuokraukset.getLkm() === 2;
     * vuokraukset.poista(v1.getPyoraId());
     * vuokraukset.getLkm() === 1;
	 * </pre>
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
	 * @param args ei k�yt�s� 
	 * @throws SailoException jos tiedoston luku ep�onnistuu.
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException 
	 * #import java.io.*;
	 * String nimi = "vuokrauksetTesti";
	 * File tied = new File(nimi + ".dat");
	 * tied.delete();
	 * Vuokraukset vuokraukset = new Vuokraukset();
     * try {
     *       vuokraukset.lueTiedostosta(nimi);
     *   } catch (SailoException e) {
     *       //e.printStackTrace();
     *       System.out.println();
     *   }
	 * Vuokraus t1 = new Vuokraus();
	 * Vuokraus t2 = new Vuokraus();
	 * t1.testiVuokraus(1, 2);
	 * t2.testiVuokraus(2, 5);
	 * t1.rekisteroi();
	 * t2.rekisteroi();
	 * vuokraukset.lisaa(t1);
	 * vuokraukset.lisaa(t2);
	 * try {
           vuokraukset.tallenna();
        } catch (SailoException e) {
            System.out.println("paska");
        }
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
		
		System.out.println("============= Py�r�t testi =================");
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
		
		System.out.println("Poistetaan vuokraus py�r�lt� jonka id on 1");
		
		vuokraukset.poista(1);
		System.out.println("Vuokrauksia on : " + vuokraukset.getLkm());
		
		iter = vuokraukset.iterator();		
		while (iter.hasNext()) System.out.println(iter.next());
		
	}
}
