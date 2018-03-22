package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Asiakkaat implements Iterable<Asiakas> {

	private static final int 	MAX_ASIAKKAITA 	= 5;
	private String				kerhonNimi		= "";
	private int 				lkm 			= 0;
	private String 				tiedostonNimi 	= "asiakkaat";
	private Asiakas 			alkiot[] 		= new Asiakas[MAX_ASIAKKAITA];
	private boolean				muutettu		= false;
	
	
	// Toteutus iteraattorille ensin
	
	/**
	 * Luokka jäsenten iteroimiselle.
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

        //File fbak = new File(getBakNimi()); // TODO: Lisää backup jutut :D
        File ftied = new File(getTiedostonNimi());
        //fbak.delete(); // if .. System.err.println("Ei voi tuhota");
        //ftied.renameTo(fbak); // if .. System.err.println("Ei voi nimetä");

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            fo.println(getNimi());
            fo.println(alkiot.length);
            for (Asiakas asiakas : this) {
                fo.println(asiakas.toString());
            }
            //} catch ( IOException e ) { // ei heitä poikkeusta
            //  throw new SailoException("Tallettamisessa ongelmia: " + e.getMessage());
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }
	
	
    private String getNimi() {
		return kerhonNimi;
	}



	/**
     * @return Tiedoston nimi.
     */
	private String getTiedostonNimi() {
		return tiedostonNimi;
	}



	/**
	 * Lisää uuden asiakkaan tietorakenteeseensa. Ottaa asiakkaan omistukseensa
	 * @param asiakas lisättävän asiakkaan viite. Huom. tietorakenne muuttuu omistajaksi
	 * @throws SailoException jos tietorakenne on jo täynnä
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
	 * Lukee asiakkaat tiedostosta. Kesken!
	 * @param hakemisto tiedoston hakemisto
	 * @throws SailoException jos lukeminen epäonnistuu
	 */
	public void lueTiedostosta(String hakemisto) throws SailoException {
		tiedostonNimi = hakemisto + "/asiakkaat.dat";
		throw new SailoException("Ei osata vielä lukea tiedostoa " + tiedostonNimi);
	}
	
	
	/**
	 * Tallentaa asiakkaat tiedostoon. Kesken!
	 * @throws SailoException jos talletus epäonnistuu
	 */
	public void talleta() throws SailoException {
		throw new SailoException("Ei osata vielä tallettaa tiedostoa " + tiedostonNimi);
	}
	
	
	/**
	 * Palauttaa asiakkaiden lukumäärän
	 * @return asiakkaiden lukumäärä
	 */
	public int getLkm() {
		return lkm;
	}
	
	
	/**
	 * Testiohjelma asiakkaille
	 * @param args ei käytösä
	 */
	public static void main(String[] args) {
		// TODO: Everything
	}
	
}
