package xyz.rnovoselov.projects.gameofthrones.data.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import xyz.rnovoselov.projects.gameofthrones.data.network.res.HouseModelRes;

/**
 * Created by novoselov on 13.10.2016.
 */

public interface RestService {

    @GET("houses/{houseId}")
    Call<HouseModelRes> getHouse(@Path("houseId") String houseId);
}
