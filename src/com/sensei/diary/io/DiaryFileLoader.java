package com.sensei.diary.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DiaryFileLoader {
	
	private Path filePath;
	
	public DiaryFileLoader( Path filePath ) throws FileNotFoundException {
		this.filePath = filePath;
		if( !filePath.toFile().exists() ) 
			throw new FileNotFoundException( "Specified file does not exist" );
	}
	
	public static void createFile( Path p ) {
		try {
			p.toFile().createNewFile();
			DiaryFileLoader dfl = new DiaryFileLoader( p );
			dfl.saveEntries( new HashMap<LocalDate, String>() );
		} catch( Exception ex ) {
			ex.printStackTrace();
		}
	}
	
	public String getPathAsString() {
		return filePath.toString();
	}
	
	public void saveEntries( Map<LocalDate, String> entries ) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream( 
				                 new FileOutputStream( filePath.toFile() ) );
		oos.writeObject( entries );
		oos.close();
	}
	
	public Map<LocalDate, String> loadEntries() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream( 
                new FileInputStream( filePath.toFile() ) );
		try {
			Object o = ois.readObject();		
			ois.close();
			return (Map<LocalDate, String>)o;
		} catch( IOException ex ) {
			// in case the file was newly created
			return new HashMap<LocalDate, String>();
		}
	}
	
}
