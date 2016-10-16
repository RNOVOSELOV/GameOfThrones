package xyz.rnovoselov.projects.gameofthrones.utils;

import xyz.rnovoselov.projects.gameofthrones.R;
import xyz.rnovoselov.projects.gameofthrones.data.managers.DataManager;

/**
 * Created by roman on 12.10.16.
 */

public interface ConstantManager {
    String TAG_PREFIX = "GOT";

    int USED_HOUSES_COUNT = 3;
    int LANNISTER_HOUSE_TAB_ID = 0;
    int STARKS_HOUSE_TAB_ID = 1;
    int TARGARYENS_HOUSE_TAB_ID = 2;

    int LANNISTER_HOUSE_ID = 229;
    int STARK_HOUSE_ID = 362;
    int TARGARYEN_HOUSE_ID = 378;

    enum SYNC_DATA_ERRORS {
        NO_ERROR {
            @Override
            public String toStringValue() {
                return "";
            }
        },
        DATA_NOT_SINCHRONIZED {
            @Override
            public String toStringValue() {
                return DataManager.getInstance().getContext().getString(R.string.error_some_data);
            }
        },
        INCORRECT_HOUSE {
            @Override
            public String toStringValue() {
                return DataManager.getInstance().getContext().getString(R.string.error_house_is_absent);
            }
        },
        NO_INTERNET_CONNECTION {
            @Override
            public String toStringValue() {
                return DataManager.getInstance().getContext().getString(R.string.error_no_connection);
            }
        };

        public abstract String toStringValue();
    }
}