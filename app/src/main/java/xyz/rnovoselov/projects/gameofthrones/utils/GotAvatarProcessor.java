package xyz.rnovoselov.projects.gameofthrones.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;

import java.util.Calendar;

import xyz.rnovoselov.projects.gameofthrones.R;

/**
 * Created by novoselov on 14.10.2016.
 */

public class GotAvatarProcessor {
    private final TextPaint mPaint = new TextPaint();
    private final Rect mBounds = new Rect();
    private final Canvas mCanvas = new Canvas();
    private TypedArray mColors = null;
    private int mTileLetterFontSize;
    private Bitmap mDefaultBitmap;
    private int mWidth;
    private int mHeight;

    public GotAvatarProcessor(Context context, int width, int height) {
        final Resources res = context.getResources();
        this.mWidth = width;
        this.mHeight = height;

        mPaint.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        mPaint.setColor(Color.WHITE);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setAntiAlias(true);

        mTileLetterFontSize = res.getDimensionPixelSize(R.dimen.font_small_12);
        mDefaultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    }

    public GotAvatarProcessor setColorsArray(Context context, int colorArray) {
        final Resources res = context.getResources();
        mColors = res.obtainTypedArray(colorArray);
        return this;
    }

    public GotAvatarProcessor setTextColor(int color) {
        mPaint.setColor(color);
        return this;
    }

    public GotAvatarProcessor setTextFontSize(Context context, int fontSize) {
        final Resources res = context.getResources();
        mTileLetterFontSize = res.getDimensionPixelSize(fontSize);
        return this;
    }

    public GotAvatarProcessor getLetterTile(String displayName) {
        String key = Calendar.getInstance().toString();
        final Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);

        final char[] mChars;
        if (displayName.isEmpty()) {
            return null;
        }
        String[] list = displayName.split(" ");
        if (list.length == 1) {
            mChars = new char[1];
            mChars[0] = Character.toUpperCase(list[0].charAt(0));
        } else {
            mChars = new char[2];
            mChars[0] = Character.toUpperCase(list[0].charAt(0));
            mChars[1] = Character.toUpperCase(list[1].charAt(0));
        }

        final Canvas c = mCanvas;
        c.setBitmap(bitmap);
        c.drawColor(pickColor(key));

        mPaint.setTextSize(mTileLetterFontSize);
        mPaint.getTextBounds(mChars, 0, mChars.length, mBounds);
        c.drawText(mChars, 0, mChars.length, 0 + mWidth / 2, 0 + mHeight / 2
                + (mBounds.bottom - mBounds.top) / 2, mPaint);
        mDefaultBitmap = bitmap;
        return this;
    }

    private static boolean isEnglishLetterOrDigit(char c) {
        return 'A' <= c && c <= 'Z' || 'a' <= c && c <= 'z' || '0' <= c && c <= '9';
    }

    private int pickColor(String key) {
        if (mColors == null) {
            return Color.BLACK;
        }
        final int index = Math.abs(key.hashCode()) % mColors.length();
        try {
            return mColors.getColor(index, Color.BLACK);
        } finally {
            mColors.recycle();
        }
    }

    public GotAvatarProcessor transformToCircle() {
        Bitmap source = mDefaultBitmap;
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);

        squaredBitmap.recycle();
        mDefaultBitmap = bitmap;
        return this;
    }

    public Bitmap process() {
        return mDefaultBitmap;
    }
}
