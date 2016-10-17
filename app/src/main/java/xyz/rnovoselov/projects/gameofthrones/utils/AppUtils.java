package xyz.rnovoselov.projects.gameofthrones.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by novoselov on 17.10.2016.
 */

public class AppUtils {
    public static void changeSearchViewTextColor(View view, int color) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(color);
                return;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i), color);
                }
            }
        }
    }
}
