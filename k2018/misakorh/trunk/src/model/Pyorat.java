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

public class Pyorat implements Iterable<Pyora> {
	
	private Collection<Pyora> alkiot = new ArrayList<>();

	private String tiedostonPerusNimi = "";
	private boolean muutettu = false;
	
	/**
	 * Oletusmuodostaja
	 */
	public Pyorat() {
		// Attribuuttien oma alustus riittää
	}
	
	
	/**
	 * Palauttaa listan hakuehtoon vastaavien pyörien viitteet
	 * @param hakuehto hakuehto
	 * @param k haettavan kentän indeksi
	 * @return pyörät, jotka vastaavat hakuehtoa
	 */
	public Collection<Pyora> etsi(String hakuehto, int k) {
		Collection<Pyora> loytyneet = new ArrayList<>(); 
        for (Pyora pyora: alkiot) { 
            loytyneet.add(pyora);  
        }
        return loytyneet; 
    }
	

	
	/**
	 * Lisää uuden pyörän tietorakenteeseen. Ottaa pyörän omistukseensa.
	 * 
	 * @param pyora lisättävän pyörän viite. Huom tietorakenne muuttuu omistajaksi
	 * @throws SailoException  jos tietorakenne on jo täynnä
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
		muutettu = true;
	}

	
	/**
	 * Palauttaa viitteen i:teen pyörään.
	 * 
	 * @param i monennenko pyörän viite halutaan
	 * @return viite pyörään, jonka indeksi on i
	 */
	public Pyora anna(int i) throws IndexOutOfBoundsException {
		//if (i < 0 || alkiot.size() <= i)
		//	throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
		//return alkiot.get(i);
		
		
		for (Pyora pyora : alkiot) {
			if(pyora.getPyoranID() == i) {
				return pyora;
			}
		}
 		return null;
	}

	
	/**
	 * Lukee pyörät tiedostosta
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
				Pyora pyora = new Pyora();
				pyora.parse(rivi);
				lisaa(pyora);
			}
			muutettu = false;
			
		} catch (FileNotFoundException e) {
			throw new SailoException("Tiedostoa " + getTiedostonNimi() + " ei löytynyt. Luodaan uusi.");
		} catch (IOException e) {
			throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
		}
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
	 * Tallentaa pyörät tiedostoon
	 * 
	 * @throws SailoException jos talletus epäonnistuu
	 */
	public void tallenna() throws SailoException {
		if (!muutettu) return;
		
		File fbak = new File(getBakNimi());
		File ftied = new File(getTiedostonNimi());
		fbak.delete();
		ftied.renameTo(fbak);
		Pyora temp;
		
		try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
			for (Pyora pyora: this) {
				fo.println(pyora.toString());
			
			}
		} catch (FileNotFoundException e) {
			throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
		} catch (IOException e) {
			throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
		}
		muutettu = false;
		
	}

	/**
	 * Palauttaa kerhon pyörien lukumäärän
	 * 
	 * @return pyörien lukumäärä
	 */
	public int getLkm() {
		return alkiot.size();
	}

	
	/**
	 * Poistaa halutun pyörän
	 * @param pyoranID halutun pyörän tunnusluku
	 * @return 0 jos ei löydy, 1 jos onnistui
	 */
	public int poista(int pyoranID) {
        int ind = etsiId(pyoranID); 
        if (ind < 0) return 0; 
        alkiot.remove(ind);
        muutettu = true; 
        return 1;
	}

	/**
	 * Etsii halutun pyörän indeksin
	 * @param pyoranID halutun pyörän tunnusluku
	 * @return indeksin numero jos onnistui, -1 jos ei
	 */
	private int etsiId(int pyoranID) {
		for (Pyora pyora : alkiot) {
			if(pyora.getPyoranID() == pyoranID)
				return pyora.getPyoranID();
		}
		return -1;
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

	}


	@Override
	public Iterator<Pyora> iterator() {
		return alkiot.iterator();
	}
	
}