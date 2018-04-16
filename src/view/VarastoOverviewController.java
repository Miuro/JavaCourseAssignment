package view;

import pyoravarasto.MainApp;

import java.io.PrintStream;
import java.util.Collection;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Asiakas;
import model.Pyora;
import model.SailoException;
import model.Vuokraus;
import model.Vuokraamo;

public class VarastoOverviewController {
	
	@FXML 
	private ComboBoxChooser<String> cbKentat;

	@FXML
	private MenuItem fxMenuTallenna;
	
	@FXML 
	private Label labelVirhe;
	
	@FXML private TextField hakuehto;

	@FXML
	private MenuItem fxMenuAvaa;

	@FXML
	private MenuItem fxMenuTulosta;

	@FXML
	private MenuItem fxMenuLopeta;

	@FXML
	private MenuItem fxMenuUusiPyora;

	@FXML
	private MenuItem fxMenuPoistaPyora;

	@FXML
	private MenuItem fxMenuApua;

	@FXML
	private MenuItem fxMenuTietoja;

	@FXML
	private Button fxUusiPyoraButton;

	@FXML
	private ScrollPane panelPyora;

	@FXML
	private Button fxTallennaButton;

	@FXML
	private Button fxVuokraaButton;

	@FXML
	private CheckBox fxVainVapaatCB;

	@FXML
	private Button fxPoistaButton;

	@FXML
	private ListChooser<Pyora> fxChooserPyorat;

	// Reference to the main application. En ole ihan varma mit� t�m� tekee.
	private MainApp mainApp;


	/**
	 * Konstruktori T�t� vissiin kutsutaan kun tapahtuu automaattinen initialize() metodi fxml-tiedoston ladattua. Dunno
	 * tbh
	 */
	public VarastoOverviewController() {

	}
	
	/**
	 * Alustaa
	 */
	@FXML
    private void initialize() {
        alusta();
    }


	/**
	 * T�t� kutsutaan MainApp:sta. M��ritt��, ett� t�m� on perusn�kym�-
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	/**
	 * Hakuehdon h�ndl�ys
	 */
	@FXML
	void handleHakuehto() {
        if ( pyoraKohdalla != null )
            hae(pyoraKohdalla.getPyoranID()); 
	}


	/**
	 * H�ndl�� Apua painikkeen
	 */
	@FXML
	void handleAvaaApua() {
		Dialogs.showMessageDialog("Ei ole viel� lis�tty");
	}


	/**
	 * H�ndl�� Tietoja painikkeen
	 */
	@FXML
	void handleAvaaTietoja() {
		Dialogs.showMessageDialog("Py�r�Varasto\nVer. 0.6\nTekij�t: Jouko Sirkka & Miro Korhonen");
	}


	/**
	 * H�ndl�� Avaa -painikkeen toiminnan
	 */
	@FXML
	void handleMenuAvaa() {
		avaa();
	}


	/**
	 * H�ndl�� lopeta painikkeen
	 */
	@FXML
	void handleMenuLopeta() {
		Dialogs.showMessageDialog("Ei ole viel� lis�tty");
	}


	/**
	 * H�ndl�� tulosta-painikkeen
	 */
	@FXML
	void handleMenuTulosta() {
		//Dialogs.showMessageDialog("Ei ole viel� lis�tty");
		mainApp.showUusiTulostusDialog();
	}


	/**
	 * H�ndl�� poistapy�r� painikkeen
	 */
	@FXML
	void handlePoistaPyora() {
		poistaPyora();
	}


	/**
	 * H�ndl�� tallennus painikkeen
	 */
	@FXML
	void handleTallenna() {
		tallenna();
	}


	/**
	 * H�ndl�� uusipy�r� painikkeen
	 */
	@FXML
	void handleUusiPyora() {
		uusiPyora();
	}


	/**
	 * Avataan vuokrausikkuna.
	 * @throws SailoException 
	 */
	@FXML
	void handleUusiVuokraus() throws SailoException {
		//mainApp.showUusiVuokrausDialog();
		vuokraaPyora(60);
	}

	//===============================================================================
	// FXML:ll��n kuulumaton koodi t�st� eteenp�in

	private String vuokraamonNimi = "MJVuokraamo";
	private Vuokraamo vuokraamo;
	private Pyora pyoraKohdalla;
	private Asiakas apuAsiakas;
	private Vuokraus apuVuokraus;
	private TextArea areaPyora = new TextArea();
	private TextField[] muokkaukset;
	private int kentta = 0;

	/**
	 * Alustetaan
	 */
	protected void alusta() {
		panelPyora.setContent(areaPyora);
		panelPyora.setFitToHeight(true);
		fxChooserPyorat.clear();
		fxChooserPyorat.addSelectionListener(e -> naytaPyora());
		setVuokraamo(new Vuokraamo());
		avaa();
	}

	/**
	 * N�ytt�� listasta valitun j�senen tiedot, tilap�isesti yhteen isoon edit-kentt��n
	 */
	protected void naytaPyora() {
		pyoraKohdalla = fxChooserPyorat.getSelectedObject();

		if (pyoraKohdalla == null) {
			areaPyora.clear();
			return;
		}

		areaPyora.setText("");
		try (PrintStream os = TextAreaOutputStream.getTextPrintStream(areaPyora)) {
			tulosta(os, pyoraKohdalla);
		}
	}

	
	/**
	 * Alustaa vuokraamon lukemalla sen valitun nimisest� tiedostosta
	 * @param nimi tiedosto josta vuokraamon tiedot luetaan
	 * @return null jos onnistuu, muuten virhe tekstin�
	 */
	protected String lueTiedosto(String nimi) {
		vuokraamonNimi = nimi;
		try {
			vuokraamo.lueTiedostosta(nimi);
			hae(0);
			return null;
		} catch (SailoException e) {
			hae(0);
			String virhe = e.getMessage();
			if (virhe != null) Dialogs.showMessageDialog(virhe);
			return virhe;
		}
	}
	
