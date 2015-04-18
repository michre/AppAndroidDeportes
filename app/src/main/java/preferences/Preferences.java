package preferences;

/**
 * Clase Preferences: almacena y edita las preferencias de usuario
 * @author Miguel Rojo Esteva
 */


import java.util.ArrayList;
import java.util.HashMap;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.miguel.appdeportes.R;

public class Preferences extends Activity {




    /*Opciones de conexion*/
    public final static boolean CONECTADO_DEFAULT = false;


    /*Hay red*/
    public final static String RED_DISPONIBLE_KEY = "redDisponible";
    public final static boolean RED_DISPONIBLE_DEFAULT = true;

    /*Nombre usuario*/
    public final static String NOMBREUSU_KEY = "name";
    public final static String  NOMBREUSU_KEY_DEFAULT = "name";

    /*Correo usuario*/
    public final static String CORREO_KEY = "mail";
    public final static String  CORREO_KEY_DEFAULT = "mail";

     /*Teléfono usuario*/
    public final static String PHONE_KEY = "phone";
    public final static String  PHONE_KEY_DEFAULT = "phone";

     /*Apellidos usuario*/
    public final static String APELLIDOS_KEY = "lastName";
    public final static String  APELLIDOS_KEY_DEFAULT = "LastName";

     /*Nick usuario*/
    public final static String USUARIO_KEY = "username";
    public final static String  USUARIO_KEY_DEFAULT = "username";

    public final static String PASSUSU_KEY = "pass";

    /*Conectado*/
    public final static String USU_CONECTADO = "conectado";
    public final static boolean USU_CONECTADO_DEFAULT = false;


    /*ID movil para GCM*/
    public final static String IDGCM_KEY = "idGCM";
    public final static String  IDGCM_DEFAULT = "";

    public final static String EXPIREIDGCM_KEY = "expireIdGCM";
    public final static Long  EXPIREIDGCM_DEFAULT = (long) 0;


    /*ID SENDER*/
    public final static String SENDERIDGCM_KEY = "idGCM";
    public final static String  SENDERIDGCM_DEFAULT = "62585979586";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        AppPreferences fragment = new AppPreferences();
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Edita el valor de red disponible con true o false
     * @param c contexto de la aplicación
     * @param bool true o false en función de si está disponible o no
     */
    public static void setRedDisponible(Context c, boolean bool){
        SharedPreferences sharedPreferences = 	 PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(RED_DISPONIBLE_KEY, bool);
        editor.commit();
    }

    /**
     * Devuelve el valor de red disponible con true o false
     * @param c contexto de la aplicación
     */
    public static boolean getRedDisponible(Context c){
        SharedPreferences sharedPreferences = 	 PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getBoolean(Preferences.RED_DISPONIBLE_KEY, Preferences.RED_DISPONIBLE_DEFAULT);
    }


    /**
     * Edita el nombre del usuario
     * @param c contexto de la aplicación
     * @param id nuevo nombre
     */
    public static void setNombreUsuario(Context c, String id){
        SharedPreferences sharedPreferences = 	 PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NOMBREUSU_KEY,id);
        editor.commit();
    }

    /**
     * Edita el nombre del usuario
     * @param c contexto de la aplicación
     */
    public static String getNombreUsuario(Context c){
        SharedPreferences sharedPreferences = 	 PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getString(Preferences.NOMBREUSU_KEY, Preferences.NOMBREUSU_KEY_DEFAULT);

    }

    /**
     * Edita el correo del usuario
     * @param c contexto de la aplicación
     * @param id nuevo nombre del jugador contrincante
     */
    public static void setCorreoUsuario(Context c, String id){
        SharedPreferences sharedPreferences = 	 PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CORREO_KEY,id);
        editor.commit();
    }

    /**
     * Edita el correo del usuario
     * @param c contexto de la aplicación
     */
    public static String getCorreoUsuario(Context c){
        SharedPreferences sharedPreferences = 	 PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getString(Preferences.CORREO_KEY, Preferences.CORREO_KEY_DEFAULT);

    }

    /**
     * Edita el valor de usuario conectado con true o false
     * @param c contexto de la aplicación
     * @param bool true o false en función de si está conectado o no
     */
    public static void setUsuConectado(Context c, boolean bool){
        SharedPreferences sharedPreferences = 	 PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(USU_CONECTADO, bool);
        editor.commit();
    }

    /**
     * Devuelve el valor de usuario conectado con true o false
     * @param c contexto de la aplicación
     */
    public static boolean getUsuConectado(Context c){
        SharedPreferences sharedPreferences = 	 PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getBoolean(Preferences.USU_CONECTADO, USU_CONECTADO_DEFAULT);
    }

    /**
     * Edita la id de GCM
     * @param c contexto de la aplicación
     * @param id nuevo nombre del jugador contrincante
     */
    public static void setIdGCM(Context c, String id){
        SharedPreferences sharedPreferences = 	 PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(IDGCM_KEY,id);
        editor.commit();
    }

    /**
     * Obtiene la id GCM
     * @param c contexto de la aplicación
     */
    public static String getIdGCM(Context c){
        SharedPreferences sharedPreferences = 	 PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getString(Preferences.IDGCM_KEY, Preferences.IDGCM_DEFAULT);

    }

    /**
     * Edita tiempo de expiracion de id de GCM
     * @param c contexto de la aplicación
     * @param id nuevo nombre del jugador contrincante
     */
    public static void setDateIdGCM(Context c, long id){
        SharedPreferences sharedPreferences = 	 PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(EXPIREIDGCM_KEY,id);
        editor.commit();
    }

    /**
     * Obtienetiempo de expiracion de id GCM
     * @param c contexto de la aplicación
     */
    public static long geExpiretIdGCM(Context c){
        SharedPreferences sharedPreferences = 	 PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getLong(Preferences.EXPIREIDGCM_KEY, Preferences.EXPIREIDGCM_DEFAULT);

    }

    /**
     * Obtiene la id servidor GCM
     * @param c contexto de la aplicación
     */
    public static String getSenderIdGCM(Context c){
        SharedPreferences sharedPreferences = 	 PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getString(Preferences.SENDERIDGCM_KEY, Preferences.SENDERIDGCM_DEFAULT);

    }
}
