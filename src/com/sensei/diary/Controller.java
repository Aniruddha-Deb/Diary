package com.sensei.diary;

import java.io.IOException;
import java.time.Month;
import java.util.Calendar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Controller {
	
	@FXML
	private Label dateField;
	@FXML
	private Label monthField;
	@FXML
	private Label yearField;
	
	@FXML
	private Button nextDayButton;
	@FXML
	private Button prevDayButton;
	@FXML
	private Button calendarButton;

	@FXML
	private DatePicker datePicker;
	@FXML 
	private Button openDateButton;

	@FXML 
	private TextArea textArea;
	
	private Scene scene = null;
	private Stage stage = null;
	
	public Controller( Stage stage ) throws IOException {
		
		this.stage = stage;
		FXMLLoader loader = new FXMLLoader();
		loader.setController( this );
		loader.setLocation( Controller.class.getResource( "/Log.fxml" ) );
		
		AnchorPane ap = (AnchorPane)loader.load();
		scene = new Scene( ap );
		stage.setScene( scene );
		stage.setMinHeight( 600 );
		stage.setMinWidth( 400 );
		
		scene.setOnKeyPressed( e -> {
			if( e.getCode() == KeyCode.EQUALS && e.isControlDown() ) {
				textArea.setStyle( "-fx-font-size: " + (textArea.getFont().getSize()+2) + "px;" );
			}
			else if( e.getCode() == KeyCode.MINUS && e.isControlDown() ) {
				textArea.setStyle( "-fx-font-size: " + (textArea.getFont().getSize()-2) + "px;" );				
			}
		});
	}
		
	@FXML
	public void initialize() {
		textArea.setFont( Constants.FONT );
		
		Calendar c = Calendar.getInstance();
		dateField.setText( c.get( Calendar.DATE )+"" );
		monthField.setText( Month.of( c.get( Calendar.MONTH ) )+"" );
		yearField.setText( c.get( Calendar.YEAR )+"" );		
	}
	
	@FXML
	public void onNextDayButtonClick( ActionEvent e ) {

	}
	
	@FXML
	public void onPreviousDayButtonClick( ActionEvent e ) {
		
	}
	
	@FXML
	public void onCalendarButtonClick( ActionEvent e ) {
		
	}
	
}
