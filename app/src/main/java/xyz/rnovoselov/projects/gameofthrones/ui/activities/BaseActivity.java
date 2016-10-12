package xyz.rnovoselov.projects.gameofthrones.ui.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import xyz.rnovoselov.projects.gameofthrones.R;
import xyz.rnovoselov.projects.gameofthrones.utils.ConstantManager;

/**
 * Created by roman on 12.10.16.
 */

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = ConstantManager.TAG_PREFIX + BaseActivity.class.getSimpleName();
    protected ProgressDialog mProgressDialog;

    public void showProgress () {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this, R.style.custom_dialog);
            mProgressDialog.setCancelable(false);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.progress_splash);
        } else {
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.progress_splash);
        }
    }

    public void hideProgress () {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    public void showError(String message, Exception error) {
        showToast(message);
        Log.e(TAG, String.valueOf(error));
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
