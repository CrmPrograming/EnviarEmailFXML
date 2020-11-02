package dad.javafx.enviaremailfxml.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.util.converter.NumberStringConverter;

public class Controller implements Initializable {
	
	// View
	
	@FXML
	private BorderPane view;
	
	@FXML
	private TextArea taMensaje;
	
	@FXML
	private TextField tfAsunto;
	
	@FXML
	private TextField tfDestinatario;
	
	@FXML
	private TextField tfRemitente;
	
	@FXML
	private TextField tfSMTP;
	
	@FXML
	private TextField tfPuerto;
	
	@FXML
	private CheckBox cbSSL;
	
	@FXML
	private PasswordField pfPasswd;
	
	@FXML
	private Button btEnviar;
	
	@FXML
	private Button btVaciar;
	
	@FXML
	private Button btCerrar;
	
	// Model
	private Model model = new Model();
	
	public Controller() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/View.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Bindings.bindBidirectional(model.servidorProperty(), tfSMTP.textProperty());		
		Bindings.bindBidirectional(tfPuerto.textProperty(), model.puertoProperty(), new NumberStringConverter());
		Bindings.bindBidirectional(model.sslProperty(), cbSSL.selectedProperty());
		Bindings.bindBidirectional(model.remitenteProperty(), tfRemitente.textProperty());
		Bindings.bindBidirectional(model.passwdProperty(), pfPasswd.textProperty());
		Bindings.bindBidirectional(model.destinatarioProperty(), tfDestinatario.textProperty());
		Bindings.bindBidirectional(model.asuntoProperty(), tfAsunto.textProperty());
		Bindings.bindBidirectional(model.mensajeProperty(), taMensaje.textProperty());
	}
	
	@FXML
	public void onActionEnviar(ActionEvent e) {
		Email email = new SimpleEmail();
		
		try {
			email.setHostName(model.getServidor());
			email.setSmtpPort(model.getPuerto());
			email.setAuthenticator(new DefaultAuthenticator(model.getRemitente(), model.getPasswd()));
			email.setSSLOnConnect(model.isSsl());		
			email.setFrom(model.getRemitente());
			email.setSubject(model.getAsunto());
			email.setMsg(model.getMensaje());
			email.addTo(model.getDestinatario());			
			email.send();
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Mensaje enviado");
			alert.setHeaderText("Mensaje enviado con éxito a '" + model.getDestinatario() + "'");			

			alert.showAndWait();
		} catch (EmailException e1) {			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("No se pudo enviar el email.");
			alert.setContentText(e1.getLocalizedMessage());

			alert.showAndWait();
			
			e1.printStackTrace();
		}
	}
	
	@FXML
	public void onActionVaciar(ActionEvent e) {
		model.setServidor("");
		model.setPuerto(0);
		model.setSsl(false);
		model.setRemitente("");
		model.setPasswd("");
		model.setDestinatario("");
		model.setAsunto("");
		model.setMensaje("");
	}
	
	@FXML
	public void onActionCerrar(ActionEvent e) {
		Platform.exit();
	}

	public BorderPane getView() {
		return view;
	}

	public void setView(BorderPane view) {
		this.view = view;
	}

}
