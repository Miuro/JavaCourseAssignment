package view;

import fi.jyu.mit.fxgui.Dialogs;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Asiakas;
import model.Pyora;
import model.Vuokraus;

public class UusiVuokrausDialogController {

	@FXML
	private TextField nimiKentta;
	@FXML
	private TextField osoiteKentta;
	@FXML
	private TextField hetuKentta;
	@FXML
	private TextField puhnumKentta;
	@FXML
	private TextField pyoraKentta;
	@FXML
	private TextField tuntivuokraKentta;
	@FXML
	private TextField kestoKentta;
	@FXML
	private Text hintaText;
	@FXML
	private Text kestoText;
	@FXML
	private Button fxPeruutaButton;
	@FXML
	private Button fxVuokraaButton;

	private Stage dialogStage;
	private Vuokraus vuokraus;
	private Pyora pyora;
	private Asiakas asiakas;
	private boolean vuokraaPainettu = false;


	/**
	 * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
	}


	/**
	 * Sulkee ikkunan.
	 */
	@FXML
	void handlePeruuta() {
		dialogStage.close();
	}


	/**
	 * Händlää Vuokraa painikkeen
	 */
	@FXML
	void handleVuokraaPyora() {
		/*
		if(pyora.getOnkoVarattu()) {
			taytaAsiakasentat();
		}
		*/
		
		Dialogs.showMessageDialog("Tämä luo vain esimerkkivuokrauksen atm");
		vuokraus.testiVuokraus(pyora.getPyoranID(), 2);
		
		vuokraaPainettu = true;
		dialogStage.close();
		
		/*
		if(syottoTarkistin()) {
			vuokraus.aseta(k, jono)
		}
		*/
	}
	
	private void taytaAsiakasKentat() {
		nimiKentta.setText(asiakas.anna(1));
		hetuKentta.setText(asiakas.anna(2));
		osoiteKentta.setText(asiakas.anna(3));
		puhnumKentta.setText(asiakas.anna(4));
		kestoKentta.setText(vuokraus.anna(3));
		hintaText.setText("Hinta: " + vuokraus.anna(5));
		kestoText.setText("Palautus: " + vuokraus.anna(4));
	}


	/**
	 * Tarkistaa, ovatko kenttiin syötetyt tiedot oikeassa muodossa.
	 * @return True, jos kentät ovat oikein, muuten false.
	 */
	private boolean syottoTarkistin() {
		// TODO Toteutus tälle
		return false;
	}


	/**
	 * Asettaa stageksi ("näyttämöksi") tämän ikkunan.
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}


	/**
	 * Konstruktori taas.
	 */
	public UusiVuokrausDialogController() {
		// voipi olla tyhjää täynnä
	}


	/**
	 * Säädetään näkymään vuokrattavan pyörän tiedot
	 * @param pyora Pyörä, mikä vuokrataan
	 */
	public void asetaPyora(Pyora pyora) {
		this.pyora = pyora;
		
		pyoraKentta.setText(pyora.getNimi());
		tuntivuokraKentta.setText(Double.toString(pyora.getVuokraPerTunti()));
		//hintaText.setText("Hinta: " + (pyora.getVuokraPerTunti() * Double.parseDouble(kestoKentta.getText())));
	}
	
	
	/**
	 * Vuokraus-olio, jota muokataan.
	 * @param vuokraus Vuokrausolio mainApp:sta
	 */
	public void asetaVuokraus(Vuokraus vuokraus) {
		this.vuokraus = vuokraus;
	}
	
	
	public void asetaAsiakas(Asiakas asiakas) {
		this.asiakas = asiakas;
		
		if(pyora.getOnkoVarattu()) {
			taytaAsiakasKentat();
		}
	}
	
	
	/**
	 * Onko vuokraa painiketta painettu, eli onko vuokraus oikein ja mennyt läpi
	 * @return True, jos vuokraus on tapahtunut
	 */
	public boolean onkoOK() {
		return vuokraaPainettu;
	}
	

}
