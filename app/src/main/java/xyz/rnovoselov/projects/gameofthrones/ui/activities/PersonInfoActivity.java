package xyz.rnovoselov.projects.gameofthrones.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import xyz.rnovoselov.projects.gameofthrones.R;
import xyz.rnovoselov.projects.gameofthrones.data.managers.DataManager;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.Person;
import xyz.rnovoselov.projects.gameofthrones.utils.AppConfig;

/**
 * Created by roman on 15.10.16.
 */

public class PersonInfoActivity extends BaseActivity {

    private static final String EXTRA_PERSON_ID = "EXTRA_PERSON_ID";

    @BindView(R.id.person_activity_coordinator)
    CoordinatorLayout mCoordinator;
    @BindViews({R.id.person_activity_icon_aliases,
            R.id.person_activity_icon_born,
            R.id.person_activity_icon_titles,
            R.id.person_activity_icon_words})
    List<ImageView> mIViews;
    @BindView(R.id.person_activity_et_words)
    EditText mEtWords;
    @BindView(R.id.person_activity_et_born)
    EditText mEtBorn;
    @BindView(R.id.person_activity_no_person_info_tv)
    TextView mErrorTextView;

    private Person mPerson;
    private DataManager mDataManager;

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
        mDataManager = DataManager.getInstance();
        Long personId = getIntent().getLongExtra(EXTRA_PERSON_ID, -1);
        if (personId < 0) {
            mErrorTextView.setVisibility(View.VISIBLE);
        } else {
            mPerson = mDataManager.getPersonFromDb(personId);
            updateUi();
        }

    }

    void updateUi(Long id) {
        mPerson = mDataManager.getPersonFromDb(id);
        updateUi();
    }

    void updateUi() {
        showSnackBar(mPerson.getName());
        int drawableId = 0;
        switch (mPerson.getPersonHouseRemoteId().intValue()) {
            case AppConfig.LANNISTER_HOUSE_ID:
                drawableId = R.drawable.lanister_icon;
                break;
            case AppConfig.STARK_HOUSE_ID:
                drawableId = R.drawable.stark_icon;
                break;
            case AppConfig.TARGARYEN_HOUSE_ID:
                drawableId = R.drawable.targarien_icon;
                break;
        }
        if (drawableId != 0) {
            for (ImageView imageView : mIViews) {
                imageView.setImageDrawable(ContextCompat.getDrawable(this, drawableId));
            }
        }
        mEtWords.setText(mDataManager.getHouseFromDb(mPerson.getPersonHouseRemoteId()).getWords());
        mEtBorn.setText(mPerson.getBorn().isEmpty() ? "no info" : mPerson.getBorn());
    }

    private void showSnackBar(String msg) {
        Snackbar.make(mCoordinator, msg, Snackbar.LENGTH_SHORT).show();
    }
}
