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
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.Person;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.PersonDao;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.Titles;
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

    //endregion
}
