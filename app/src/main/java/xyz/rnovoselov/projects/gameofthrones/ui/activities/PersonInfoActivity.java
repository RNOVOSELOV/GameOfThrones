package xyz.rnovoselov.projects.gameofthrones.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.rnovoselov.projects.gameofthrones.R;
import xyz.rnovoselov.projects.gameofthrones.data.managers.DataManager;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.Person;

/**
 * Created by roman on 15.10.16.
 */

public class PersonInfoActivity extends BaseActivity {

    private static final String EXTRA_PERSON_ID = "EXTRA_PERSON_ID";

    @BindView(R.id.person_activity_coordinator)
    CoordinatorLayout mCoordinator;

    private Person mPerson;

    public static Intent newIntent(Context context, Long personRemoteId) {
        Intent intent = new Intent(context, PersonInfoActivity.class);
        intent.putExtra(EXTRA_PERSON_ID, personRemoteId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        ButterKnife.bind(this);

        Long personId = getIntent().getLongExtra(EXTRA_PERSON_ID, -1);
        if (personId < 0) {
            //TODO обработать
        } else {
            mPerson = DataManager.getInstance().getPersonFromDb(personId);
            updateUi();
        }

    }

    void updateUi () {
        showSnackBar(mPerson.getName());
    }

    private void showSnackBar(String msg) {
        Snackbar.make(mCoordinator, msg, Snackbar.LENGTH_SHORT).show();
    }
}
