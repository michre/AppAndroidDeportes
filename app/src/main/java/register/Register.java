package register;

/**
 * Clase Register: registra un usuario en el servidor
 * @author
 */


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

import preferences.Preferences;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.miguel.appdeportes.R;

public class Register extends Activity implements OnClickListener {
    private EditText editTextName;
    private EditText editTextLastName;
    private EditText editTextPhone;
    private EditText editTextMail;

    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextConfirmedPassword;

    private String NEW_ACCOUNT_PAGE = "http://www.appdeportesprueba.esy.es/mobile/registro.php";
    private NuevoUsuServer nuevoUsuServ;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        editTextName = (EditText) findViewById(R.id.accountName);
        editTextLastName = (EditText) findViewById(R.id.accountLastName);
        editTextPhone = (EditText) findViewById(R.id.accountPhone);
        editTextMail = (EditText) findViewById(R.id.accountMail);
        editTextUsername = (EditText) findViewById(R.id.accountUsername);
        editTextPassword = (EditText) findViewById(R.id.accountPassword);
        editTextConfirmedPassword = (EditText) findViewById(R.id.accountConfirmedPassword);

		/*Boton registro*/
        Button acceptButton = (Button) findViewById(R.id.accountAcceptButton);
        acceptButton.setOnClickListener(this);

		/*Boton cancelar registro*/
        Button cancelButton = (Button) findViewById(R.id.accountCancelButton);
        cancelButton.setOnClickListener(this);

    }

    /**
     * Añade un usuario
     */

    public void addUser(){
        Toast.makeText(Register.this, "Usuario añadido correctamente.",
                Toast.LENGTH_LONG).show();


    }

    /**
     * Crea md5 a partir de una cadena
     * @param string
     * @return cadena con md5
     * @throws UnsupportedEncodingException
     */
    public static String creaMD5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * Crea una nueva cuenta de usuario en el servidor.
     * @param
     */
    private void newAccount() {
		/*Crea una nueva instancia para generar el hilo*/
        nuevoUsuServ = new NuevoUsuServer ();

		/*Parsea los datos del usuario*/
        String userName = editTextUsername.getText().toString();
        String pass = editTextPassword.getText().toString();
        String confPass = editTextConfirmedPassword.getText().toString();
        String name = editTextName.getText().toString();
        String lastName =  editTextLastName.getText().toString();
        String phone = editTextPhone.getText().toString();
        String mail = editTextMail.getText().toString();

		/*Comprobacion de los campos*/
        if (!pass.equals("") && !name.equals("") && pass.equals(confPass)) {
            String s = creaMD5 (pass);

            nuevoUsuServ.execute(name, lastName, phone, mail,userName, s);
            //Caso algun campo vacio
        } else if (pass.equals("") || confPass.equals("") || name.equals("")){
            Toast.makeText(Register.this, R.string.accountSecondToastMessage,
                    Toast.LENGTH_SHORT).show();
            //Caso las contraseñas no coinciden
        }else if (!pass.equals(confPass)){
            Toast.makeText(Register.this, R.string.accountThirdToastMessage,
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Comprueba si un boton ha sido pulsado
     * @param v view que contiene el identificador del boton
     * @return
     */
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.accountAcceptButton) {
            newAccount();
        } else if (id == R.id.accountCancelButton) {
            finish();
        }
    }

    /**
     * Clase NuevoUsuServer: añade un usuario al servidorr.
     * @author Miguel Rojo Esteva
     */
    private class NuevoUsuServer extends AsyncTask<Object, String, Boolean> {
        private String DEBUG_TAG ="debug";
        private ProgressDialog waitDialog;

        /**
         * Muestra un mensaje mientras se añade el usuario
         * @see android.os.AsyncTask#onPreExecute()
         */
        protected void onPreExecute() {

            waitDialog = ProgressDialog.show(Register.this, "Por favor, espere", "Comprobando si el usuario está disponible", true, true);
            waitDialog.setCancelable(false);

			/*waitDialog.setOnCancelListener(new OnCancelListener() { 	@Override
				public void onCancel(DialogInterface arg0) {
					Log.d(DEBUG_TAG, "onCancel inside onPreExecute()");
					NuevoUsuServer.this.cancel(true);

				}
			});*/
        }

        /**
         * Comprueba si el procedimiento ha finalizado con exito
         * @see android.os.AsyncTask#onPreExecute()
         */
        protected void onPostExecute(Boolean result) {
            waitDialog.dismiss();

            if (!isCancelled()) {
                Log.d(DEBUG_TAG, "Nuevo usuario añadido.");

				/*Si el registro se ha efectuado con exito -> lo registra en GCM, si no -> muestra el error*/
                if (result) {
                    addUser();
                    //Registra el dispositivo en GCM
                    //Pone a 0 el tiempo para asegurar el registro
                    Preferences.setDateIdGCM(Register.this, 0);
                    Intent intent =new Intent("register.REGISTERGCM");
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(Register.this, "Error al realizar el registro. Usuario no disponible.",
                            Toast.LENGTH_LONG).show();
                }
            } else
                Log.d(DEBUG_TAG, "Inside onPostExecute(), but cancelled.");


        }

        @Override
        protected Boolean doInBackground(Object... params) {
            boolean result=false;

            if (Preferences.getRedDisponible(Register.this))
                result = postSettingsToServer((String)params[0], (String)params[1],(String)params[2],
            (String)params[3],(String)params[4],(String)params[5]);
            return result;
        }

        private boolean postSettingsToServer(String name, String lastName, String phone, String mail,
                                             String username, String password) {
            boolean state = false;

			/*Rellena los campos de la url: usu y pass*/
            Vector<NameValuePair> vars = new Vector<NameValuePair>();
            vars.add(new BasicNameValuePair(Preferences.NOMBREUSU_KEY, name));
            vars.add(new BasicNameValuePair(Preferences.APELLIDOS_KEY, lastName));
            vars.add(new BasicNameValuePair(Preferences.PHONE_KEY, phone));
            vars.add(new BasicNameValuePair(Preferences.CORREO_KEY, mail));
            vars.add(new BasicNameValuePair(Preferences.USUARIO_KEY, username));
            vars.add(new BasicNameValuePair(Preferences.PASSUSU_KEY, password));

			/*Crea la url*/
            String url = NEW_ACCOUNT_PAGE + "?"
                    + URLEncodedUtils.format(vars, null);

            HttpGet request = new HttpGet(url);

            Log.d("debug", "url: "+url);


            try {
				/*Envia la peticion al servidor con la url*/
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                HttpClient client = new DefaultHttpClient();
                String responseBody = client.execute(request, responseHandler);
                Log.d("debug", "responsebody: "+responseBody);

                //Caso de error: el usuario ya existe
                if(responseBody != null &&  responseBody.length()==2){
                    int valor = Integer.parseInt(responseBody);
                    if (valor == -1){
                        state = false;
                    }else{
                        state = true;
                    }
                    //Caso registro correcto
                }else if (responseBody != null && responseBody.length() > 0){
                    Preferences.setCorreoUsuario(Register.this, mail);
                    Preferences.setUsuConectado(Register.this, true);
                    state = true;
                    //Caso error
                }else{
                    state=false;
                }

            } catch (ClientProtocolException e) {
                Log.e(DEBUG_TAG, "Failed to get playerId (protocol): ", e);
            } catch (IOException e) {
                Log.e(DEBUG_TAG, "Failed to get playerId (io): ", e);
            }

            return state;
        }



    }
}

