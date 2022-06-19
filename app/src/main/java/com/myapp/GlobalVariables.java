package com.myapp;

import com.google.firebase.firestore.FirebaseFirestore;
import com.myapp.model.EnWord;

import java.util.ArrayList;
import java.util.Locale;

public class GlobalVariables {
//    public static String userId = "AzAfcdOWunPZJY6PTSlmJNn2m5W2";
    public static String userId = "";

    public static ArrayList<EnWord> listAllWords =  new ArrayList();
    public static ArrayList<EnWord>  listAllSavedWords =  new ArrayList();
    public static FirebaseFirestore db;
    public static ArrayList<Long>  listSavedWordId = new ArrayList();
    public static ArrayList<EnWord>  listFilteredWords = new ArrayList();
    public static int limit = 15;
    public static int offset = 0;

    public static String productDescription = "";

    public static String access_token= "";
    public static String refresh_token ="";
    //SETTINGS
    public static Locale VOICE_LANGUAGE = Locale.US;
    public static float VOICE_SPEED = 1; // NORMAL

    public static String FILE_CONFIG_NAME = "config.dat";
    public static String FILE_HISTORY_NAME = "history.dat";
    public static String FILE_ALARM_SET = "alarm.dat";
    public static String userNote="";
//    public static Settings SETTINGS;
}
