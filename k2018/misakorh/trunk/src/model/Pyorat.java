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
 * Tietorakenne pyörille. Sisältää metodit mm. pyörän tallenukselle.
 * @author Jouko Sirkka, Miro Korhonen
 * @version 1.0, 15.5.2018
 */
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
	 * Asetin muutettu-fieldille
	 * @param arvo Uusi arvo
	 */
	public void setMuutettu(boolean arvo) {
		muutettu = arvo;
	}
	
	/**
	 * Palauttaa listan hakuehtoon vastaavien pyörien viitteet
	 * @param hakuehto hakuehto
	 * @param vapaanaValittu Onko checkbox valittu.
	 * @return pyörät, jotka vastaavat hakuehtoa
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
	 * Lisää uuden pyörän tietorakenteeseen. Ottaa pyörän omistukseensa.
	 * 
	 * @param pyora lisättävän pyörän viite. Huom tietorakenne muuttuu omistajaksi
	 */
	public void lisaa(Pyora pyora) {
		//if(pyora.getPyoranID() == 0) {
		//	pyora.rekisteroi();			
		//}
		//pyora.rekisteroi();
		alkiot.add(pyora);
		muutettu = true;
	}
	
	
	/**
	 * Muuttaa pyörän toiseksi
	 * @param alku muutettava pyörä
	 * @param uusi pyörä jolla alku korvataan
	 */
	/*
	public void muutaPyora(Pyora alku, Pyora uusi) {
		alku = uusi;
		muutettu = true;
	}
	*/
	
	
	/**
	 * Antaa kokoelman kaikista vapaana olevista pyöristä
	 * @return Kaikki vapaana olevat pyörät
	 */
	public Collection<Pyora> annaVapaat(){
		Collection<Pyora> vapaat = new ArrayList<>();
		for(Pyora pyora : alkiot) {
			if(!pyora.getOnkoVarattu()) vapaat.add(pyora);
		}
		return vapaat;
	}

	
	/**
	 * Palauttaa viitteen haluttuun pyörään
	 * 
	 * @param i Halutun pyörän ID
	 * @return viite pyörään, jonka ID on annettu ID
	 * @throws IndexOutOfBoundsException jos ylitetään rajat.
	 * <pre name="test">
	 * Pyorat pyorat = new Pyorat();
	 * Pyora p = new Pyora();
	 * Pyora q = new Pyora();
	 * p.vastaaJopo();
	 * q.vastaaJopo2();
	 * p.setPyoranID(8);
	 * pyorat.lisaa(p);
	 * pyorat.lisaa(q);
	 * pyorat.anna(8) === p;
	 * p.setPyoranID(10);
	 * pyorat.anna(8) === null;
	 * </pre>
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
	 * Lukee pyörät tiedostosta
	 * 
	 * @param tied tiedoston nimi
	 * @throws SailoException jos luku epäonnistuu.
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
	 * Poistaa pyörän ID:n mukaan
	 * @param pyoranID poistettavan pyörän id
	 * @return True jos poistettiin
	 * 	 */
	public boolean poista(int pyoranID) {
		
        Pyora p = anna(pyoranID);
        if(p == null)
        	return false;
        alkiot.remove(p);
        
        
        muutettu = true; 
        return true;
	}

	/**
	 * Palauttaa kopion listasta, jossa pyörät ovat hinnan mukaan kasvavassa järjestyksessä
	 * @param lista lista pyöristä, mistä haetaan.
	 * @return Sama lista pyöriä, mutta järjestyksessä
	 * @throws SailoException jos tapahtuu virhe pyörien luvussa.
	 */
	public Collection<Pyora> jarjestaHalvin(Collection<Pyora> lista) throws SailoException {
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
	 * Palauttaa iteraattorin tietorakenteelle
	 */
	@Override
	public Iterator<Pyora> iterator() {
		return alkiot.iterator();
	}
	
	
	/**
	 * Testataan toimivuutta
	 * 
	 * @param args Ei käytössä
	 * @throws SailoException Jos tiedoston luku epäonnistuu.
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
	public static void main(String[] args) throws SailoException {
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
		
		
		for (Pyora pyora : pyorat) System.out.println(pyora);
		
		jopo1.setOnkoVarattu(true);
		System.out.println();
		for (Pyora pyora : pyorat) System.out.println(pyora);

		/*
		System.out.println("============= Pyörät testi =================");
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
		System.out.println("Pyöriä on : " + pyorat.getLkm());
		
		Iterator<Pyora> iter = pyorat.iterator();
		while (iter.hasNext()) System.out.println(iter.next());
		
		System.out.println("Poistetaan pyörä jonka id on 1");
		
		pyorat.poista(1);
		System.out.println("Pyöriä on : " + pyorat.getLkm());
		
		iter = pyorat.iterator();		
		while (iter.hasNext()) System.out.println(iter.next());
		*/
	}




	
}