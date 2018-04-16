package model;

import java.io.File;
import java.util.Collection;

public class Vuokraamo {
	private Pyorat pyorat = new Pyorat();
	private Vuokraukset vuokraukset = new Vuokraukset();
	private Asiakkaat asiakkaat = new Asiakkaat();
	
	/**
	 * Lis�� uuden py�r�n py�riin
	 * @param pyora lis�tt�v� py�r�
	 * @throws SailoException jos tietorakenne jo t�ynn�
	 */
	public void lisaaPyora(Pyora pyora) throws SailoException{
		pyorat.lisaa(pyora);
	}
	
	/**
	 * Lis�� vuokrauksen vuokrauksiin
	 * @param vuokraus lis�tt�v� vuokraus
	 */
	public void lisaaVuokraus(Vuokraus vuokraus) {
		Pyora temp = pyorat.anna(vuokraus.getPyoraId());
		if(temp.getOnkoVarattu() == true) return;
		temp.setOnkoVarattu(true);
		vuokraukset.lisaa(vuokraus);
	}
	
	/**
	 * Lis�� asiakkaan asiakkaisiin
	 * @param asiakas lis�tt�v� asiakas
	 * @throws SailoException jos tietorakenne jo t�ynn�
	 */
	public void lisaaAsiakas(Asiakas asiakas) throws SailoException{
		asiakkaat.lisaa(asiakas);
	}
	
	/**
	 * Antaa halutun py�r�n vuokrauksen
	 * @param pyora py�r� jonka vuokrausta halutaan
	 * @return py�r�n vuokraus, null jos ei ole vuokrausta
	 */
	public Vuokraus annaVuokraus(Pyora pyora){
		if(pyora.getOnkoVarattu() == true) {
			Vuokraus v = vuokraukset.etsi(pyora.getPyoranID());
			return v;		
		}
		else 
			return null;
	}
	
	/**
	 * Antaa py�rien lukum��r�n
	 * @return py�rien lukum��r�
	 */
	public int getPyoria() {
		return pyorat.getLkm();
	}
	
	/**
	 * Asettaa tiedostojen nimet ja luo tiedostorakenteen
	 * @param nimi hakemiston nimi
	 */
	public void setTiedosto(String nimi) {
		File dir = new File(nimi);
		dir.mkdirs();
		String hakemistonNimi = "";
		if(!nimi.isEmpty()) hakemistonNimi = nimi + "/";
		pyorat.setTiedostonPerusNimi(hakemistonNimi + "pyorat");
		vuokraukset.setTiedostonPerusNimi(hakemistonNimi + "vuokraukset");
		asiakkaat.setTiedostonPerusNimi(hakemistonNimi + "asiakkaat");
	}

	/**
	 * Luetuttaa kaikki tiedostot
	 * @param nimi tiedoston nimi
	 * @throws SailoException jos tulee poikkeus
	 */
	public void lueTiedostosta(String nimi) throws SailoException{
		pyorat = new Pyorat();
		vuokraukset = new Vuokraukset();
		asiakkaat = new Asiakkaat();
		
		setTiedosto(nimi);
		pyorat.lueTiedostosta();
		vuokraukset.lueTiedostosta();
		asiakkaat.lueTiedostosta();		
	}
	
	
    /** 
     * Palauttaa "taulukossa" hakuehtoon vastaavien py�rien viitteet 
     * @param hakuehto hakuehto  
     * @param k etsitt�v�n kent�n indeksi  
     * @return tietorakenteen l�ytyneist� py�rist� 
     * @throws SailoException Jos jotakin menee v��rin
     */ 
    public Collection<Pyora> etsi(String hakuehto, int k) throws SailoException { 
        return pyorat.etsi(hakuehto, k); 
    } 
	
    /**
     * Tallentaa tiedostot
     * @throws SailoException
     */
	public void tallenna() throws SailoException{
		String virhe = "";
		try {
			pyorat.tallenna();
		} catch (SailoException e) {
			virhe = e.getMessage();
		} 
		try {
			vuokraukset.tallenna();
		} catch (SailoException e) {
			virhe += e.getMessage();
		}
		try {
			asiakkaat.tallenna();
		} catch (SailoException e) {
			virhe += e.getMessage();
		}
		if(!"".equals(virhe)) throw new SailoException(virhe);
	}
	
	/**
	 * Poistaa valitun py�r�n
	 * @param pyoraKohdalla valittu py�r�
	 * @return true jos onnistui, muuten false
	 */
	public boolean poistaPyora(Pyora pyoraKohdalla) {
		
        if ( pyoraKohdalla == null ) return false;
        boolean ret = pyorat.poista(pyoraKohdalla.getPyoranID()); 
        return ret; 
	}
	

	/**
 	* Testi ohjelmaa vuokramolle
 	* @param args ei k�yt�s�
 	*/
	public static void main(String[] args) {
		Vuokraamo testi = new Vuokraamo();
		try {
			Pyora p1 = new Pyora(), p2 = new Pyora();
			p1.rekisteroi();
			p1.vastaaJopo();
			p2.rekisteroi();
			p2.vastaaJopo();
			
		
			testi.lisaaPyora(p1);
			testi.lisaaPyora(p2);
			
			Asiakas a1 = new Asiakas();
			a1.rekisteroi();
			a1.vastaaHessuHopo();
			testi.lisaaAsiakas(a1);
			
			//int id1 = p1.getPyoranID();
			//int i2 = p2.getPyoranID();
			
			Vuokraus v1 = new Vuokraus(10, p1.getVuokraPerTunti(), p1.getPyoranID(),a1.getAsiakasId());
			v1.rekisteroi();
			v1.testiVuokraus(p1.getPyoranID(),5);
			
			
			v1.tulosta(System.out);
			
			
			
			testi.lisaaVuokraus(v1);
			
			testi.tallenna();
			testi.lueTiedostosta("MJVuokraamo");
			
		} catch (SailoException e) {
			System.out.println(e.getMessage());
		}
	}


}
