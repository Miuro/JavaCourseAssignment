package model;

public class Asiakkaat {

	private static final int 	MAX_ASIAKKAITA 	= 5;
	private int 				lkm 			= 0;
	private String 				tiedostonNimi 	= "";
	private Asiakas 			alkiot[] 		= new Asiakas[MAX_ASIAKKAITA];
	
	/**
	 * Oletusmuodostaja
	 */
	public Asiakkaat() {
		//
	}
	
	/**
	 * Lis�� uuden asiakkaan tietorakenteeseensa. Ottaa asiakkaan omistukseensa
	 * @param asiakas lis�tt�v�n asiakkaan viite. Huom. tietorakenne muuttuu omistajaksi
	 * @throws SailoException jos tietorakenne on jo t�ynn�
	 */
	public void lisaa(Asiakas asiakas) throws SailoException {
		if(lkm >= alkiot.length) throw new SailoException("Liikaa alkioita");
		alkiot[lkm] = asiakas;
		lkm++;
	}
	
	public static void main(String[] args) {
		// TODO: Everything
	}
}
