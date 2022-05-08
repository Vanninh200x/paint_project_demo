package com.example.test_paint_company;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DrawView extends View {
    private static final float TOUCH_TOLERANCE = 4;
    private float mX, mY;
    private Path mPath;

    private Paint mPaint;

    private ArrayList<Stroke> paths = new ArrayList<>();
    private int currentColor;
    private int strokeWidth;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPaint.setAlpha(0xff);
    }

    public void init(int height, int width) {
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        currentColor = Color.BLACK;
        strokeWidth = 20;
    }

    public void setColor(int color){
        currentColor = color;
    }

    public void setStrokeWidth(int width){
        strokeWidth = width;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        canvas.save();
        int backgroundColor = Color.WHITE;
        mCanvas.drawColor(backgroundColor);

        for (Stroke fp :
                paths) {
            mPaint.setColor(fp.getColor());
            mPaint.setStrokeWidth(fp.getStrokeWidth());

            mCanvas.drawPath(fp.getPath(), mPaint);
        }

        canvas.drawBitmap(mBitmap,0,0, mBitmapPaint);
        canvas.restore();
    }

    private void touchStart(float x, float y) {
        mPath = new Path();
        Stroke fp = new Stroke(currentColor, strokeWidth, mPath);
        paths.add(fp);

        //finally remove any curve or line from the path
        mPath.reset();
        //this methods sets the starting point of the line being drawn
        mPath.moveTo(x, y);
        //we save the current coordinates of the finger
        mX = x;
        mY = y;
    }

    //in this method we check if the move of finger on the
    // screen is greater than the Tolerance we have previously defined,
    //then we call the quadTo() method which actually smooths the turns we create,
    //by calculating the mean position between the previous position and current position
    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    //at the end, we call the lineTo method which simply draws the line until
    //the end position
    private void touchUp() {
        mPath.lineTo(mX, mY);
    }

    //the onTouchEvent() method provides us with the information about the type of motion
    //which has been taken place, and according to that we call our desired methods
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }

        return true;
    }
}
