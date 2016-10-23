package xyz.rnovoselov.projects.gameofthrones.ui.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.rnovoselov.projects.gameofthrones.BuildConfig;
import xyz.rnovoselov.projects.gameofthrones.R;
import xyz.rnovoselov.projects.gameofthrones.data.network.res.HouseModelRes;
import xyz.rnovoselov.projects.gameofthrones.data.network.res.PersonModelRes;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.House;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.Person;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.Titles;
import xyz.rnovoselov.projects.gameofthrones.mvp.presenters.ISplashPresenter;
import xyz.rnovoselov.projects.gameofthrones.mvp.presenters.SplashPresenter;
import xyz.rnovoselov.projects.gameofthrones.mvp.views.ISplashView;
import xyz.rnovoselov.projects.gameofthrones.utils.ConstantManager;

public class SplashScreenActivity extends BaseActivity
        implements ISplashView {

    private static final String TAG = ConstantManager.TAG_PREFIX + SplashScreenActivity.class.getSimpleName();

    private SplashPresenter mPresenter = SplashPresenter.getInstance();

    @BindView(R.id.splash_coordinator)
    CoordinatorLayout mCoordinatorLayout;

    //region ================= LIFECYCLE =================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            //сохранять массивы на случай поворотов
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.takeView(this);
        mPresenter.initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.dropView();
    }

    //endregion

    //region ================= ISplashView =================

    @Override
    public void showMessage(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showError(Throwable error) {
        if (BuildConfig.DEBUG) {
            showMessage(error.getMessage());
            error.printStackTrace();
        } else {
            showMessage("Извините, что то пошло не так, попробуйте позже");
        }
    }

    @Override
    public void showLoad() {
        showProgress();
    }

    @Override
    public void hideLoad() {
        hideProgress();
    }

    @Override
    public ISplashPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void startMainActivity(ConstantManager.SYNC_DATA_ERRORS syncError) {
        startActivity(HouseListActivity.newIntent(this, syncError));
        ActivityCompat.finishAfterTransition(this);
    }

    //endregion
}