package xyz.rnovoselov.projects.gameofthrones.ui.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;

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

    private volatile List<Person> persons = new ArrayList<>();
    private volatile List<House> houses = new ArrayList<>();
    private volatile List<Titles> titles = new ArrayList<>();
    private volatile int sessionCounter;

    private volatile ConstantManager.SYNC_DATA_ERRORS syncError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mDataManager = DataManager.getInstance();
        ButterKnife.bind(this);
        sessionCounter = 0;
        syncError = ConstantManager.SYNC_DATA_ERRORS.NO_ERROR;

        if (savedInstanceState != null) {
            //сохранять массивы на случай поворотов
        }

        mHouseDao = mDataManager.getDaoSession().getHouseDao();
        mTitlesDao = mDataManager.getDaoSession().getTitlesDao();
        mPersonDao = mDataManager.getDaoSession().getPersonDao();
    }

    @Override
    protected void onStart() {
        super.onStart();
        showProgress();
        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            processHouse(ConstantManager.STARK_HOUSE_ID);
            processHouse(ConstantManager.LANNISTER_HOUSE_ID);
            processHouse(ConstantManager.TARGARYEN_HOUSE_ID);
            sessionCounter = sessionCounter + ConstantManager.USED_HOUSES_COUNT;
        } else {
            syncError = ConstantManager.SYNC_DATA_ERRORS.NO_INTERNET_CONNECTION;
            launchMainActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void processHouse(final int id) {

        Call<HouseModelRes> call = mDataManager.getHouse(String.valueOf(id));
        call.enqueue(new Callback<HouseModelRes>() {
            @Override
            public void onResponse(Call<HouseModelRes> call, Response<HouseModelRes> response) {
                if (response.code() == 200) {
                    houses.add(new House((long) id, response.body()));
//                        House house = new House((long) id, response.body());
//                        mDataManager.getDaoSession().getHouseDao().insertOrReplace(house);

                    List<String> housePersonUrls = response.body().getPersonsUrls();
                    sessionCounter += housePersonUrls.size();
                    for (String urlPerson : housePersonUrls) {
                        processPersonUrl(id, urlPerson);
                    }
                } else if (response.code() == 404) {
                    if (ConstantManager.SYNC_DATA_ERRORS.INCORRECT_HOUSE.ordinal() > syncError.ordinal()) {
                        syncError = ConstantManager.SYNC_DATA_ERRORS.INCORRECT_HOUSE;
                    }
                } else {
                    if (ConstantManager.SYNC_DATA_ERRORS.DATA_NOT_SINCHRONIZED.ordinal() > syncError.ordinal()) {
                        syncError = ConstantManager.SYNC_DATA_ERRORS.DATA_NOT_SINCHRONIZED;
                    }
                }
                launchMainActivity();
            }

            @Override
            public void onFailure(Call<HouseModelRes> call, Throwable t) {
                if (ConstantManager.SYNC_DATA_ERRORS.INCORRECT_HOUSE.ordinal() > syncError.ordinal()) {
                    syncError = ConstantManager.SYNC_DATA_ERRORS.INCORRECT_HOUSE;
                }
                launchMainActivity();
            }
        });
    }

    private void processPersonUrl(final int houseId, String url) {
        String[] parts = url.split("/");
        final String personId = parts[parts.length - 1];
        Call<PersonModelRes> call = mDataManager.getPerson(personId);
        call.enqueue(new Callback<PersonModelRes>() {
            @Override
            public void onResponse(Call<PersonModelRes> call, Response<PersonModelRes> response) {
                if (response.code() == 200) {
                    persons.add(new Person((long) houseId, Long.valueOf(personId), response.body()));
//                    Person person = new Person((long) houseId, Long.valueOf(personId), response.body());
//                    mDataManager.getDaoSession().getPersonDao().insertOrReplace(person);

                    List<String> titl = response.body().getTitles();
                    List<String> alia = response.body().getAliases();
                    for (String title : titl) {
                        titles.add(new Titles(Long.valueOf(personId), true, title));
//                        Titles t = new Titles(Long.valueOf(personId), false, title);
//                        mDataManager.getDaoSession().getTitlesDao().insertOrReplace(t);
                    }
                    for (String alias : alia) {
                        titles.add(new Titles(Long.valueOf(personId), false, alias));
//                        Titles t = new Titles(Long.valueOf(personId), false, alias);
//                        mDataManager.getDaoSession().getTitlesDao().insertOrReplace(t);
                    }
                } else if (response.code() == 404) {
                    if (ConstantManager.SYNC_DATA_ERRORS.DATA_NOT_SINCHRONIZED.ordinal() > syncError.ordinal()) {
                        syncError = ConstantManager.SYNC_DATA_ERRORS.DATA_NOT_SINCHRONIZED;
                    }
                } else {
                    if (ConstantManager.SYNC_DATA_ERRORS.DATA_NOT_SINCHRONIZED.ordinal() > syncError.ordinal()) {
                        syncError = ConstantManager.SYNC_DATA_ERRORS.DATA_NOT_SINCHRONIZED;
                    }
                }
                launchMainActivity();
            }

            @Override
            public void onFailure(Call<PersonModelRes> call, Throwable t) {
                if (ConstantManager.SYNC_DATA_ERRORS.DATA_NOT_SINCHRONIZED.ordinal() > syncError.ordinal()) {
                    syncError = ConstantManager.SYNC_DATA_ERRORS.DATA_NOT_SINCHRONIZED;
                }
                launchMainActivity();
            }
        });
    }

    void launchMainActivity() {
        sessionCounter--;
        if (sessionCounter > 0) {
            return;
        }
        mHouseDao.insertOrReplaceInTx(houses);
        mPersonDao.insertOrReplaceInTx(persons);
        mTitlesDao.insertOrReplaceInTx(titles);
        startMainActivity();
    }

    private void startMainActivity() {
        hideProgress();
        startActivity(HouseListActivity.newIntent(this, syncError));
        ActivityCompat.finishAfterTransition(this);
    }
}