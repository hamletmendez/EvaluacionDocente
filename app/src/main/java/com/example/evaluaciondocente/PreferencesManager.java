package com.example.evaluaciondocente;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Hamlet Méndez on 11/9/2017.
 */

public class PreferencesManager {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    //Definir las preferencias como privadas
    private static final int PRIVATE_MODE = 0;

    //Definir el nombre de archivo donde se almacenará la preferencia
    private static final String PREFERENCE_FILE_NAME = "pucmm-evaluacion-profesor";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    //Constructor de la clase
    public PreferencesManager(Context context){
        this.context = context;
        preferences = this.context.getSharedPreferences(PREFERENCE_FILE_NAME,PRIVATE_MODE);
        editor = preferences.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime){
        editor.putBoolean(IS_FIRST_TIME_LAUNCH,isFirstTime);
        editor.commit();
    }
    public boolean isFirstTimeLaunch(){
        return preferences.getBoolean(IS_FIRST_TIME_LAUNCH,true);
    }
}
