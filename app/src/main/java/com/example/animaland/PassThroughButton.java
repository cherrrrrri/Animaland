package com.example.animaland;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class PassThroughButton extends androidx.appcompat.widget.AppCompatButton{

    private Bitmap mBitmap;

    public PassThroughButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e("H3c", "csd");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int color = mBitmap.getPixel((int) event.getX(), (int) event.getY());
            Log.e("H3c", "cl.." + color);
            if (color == 0||color==-11058385) {
                return false;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w == 0 && h == 0 && oldw == 0 && oldh == 0) {
            super.onSizeChanged(w, h, oldw, oldh);
        } else {
            final StateListDrawable bkg = (StateListDrawable) getBackground();
            mBitmap = Bitmap.createScaledBitmap(
                    ((BitmapDrawable) bkg.getCurrent()).getBitmap(),
                    getWidth(), getHeight(), true);
        }
    }
}