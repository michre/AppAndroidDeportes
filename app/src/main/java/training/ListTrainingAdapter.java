package training;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.miguel.appdeportes.R;

import java.util.List;

import database.Training;

public class ListTrainingAdapter extends BaseAdapter {

    private Context context;
    private List<Training> elementos;

    public ListTrainingAdapter(Context context, List<Training> elementos) {
        this.context = context;
        this.elementos = elementos;
    }

    @Override
    public int getCount() {
        return this.elementos.size();
    }

    @Override
    public Object getItem(int position) {
        return this.elementos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        //Crea una nueva vista dentro de la lista
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_item, parent, false);
        }

        //Establece la informacion de los elementos
        TextView tvTeam = (TextView) rowView.findViewById(R.id.team);
        TextView tvDate = (TextView) rowView.findViewById(R.id.date);
        TextView tvPlace = (TextView) rowView.findViewById(R.id.place);

        Training elemento = this.elementos.get(position);
        tvTeam.setText(elemento.getTeam());
        tvDate.setText(elemento.getDate());
        tvPlace.setText(elemento.getPlace());

        return rowView;
    }
}

