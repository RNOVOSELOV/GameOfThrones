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

/**
 * Класс для генерации аватара персонажа, так как апи пока н епредоставляет его
 */
public class GotAvatarProcessor {
    private final TextPaint mPaint = new TextPaint();
    private final Rect mBounds = new Rect();
    private final Canvas mCanvas = new Canvas();
    private TypedArray mColors;
    private int mTileLetterFontSize;
    private Bitmap mDefaultBitmap;
    private int mWidth;
    private int mHeight;
    String key;

    /**
     * Конструктор
     *
     * @param width  ширина иконки
     * @param height высота иконки
     */
    public GotAvatarProcessor(int width, int height) {
        key = Calendar.getInstance().toString();
        this.mWidth = width;
        this.mHeight = height;
        mColors = null;
        mPaint.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        mPaint.setColor(Color.WHITE);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setAntiAlias(true);
        mTileLetterFontSize = 32;
        mDefaultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    }

    /**
     * Метод задаем массив цветов, из которых будет выбираться случайный цвет для аватарки.
     * Либо цвет на основании ключа при использовании setStaticColorGeneratorKey(String key).
     *
     * @param context    контекст для получения списка цветов.
     * @param colorArray идентификатор ресурса массива цветов из R.array
     * @return
     */
    public GotAvatarProcessor setColorsArray(Context context, int colorArray) {
        final Resources res = context.getResources();
        mColors = res.obtainTypedArray(colorArray);
        return this;
    }

    /**
     * Метод для задания статического ключа выбора цвета из массива цветов. Если не задать,
     * то цвет будет выбираться на основании времени генерации иконки. Задавая статический ключ исключаем
     * возможность появления разных цветов фона для одного и того же персонажа
     *
     * @param key статический ключ типа {@link String} для выбора цвета из массива цветов
     * @return
     */
    public GotAvatarProcessor setStaticColorGeneratorKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Метод задает цвет для букв в аватарке (по умолчанию белый)
     *
     * @param color цвет
     * @return
     */
    public GotAvatarProcessor setTextColor(int color) {
        mPaint.setColor(color);
        return this;
    }

    /**
     * Метод задает размер шрифта в иконке.
     *
     * @param context  контекст.
     * @param fontSize идентификатор, указывающий на размер шрифта в sp
     * @return
     */
    public GotAvatarProcessor setTextFontSize(Context context, int fontSize) {
        final Resources res = context.getResources();
        mTileLetterFontSize = res.getDimensionPixelSize(fontSize);
        return this;
    }


    /**
     * Метод получает строку, из которой получаем буквы для вставки в иконку.
     * Если строка состоит из одного слова, то вставляется первая буква слова.
     * Если строка сотоит из двух или более слов, то вставляются первые буквы первого и второго слов.
     *
     * @param displayName строка из которой выбираются буквы для иконки.
     * @return
     */
    public GotAvatarProcessor getLetterTile(String displayName) {
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
        c.drawColor(pickColor());

        mPaint.setTextSize(mTileLetterFontSize);
        mPaint.getTextBounds(mChars, 0, mChars.length, mBounds);
        c.drawText(mChars, 0, mChars.length, (float) (mWidth / 2), (float) (mHeight / 2 + (mBounds.bottom - mBounds.top) / 2), mPaint);
        mDefaultBitmap = bitmap;
        return this;
    }

    /**
     * Метод для выбора цвета из массива цветов. Если массив не указан, по умолчанию используется черный цвет.
     *
     * @return
     */
    private int pickColor() {
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

    /**
     * Метод для преобразования иконки в круг.
     *
     * @return
     */
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

    /**
     * Метод возвращает сформированную иконку
     *
     * @return сформированный обьект типа {@link Bitmap}
     */
    public Bitmap process() {
        return mDefaultBitmap;
    }
}
