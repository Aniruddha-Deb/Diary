package com.sensei.diary;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Map;

import com.jfoenix.controls.JFXButton;
import com.sensei.diary.io.DiaryFileLoader;
import com.sensei.diary.prefs.PreferenceManager;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
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
	private DatePicker datePicker;
	@FXML 
	private Button openDateButton;

	@FXML
	private SplitPane splitPane;
	
	@FXML 
	private TextArea textArea;
	
	private LocalDate currentDate;
	private Map<LocalDate, String> entries = null;
	
	public Controller( Map<LocalDate, String> entries, Stage stage ) throws IOException {
		this.entries = entries;
		currentDate = LocalDate.now();
		
		addListeners( stage );
	}
	
	private void addListeners( Stage stage ) {
		stage.setOnCloseRequest( e -> {
			PreferenceManager.setPreference( PreferenceManager.PREV_DIARY_FILE_LOC, 
											DiaryFileLoader.getInstance().getPathAsString() );
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
		calendarButton.setGraphic( new FontAwesomeIconView( FontAwesomeIcon.CALENDAR ) );
		textArea.setFont( Constants.FONT );
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
		LocalDate ld = datePicker.getValue();
		currentDate = ld;
		refreshView();
	}
}
