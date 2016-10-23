package xyz.rnovoselov.projects.gameofthrones.mvp.presenters;

import android.support.annotation.Nullable;

import xyz.rnovoselov.projects.gameofthrones.mvp.models.SplashModel;
import xyz.rnovoselov.projects.gameofthrones.mvp.views.ISplashView;
import xyz.rnovoselov.projects.gameofthrones.utils.ConstantManager;

/**
 * Created by roman on 23.10.16.
 */

public class SplashPresenter implements ISplashPresenter {

    private static SplashPresenter INSTANCE = new SplashPresenter();
    private SplashModel mSplashModel;
    private ISplashView mSplashView;

    private SplashPresenter() {
        mSplashModel = new SplashModel();
    }

    public static SplashPresenter getInstance() {
        return INSTANCE;
    }

    @Override
    public void takeView(ISplashView view) {
        mSplashView = view;
    }

    @Override
    public void initView() {
        mSplashModel.startQueryQueue();
        if (getView() != null) {
            getView().showLoad();
        }
    }

    @Override
    public void dropView() {
        mSplashView = null;
    }

    @Nullable
    @Override
    public ISplashView getView() {
        return mSplashView;
    }

    @Override
    public void loadCompletedModelCallback(ConstantManager.SYNC_DATA_ERRORS error) {
        if (getView() != null) {
            getView().hideLoad();
            getView().startMainActivity(error);
        }
    }
}
