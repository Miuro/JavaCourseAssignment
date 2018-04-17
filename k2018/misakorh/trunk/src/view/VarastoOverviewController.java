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

	// Reference to the main application. En ole ihan varma mitä tämä tekee.
	private MainApp mainApp;


	/**
	 * Konstruktori Tätä vissiin kutsutaan kun tapahtuu automaattinen initialize() metodi fxml-tiedoston ladattua. Dunno
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
	 * Tätä kutsutaan MainApp:sta. Määrittää, että tämä on perusnäkymä-
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	/**
	 * Hakuehdon händläys
	 */
	@FXML
	void handleHakuehto() {
        if ( pyoraKohdalla != null )
            hae(pyoraKohdalla.getPyoranID()); 
	}


	/**
	 * Händlää Apua painikkeen
	 */
	@FXML
	void handleAvaaApua() {
		Dialogs.showMessageDialog("Ei ole vielä lisätty");
	}


	/**
	 * Händlää Tietoja painikkeen
	 */
	@FXML
	void handleAvaaTietoja() {
		Dialogs.showMessageDialog("PyöräVarasto\nVer. 0.6\nTekijät: Jouko Sirkka & Miro Korhonen");
	}


	/**
	 * Händlää Avaa -painikkeen toiminnan
	 */
	@FXML
	void handleMenuAvaa() {
		avaa();
	}


	/**
	 * Händlää lopeta painikkeen
	 */
	@FXML
	void handleMenuLopeta() {
		Dialogs.showMessageDialog("Ei ole vielä lisätty");
	}


	/**
	 * Händlää tulosta-painikkeen
	 */
	@FXML
	void handleMenuTulosta() {
		//Dialogs.showMessageDialog("Ei ole vielä lisätty");
		mainApp.showUusiTulostusDialog();
	}


	/**
	 * Händlää poistapyörä painikkeen
	 */
	@FXML
	void handlePoistaPyora() {
		poistaPyora();
	}


	/**
	 * Händlää tallennus painikkeen
	 */
	@FXML
	void handleTallenna() {
		tallenna();
	}


	/**
	 * Händlää uusipyörä painikkeen
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
		if(pyoraKohdalla == null) {
			Dialogs.showMessageDialog("Valitse vuokrattava pyörä");
			return;
		}
		
		if (pyoraKohdalla.getOnkoVarattu() == true) {
			Dialogs.showMessageDialog("Pyörä on jo vuokrattu");
			return;
		}
		
		apuVuokraus = new Vuokraus();
		mainApp.showUusiVuokrausDialog(pyoraKohdalla, apuVuokraus);
		vuokraamo.lisaaVuokraus(apuVuokraus);
		hae(pyoraKohdalla.getPyoranID());
		//vuokraaPyora(5);
	}

	//===============================================================================
	// FXML:llään kuulumaton koodi tästä eteenpäin

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
	 * Näyttää listasta valitun pyörän tiedot, tilapäisesti yhteen isoon edit-kenttään
	 */
	protected void naytaPyora() {
		pyoraKohdalla = fxChooserPyorat.getSelectedObject();

		if (pyoraKohdalla == null) {
			areaPyora.clear();
			return;
		}
		
		apuVuokraus = null;
		if(pyoraKohdalla.getOnkoVarattu() == true) {
			apuVuokraus = vuokraamo.annaVuokraus(pyoraKohdalla);
		}

		areaPyora.setText("");
		try (PrintStream os = TextAreaOutputStream.getTextPrintStream(areaPyora)) {
			tulosta(os, pyoraKohdalla);
		}
	}

	
	/**
	 * Alustaa vuokraamon lukemalla sen valitun nimisestä tiedostosta
	 * @param nimi tiedosto josta vuokraamon tiedot luetaan
	 * @return null jos onnistuu, muuten virhe tekstinä
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
     * Kysytään tiedoston nimi ja luetaan se
     * @return true jos onnistui, false jos ei
     */
    public boolean avaa() {
    	//String uusiNimi = ""; // TODO dialog joka kysyy nimeä
        //if(uusiNimi == null) return false;
        lueTiedosto(vuokraamonNimi);
        return true;
    }
	
    
    /**
     * Tietojen tallennus
     * @return null jos onnistuu, muuten virhe tekstinä
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
	 * Luo uuden jäsenen jota aletaan editoimaan
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
	 * Poistaa halutun pyörän
	 */
	private void poistaPyora() {
		if (pyoraKohdalla == null)
			return;
		if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko jäsen: " + pyoraKohdalla.getNimi(), "Kyllä", "Ei") )
            return;
		vuokraamo.poistaPyora(pyoraKohdalla);
		int index = fxChooserPyorat.getSelectedIndex();
        hae(0);
        fxChooserPyorat.setSelectedIndex(index);
	}


	/**
	 * Tekee vuokrauksen valitulle pyörälle.
	 */
	public void vuokraaPyora(int kesto) throws SailoException {
		// JOptionPane.showMessageDialog(null, "Vielä ei osata lisätä vuokrausta!" );
		if (pyoraKohdalla == null) return;
		//Vuokraus vuokraus = new Vuokraus(kesto, pyoraKohdalla.getVuokraPerTunti(), pyoraKohdalla.getPyoranID(), 1); // TODO: Asiakkaat lol
		Vuokraus vuokraus = new Vuokraus();
		vuokraus.rekisteroi();
		vuokraus.testiVuokraus(pyoraKohdalla.getPyoranID(),kesto);
		vuokraamo.lisaaVuokraus(vuokraus);
		pyoraKohdalla.setOnkoVarattu(true); 
		hae(pyoraKohdalla.getPyoranID());
	}


	/**
	 * Hakee jäsenten tiedot listaan
	 * @param jnro jäsenen numero, joka aktivoidaan haun jälkeen
	 */
	protected void hae(int pyoraID) {
		int k = cbKentat.getSelectionModel().getSelectedIndex();
        String ehto = hakuehto.getText(); 
        if (k > 0 || ehto.length() > 0)
            naytaVirhe(String.format("Ei osata hakea (kenttä: %d, ehto: %s)", k, ehto));
        fxChooserPyorat.clear();
        
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
            Dialogs.showMessageDialog("Pyörän hakemisessa ongelmia! " + ex.getMessage());
        }
        fxChooserPyorat.setSelectedIndex(index); // tästä tulee muutosviesti joka näyttää jäsenen
	}
	
	/**
	 * Näyttää virheen
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
	 * @param vuokraamo Vuokraamo jota käytetään tässä käyttöliittymässä
	 */
	public void setVuokraamo(Vuokraamo vuokraamo) {
		this.vuokraamo = vuokraamo;
		naytaPyora();
	}


	/**
	 * Tulostaa pyörän tiedot.
	 * @param os Tietovirta, mihin tulostetaan
	 * @param pyora Tulostettava pyörä
	 */
	private void tulosta(PrintStream os, Pyora pyora) {
		os.println("----------------------------------------------");
		pyora.tulosta(os);
		if(apuVuokraus != null) {
			apuVuokraus.tulosta(os);			
		}

		os.println("----------------------------------------------");
	}


	/**
	 * Testihommia
	 * @param args ei käytösä
	 */
	public static void main(String[] args) {
		/*
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
		*/
	}

}
