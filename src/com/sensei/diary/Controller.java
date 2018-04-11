package com.sensei.diary;

import java.time.Month;
import java.util.Calendar;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class Controller{
	
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
	
	
	
	@FXML
	public void initialize() {
		Calendar c = Calendar.getInstance();
		dateField.setText( c.get( Calendar.DATE )+"" );
		monthField.setText( Month.of( c.get( Calendar.MONTH ) )+"" );
		yearField.setText( c.get( Calendar.YEAR )+"" );
	}
	
}