    /**
     * Kysyt��n tiedoston nimi ja luetaan se
     * @return true jos onnistui, false jos ei
     */
    public boolean avaa() {
    	//String uusiNimi = ""; // TODO dialog joka kysyy nime�
        //if(uusiNimi == null) return false;
        lueTiedosto(vuokraamonNimi);
        return true;
    }
	
    
    /**
     * Tietojen tallennus
     * @return null jos onnistuu, muuten virhe tekstin�
     */
    private String tallenna() {
        try {
            vuokraamo.tallenna();
            return null;
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + ex.getMessage());
            return ex.getMessage();
        }
    }
    
    /**
     * Tarkistetaan onko tallennus tehty
     * @return true jos saa sulkea sovelluksen, false jos ei
     */
    public boolean voikoSulkea() {
        tallenna();
        return true;
    }

    
	/**
	 * Luo uuden j�senen jota aletaan editoimaan
	 */
	protected void uusiPyora() {
		Pyora uusi = new Pyora();
		uusi.rekisteroi();
		uusi.vastaaJopo();

		try {
			vuokraamo.lisaaPyora(uusi);
		} catch (SailoException e) {
			Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
			return;
		}

		hae(uusi.getPyoranID());
	}
	
	/**
	 * Poistaa halutun py�r�n
	 */
	private void poistaPyora() {
		if (pyoraKohdalla == null)
			return;
		if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko j�sen: " + pyoraKohdalla.getNimi(), "Kyll�", "Ei") )
            return;
		vuokraamo.poistaPyora(pyoraKohdalla);
		int index = fxChooserPyorat.getSelectedIndex();
        hae(0);
        fxChooserPyorat.setSelectedIndex(index);
	}


	/**
	 * Tekee vuokrauksen valitulle py�r�lle.
	 */
	public void vuokraaPyora(int kesto) throws SailoException {
		// JOptionPane.showMessageDialog(null, "Viel� ei osata lis�t� vuokrausta!" );
		if (pyoraKohdalla == null) return;
		Vuokraus vuokraus = new Vuokraus(kesto, pyoraKohdalla.getVuokraPerTunti(), pyoraKohdalla.getPyoranID(), 1); // TODO: Asiakkaat lol
		pyoraKohdalla.setOnkoVarattu(true); 
		vuokraus.rekisteroi();
		vuokraus.testiVuokraus(pyoraKohdalla.getPyoranID(),kesto);
		vuokraamo.lisaaVuokraus(vuokraus);
		hae(pyoraKohdalla.getPyoranID());
	}


	/**
	 * Hakee j�senten tiedot listaan
	 * @param jnro j�senen numero, joka aktivoidaan haun j�lkeen
	 */
	protected void hae(int pyoraID) {
		int k = cbKentat.getSelectionModel().getSelectedIndex();
        String ehto = hakuehto.getText(); 
        if (k > 0 || ehto.length() > 0)
            naytaVirhe(String.format("Ei osata hakea (kentt�: %d, ehto: %s)", k, ehto));
        else
        	fxChooserPyorat.clear(); // t�m� oli muuten ennen elsen ulkopuolella
        
        int index = 0;
        Collection<Pyora> pyorat;
        try {
            pyorat = vuokraamo.etsi(ehto, k);
            int i = 0;
            for (Pyora pyora:pyorat) {
                if (pyora.getPyoranID() == pyoraID) index = i;
                fxChooserPyorat.add(pyora.getNimi(), pyora);
                i++;
            }
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Py�r�n hakemisessa ongelmia! " + ex.getMessage());
        }
        fxChooserPyorat.setSelectedIndex(index); // t�st� tulee muutosviesti joka n�ytt�� j�senen
	}
	
	/**
	 * N�ytt�� virheen
	 * @param virhe virheen teksti
	 */
    private void naytaVirhe(String virhe) {
    	
    	if (virhe == null || virhe.isEmpty())
    		Dialogs.showMessageDialog("Tapahtui virhe");
    	else
    		Dialogs.showMessageDialog("Tapahtui virhe: " + virhe);
    	/*
        if ( virhe == null || virhe.isEmpty() ) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
        */
    }



	/**
	 * @param vuokraamo Vuokraamo jota k�ytet��n t�ss� k�ytt�liittym�ss�
	 */
	public void setVuokraamo(Vuokraamo vuokraamo) {
		this.vuokraamo = vuokraamo;
		naytaPyora();
	}


	/**
	 * Tulostaa py�r�n tiedot.
	 * @param os Tietovirta, mihin tulostetaan
	 * @param pyora Tulostettava py�r�
	 */
	private void tulosta(PrintStream os, Pyora pyora) {
		os.println("----------------------------------------------");
		pyora.tulosta(os);

		apuVuokraus = vuokraamo.annaVuokraus(pyoraKohdalla);
			if(apuVuokraus != null) 
				apuVuokraus.tulosta(os);

		os.println("----------------------------------------------");
	}


	/**
	 * Testihommia
	 * @param args ei k�yt�s�
	 */
	public static void main(String[] args) {
		Vuokraamo testi = new Vuokraamo();
		Pyora p1 = new Pyora();
		p1.rekisteroi();
		p1.vastaaJopo();
		try {
			testi.lisaaPyora(p1);
		} catch (SailoException e) {
			e.printStackTrace();
		}
		try {
			testi.etsi("Jopo", 1);
		} catch (SailoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
