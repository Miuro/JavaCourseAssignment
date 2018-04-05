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

public class Pyorat {
	
	private ArrayList<Pyora> alkiot = new ArrayList<>();

	private String tiedostonPerusNimi = "";
	private boolean muutettu = false;
	
	/**
	 * Oletusmuodostaja
	 */
	public Pyorat() {
		// Attribuuttien oma alustus riitt��
	}
	
	
	/**
	 * Palauttaa listan hakuehtoon vastaavien py�rien viitteet
	 * @param hakuehto hakuehto
	 * @param k haettavan kent�n indeksi
	 * @return py�r�t, jotka vastaavat hakuehtoa
	 */
	public Collection<Pyora> etsi(String hakuehto, int k) {
		Collection<Pyora> loytyneet = new ArrayList<>(); 
        for (Pyora pyora: alkiot) { 
            loytyneet.add(pyora);  
        } 
        return loytyneet; 
    }
	

	
	/**
	 * Lis�� uuden py�r�n tietorakenteeseen. Ottaa py�r�n omistukseensa.
	 * 
	 * @param pyora lis�tt�v�n py�r�n viite. Huom tietorakenne muuttuu omistajaksi
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
		muutettu = true;
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
	 * Lukee py�r�t tiedostosta
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
			throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
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
		tiedostonPerusNimi = tied;
	}
	
	
	/**
	 * Palauttaa tiedoston nimen jota k�ytet��n tallennukseen ilman p��tett�
	 * 
	 * @return tallennustiedoston nimi
	 */
	public String getTiedostonPerusNimi() {
		return tiedostonPerusNimi;
	}
	
	
	/**
	 * Palauttaa tiedoston nimen jota k�ytet��n tallennukseen (.dat)
	 * 
	 * @return tiedoston nimi + .dat
	 */
	public String getTiedostonNimi() {
		return tiedostonPerusNimi + ".dat";
	}
	
	
	/**
	 * Palauttaa varakopiotiedoston nimen
	 * 
	 * @return varakopiotiedoston nimi
	 */
	public String getBakNimi() {
		return tiedostonPerusNimi + ".bak";
	}

	
	/**
	 * Tallentaa py�r�t tiedostoon
	 * 
	 * @throws SailoException jos talletus ep�onnistuu
	 */
	public void tallenna() throws SailoException {
		if (!muutettu) return;
		
		File fbak = new File(getBakNimi());
		File ftied = new File(getTiedostonNimi());
		fbak.delete();
		ftied.renameTo(fbak);
		
		try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
			for(int i = 0; i < getLkm(); i++) {
				fo.println(anna(i).toString());
			}
		} catch (FileNotFoundException e) {
			throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
		} catch (IOException e) {
			throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
		}
		
		muutettu = false;
		
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
