package xyz.rnovoselov.projects.gameofthrones.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.rnovoselov.projects.gameofthrones.R;
import xyz.rnovoselov.projects.gameofthrones.data.managers.DataManager;
import xyz.rnovoselov.projects.gameofthrones.data.network.res.HouseModelRes;
import xyz.rnovoselov.projects.gameofthrones.data.network.res.PersonModelRes;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.House;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.HouseDao;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.Person;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.PersonDao;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.Titles;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.TitlesDao;
import xyz.rnovoselov.projects.gameofthrones.utils.AppConfig;
import xyz.rnovoselov.projects.gameofthrones.utils.ConstantManager;
import xyz.rnovoselov.projects.gameofthrones.utils.NetworkStatusChecker;

public class SplashScreenActivity extends BaseActivity {

    private static final String TAG = ConstantManager.TAG_PREFIX + SplashScreenActivity.class.getSimpleName();
    @BindView(R.id.splash_coordinator)
    CoordinatorLayout mCoordinatorLayout;

    private DataManager mDataManager;
    private HouseDao mHouseDao;
    private TitlesDao mTitlesDao;
    private PersonDao mPersonDao;
    List<Person> persons = new ArrayList<>();
    List<House> houses = new ArrayList<>();


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mDataManager = DataManager.getInstance();
        ButterKnife.bind(this);

        mHouseDao = mDataManager.getDaoSession().getHouseDao();
        mTitlesDao = mDataManager.getDaoSession().getTitlesDao();
        mPersonDao = mDataManager.getDaoSession().getPersonDao();
    }

    @Override
    protected void onStart() {
        super.onStart();
        showProgress();
   //     processHouse(AppConfig.STARK_HOUSE_ID);
    //    processHouse(AppConfig.LANNISTER_HOUSE_ID);
        processHouse(AppConfig.TARGARYEN_HOUSE_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
                mHouseDao.insertOrReplaceInTx(houses);
                mPersonDao.insertOrReplaceInTx(persons);
            }
        }, 5000);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void processHouse(final int id) {
        if (NetworkStatusChecker.isNetworkAvailable(this)) {

            Call<HouseModelRes> call = mDataManager.getHouse(String.valueOf(id));
            call.enqueue(new Callback<HouseModelRes>() {
                @Override
                public void onResponse(Call<HouseModelRes> call, Response<HouseModelRes> response) {
                    if (response.code() == 200) {
                        houses.add(new House((long) id, response.body()));
                        List<String> housePersonUrls = response.body().getPersonsUrls();
                        for (String urlPerson : housePersonUrls) {
                            processPersonUrl(id, urlPerson);
                        }
                    } else if (response.code() == 404) {
                        showSnackbar("Дом не обнаружен");
                    } else {
                        showSnackbar("Ошибка получения информации о доме " + String.valueOf(id));
                    }
                }

                @Override
                public void onFailure(Call<HouseModelRes> call, Throwable t) {
                    // TODO обработать ошибки
                }
            });
        }
    }

    private List<Titles> getTitlesFromHouseResponse(int id, HouseModelRes model) {
        List<Titles> titles = new ArrayList<>();
        for (String title : model.getTitles()) {
            titles.add(new Titles((long) id, title));
        }
        return titles;
    }

    private void processPersonUrl(final int houseId, String url) {
        String [] parts = url.split("/");
        final String personId = parts[parts.length - 1];
        Call<PersonModelRes> call = mDataManager.getPerson(personId);
        call.enqueue(new Callback<PersonModelRes>() {
            @Override
            public void onResponse(Call<PersonModelRes> call, Response<PersonModelRes> response) {
                if (response.code() == 200) {
                    showSnackbar(response.body().toString());
                    persons.add(new Person((long) houseId, Long.valueOf(personId), response.body()));
                } else if (response.code() == 404) {
                    showSnackbar("Персонаж не обнаружен");
                } else {
                    showSnackbar("Ошибка получения информации о персонаже ");
                }
            }

            @Override
            public void onFailure(Call<PersonModelRes> call, Throwable t) {
                showSnackbar("Ошибка получения информации о персонаже ");
            }
        });
    }

    private void savePersonsInDb(int house, HouseModelRes response) {

    }

}
