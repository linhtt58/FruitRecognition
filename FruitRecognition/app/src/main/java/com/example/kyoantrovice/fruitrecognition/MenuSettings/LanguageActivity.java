package com.example.kyoantrovice.fruitrecognition.MenuSettings;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kyoantrovice.fruitrecognition.HomeScreen;
import com.example.kyoantrovice.fruitrecognition.MenuSettings.LanguageCustomeListview.LanguageAdapter;
import com.example.kyoantrovice.fruitrecognition.MenuSettings.LanguageCustomeListview.LanguageTable;
import com.example.kyoantrovice.fruitrecognition.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanguageActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String[] nations = new String[] {"Vietnamese","English"};

    public static final Integer[] images = {R.drawable.vietnam_flag,R.drawable.english_flag};

    ListView listView;
    List<LanguageTable> languageTableList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        languageTableList = new ArrayList<LanguageTable>();
        for(int i = 0; i< nations.length; i++){
            LanguageTable languageTable = new LanguageTable(images[i],nations[i]);
            languageTableList.add(languageTable);
        }

        listView = (ListView) findViewById(R.id.list);
        LanguageAdapter adapter = new LanguageAdapter(this,R.layout.list_item_language,languageTableList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0){
            String languageToLoad = "vi";
            changeLanguage(languageToLoad);
            Toast.makeText(getApplicationContext(),"Vietnamese has been chosen",Toast.LENGTH_SHORT).show();
        } else if (position == 1){
            String languageToLoad = "en";
            changeLanguage(languageToLoad);
            Toast.makeText(getApplicationContext(),"English has been chosen",Toast.LENGTH_SHORT).show();
        }
    }

    public void changeLanguage(String languageToLoad){
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
    }

    public void changeActivity(){
        Intent intent = new Intent(LanguageActivity.this, HomeScreen.class);
        startActivity(intent);
    }

}
