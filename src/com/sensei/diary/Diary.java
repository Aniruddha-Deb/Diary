package com.sensei.diary;

import static com.sensei.diary.prefs.PreferenceManager.PREV_DIARY_FILE_LOC;
import static com.sensei.diary.prefs.PreferenceManager.getPreference;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.sensei.diary.io.DiaryFileLoader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Diary extends Application {

	@Override
	public void start( Stage stage ) throws Exception {
		
		String diaryFileLoc = getDiaryFileLocation();
		DiaryFileLoader dfl = new DiaryFileLoader( Paths.get( diaryFileLoc ) );
		Map<LocalDate, String> entries = loadEntriesFromDFL( dfl );
		
		Controller c = new Controller( entries );
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation( Controller.class.getResource( "/Log_v2.fxml" ) );
		loader.setController( c );
		
		AnchorPane ap = (AnchorPane)loader.load();
		Scene scene = new Scene( ap );
		stage.setScene( scene );
		stage.setMinHeight( 600 );
		stage.setMinWidth( 400 );
		stage.show();
	}
	
	private String getDiaryFileLocation() {
		String diaryFileLocation = getPreference( PREV_DIARY_FILE_LOC );
		if( diaryFileLocation == null ) {
			diaryFileLocation = launchNewDiaryFileWizard();
			
			if( diaryFileLocation == null )
				// cancel button was pressed
				System.exit( 0 );
		}
		return diaryFileLocation;
	}
	
	private Map<LocalDate, String> loadEntriesFromDFL( DiaryFileLoader dfl ) {		
		try {
			return dfl.loadEntries();
		}
		catch( IOException ex ) {
			showErrorAlert( "An error occured while trying to load " + dfl.getPathAsString() );
		}
		catch( ClassNotFoundException ex ) {
			showErrorAlert( "An error occured while trying to load " + dfl.getPathAsString() );
		}
		return null;
	}
	
	private String launchNewDiaryFileWizard() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle( "Create a new diary file" );
		chooser.setInitialFileName( ".diary" );
		File f = chooser.showSaveDialog( null );
		if( f != null ) {
			if( !f.getName().endsWith( ".diary" ) ) {
				showErrorAlert( "The file was not saved with a .diary extension." );
				return launchNewDiaryFileWizard();
			}
			else {
				DiaryFileLoader.createFile( f.toPath() );
				return f.getPath();
			}
		}
		return null;
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
