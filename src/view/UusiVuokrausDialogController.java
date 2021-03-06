package view;

import java.util.List;

import fi.jyu.mit.fxgui.Dialogs;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Asiakas;
import model.Pyora;
import model.Vuokraus;

/**
 * Controller vuokrausikkunalle. Sis�lt�� kent�t, jolla voidaan luoda uusia vuokrauksia, tai poistaa olemassa oleva.
 * @author Jouko Sirkka
 * @version 1.1, 15.5.2018
 */
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
	private int muokkaus = 0; // mit� tehtiin -1 on vuokraus poistettiin, 0 ei mit��n, 1 luotiin uusi vuokraus
	private List<TextField> tekstikentat;


	/**
	 * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		tekstikentat = List.<TextField>of(nimiKentta, osoiteKentta,hetuKentta,puhnumKentta,pyoraKentta,tuntivuokraKentta,kestoKentta);
	}


	/**
	 * Sulkee ikkunan.
	 */
	@FXML
	void handlePeruuta() {
		dialogStage.close();
	}


	/**
	 * H�ndl�� Vuokraa painikkeen
	 */
	@FXML
	void handleVuokraaPyora() {
		if (lueKentat()) {
			muokkaus = 1;
			dialogStage.close();
		}

	}


	/**
	 * H�ndl�� ikkunan hinta ja kesto kohdan tekstien muutokset
	 */
	@FXML
	void handleHintaAikaMuutos() {
		if(kestoKentta.getText().isEmpty()) return;
		kestoText.setText("Vuokrauksen kesto : " + Double.parseDouble((kestoKentta.getText())) + " tuntia");
		hintaText.setText("Hinta : " + (pyora.getVuokraPerTunti() * Double.parseDouble(kestoKentta.getText())) + "�");
	}


	/**
	 * Lukee kent�t, luo asiakkaan niiden arvojen mukaan.
	 * @return
	 */
	private boolean lueKentat() {
		if (tarkastaKentat() == false) {
			return false;
		}
		
		try {
			asiakas.aseta(1, nimiKentta.getText());
			asiakas.aseta(2, hetuKentta.getText());
			asiakas.aseta(3, osoiteKentta.getText());
			asiakas.aseta(4, puhnumKentta.getText());

			vuokraus.aseta(1, Integer.toString(pyora.getPyoranID()));
			vuokraus.aseta(2, Integer.toString(asiakas.getAsiakasId()));
			vuokraus.aseta(3, kestoKentta.getText());
			vuokraus.setPalautusAika(Integer.parseInt(kestoKentta.getText()));
			vuokraus.aseta(5, Double.toString((Double.parseDouble(vuokraus.anna(3)) * pyora.getVuokraPerTunti())));

		} catch (Exception e) {
			Dialogs.showMessageDialog("Tarkasta kent�t " + e.getMessage());
			return false;
		}

		return true;
	}


	/**
	 * Tarkastaa tekstikenttiin sy�t�n oikeellisuuden. Ei hyv�ksy tyhji� kentti� tai
	 * teksti�, joka sis�lt�� '|' merkin.
	 * 
	 * @return True, jos sy�te on hyv�.
	 */
	private boolean tarkastaKentat() {
		String s = "";
		boolean onkoVirheita = false;

		for (TextField textField : tekstikentat) {
			if (textField.getText().contains("|")) {
				s = s + textField.getId() + " ";
				onkoVirheita = true;
			}
		}
		if (onkoVirheita == true) {
			Dialogs.showMessageDialog("Tekstikent�t " + s + "sis�lt�v�t laittoman merkin '|'");
			return false;
		}
		
		s = "";
		for (TextField textField : tekstikentat) {
			if (textField.getText().isEmpty()) {
				s = s + textField.getId() + " ";
				onkoVirheita = true;
			}
		}
		if(onkoVirheita == true) {			
			Dialogs.showMessageDialog("Kent�t " + s + "eiv�t saa olla tyhji�");
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
	 * Asettaa stageksi ("n�ytt�m�ksi") t�m�n ikkunan.
	 * @param dialogStage N�ytt�m�.
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}


	/**
	 * Konstruktori taas.
	 */
	public UusiVuokrausDialogController() {
		// voipi olla tyhj�� t�ynn�
	}


	/**
	 * S��det��n n�kym��n vuokrattavan py�r�n tiedot
	 * @param pyora1 Py�r�, mik� vuokrataan
	 */
	public void asetaPyora(Pyora pyora1) {
		this.pyora = pyora1;

		pyoraKentta.setText(pyora1.getNimi());
		tuntivuokraKentta.setText(Double.toString(pyora1.getVuokraPerTunti()));
	}


	/**
	 * Vuokraus-olio, jota muokataan.
	 * @param vuokraus1 Vuokrausolio mainApp:sta
	 */
	public void asetaVuokraus(Vuokraus vuokraus1) {
		this.vuokraus = vuokraus1;
	}


	/**
	 * Asettaa asiakkaan, jota k�ytet��n dialogissa. Muuttaa Vuokraa-napin Kuittaa vuokraus-napiksi, jos asetettu
	 * asiakas on uusi.
	 * @param asiakas1 Asiakas
	 */
	public void asetaAsiakas(Asiakas asiakas1) {
		this.asiakas = asiakas1;

		if (pyora.getOnkoVarattu()) {
			taytaAsiakasKentat();

			setMuokattavuus(false);

			fxVuokraaButton.setText("Kuittaa vuokraus");
			fxVuokraaButton.setOnAction((event) -> {
				muokkaus = -1;

				dialogStage.close();
			});

		}
	}


	private void setMuokattavuus(boolean b) {
		nimiKentta.setEditable(b);
		osoiteKentta.setEditable(b);
		hetuKentta.setEditable(b);
		puhnumKentta.setEditable(b);
		pyoraKentta.setEditable(b);
		tuntivuokraKentta.setEditable(b);
		kestoKentta.setEditable(b);

	}


	/**
	 * Palauttaa numeron sen mukaan mit� tehtiin
	 * @return -1 jos poistettiin vuokraus, 0 jos ei tehty mit��n, 1 jos luotiin uusi vuokraus
	 */
	public int mitaTehtiin() {
		return muokkaus;
	}

}
