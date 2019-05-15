package com.example.kyoantrovice.fruitrecognition.DrawingContour;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by KyoAntrovice on 4/2/2016.
 */
public class DrawingView extends View {
    int width;
    int height;
    Paint mPaint;
    Bitmap mBitmap;
    Canvas mCanvas;
    Path mPath;
    Paint mBitmapPaint;

    private float mX,mY;
    private static final float TOUCH_TOLERANCE = 4;

    Coordinate coordinate = new Coordinate();

    public static boolean isDraw = false;

    public DrawingView(Context context){
        super(context);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(2);

        mPath = new Path();
        mBitmapPaint = new Paint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        width =w;
        height =h;
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(mBitmap,0,0,mBitmapPaint);
        canvas.drawPath(mPath,mPaint);
    }

    public void touch_start(float x, float y){
        //mPath.reset()
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    public void touch_move(float x, float y){
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }

    public void touch_up() {
        mPath.lineTo(mX, mY);
        mCanvas.drawPath(mPath, mPaint);
        mPath.reset();
    }

    public void clear(){
        mBitmap = Bitmap.createBitmap(width,height ,
                Bitmap.Config.ARGB_8888);

        mCanvas = new Canvas(mBitmap);
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        //Added later..
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(2);
        invalidate();
    }
}

