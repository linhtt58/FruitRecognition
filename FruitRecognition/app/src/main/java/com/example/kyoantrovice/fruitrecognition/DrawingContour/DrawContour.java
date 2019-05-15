package com.example.kyoantrovice.fruitrecognition.DrawingContour;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kyoantrovice.fruitrecognition.MenuSettings.HelpActivity;
import com.example.kyoantrovice.fruitrecognition.R;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class DrawContour extends AppCompatActivity {
    public static final String PREFERENCES_FILENAME = "MySharedPreference";
    public static final String DIALOGHELP_SHOWN = "dialogHelpShown";
    Drawable d;
    DrawingView mDrawingView;
    FrameLayout mDrawingPad;
    ProgressDialog progressDialog;
    Boolean dialogHelpShown = false;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDrawingView = new DrawingView(this);
        setContentView(R.layout.activity_draw_contour);

        progressDialog = new ProgressDialog(this);

        mDrawingPad = (FrameLayout) findViewById(R.id.view_drawing_pad);

        preferences = getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE);
        if(preferences.contains(DIALOGHELP_SHOWN)){
            dialogHelpShown = preferences.getBoolean(DIALOGHELP_SHOWN,false);
        }
        if(!dialogHelpShown){
            showHelpDialog();
        }

        Intent intent = getIntent();
        String picturePath = intent.getExtras().getString("rootImagePath");
        d = Drawable.createFromPath(picturePath);

        mDrawingPad.setBackgroundDrawable(d);
        mDrawingPad.addView(mDrawingView);
        mDrawingPad.setDrawingCacheEnabled(true);

        mDrawingPad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mDrawingView.touch_start(x, y);
                        mDrawingView.coordinate.setStartX(event.getX());
                        mDrawingView.coordinate.setStartY(event.getY());
                        mDrawingView.invalidate();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mDrawingView.touch_move(x, y);
                        mDrawingView.coordinate.setEndX(event.getX());
                        mDrawingView.coordinate.setEndY(event.getY());
                        mDrawingView.invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        mDrawingView.touch_up();
                        mDrawingView.coordinate.setEndX(event.getX());
                        mDrawingView.coordinate.setEndY(event.getY());
                        if (mDrawingView.coordinate.getStartX() != mDrawingView.coordinate.getEndX() && mDrawingView.coordinate.getStartY() != mDrawingView.coordinate.getEndY()) {
                            mDrawingView.mCanvas.drawLine(mDrawingView.coordinate.getStartX(), mDrawingView.coordinate.getStartY(), mDrawingView.coordinate.getEndX(), mDrawingView.coordinate.getEndY(), mDrawingView.mPaint);
                            showDialog();
                        }

                        if (mDrawingView.coordinate.getStartX() == mDrawingView.coordinate.getEndX() && mDrawingView.coordinate.getStartY() == mDrawingView.coordinate.getEndY()) {
                            showDialog();
                        }
                        Log.d("ACTION_Cancel", "Started");
                        mDrawingView.invalidate();
                        break;
                }
                return true;
            }
        });

    }

    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you done drawing contour images");

        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(DrawContour.this, "You clicked yes button", Toast.LENGTH_LONG).show();
                new SegmentImage().execute();
            }
        });

        alertDialogBuilder.setNegativeButton("Redraw", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDrawingPad.setBackgroundDrawable(d);
                Toast.makeText(DrawContour.this, "You clicked redraw button", Toast.LENGTH_SHORT).show();
                mDrawingView.clear();
                //finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = alertDialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP | Gravity.LEFT;
        wmlp.x = 100;   //x position
        wmlp.y = 100;   //y position

        alertDialog.show();
    }

    /*private void showDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle("Question");

        TextView text = (TextView) dialog.findViewById(R.id.titleDialog);
        text.setText("Are you done drawing contour?");

        // set the custom dialog components - button
        Button notshowBtn = (Button) dialog.findViewById(R.id.notshowBtn);
        notshowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button yesBtn = (Button) dialog.findViewById(R.id.yesBtn);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DrawContour.this, "You clicked yes button", Toast.LENGTH_LONG).show();
                new SegmentImage().execute();
            }
        });

        Button noBtn = (Button) dialog.findViewById(R.id.noBtn);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingPad.setBackgroundDrawable(d);
                Toast.makeText(DrawContour.this, "You clicked redraw button", Toast.LENGTH_SHORT).show();
                mDrawingView.clear();
            }
        });

        dialog.show();
    }*/

    private void showHelpDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you need help?");

        alertDialogBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(DrawContour.this, "You clicked no button", Toast.LENGTH_SHORT).show();
                preferences.edit().putBoolean(DIALOGHELP_SHOWN, true).commit();
            }
        });

        alertDialogBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(DrawContour.this, "You clicked yes button", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(DrawContour.this, HelpActivity.class);
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private class SegmentImage extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(DrawContour.this);
            progressDialog.setMessage("Recognizing Fruit...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Toast.makeText(DrawContour.this," Processing",Toast.LENGTH_SHORT).show();
            getImage();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Dismiss the progress dialog
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }


    public void getImage(){
        Bitmap mBitmap = mDrawingPad.getDrawingCache();
        if(mBitmap == null){
            Log.d("Image: ","Null");
        } else {
            Log.d("Image: ", mBitmap.toString());
        }
        File folder = new File(Environment.getExternalStorageDirectory().toString());
        boolean success = false;
        if (!folder.exists())
        {
            success = folder.mkdirs();
        }

        System.out.println(success+"folder");

        File file = new File(Environment.getExternalStorageDirectory().toString() + "/sample.png");

        if ( !file.exists() )
        {
            try {
                success = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println(success+"file");
        FileOutputStream fos = null;
        Bitmap contour = getContour(mBitmap);
        Log.d("Contour", contour.toString());
        try {
            fos = new FileOutputStream(file);
            System.out.println(fos);
            contour.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (NullPointerException e){
            e.printStackTrace();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private Bitmap convertToBinaryImage(Bitmap bitmap){
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        //List<MatOfPoint> contours = getContour(bitmap);
        return result;
    }

    private Bitmap getContour(Bitmap bitmap){
        for(int i = 0;i<bitmap.getHeight();i++){
            int num = 0;
            for(int j = 0;j<bitmap.getWidth();j++){
                if(bitmap.getPixel(j,i) == Color.BLUE){
                    num++;
                }
                if(num%2 == 1){
                    bitmap.setPixel(j,i,Color.WHITE);
                }
                else {
                    bitmap.setPixel(j,i,Color.BLACK);
                }
            }
        }
        return bitmap;
    }


}