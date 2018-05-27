package com.sensei.diary;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Map;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.skins.JFXDatePickerContent;
import com.jfoenix.skins.JFXDatePickerSkin;
import com.sensei.diary.io.DiaryFileLoader;
import com.sensei.diary.prefs.PreferenceManager;
import com.sun.javafx.scene.control.skin.DatePickerSkin;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
	private TextArea textArea;
	
	private DatePicker datePicker;
	private Node datePickerPopupContent;
	
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
			saveCurrentEntry();
		} );
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
		refreshView();		
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
}
