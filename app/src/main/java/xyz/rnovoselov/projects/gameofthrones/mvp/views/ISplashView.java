package xyz.rnovoselov.projects.gameofthrones.mvp.views;

import xyz.rnovoselov.projects.gameofthrones.mvp.presenters.ISplashPresenter;
import xyz.rnovoselov.projects.gameofthrones.utils.ConstantManager;

/**
 * Created by roman on 23.10.16.
 */

public interface ISplashView {

    void showMessage(String message);

    void showError(Throwable error);

    void showLoad();

    void hideLoad();

    ISplashPresenter getPresenter();

    void startMainActivity(ConstantManager.SYNC_DATA_ERRORS syncError);
}
