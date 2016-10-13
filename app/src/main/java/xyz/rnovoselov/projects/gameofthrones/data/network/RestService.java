package xyz.rnovoselov.projects.gameofthrones.data.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import xyz.rnovoselov.projects.gameofthrones.data.network.res.HouseModelRes;
import xyz.rnovoselov.projects.gameofthrones.data.network.res.PersonModelRes;

/**
 * Created by novoselov on 13.10.2016.
 */

public interface RestService {

    @GET("houses/{houseId}")
    Call<HouseModelRes> getHouse(@Path("houseId") String houseId);

    @GET("characters/{personId}")
    Call<PersonModelRes> getPerson(@Path("personId") String personId);
}
