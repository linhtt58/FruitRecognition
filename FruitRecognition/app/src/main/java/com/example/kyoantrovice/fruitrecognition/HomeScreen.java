package com.example.kyoantrovice.fruitrecognition;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.kyoantrovice.fruitrecognition.CameraFragament.CameraFragment;
import com.example.kyoantrovice.fruitrecognition.DrawingContour.DrawContour;
import com.example.kyoantrovice.fruitrecognition.MenuSettings.FeedbackActivity;
import com.example.kyoantrovice.fruitrecognition.MenuSettings.HelpActivity;
import com.example.kyoantrovice.fruitrecognition.MenuSettings.LanguageActivity;
import com.example.kyoantrovice.fruitrecognition.MenuSettings.MenuAboutus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeScreen extends AppCompatActivity {

    private static final int RESULT_LOAD_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        DisplayView display = new DisplayView();
        display.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.menu_help:
                Intent helpIntent = new Intent(HomeScreen.this, HelpActivity.class);
                startActivity(helpIntent);
                return true;
            case R.id.menu_feedback:
                Intent feedbackIntent = new Intent(HomeScreen.this, FeedbackActivity.class);
                startActivity(feedbackIntent);
                return true;
            case R.id.menu_language:
                Intent languageIntent = new Intent(HomeScreen.this, LanguageActivity.class);
                startActivity(languageIntent);
                return true;
            case R.id.menu_aboutus:
                Intent intent = new Intent(HomeScreen.this, MenuAboutus.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    // return images when choosing from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == RESULT_LOAD_CAMERA){
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                Log.d("picturePath",picturePath);
                cursor.close();

                Intent intentTransfer = new Intent(HomeScreen.this,DrawContour.class);
                intentTransfer.putExtra("rootImagePath",picturePath);
                startActivity(intentTransfer);

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void displayView(){
        // update the main content by replacing fragments
        Fragment fragment = null;
        fragment = new CameraFragment();
        if(fragment != null){
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit();
        }
    }

    private class DisplayView extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            displayView();
            return null;
        }
    }

}
