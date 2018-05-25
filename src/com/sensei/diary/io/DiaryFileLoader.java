package com.sensei.diary.io;

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
    
    private static Path filePath;
    private static DiaryFileLoader instance = null;
    
    public static void initPath( Path filePath ) throws FileNotFoundException {
        DiaryFileLoader.filePath = filePath;
        if( !filePath.toFile().exists() ) 
            throw new FileNotFoundException( "Specified file does not exist" );     
    }
    
    public static DiaryFileLoader getInstance() {
    	if( filePath == null ) return null;
        if( instance == null ) instance = new DiaryFileLoader();
        return instance;
    }
    
    public static void createFile( Path p ) {
        try {
        	
        	// TODO tidy up this dangerous code
            p.toFile().createNewFile();
            DiaryFileLoader.initPath( p );
            DiaryFileLoader dfl = new DiaryFileLoader();
            dfl.saveEntries( new HashMap<LocalDate, String>() );
            DiaryFileLoader.initPath( null );
        } catch( Exception ex ) {
            ex.printStackTrace();
        }
    }
    
    public String getPathAsString() {
        return filePath.toString();
    }
    
    public void saveEntries( Map<LocalDate, String> entries ) {
        try {
	    	ObjectOutputStream oos = new ObjectOutputStream( 
	                                 new FileOutputStream( filePath.toFile() ) );
	        oos.writeObject( entries );
	        oos.close();
        } catch( IOException ex ) {
        	ex.printStackTrace();
        }
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
