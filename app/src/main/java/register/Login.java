package register;

/* Clase Login: conecta el usuario con el servidor obteniendo la id GCM
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


        import com.example.miguel.appdeportes.MainActivity;
        import com.example.miguel.appdeportes.R;

        import preferences.Preferences;
        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.DialogInterface.OnCancelListener;
        import android.content.pm.ActivityInfo;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

public class Login extends Fragment implements OnClickListener {
    //public class Login extends Fragment {
    private EditText editTextUsername;
    private EditText editTextPassword;

    //private String NEW_ACCOUNT_PAGE = "http://mobilecontrol.net84.net/loginMobile.php";
    private String NEW_ACCOUNT_PAGE = "http://www.appdeportesprueba.esy.es/mobile/loginMobile.php";
    private LoginServer loginServer;

    //public void onCreate(Bundle savedInstanceState) {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.login);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        View view;

        if(Preferences.getUsuConectado(getActivity())){

            view = inflater.inflate(R.layout.login2, container, false);

        }else{
            view = inflater.inflate(R.layout.login, container, false);
        }


        return view;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);



        editTextUsername = (EditText) getView().findViewById(R.id.accountUsername);
        editTextPassword = (EditText) getView().findViewById(R.id.accountPassword);

        if(Preferences.getUsuConectado(getActivity())){

			/*Boton cerrar sesion*/
            Button btnCerrarSesion = (Button) getView().findViewById(R.id.btnCerrarSesion);
            btnCerrarSesion.setOnClickListener(this);

			/*Boton cancelar registro*/
            Button registerButton = (Button) getView().findViewById(R.id.registerButton);
            registerButton.setOnClickListener(this);

        }else{
			 /*Boton registro*/
            Button acceptButton = (Button) getView().findViewById(R.id.accountAcceptButton);
            acceptButton.setOnClickListener(this);


				/*Boton cancelar registro*/
            Button registerButton = (Button) getView().findViewById(R.id.registerButton);
            registerButton.setOnClickListener(this);
        }


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
     * Conecta el usuario con el servidor.
     * @param
     */
    private void login() {
		/*Crea una nueva instancia para generar el hilo*/
        loginServer = new LoginServer ();

		/*Parsea los datos del usuario*/
        String name = editTextUsername.getText().toString();
        String pass = editTextPassword.getText().toString();

		/*Comprobacion de los campos*/
        if (!pass.equals("") && !name.equals("")) {
            String s = creaMD5 (pass);

            loginServer.execute(name, s);
            //Caso algun campo vacio
        } else if (pass.equals("") || name.equals("")){
            //Toast.makeText(Login.this, R.string.accountSecondToastMessage,
            //		Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * Comprueba si un boton ha sido pulsado
     * @param v view que contiene el identificador del boton
     * @return
     */
    public void onClick(View v) {

        int id = v.getId();
        if(Preferences.getUsuConectado(getActivity())){
            //cerrar sesion
            if (id == R.id.btnCerrarSesion) {


                Preferences.setUsuConectado(getActivity(), false);

                Intent intent=new Intent("register.CLOSESESSION");
                startActivity(intent);
                getActivity().finish();

                //ir a la ventana de registro
            } else if (id == R.id.registerButton){
                Intent intent=new Intent("register.REGISTER");
                startActivity(intent);
            }
            //Login
        }else{

            if (id == R.id.accountAcceptButton) {
                login();
            } else if (id == R.id.accountCancelButton) {
                getActivity().finish();
            } else if (id == R.id.registerButton){
                Intent intent=new Intent("register.REGISTER");
                startActivity(intent);
            }
        }


    }

    /**
     * Clase NuevoUsuServer: añade un usuario al servidorr.
     * @author Miguel Rojo Esteva
     */
    private class LoginServer extends AsyncTask<Object, String, Boolean> {
        private String DEBUG_TAG ="loginAppDeportes";
        private ProgressDialog waitDialog;

        /**
         * Muestra un mensaje mientras se añade el usuario
         * @see android.os.AsyncTask#onPreExecute()
         */
        protected void onPreExecute() {

            waitDialog = ProgressDialog.show(getActivity(), "Por favor, espere", "Comprobando los datos del usuario", true);
            waitDialog.setCancelable(false);
			/*	waitDialog.setOnCancelListener(new OnCancelListener() { 	@Override
				public void onCancel(DialogInterface arg0) {
					Log.d(DEBUG_TAG, "onCancel inside onPreExecute()");
					LoginServer.this.cancel(true);

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
                Log.d(DEBUG_TAG, "Login realizado con éxito.");

				/*Si el registro se ha efectuado con exito -> lo registra en GCM, si no -> muestra el error*/
                if (result) {
                    //Registra el dispositivo en GCM
                    Intent intent =new Intent("register.REGISTERGCM");
                    startActivity(intent);
                    getActivity().finish();

                }else{
                    //Toast.makeText(Login.this, "Error al realizar el login. Usuario o contraseña incorrectos.",
                    //	Toast.LENGTH_LONG).show();
                }
            } else
                Log.d(DEBUG_TAG, "Inside onPostExecute(), but cancelled.");



        }

        @Override
        protected Boolean doInBackground(Object... params) {
            boolean result=false;

            if (Preferences.getRedDisponible( getActivity()))
                result = postSettingsToServer((String)params[0], (String)params[1]);
            return result;
        }


        private boolean postSettingsToServer(String username, String password) {
            boolean state = false;

			/*Rellena los campos de la url: usu y pass*/
            Vector<NameValuePair> vars = new Vector<NameValuePair>();
            vars.add(new BasicNameValuePair(Preferences.CORREO_KEY, username));
            vars.add(new BasicNameValuePair(Preferences.PASSUSU_KEY, password));

			/*Crea la url*/
            String url = NEW_ACCOUNT_PAGE + "?"
                    + URLEncodedUtils.format(vars, null);

            HttpGet request = new HttpGet(url);

            Log.d(DEBUG_TAG, "url: "+url);


            try {
				/*Envia la peticion al servidor con la url*/
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                HttpClient client = new DefaultHttpClient();
                String responseBody = client.execute(request, responseHandler);
                Log.d(DEBUG_TAG, "responsebody: "+responseBody);

                //Caso de error: el usuario ya existe
                if(responseBody != null &&  responseBody.length()==2){
                    int valor = Integer.parseInt(responseBody);
                    if (valor == -1){
                      //  Toast.makeText(getActivity(),R.string.accountLoginErrorToastMessage,Toast.LENGTH_SHORT).show();
                        state = false;
                    }else{
                        state = true;
                    }
                    //Caso registro correcto
                }else if (responseBody != null && responseBody.length() > 0){
                    Preferences.setNombreUsuario( getActivity(), username);
                    Preferences.setUsuConectado( getActivity(), true);
                    //Pone a 0 el tiempo para asegurar el registro
                    Preferences.setDateIdGCM( getActivity(), 0);
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
