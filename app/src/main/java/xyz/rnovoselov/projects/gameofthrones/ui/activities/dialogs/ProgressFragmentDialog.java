package xyz.rnovoselov.projects.gameofthrones.ui.activities.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import xyz.rnovoselov.projects.gameofthrones.R;

/**
 * Created by roman on 16.10.16.
 */

public class ProgressFragmentDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.progress_splash, null);
        return new AlertDialog.Builder(getActivity(), R.style.progress_theme)
                .setCancelable(false)
                .setView(view)
                .create();
    }
}
