package com.sensei.diary;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.util.Map;
import java.util.Optional;

import com.sensei.diary.io.DiaryLoader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import static com.sensei.diary.prefs.PreferenceManager.*;

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
	private SplitPane splitPane;
	
	@FXML 
	private TextArea textArea;
	
	private Scene scene = null;
	private Stage stage = null;
	
	private LocalDate currentDate;
	private DiaryLoader dl = null;
	private Map<LocalDate, String> entries = null;
	
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
		stage.show();

		createDiaryLoader();
		
		addFontSizeChangeListener();
	}
	
	private void createDiaryLoader() {
		String prevDiaryFileLoc = getPreferences().get( PREV_DIARY_FILE_LOC, "-1" );
		if( prevDiaryFileLoc.equals( "-1" ) ) {
			System.out.println( "Could not load prev diary" );
			Alert a = new Alert( AlertType.ERROR );
			a.setHeaderText( "Could not load prev diary" );
			a.show();
		}
		else {
			dl = new DiaryLoader( Paths.get( prevDiaryFileLoc ) );
			try {
				entries = dl.loadEntries();
			} catch( Exception ex ) {
				// TODO show a UI error window from here.
			}
		}
	}
		
	private void addFontSizeChangeListener() {

		scene.setOnKeyPressed( e -> {
			if( e.getCode() == KeyCode.EQUALS && e.isControlDown() ) {
				textArea.setStyle( "-fx-font-size: " + (textArea.getFont().getSize()+2) + "px;" );
			}
			else if( e.getCode() == KeyCode.MINUS && e.isControlDown() ) {
				textArea.setStyle( "-fx-font-size: " + (textArea.getFont().getSize()-2) + "px;" );				
			}
		});		
	}
	
	private void refreshView() {
		if( entries != null ) {
			String entry = entries.get( currentDate );
			if( entry != null ) textArea.setText( entry );
		}
		dateField.setText( String.valueOf( currentDate.getDayOfMonth() ) );
		monthField.setText( currentDate.getMonth().toString() );
		yearField.setText( String.valueOf( currentDate.getYear() ) );		
	}
	
	private boolean showSaveEntryPrompt() {
		String text = textArea.getText();
		if( text.equals( entries.get( currentDate ) ) ) return true;
		
		Alert saveAlert = new Alert( AlertType.INFORMATION );
		saveAlert.getButtonTypes().remove( 0 );
		saveAlert.getButtonTypes().add( ButtonType.CANCEL );
		saveAlert.getButtonTypes().add( ButtonType.YES );
		saveAlert.getButtonTypes().add( ButtonType.NO );
		saveAlert.setHeaderText( "Do you want to save the current entry?" );
		Optional<ButtonType> alertResult = saveAlert.showAndWait();

		if( alertResult.get() == ButtonType.YES ) {
			entries.put( currentDate, text );
			return true;
		}
		else if( alertResult.get() == ButtonType.NO ) {
			return true;
		}
		else return false; // cancel button clicked
	}

	@FXML
	public void initialize() {		
		currentDate = LocalDate.now();		
		textArea.setFont( Constants.FONT );
		refreshView();		
	}
	
	@FXML
	public void onNextDayButtonClick( ActionEvent e ) {
		boolean switchToNext = showSaveEntryPrompt();
		if( switchToNext ) {
			currentDate = currentDate.plus( Period.ofDays( 1 ) );
			refreshView();
		}
	}
	
	@FXML
	public void onSplitPaneMouseClicked( MouseEvent e ) {
		System.out.println( "Mouse Clicked!" );
	}
	
	@FXML
	public void onPreviousDayButtonClick( ActionEvent e ) {
		boolean switchToNext = showSaveEntryPrompt();
		if( switchToNext ) {
			currentDate = currentDate.minus( Period.ofDays( 1 ) );
			refreshView();
		}
	}
	
	@FXML
	public void onCalendarButtonClick( ActionEvent e ) {
		LocalDate ld = datePicker.getValue();
		currentDate = ld;
		refreshView();
	}
}
