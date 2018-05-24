package com.sensei.diary;

import javafx.application.Application;
import javafx.stage.Stage;

public class Diary extends Application{

	public static void main( String[] args ){
		launch( args );
	}
	
	@Override
	public void start( Stage primaryStage ) throws Exception {
		new Controller( primaryStage );
	}
}
