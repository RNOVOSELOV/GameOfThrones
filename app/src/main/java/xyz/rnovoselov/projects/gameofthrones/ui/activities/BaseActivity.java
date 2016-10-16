package xyz.rnovoselov.projects.gameofthrones.ui.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import xyz.rnovoselov.projects.gameofthrones.ui.activities.dialogs.ProgressFragmentDialog;
import xyz.rnovoselov.projects.gameofthrones.utils.ConstantManager;

/**
 * Created by roman on 12.10.16.
 */

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = ConstantManager.TAG_PREFIX + BaseActivity.class.getSimpleName();
    private final static String DIALOG_PROGRESS = "DAILOG_PROGRESS";
    protected ProgressFragmentDialog mProgressFragmentDialog;


    public void showProgress() {

        FragmentManager manager = getSupportFragmentManager();
        if (mProgressFragmentDialog == null) {
            mProgressFragmentDialog = new ProgressFragmentDialog();
            mProgressFragmentDialog.setCancelable(false);
        }
        mProgressFragmentDialog.show(manager, DIALOG_PROGRESS);
    }

    public void hideProgress() {
        if (mProgressFragmentDialog != null && !mProgressFragmentDialog.isHidden()) {
            mProgressFragmentDialog.dismiss();
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
