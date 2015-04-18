package com.example.miguel.appdeportes;
/**
 * Clase MainActivity: actividad principal que contiene el paginador (paginas que se muestran en la interfaz)
 * @author Miguel Rojo Esteva
 */
import java.util.ArrayList;
import java.util.List;

import com.viewpagerindicator.TitlePageIndicator;

import database.DatabaseAdapter;
import main.Inicio;
import preferences.Preferences;
import register.Login;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends FragmentActivity
{
    //Clase que permite al usuario cambiar de pagina hacia la derecha e izquierda. Se usa junto a fragmentos
    private ViewPager viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicializa el paginador
        initPaging();

        //Usa una libreria externa para mostrar el nombre de la pagina en la parte superior para mejorar la experiencia de usuario
        TitlePageIndicator titleIndicator = (TitlePageIndicator)findViewById(R.id.indicator);
        titleIndicator.setViewPager(viewPager);
        titleIndicator.setCurrentItem(0);

    }


    /**
     * Inicializa paginador
     */
    private void initPaging() {
        //Crea tantos fragmentos como paginas dispondra la interfaz
        Inicio fragmentOne = new Inicio();
        Login fragmentTwo= new Login();

        //Crea un paginador y le añade los fragmentos
        Paginador pagerAdapter = new Paginador(getSupportFragmentManager());
        pagerAdapter.addPagina(fragmentOne);
        pagerAdapter.addPagina(fragmentTwo);

        //Establece el paginador en el viewPager
        viewPager = (ViewPager) super.findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerAdapter);
    }

    /**
     * Menu
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        //Comprueba si esta conectado -> si esta conectado muestra un menu con la opcion de cerrar sesion,
        // en caso contrario muestra el mismo menu sin esa opcion
        if(Preferences.getUsuConectado(this)){

            inflater.inflate(R.menu.menu_sesion, menu);

        }else{
            inflater.inflate(R.menu.menu, menu);
        }

        return true;
    }

    /**
     * Opciones del menu
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        //Comprueba si esta conectado -> como hay dos menus distintos (por la opcion de cerrar sesion) -> tambien hay capturas distintas
        if(Preferences.getUsuConectado(this)){

            switch (item.getItemId()) {
                case R.id.menuCerrarSesion:
                    Preferences.setUsuConectado(this, false);
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                    return true;
            }
        }else{
            switch (item.getItemId()) {
                case R.id.menuIniSesion:
                    Intent intent=new Intent("es.uam.eps.tfg.register.LOGIN");
                    startActivity(intent);
                    return true;
            }
        }
        return false;


    }

    /**
     * Clase que implementa el FragmentPagerAdapter necesario para la aplicacion
     *
     */
    class Paginador extends FragmentPagerAdapter{

        //Nombres que se asignara a los fragmentos: el fragmento 0 se llamara Inicio y el 1 login.
        private final String[] TITLES = new String[] {
                "Inicio",
                "Login"
        };
        public final int NUM_TITLES = TITLES.length;

        //Almacena la lista de los fragmentos disponibles
        private final List<Fragment> paginas = new ArrayList <Fragment> ();

        public Paginador (FragmentManager manager){
            super (manager);
        }

        //Añade un fragmento
        public void addPagina (Fragment pag){
            paginas.add(pag);
            notifyDataSetChanged();

        }

        //Devuelve el fragmento de la posicion arg0
        @Override
        public Fragment getItem(int arg0) {
            return paginas.get(arg0);
        }

        //Devuelve el numero de fragmentos
        @Override
        public int getCount() {
            return paginas.size();
        }

        //Devuelve el nombre asignado al fragmento
        @Override
        public CharSequence getPageTitle(int arg0) {
            return TITLES[arg0 % NUM_TITLES].toUpperCase();
        }
    }

}
