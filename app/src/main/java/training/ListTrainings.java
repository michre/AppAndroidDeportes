package training;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.miguel.appdeportes.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import database.DatabaseAdapter;
import database.Training;


public class ListTrainings extends Activity {

    private ListView listView;
    private String DEBUG_TAG ="debug_listTrainings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.lista_entrenamientos);

        this.listView = (ListView) findViewById(R.id.listApps);

        CargaListaEntrenamientos cargaEntrenamientos = new CargaListaEntrenamientos ();
        cargaEntrenamientos.execute();
    }

    private class CargaListaEntrenamientos extends AsyncTask<Object, String, Boolean> {
        private String DEBUG_TAG ="debug";
        private ProgressDialog waitDialog;

        /**
         * Muestra un mensaje mientras se añaden los entrenamientos
         * @see android.os.AsyncTask#onPreExecute()
         */
        protected void onPreExecute() {

            waitDialog = ProgressDialog.show(ListTrainings.this, "Por favor, espere", "Cargando la lista de entrenamientos", true, true);
            waitDialog.setCancelable(false);

        }

        /**
         * Comprueba si el procedimiento ha finalizado con exito
         * @see android.os.AsyncTask#onPreExecute()
         */
        protected void onPostExecute(Boolean result) {
            waitDialog.dismiss();
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            boolean result=false;

            result = cargaApps();

            return result;
        }


        private boolean cargaApps() {

            final List<Training> listaApps = new ArrayList<Training>();


            //Obtiene la lista de las aplicaciones instaladas en el sistema
           // PackageManager pm = getPackageManager();
           // List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);


            //Base de datos
            DatabaseAdapter db = new DatabaseAdapter(ListTrainings.this);
            db.open();
            final List<Training> listEntrenamientos = db.getAllTrainings();

            db.close();
           /* for (ApplicationInfo packageInfo : packages) {
                packageInfo.loadLabel(getPackageManager()).toString();
                Resources res = getResources();
                Drawable drawable;
                String nombreApp = packageInfo.loadLabel(getPackageManager()).toString();

                nombreApp = nombreApp.replace("'", "&quot");

                if(nombreApp!=null && packageInfo.packageName!=null ){
                    //Si la aplicacion esta en el filtro -> la elimina del filtro
                    if(db.existeAplicacionFiltro(nombreApp, packageInfo.packageName)){
                        drawable = res.getDrawable( R.drawable.cancelar2 );

                        //Si no esta -> la a�ade
                    }else{
                        drawable = res.getDrawable( R.drawable.aceptar2 );


                    }

                    //db.close();

                    listaApps.add(new AppInfo(nombreApp, packageInfo.packageName, packageInfo.name, packageInfo.loadIcon(getPackageManager()), drawable));
                }
            }
            db.close();*/

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListTrainings.this.listView.setAdapter(
                            new ListTrainingAdapter(ListTrainings.this, listEntrenamientos));

                    // Ejecutado al hacer clic sobre un elemento
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapter, View view,
                                                int position, long arg) {
                            //Obtiene el elemento a editar
                            Training item = (Training) listView.getAdapter().getItem(position);

                            Intent intent=new Intent("actions.PICKERCALIFICATION");
                            //Intent intent = new Intent(context, PickerCalification.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Log.d("recAction", "recibido: " + item.getId());
                            intent.putExtra("idTrainingPlayer", item.getId());
                            ListTrainings.this.startActivity(intent);

                        }
                    });
                }
            });



            return true;
        }

    }
}
