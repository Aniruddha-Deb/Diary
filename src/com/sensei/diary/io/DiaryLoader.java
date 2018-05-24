package com.sensei.diary.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Map;

public class DiaryLoader {
	
	private Path filePath;
	
	public DiaryLoader( Path filePath ) {
		this.filePath = filePath;
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
		Object o = ois.readObject();		
		ois.close();
		return (Map<LocalDate, String>)o;
	}
	
}
