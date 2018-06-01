package com.sensei.diary;

import static com.sensei.diary.prefs.PreferenceManager.PREV_DIARY_FILE_LOC;
import static com.sensei.diary.prefs.PreferenceManager.getPreference;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import com.sensei.diary.io.DiaryFileLoader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Diary extends Application {

	@Override
	public void start( Stage stage ) throws Exception {

		initDiaryFileLoader();
		Map<LocalDate, String> entries = loadEntriesFromDFL();
		
		Controller c = new Controller( entries, stage );
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation( Controller.class.getResource( "/Log_v2.fxml" ) );
		loader.setController( c );
		
		AnchorPane ap = (AnchorPane)loader.load();
		Scene scene = new Scene( ap );
		scene.setOnKeyPressed( e -> System.out.println( e.getCode() ) );
		stage.setScene( scene );
		stage.setMinHeight( 600 );
		stage.setMinWidth( 400 );
		stage.show();		
	}
	
	private void initDiaryFileLoader() throws Exception {
		String diaryFileLocation = getPreference( PREV_DIARY_FILE_LOC );
		Path p = Paths.get( diaryFileLocation );
		if( diaryFileLocation == null || !Files.exists( p ) ) {
			diaryFileLocation = launchNewDiaryFileWizard();
			
			if( diaryFileLocation == null )
				// cancel button was pressed
				stop();
		}
		p = Paths.get( diaryFileLocation );
		DiaryFileLoader.initPath( p );
	}
	
	private Map<LocalDate, String> loadEntriesFromDFL() throws Exception {		
		try {
			return DiaryFileLoader.getInstance().loadEntries();
		}
		catch( Exception ex ) {
			showErrorAlert( "An error occured while trying to load " + 
					DiaryFileLoader.getInstance().getPathAsString() );
			stop();
		}
		return null;
	}
	
	private String openFile() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle( "Open a diary file" );
		chooser.setSelectedExtensionFilter( new ExtensionFilter( "Diary file", "*.diary" ) );
		File f = chooser.showOpenDialog( null );
		return f == null ? null : f.getAbsolutePath() ;		
	}
	
	private String createFile() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle( "Create a new diary file" );
		chooser.setInitialFileName( ".diary" );
		File f = chooser.showSaveDialog( null );
		if( f != null ) {
			if( !f.getName().endsWith( ".diary" ) ) {
				showErrorAlert( "The file was not saved with a .diary extension." );
				return createFile();
			}
			else {
				return f.getAbsolutePath();
			}
		}	
		return null;
	}
	
	private String launchNewDiaryFileWizard() {
		Alert alert = new Alert( AlertType.ERROR );
		alert.setHeaderText( "The previously opened diary file could not be found" );
		alert.getButtonTypes().remove( 0 );
		alert.getButtonTypes().add( new ButtonType( "Open another file", ButtonData.YES ) );
		alert.getButtonTypes().add( new ButtonType( "Create a new file", ButtonData.NO ) );
		Optional<ButtonType> btypes = alert.showAndWait();
		if( btypes.get().getButtonData() == ButtonData.YES ) {
			return openFile();			
		}
		else {
			return createFile();
		}
	}
		
	private void showErrorAlert( String error ) {
		Alert alert = new Alert( AlertType.ERROR );
		alert.setHeaderText( error );
		alert.showAndWait();
	}
	
	public static void main( String[] args ) {
		launch( args );
	}
}
