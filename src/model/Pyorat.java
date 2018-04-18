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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Pyorat implements Iterable<Pyora> {
	
	private Collection<Pyora> alkiot = new ArrayList<>();

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
	 * @return py�r�t, jotka vastaavat hakuehtoa
	 */
	public Collection<Pyora> etsi(String hakuehto, boolean vapaanaValittu) {
		String ehto = "*";
		if(hakuehto != null && hakuehto.length() > 0) ehto = hakuehto;
		Collection<Pyora> loytyneet = new ArrayList<>();
		if (vapaanaValittu) {
			for(Pyora pyora : annaVapaat()) {
	        	if (ehto == "*") loytyneet.add(pyora);
	            if (pyora.toStringNOID().toLowerCase().contains(ehto.toLowerCase())) loytyneet.add(pyora);
			}
			return loytyneet;
		}
        for (Pyora pyora: this) { 
        	if (ehto == "*") loytyneet.add(pyora);
            if (pyora.toStringNOID().toLowerCase().contains(ehto.toLowerCase())) loytyneet.add(pyora);  
        }
        return loytyneet; 
    }
	

	
	/**
	 * Lis�� uuden py�r�n tietorakenteeseen. Ottaa py�r�n omistukseensa.
	 * 
	 * @param pyora lis�tt�v�n py�r�n viite. Huom tietorakenne muuttuu omistajaksi
	 * @throws SailoException  jos tietorakenne on jo t�ynn�
	 */
	public void lisaa(Pyora pyora) {
		alkiot.add(pyora);
		muutettu = true;
	}
	
	
	/**
	 * Muuttaa py�r�n toiseksi
	 * @param alku muutettava py�r�
	 * @param uusi py�r� jolla alku korvataan
	 */
	public void muutaPyora(Pyora alku, Pyora uusi) {
		alku = uusi;
		muutettu = true;
	}
	
	/**
	 * Antaa kokoelman kaikista vapaana olevista py�rist�
	 * @return Kaikki vapaana olevat py�r�t
	 */
	public Collection<Pyora> annaVapaat(){
		Collection<Pyora> vapaat = new ArrayList<>();
		for(Pyora pyora : alkiot) {
			if(!pyora.getOnkoVarattu()) vapaat.add(pyora);
		}
		return vapaat;
	}

	
	/**
	 * Palauttaa viitteen haluttuun py�r��n
	 * 
	 * @param i Halutun py�r�n ID
	 * @return viite py�r��n, jonka ID on annettu ID
	 */
	public Pyora anna(int i) throws IndexOutOfBoundsException {
		for (Pyora pyora : alkiot) {
			if(pyora.getPyoranID() == i) {
				return pyora;
			}
		}
 		return null;
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
			throw new SailoException("Tiedostoa " + getTiedostonNimi() + " ei l�ytynyt. Luodaan uusi.");
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
	 * Palauttaa kerhon py�rien lukum��r�n
	 * 
	 * @return py�rien lukum��r�
	 */
	public int getLkm() {
		return alkiot.size();
	}
	


	
	/**
	 * Poistaa py�r�n ID:n mukaan
	 * @param pyoranID poistettavan py�r�n id
	 * @return True jos poistettiin
	 * 	 */
	public boolean poista(int pyoranID) {
		
        Pyora p = anna(pyoranID);
        if(p == null)
        	return false;
        else
        	alkiot.remove(p);
        
        
        muutettu = true; 
        return true;
	}

	/**
	 * Palauttaa kopion listasta, jossa py�r�t ovat hinnan mukaan kasvavassa j�rjestyksess�
	 * @return Sama lista py�ri�, mutta j�rjestyksess�
	 */
	public Collection<Pyora> jarjestaHalvin(Collection<Pyora> lista) {
		Collection<Pyora> jarjestetty = new ArrayList<>();
		Collection<Pyora> temp = new ArrayList<>();
		
		for (Pyora pyora : temp) System.out.println(pyora);
		
		for (Pyora pyora : lista) {
			Pyora asd = new Pyora();
			asd.parse(pyora.toString());
			temp.add(asd);
		}
		
		while(temp.size() > 0) {
			Pyora halvin =  temp.iterator().next();
			for (Pyora pyora : temp) {
				if(Double.compare(halvin.getVuokraPerTunti(), pyora.getVuokraPerTunti()) > 0) {
					 halvin = pyora;
				}
			}
			jarjestetty.add(halvin);
			temp.remove(halvin);
		}
		return jarjestetty;
	}

	/**
	 * Testataan toimivuutta
	 * 
	 * @param args
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException
	 * Pyorat pyorat = new Pyorat();
	 * Pyora jopo1 = new Pyora(), jopo2 = new Pyora();
	 * jopo1.rekisteroi();
	 * jopo1.vastaaJopo();
	 * jopo2.rekisteroi();
	 * jopo2.vastaaJopo();
	 * String nimi = "pyoratTesti";
	 * pyorat.lueTiedostosta(nimi); #THROWS SailoException
	 * pyorat.lisaa(jopo1);
	 * pyorat.lisaa(jopo2);
	 * pyorat.tallenna();
	 * pyorat.getLkm() === 2;
	 * pyorat.poista(1);
	 * pyorat.getLkm() === 1;
	 * </pre>
	 */
	public static void main(String[] args) {
		Pyorat pyorat = new Pyorat();

		Pyora jopo1 = new Pyora(), jopo2 = new Pyora(), jopo3 = new Pyora();
		jopo1.rekisteroi();
		jopo1.vastaaJopo();
		jopo2.rekisteroi();
		jopo2.vastaaJopo();
		jopo3.vastaaJopo();
		jopo3.rekisteroi();
		jopo2.aseta(4, "15");
		jopo3.aseta(4, "20");
		
		
		pyorat.lisaa(jopo3);
		pyorat.lisaa(jopo1);
		pyorat.lisaa(jopo2);
		
		//pyorat.jarjestaHalvin();
		
		for (Pyora pyora : pyorat) {
			System.out.println(pyora);
		}

		/*
		System.out.println("============= Py�r�t testi =================");
		String nimi = "pyoratTesti";
		File ftied = new File(nimi+ ".dat");
		ftied.delete();
		
		try {
			pyorat.lueTiedostosta(nimi);
		} catch (SailoException e) {
			//e.printStackTrace();
			System.out.println(e.getMessage());
		}
		pyorat.lisaa(jopo1);
		pyorat.lisaa(jopo2);

		try {
			pyorat.tallenna();
		} catch (SailoException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		System.out.println("Py�ri� on : " + pyorat.getLkm());
		
		Iterator<Pyora> iter = pyorat.iterator();
		while (iter.hasNext()) System.out.println(iter.next());
		
		System.out.println("Poistetaan py�r� jonka id on 1");
		
		pyorat.poista(1);
		System.out.println("Py�ri� on : " + pyorat.getLkm());
		
		iter = pyorat.iterator();		
		while (iter.hasNext()) System.out.println(iter.next());
		*/
		
		
	}




	/**
	 * Palauttaa iteraattorin tietorakenteelle
	 */
	@Override
	public Iterator<Pyora> iterator() {
		return alkiot.iterator();
	}
	
}