package view;

import fi.jyu.mit.fxgui.Dialogs;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Asiakas;
import model.Pyora;
import model.SailoException;
import model.Vuokraamo;
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
	private Vuokraamo vuokraamo;
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
		if (lueKentat()) {
			vuokraaPainettu = true;
			pyora.setOnkoVarattu(true);
			dialogStage.close();
		}
	}
		
	@FXML
	void handleHintaAikaMuutos() {
		kestoText.setText("Vuokrauksen kesto : " + Double.parseDouble((kestoKentta.getText())) + " tuntia");
		hintaText.setText("Hinta : " + (pyora.getVuokraPerTunti() * Double.parseDouble(kestoKentta.getText())) + "€");
	}


	/**
	 * Lukee kentät, luo asiakkaan niiden arvojen mukaan.
	 * @return
	 */
	private boolean lueKentat() {
		try {
			asiakas.aseta(1, nimiKentta.getText());
			asiakas.aseta(2, hetuKentta.getText());
			asiakas.aseta(3, osoiteKentta.getText());
			asiakas.aseta(4, puhnumKentta.getText());

			vuokraamo.lisaaAsiakas(asiakas);

			vuokraus.aseta(1, Integer.toString(pyora.getPyoranID()));
			vuokraus.aseta(2, Integer.toString(asiakas.getAsiakasId()));
			vuokraus.aseta(3, kestoKentta.getText());
			vuokraus.setPalautusAika(Integer.parseInt(kestoKentta.getText()));
			vuokraus.aseta(5, Double.toString((Double.parseDouble(vuokraus.anna(3)) * pyora.getVuokraPerTunti())));

			vuokraamo.lisaaVuokraus(vuokraus);

		} catch (Exception e) {
			Dialogs.showMessageDialog("Kenttien luvussa onglemia!");
			return false;
		}

		return true;
	}

	/**
	 * Hakee asiakkaan tiedot, ja kirjoittaa ne dialogin kenttiin.
	 */
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


	/**
	 * Asettaa asiakkaan, jota käytetään dialogissa.
	 * Muuttaa Vuokraa-napin Kuittaa vuokraus-napiksi, jos asetettu asiakas on uusi.
	 * @param asiakas Asiakas
	 */
	public void asetaAsiakas(Asiakas asiakas) {
		this.asiakas = asiakas;

		if (pyora.getOnkoVarattu()) {
			taytaAsiakasKentat();

			fxVuokraaButton.setText("Kuittaa vuokraus");
			fxVuokraaButton.setOnAction((event) -> {
				vuokraamo.poistaAsiakas(asiakas);
				vuokraamo.poistaVuokraus(vuokraus);
				pyora.setOnkoVarattu(false);
				vuokraaPainettu = true;
				try {
					vuokraamo.tallenna();
				} catch (SailoException e) {
					Dialogs.showMessageDialog(e.getMessage());
				}
				dialogStage.close();
			});

		}
	}


	/**
	 * Asettaa dialogissa käytettävän vuokraamon.
	 * @param vuokraamo
	 */
	public void asetaVuokraamo(Vuokraamo vuokraamo) {
		this.vuokraamo = vuokraamo;
	}


	/**
	 * Onko vuokraa painiketta painettu, eli onko vuokraus oikein ja mennyt läpi
	 * @return True, jos vuokraus on tapahtunut
	 */
	public boolean onkoOK() {
		return vuokraaPainettu;
	}

}
