package com.sensei.diary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Diary extends Application{

	public static void main( String[] args ){
		launch( args );
	}
	
	@Override
	public void start( Stage primaryStage ) throws Exception{
		Controller c = new Controller();
		
		FXMLLoader loader = new FXMLLoader();
		loader.setController( c );
		loader.setLocation( Diary.class.getResource( "/Log.fxml" ) );
		
		AnchorPane ap = (AnchorPane)loader.load();
		Scene s = new Scene( ap );
		primaryStage.setScene( s );
		primaryStage.setTitle( "Hello" );
		primaryStage.show();
	}
}
