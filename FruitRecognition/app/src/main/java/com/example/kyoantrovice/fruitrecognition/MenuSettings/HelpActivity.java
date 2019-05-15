package com.example.kyoantrovice.fruitrecognition.MenuSettings;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kyoantrovice.fruitrecognition.MenuSettings.HelpAdapter.SlidingImage_Adapter;
import com.example.kyoantrovice.fruitrecognition.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HelpActivity extends AppCompatActivity {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES= {R.drawable.one,R.drawable.two,R.drawable.three,R.drawable.four
    ,R.drawable.five,R.drawable.six};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Init start = new Init();
        start.execute();
    }

    private void init(){
        for(int i = 0;i <IMAGES.length;i++){
            ImagesArray.add(IMAGES[i]);
            mPager = (ViewPager) findViewById(R.id.pager);
            mPager.setAdapter(new SlidingImage_Adapter(HelpActivity.this, ImagesArray));
            CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);

            indicator.setViewPager(mPager);

            final float density = getResources().getDisplayMetrics().density;

            //Set circle indicator radius
            indicator.setRadius(5 * density);

            NUM_PAGES =IMAGES.length;

            // Auto start of viewpager
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                @Override
                public void run() {
                    if(currentPage == NUM_PAGES){
                        currentPage = 0;
                    }
                    mPager.setCurrentItem(currentPage++, true);
                }
            };
            Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            },10000,10000);

            // Pager listener over indicator
            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    currentPage = position;
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    private class Init extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            init();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }
}
