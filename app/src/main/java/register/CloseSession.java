package register;

/**
 * Clase CloseSession: cierra la sesion del dispositivo y elimina los datos del servidor.
 * @author Miguel Rojo Esteva
 */


import java.io.IOException;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;




import com.example.miguel.appdeportes.MainActivity;
import com.example.miguel.appdeportes.R;

import preferences.Preferences;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class CloseSession extends Activity{
    String TAG = "Debug_RegisterGCM";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.vacia);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        startActivity(new Intent(CloseSession.this, MainActivity.class));


    }

}
