package xyz.rnovoselov.projects.gameofthrones.mvp.presenters;

import android.support.annotation.Nullable;

import xyz.rnovoselov.projects.gameofthrones.mvp.views.ISplashView;
import xyz.rnovoselov.projects.gameofthrones.utils.ConstantManager;

/**
 * Created by roman on 23.10.16.
 */

public interface ISplashPresenter {

    void takeView(ISplashView authView);

    void dropView();

    void initView();

    @Nullable
    ISplashView getView();

    void loadCompletedModelCallback (ConstantManager.SYNC_DATA_ERRORS error);
}
