package database;

/**
 * Clase DatabaseAdapter: base de datos de la aplicación con todas sus tablas.
 */

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseAdapter {
	
	//variables de la tabla
	private static final String TABLA_EQUIPOS = "teamTable";
	public static final String ID = "id_training_player";
	public static final String TRAINING_DATE = "date";
	public static final String TRAINING_PLACE = "place";
	public static final String TRAINING_TEAM = "team";

	private static final String DATABASE_NAME = "aplicaciones.db";
	private static final int DATABASE_VERSION = 3;

	private DatabaseHelper helper;
    private Context context;
	private SQLiteDatabase db;

	public DatabaseAdapter(Context context) {
		this.context = context;
        helper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		public void onCreate(SQLiteDatabase db) {
			createTable(db);
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLA_EQUIPOS);
			/*db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUNDS);*/
			createTable(db);
		}

		private void createTable(SQLiteDatabase db) {
			//Crea la tabla de las aplicaciones (filtro)
			
			String sql = "CREATE TABLE " + TABLA_EQUIPOS + " (" + ID
					+ " TEXT PRIMARY KEY, " + TRAINING_TEAM
					+ " TEXT, " + TRAINING_DATE
                    + " TEXT, " + TRAINING_PLACE + " TEXT);";


			/*String str2 = "CREATE TABLE " + TABLE_USERS + " (" + ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME
					+ " TEXT UNIQUE, " + PASSWORD + " TEXT);";
			*/
			
			try {
				db.execSQL(sql);
				
				/*db.execSQL(str2);*/
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public DatabaseAdapter open() throws SQLException {
		db = helper.getWritableDatabase();
		return this;
	}

	public void close() {
		db.close();
	}
	
	/*******************************************
	 * 				CONSULTAS
	 *******************************************/
	/**
	 * Inserta un nuevo entrenamiento
	 * @param id id del entrenamiento
	 * @param team nombre del equipo
	 * @param date fecha del entrenamiento
	 * @param place lugar del entrenamiento
	 */
	public long insertTraining (String id, String team,
                                  String date, String place) {
		ContentValues values = new ContentValues();
		values.put(ID, id);
		values.put(TRAINING_DATE, date);
		values.put(TRAINING_PLACE, place);
		values.put(TRAINING_TEAM, team);

        if(!existePartidaEnJuego(id)) {
            long rowInserted = db.insert(TABLA_EQUIPOS, null, values);
            if(rowInserted != -1)
                Toast.makeText(context, "New row added, row id: " + rowInserted, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, "Something wrong", Toast.LENGTH_SHORT).show();

            return rowInserted;
        }else{
            return 0;
        }
	}

    /**
     * Devuelve una lista con todos los entrenamientos
     * @return lista de entrenamientos
     */
    public ArrayList<Training> getAllTrainings() {
        ArrayList<Training> list = new ArrayList<Training>();
        String sql = "SELECT " + ID + ", " + TRAINING_DATE + ", "
                + TRAINING_PLACE + ", " + TRAINING_TEAM + " " + "FROM " + TABLA_EQUIPOS;
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndex(ID);
            int date = cursor.getColumnIndex(TRAINING_DATE);
            int place = cursor.getColumnIndex(TRAINING_PLACE);
            int team = cursor.getColumnIndex(TRAINING_TEAM);

            do {
                list.add( new Training(cursor.getString(date),
                        cursor.getString(place),
                        cursor.getString(team),
                        cursor.getString(id)));
            } while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * Comprueba si existe un entrenamiento
     * @param id id de la partida
     * @return true si se existe, false en caso contrario
     */
    public boolean existePartidaEnJuego(String id) {
        String[] campos = new String[] {"id_training_player"};
        String[] args = new String[] {""+id};

        Cursor cursor = db.query(TABLA_EQUIPOS, campos, "id_training_player=?", args, null, null, null);

        int count = cursor.getCount();

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        if (count == 0)
            return false;
        else
            return true;
    }

	/**
	 * Elimina un entrenamiento
	 * @param id id del entrenamiento
	 */
	public boolean removeTraining (String id) {
		
		db.delete(TABLA_EQUIPOS,ID+"='"+id+ "'", null);
		return db.delete(TABLA_EQUIPOS, ID+"='"+id+ "'", null) > 0;
	
	}
}
