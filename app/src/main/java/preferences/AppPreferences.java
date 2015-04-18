package preferences;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.miguel.appdeportes.R;

public class AppPreferences extends PreferenceFragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

}