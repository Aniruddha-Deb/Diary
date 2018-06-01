package com.sensei.diary;

import java.awt.TextField;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Map;

import com.jfoenix.controls.JFXButton;
import com.sensei.diary.io.DiaryFileLoader;
import com.sensei.diary.prefs.PreferenceManager;
import com.sun.javafx.scene.control.skin.DatePickerSkin;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCombination.Modifier;
import javafx.scene.input.KeyCombination.ModifierValue;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class Controller {
		
	@FXML
	private Label dateField;
	@FXML
	private Label monthField;
	@FXML
	private Label yearField;
	
	@FXML
	private JFXButton nextDayButton;
	@FXML
	private JFXButton prevDayButton;
	@FXML
	private JFXButton calendarButton;
	
	@FXML
	private MenuBar menuBar;

	@FXML 
	private TextArea textArea;
	
	private DatePicker datePicker;
	private Node datePickerPopupContent;
	
	private int fontSize;
	
	private Stage stage = null;
	private LocalDate currentDate;
	private Map<LocalDate, String> entries = null;
	
	public Controller( Map<LocalDate, String> entries, Stage stage ) throws IOException {
		this.entries = entries;
		currentDate = LocalDate.now();
		
		this.stage = stage;
		addListeners( stage );
	}
	
	private void addListeners( Stage stage ) {
		stage.setOnCloseRequest( e -> {
			PreferenceManager.setPreference( PreferenceManager.PREV_DIARY_FILE_LOC, 
											DiaryFileLoader.getInstance().getPathAsString() );
			PreferenceManager.setPreference( PreferenceManager.FONT_SIZE, 
											 String.valueOf( fontSize ) );
			saveCurrentEntry();
		} );
		//stage.getScene().setOnKeyPressed( e -> System.out.println( e.getCode() ) );
	}
	
	private void refreshView() {
		String entryforCurrentDate = entries.get( currentDate );
		if( entryforCurrentDate != null ) textArea.setText( entryforCurrentDate );
		else textArea.clear();
		
		dateField.setText( String.valueOf( currentDate.getDayOfMonth() ) );
		monthField.setText( currentDate.getMonth().toString() );
		yearField.setText( String.valueOf( currentDate.getYear() ) );		
	}
	
	private void saveCurrentEntry() {
		entries.put( currentDate, textArea.getText() );
		DiaryFileLoader.getInstance().saveEntries( entries );
	}
	
	@FXML
	public void initialize() {		
		textArea.setWrapText( true );
		calendarButton.setGraphic( new FontAwesomeIconView( FontAwesomeIcon.CALENDAR ) );
		textArea.setFont( Constants.FONT );
		datePicker = new DatePicker();
		datePickerPopupContent = new DatePickerSkin( datePicker ).getPopupContent();
		menuBar.setUseSystemMenuBar( true );
		refreshView();		
		
		fontSize = loadFontSize();	
		Constants.FONT = new Font( Constants.FONT.getFamily(), fontSize );
		textArea.setFont( Constants.FONT );
	}
	
	private int loadFontSize() {
		String font = PreferenceManager.getPreference( PreferenceManager.FONT_SIZE );
		if( font == null ) { 
			PreferenceManager.setPreference( PreferenceManager.FONT_SIZE, "12" );
			return 12;			
		}
		else return Integer.parseInt( font );
	}
	
	@FXML
	public void onNextDayButtonClick( ActionEvent e ) {
		saveCurrentEntry();
		currentDate = currentDate.plus( Period.ofDays( 1 ) );
		refreshView();
	}
	
	@FXML
	public void onPreviousDayButtonClick( ActionEvent e ) {
		saveCurrentEntry();
		currentDate = currentDate.minus( Period.ofDays( 1 ) );
		refreshView();
	}
	
	@FXML
	public void onCalendarButtonClick( ActionEvent e ) {
		saveCurrentEntry();

		Popup p = new Popup();
		p.setX( calendarButton.getLayoutX() + stage.getX() );
		p.setY( calendarButton.getLayoutY() + stage.getY() + 40 );
		p.getContent().add( datePickerPopupContent );
		p.show( stage );
		p.setAutoHide( true );
		p.setHideOnEscape( true );
		
		p.setOnHidden( event -> datePicker.setValue( currentDate ) );
		
		datePicker.setOnAction( event -> {
			currentDate = datePicker.getValue();
			refreshView();
			p.hide();
		});
	
	}
	
	// TODO change hacky code
	public void fireFontPlus() {
		menuBar.getMenus().get( 1 ).getItems().get( 0 ).fire();
	}
	
	// TODO change hacky code
	public void fireFontMinus() {
		menuBar.getMenus().get( 1 ).getItems().get( 1 ).fire();		
	}
	
	@FXML
	public void onNewFileClick( ActionEvent e ) {
		// TODO implement
	}
	
	@FXML
	public void onOpenFileClick( ActionEvent e ) {
		// TODO implement
	}
	
	@FXML
	public void onPreferencesClick( ActionEvent e ) {
		// TODO implement
	}
	
	@FXML
	public void increaseFont( ActionEvent e ) {
		System.out.println( "Increase font method called" );
		fontSize += 2;
		textArea.setStyle( "-fx-font-size: "+fontSize+"px" );
	}
	
	@FXML
	public void decreaseFont( ActionEvent e ) {
		System.out.println( "Decrease font method called" );
		fontSize -= 2;
		textArea.setStyle( "-fx-font-size: "+fontSize+"px" );		
	}
}
