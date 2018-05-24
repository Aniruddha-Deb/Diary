package com.sensei.diary.prefs;

import java.util.prefs.Preferences;

public class PreferenceManager {
	
	private static Preferences prefs = null;
	private static final String PREFS_NODE_NAME = "DIARY_PREFS";
	
	// Constant key preference values
	public static final String FONT_SIZE = "FONT_SIZE";
	public static final String PREV_DIARY_FILE_LOC = "PREV_DIARY_FILE_LOC";
	
	public static Preferences getPreferences() {
		if( prefs == null ) prefs = Preferences.userRoot().node( PREFS_NODE_NAME );
		return prefs;
	}
}
