package com.sensei.diary.io.poi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectOutputStream;

public class ObjectSerializationPoI {
	
	public static void main( String[] args ) throws Exception {
		File f = new File( "res/serialized_string.string" );
		
		BufferedReader br = new BufferedReader( new FileReader( f ) );
		String s;
		while( (s=br.readLine()) != null ) {
			System.out.print( s );
		}
		
		String str = "Hello";
		ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream( f ) );
		oos.writeObject( str );
		oos.close();
			
	}

}
