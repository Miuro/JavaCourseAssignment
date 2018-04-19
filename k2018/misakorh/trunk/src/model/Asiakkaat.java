package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Asiakkaat implements Iterable<Asiakas> {

	private static final int 	MAX_ASIAKKAITA 	= 5;
	private int 				lkm 			= 0;
	private String 				tiedostonPerusNimi 	= "asiakkaat";
	private Asiakas 			alkiot[] 		= new Asiakas[MAX_ASIAKKAITA];
	private boolean				muutettu		= false;
	
	
	// Toteutus iteraattorille ensin
	
	/**
	 * Luokka j�senten iteroimiselle.
	 */
	public class AsiakkaatIterator implements Iterator<Asiakas>{
		private int kohdalla = 0;
		
		@Override
		public boolean hasNext() {
			return kohdalla < lkm;
		}

		
		/**
		 * Palauttaa seuraavan asiakkaan.
		 * @return Seuraava asiakas, jos sellainen on olemassa.
		 */
		@Override
		public Asiakas next() throws NoSuchElementException {
			if ( !hasNext() ) throw new NoSuchElementException("Seuraavaa asiakasta ei ole");
			return anna(kohdalla++);
		}
		
	}
	
	/**
	 * Palauttaa iteraattorin asiakkaille.
	 */
	public Iterator<Asiakas> iterator() {
		return new AsiakkaatIterator();
	}
	
	
	
	/**
	 * Oletusmuodostaja
	 */
	public Asiakkaat() {
		//
	}
	
	
    /**
     * Tallentaa asiakkaan tiedostoon.  
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;

        File fbak = new File(getBakNimi()); // TODO: Lis�� backup jutut :D
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); // if .. System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); // if .. System.err.println("Ei voi nimet�");

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            //fo.println(getNimi());
            //fo.println(alkiot.length);  Eik� n�ihin riit� vaan et tulostetaan ne asiakkaan tiedot
            for (Asiakas asiakas : this) {
                fo.println(asiakas.toString());
            }
            
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }



	/**
	 * Palauttaa tiedoston nimen jota k�ytet��n tallennukseen
	 * 
     * @return Tiedoston nimi.
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
		return this.tiedostonPerusNimi + ".bak";
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
	 * Asettaa tiedoston nimen ilman p��tett�
	 * 
	 * @param tied haluttu tiedoston nimi
	 */
	public void setTiedostonPerusNimi(String tied) {
		this.tiedostonPerusNimi = tied;
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
	 * Lukee asiakkaat tiedostosta
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
				Asiakas asiakas = new Asiakas();
				asiakas.parse(rivi);
				lisaa(asiakas);
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