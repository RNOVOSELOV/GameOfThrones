package xyz.rnovoselov.projects.gameofthrones.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

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
    @BindView(R.id.splash_image_view)
    ImageView mImageView;

    private DataManager mDataManager;
    private HouseDao mHouseDao;
    private TitlesDao mTitlesDao;
    private PersonDao mPersonDao;

    private volatile List<Person> persons = new ArrayList<>();
    private volatile List<House> houses = new ArrayList<>();
    private volatile List<Titles> titles = new ArrayList<>();
    private volatile int sessionCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mDataManager = DataManager.getInstance();
        ButterKnife.bind(this);
        sessionCounter = 0;

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
        processHouse(AppConfig.STARK_HOUSE_ID);
        processHouse(AppConfig.LANNISTER_HOUSE_ID);
        processHouse(AppConfig.TARGARYEN_HOUSE_ID);
        sessionCounter = sessionCounter + ConstantManager.USED_HOUSES_COUNT;
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
        if (NetworkStatusChecker.isNetworkAvailable(this)) {
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
                        showSnackbar("Дом не обнаружен");
                    } else {
                        showSnackbar("Ошибка получения информации о доме " + String.valueOf(id));
                    }
                    launchMainActivity();
                }

                @Override
                public void onFailure(Call<HouseModelRes> call, Throwable t) {
                    launchMainActivity();
                    // TODO обработать ошибки
                }
            });
        }
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
                    showSnackbar("Персонаж не обнаружен");
                } else {
                    showSnackbar("Ошибка получения информации о персонаже ");
                }
                launchMainActivity();
            }

            @Override
            public void onFailure(Call<PersonModelRes> call, Throwable t) {
                showSnackbar("Ошибка получения информации о персонаже ");
                launchMainActivity();
            }
        });
    }

    void launchMainActivity() {
        sessionCounter--;
        if (sessionCounter != 0) {
            return;
        }
        mHouseDao.insertOrReplaceInTx(houses);
        mPersonDao.insertOrReplaceInTx(persons);
        mTitlesDao.insertOrReplaceInTx(titles);
        hideProgress();
        Intent intent = new Intent(this, HouseListActivity.class);
        startActivity(intent);
        ActivityCompat.finishAfterTransition(this);
    }
}
