package xyz.rnovoselov.projects.gameofthrones.ui.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.rnovoselov.projects.gameofthrones.R;
import xyz.rnovoselov.projects.gameofthrones.data.managers.DataManager;
import xyz.rnovoselov.projects.gameofthrones.data.network.res.HouseModelRes;
import xyz.rnovoselov.projects.gameofthrones.utils.ConstantManager;
import xyz.rnovoselov.projects.gameofthrones.utils.NetworkStatusChecker;

public class SplashScreenActivity extends BaseActivity {

    private static final String TAG = ConstantManager.TAG_PREFIX + SplashScreenActivity.class.getSimpleName();
    @BindView(R.id.splash_coordinator)
    CoordinatorLayout mCoordinatorLayout;

    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mDataManager = DataManager.getInstance();
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showProgress();
        getHouse(362);
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void getHouse(final int id) {
        if (NetworkStatusChecker.isNetworkAvailable(this)) {

            Call<HouseModelRes> call = mDataManager.getHouse(String.valueOf(id));
            call.enqueue(new Callback<HouseModelRes>() {
                @Override
                public void onResponse(Call<HouseModelRes> call, Response<HouseModelRes> response) {
                    if (response.code() == 200) {
                        showSnackbar(response.body().toString());
                        Log.d(TAG, response.body().toString());
                    } else if (response.code() == 404) {
                        showSnackbar("Дом не обнаружен");
                    } else {
                        showSnackbar("Ошибка получения информации о доме " + String.valueOf(id));
                    }
                    hideProgress();
                }

                @Override
                public void onFailure(Call<HouseModelRes> call, Throwable t) {
                    // TODO обработать ошибки
                    hideProgress();
                }
            });
        }
    }
}
