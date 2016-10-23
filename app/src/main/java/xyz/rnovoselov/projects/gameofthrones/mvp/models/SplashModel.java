package xyz.rnovoselov.projects.gameofthrones.mvp.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.rnovoselov.projects.gameofthrones.data.managers.DataManager;
import xyz.rnovoselov.projects.gameofthrones.data.network.res.HouseModelRes;
import xyz.rnovoselov.projects.gameofthrones.data.network.res.PersonModelRes;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.House;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.HouseDao;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.Person;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.PersonDao;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.Titles;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.TitlesDao;
import xyz.rnovoselov.projects.gameofthrones.mvp.presenters.SplashPresenter;
import xyz.rnovoselov.projects.gameofthrones.utils.ConstantManager;
import xyz.rnovoselov.projects.gameofthrones.utils.NetworkStatusChecker;

/**
 * Created by roman on 23.10.16.
 */

public class SplashModel {

    private volatile ConstantManager.SYNC_DATA_ERRORS syncError;

    private DataManager mDataManager;

    private HouseDao mHouseDao;
    private TitlesDao mTitlesDao;
    private PersonDao mPersonDao;

    private volatile List<Person> persons = new ArrayList<>();
    private volatile List<House> houses = new ArrayList<>();
    private volatile List<Titles> titles = new ArrayList<>();

    private volatile int sessionCounter;
    private boolean queueIsStarted;

    public SplashModel() {
        syncError = ConstantManager.SYNC_DATA_ERRORS.NO_ERROR;
        mDataManager = DataManager.getInstance();

        queueIsStarted = false;
        sessionCounter = 0;

        mHouseDao = mDataManager.getDaoSession().getHouseDao();
        mTitlesDao = mDataManager.getDaoSession().getTitlesDao();
        mPersonDao = mDataManager.getDaoSession().getPersonDao();
    }

    /**
     * Метод запускает очередь запросов к серверу
     */
    public void startQueryQueue() {
        if (queueIsStarted) {
            return;
        }
        long currTime = Calendar.getInstance().getTimeInMillis();
        if (currTime - (24 * 60 * 60 * 1000) < mDataManager.getPreferenceManager().getLastDbUpdateTime()) {
            SplashPresenter.getInstance().loadCompletedModelCallback(syncError);
            return;
        }
        queueIsStarted = true;
        if (NetworkStatusChecker.isNetworkAvailable(mDataManager.getContext())) {
            processHouse(ConstantManager.STARK_HOUSE_ID);
            processHouse(ConstantManager.LANNISTER_HOUSE_ID);
            processHouse(ConstantManager.TARGARYEN_HOUSE_ID);
            sessionCounter = sessionCounter + ConstantManager.USED_HOUSES_COUNT;
        } else {
            syncError = ConstantManager.SYNC_DATA_ERRORS.NO_INTERNET_CONNECTION;
            launchMainActivity();
        }
    }

    /**
     * Метод запрашивет информацию по дому
     *
     * @param id идентификатор дома, для которго необходиом запросить инфу
     */
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

    /**
     * Метод запрашивает информацию о конкретном персонаже
     *
     * @param houseId идентификатор дома, которому принадлежит персонаж
     * @param url     URL, по которому можно получить данные
     */
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


    private void launchMainActivity() {
        sessionCounter--;
        if (sessionCounter > 0) {
            return;
        }
        mHouseDao.insertOrReplaceInTx(houses);
        mPersonDao.insertOrReplaceInTx(persons);
        mTitlesDao.insertOrReplaceInTx(titles);
        if (syncError == ConstantManager.SYNC_DATA_ERRORS.NO_ERROR){
            mDataManager.getPreferenceManager().writeLastDbUpdateTime(Calendar.getInstance().getTimeInMillis());
        }
        SplashPresenter.getInstance().loadCompletedModelCallback(syncError);
    }
}
