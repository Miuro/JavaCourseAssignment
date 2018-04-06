package model;

import java.io.File;
import java.util.Collection;

public class Vuokraamo {
	private Pyorat pyorat = new Pyorat();
	private Vuokraukset vuokraukset = new Vuokraukset();
	private Asiakkaat asiakkaat = new Asiakkaat();
	
	public void lisaaPyora(Pyora pyora) throws SailoException{
		pyorat.lisaa(pyora);
	}
	
	
	public void lisaaVuokraus(Vuokraus vuokraus) throws SailoException {
		vuokraukset.lisaa(vuokraus);
	}
	
	
	public void lisaaAsiakas(Asiakas asiakas) throws SailoException{
		asiakkaat.lisaa(asiakas);
	}
	
	public Vuokraus annaVuokraus(Pyora pyora) throws SailoException {
		return vuokraukset.annaVuokraus(pyora.getPyoranID());
	}
	
	public int getPyoria() {
		return pyorat.getLkm();
	}
	
	public void setTiedosto(String nimi) {
		File dir = new File(nimi);
		dir.mkdirs();
		String hakemistonNimi = "";
		if(!nimi.isEmpty()) hakemistonNimi = nimi + "/";
		pyorat.setTiedostonPerusNimi(hakemistonNimi + "pyorat");
		vuokraukset.setTiedostonPerusNimi(hakemistonNimi + "vuokraukset");
		asiakkaat.setTiedostonPerusNimi(hakemistonNimi + "asiakkaat");
	}

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
     * Palauttaa "taulukossa" hakuehtoon vastaavien pyörien viitteet 
     * @param hakuehto hakuehto  
     * @param k etsittävän kentän indeksi  
     * @return tietorakenteen löytyneistä pyöristä 
     * @throws SailoException Jos jotakin menee väärin
     */ 
    public Collection<Pyora> etsi(String hakuehto, int k) throws SailoException { 
        return pyorat.etsi(hakuehto, k); 
    } 
	
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
	
	public int poistaPyora(Pyora pyoraKohdalla) {
		
        if ( pyoraKohdalla == null ) return 0;
        int ret = pyorat.poista(pyoraKohdalla.getPyoranID()); 
        return ret; 
	}
	
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
			
			//int id1 = p1.getPyoranID();
			//int i2 = p2.getPyoranID();
			
			Asiakas a1 = new Asiakas();
			a1.vastaaHessuHopo();
			a1.rekisteroi();
			
			Vuokraus v1 = new Vuokraus();
			v1.testiVuokraus(5);
			
			testi.lueTiedostosta("Testi");
			
		} catch (SailoException e) {
			System.out.println(e.getMessage());
		}
	}


}
