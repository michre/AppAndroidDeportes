package main;



/**
 * Clase Inicio: contiene la pagina inicial que se mostrará en la interfaz
 * @author Miguel Rojo Esteva
 */


        import android.app.NotificationManager;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.NotificationCompat;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.View.OnClickListener;
        import android.widget.Button;

        import com.example.miguel.appdeportes.R;

        import database.DatabaseAdapter;

public class Inicio extends Fragment implements OnClickListener{

    private ViewGroup container;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.inicio, container, false);
        container = container;

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);


	        /*Boton crea notificacion*/
        Button creaNoti = (Button) getView().findViewById(R.id.btnCreateNotify);
        creaNoti.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnCreateNotify){

            Intent intent=new Intent("training.LISTTRAININGS");
            //Intent intent = new Intent(context, PickerCalification.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Inicio.this.startActivity(intent);
         /*   NotificationManager nManager = (NotificationManager)  getActivity().getSystemService( getActivity().NOTIFICATION_SERVICE);
            NotificationCompat.Builder ncomp = new NotificationCompat.Builder( getActivity());
            ncomp.setContentTitle("Ejemplo de notificación");
            ncomp.setContentText("Consulte la web para comprobar el funcionamiento");
            ncomp.setTicker("Titulo notificación");
          //  ncomp.setSmallIcon(R.drawable.logo);
            ncomp.setSmallIcon(R.drawable.common_signin_btn_icon_dark);
            ncomp.setAutoCancel(true);
            nManager.notify((int)System.currentTimeMillis(),ncomp.build());*/
        }

    }

}

