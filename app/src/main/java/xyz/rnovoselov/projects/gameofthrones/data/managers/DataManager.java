package xyz.rnovoselov.projects.gameofthrones.data.managers;

import android.content.Context;

import okhttp3.RequestBody;
import retrofit2.Call;
import xyz.rnovoselov.projects.gameofthrones.data.network.RestService;
import xyz.rnovoselov.projects.gameofthrones.data.network.ServiceGenerator;
import xyz.rnovoselov.projects.gameofthrones.data.network.res.HouseModelRes;
import xyz.rnovoselov.projects.gameofthrones.utils.GotApplication;

/**
 * Created by roman on 13.10.16.
 */

public class DataManager {

    private Context mContext;
    private RestService mRestService;

    private DataManager() {
        mContext = GotApplication.getAppContext();
        mRestService = ServiceGenerator.createService(RestService.class);
    }

    private static class DataMangerHolder {
        private final static DataManager INSTANCE = new DataManager();
    }

    public static DataManager getInstance() {
        return DataMangerHolder.INSTANCE;
    }

    public Context getContext() {
        return mContext;
    }

    // region ========== API ==========

    /**
     * Метод получения информации о выбранном доме земли Вестероса
     * @param houseId идентификатор дома, о котором необходима информация
     * @return модельный класс типа {@link HouseModelRes}
     */
    public Call<HouseModelRes> getHouse(String houseId) {
        return mRestService.getHouse(houseId);
    }

    // endregion
}
