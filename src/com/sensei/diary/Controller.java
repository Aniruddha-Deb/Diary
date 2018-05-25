package com.sensei.diary;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Map;

import com.jfoenix.controls.JFXButton;
import com.sensei.diary.io.DiaryFileLoader;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

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
	
	public Controller( Map<LocalDate, String> entries ) throws IOException {
		this.entries = entries;
		currentDate = LocalDate.now();
	}
	
	private void refreshView() {
		dateField.setText( String.valueOf( currentDate.getDayOfMonth() ) );
		monthField.setText( currentDate.getMonth().toString() );
		yearField.setText( String.valueOf( currentDate.getYear() ) );		
	}
	
	@FXML
	public void initialize() {		
		calendarButton.setGraphic( new FontAwesomeIconView( FontAwesomeIcon.CALENDAR ) );
		textArea.setFont( Constants.FONT );
		refreshView();		
	}
	
	@FXML
	public void onNextDayButtonClick( ActionEvent e ) {
		currentDate = currentDate.plus( Period.ofDays( 1 ) );
		refreshView();
	}
	
	@FXML
	public void onSplitPaneMouseClicked( MouseEvent e ) {
		System.out.println( "Mouse Clicked!" );
	}
	
	@FXML
	public void onPreviousDayButtonClick( ActionEvent e ) {
		currentDate = currentDate.minus( Period.ofDays( 1 ) );
		refreshView();
	}
	
	@FXML
	public void onCalendarButtonClick( ActionEvent e ) {
		LocalDate ld = datePicker.getValue();
		currentDate = ld;
		refreshView();
	}
}
