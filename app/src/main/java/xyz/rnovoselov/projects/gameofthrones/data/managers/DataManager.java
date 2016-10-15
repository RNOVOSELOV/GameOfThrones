package xyz.rnovoselov.projects.gameofthrones.data.managers;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import xyz.rnovoselov.projects.gameofthrones.data.network.RestService;
import xyz.rnovoselov.projects.gameofthrones.data.network.ServiceGenerator;
import xyz.rnovoselov.projects.gameofthrones.data.network.res.HouseModelRes;
import xyz.rnovoselov.projects.gameofthrones.data.network.res.PersonModelRes;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.DaoSession;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.House;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.HouseDao;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.Person;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.PersonDao;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.Titles;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.TitlesDao;
import xyz.rnovoselov.projects.gameofthrones.utils.ConstantManager;
import xyz.rnovoselov.projects.gameofthrones.utils.GotApplication;

/**
 * Created by roman on 13.10.16.
 */

public class DataManager {

    private static final String TAG = ConstantManager.TAG_PREFIX + DataManager.class.getSimpleName();

    private Context mContext;
    private RestService mRestService;
    private DaoSession mDaoSession;

    private DataManager() {
        mContext = GotApplication.getAppContext();
        mRestService = ServiceGenerator.createService(RestService.class);
        mDaoSession = GotApplication.getDaoSession();
    }

    private static class DataMangerHolder {
        private final static DataManager INSTANCE = new DataManager();
    }

    public static DataManager getInstance() {
        return DataMangerHolder.INSTANCE;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public Context getContext() {
        return mContext;
    }

    // region ========== API ==========

    /**
     * Метод получения информации о выбранном доме земли Вестероса
     *
     * @param houseId идентификатор дома, о котором необходима информация
     * @return модельный класс типа {@link HouseModelRes}
     */
    public Call<HouseModelRes> getHouse(String houseId) {
        return mRestService.getHouse(houseId);
    }

    public Call<PersonModelRes> getPerson(String personId) {
        return mRestService.getPerson(personId);
    }
    // endregion

    //region ========== DATABASE ==========

    public House getHouseFromDb(Long homeId) {
        House house = new House();
        try {
            house = mDaoSession.queryBuilder(House.class)
                    .where(HouseDao.Properties.RemoteId.eq(homeId))
                    .build()
                    .unique();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Ошибка получения данных из БД", e);
        }
        return house;
    }

    public List<Person> getHousePersonsFromDb(int homeId) {
        List<Person> persons = new ArrayList<>();
        try {
            persons = mDaoSession.queryBuilder(Person.class)
                    .where(PersonDao.Properties.PersonHouseRemoteId.eq(homeId))
                    .orderAsc(PersonDao.Properties.Name)
                    .build()
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Ошибка получения данных из БД", e);
        }
        return persons;
    }

    public List<Person> getHousePersonsByNameFromDb(int homeId, String nm) {
        List<Person> persons = new ArrayList<>();
        try {
            persons = mDaoSession.queryBuilder(Person.class)
                    .where(PersonDao.Properties.PersonHouseRemoteId.eq(homeId),
                            PersonDao.Properties.SearchName.like("%" + nm.toUpperCase() + "%"))
                    .orderAsc(PersonDao.Properties.Name)
                    .build()
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Ошибка получения данных из БД", e);
        }
        return persons;
    }

    public Person getPersonFromDb(Long remoteId) {
        Person person = new Person();
        try {
            person = mDaoSession.queryBuilder(Person.class)
                    .where(PersonDao.Properties.PersonRemoteId.eq(remoteId))
                    .build()
                    .unique();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Ошибка получения данных из БД", e);
        }
        return person;
    }

    public List<String> getPersonTitles(Long personRemoteId) {
        List<Titles> titles = new ArrayList<>();
        try {
            titles = mDaoSession.queryBuilder(Titles.class)
                    .where(TitlesDao.Properties.TitlePersonRemoteId.eq(personRemoteId), TitlesDao.Properties.IsTitle.eq(true))
                    .orderAsc(TitlesDao.Properties.Characteristic)
                    .build()
                    .list();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Ошибка получения данных из БД", e);
        }

        List<String> personTitles = new ArrayList<>();
        for (Titles t : titles) {
            personTitles.add(t.getCharacteristic());
        }
        return personTitles;
    }

    public List<String> getPersonAliases(Long personRemoteId) {
        List<Titles> titles = new ArrayList<>();
        try {
            titles = mDaoSession.queryBuilder(Titles.class)
                    .where(TitlesDao.Properties.TitlePersonRemoteId.eq(personRemoteId), TitlesDao.Properties.IsTitle.eq(false))
                    .orderAsc(TitlesDao.Properties.Characteristic)
                    .build()
                    .list();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Ошибка получения данных из БД", e);
        }

        List<String> personAliases = new ArrayList<>();
        for (Titles t : titles) {
            personAliases.add(t.getCharacteristic());
        }
        return personAliases;
    }

    //endregion
}
