package xyz.rnovoselov.projects.gameofthrones.utils;

/**
 * Created by roman on 13.10.16.
 */

public interface AppConfig {

    String BASE_URL = "http://anapioficeandfire.com/api/";

    int LANNISTER_HOUSE_ID = 229;
    int STARK_HOUSE_ID = 362;
    int TARGARYEN_HOUSE_ID = 378;

    int MAX_READ_TIMEOUT = 5000;
    int MAX_CONNECT_TIMEOUT = 5000;
    int SEARCH_DELAY = 1500;

}
