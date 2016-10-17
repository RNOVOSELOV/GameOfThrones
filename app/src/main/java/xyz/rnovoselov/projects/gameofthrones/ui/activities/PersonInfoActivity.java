package xyz.rnovoselov.projects.gameofthrones.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.rnovoselov.projects.gameofthrones.R;
import xyz.rnovoselov.projects.gameofthrones.data.managers.DataManager;
import xyz.rnovoselov.projects.gameofthrones.data.network.res.PersonModelRes;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.Person;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.Titles;
import xyz.rnovoselov.projects.gameofthrones.ui.adapters.PersonListAdapter;
import xyz.rnovoselov.projects.gameofthrones.utils.AppConfig;
import xyz.rnovoselov.projects.gameofthrones.utils.ConstantManager;

/**
 * Created by roman on 15.10.16.
 */

public class PersonInfoActivity extends BaseActivity {

    private static final String TAG = ConstantManager.TAG_PREFIX + PersonInfoActivity.class.getSimpleName();
    private static final String EXTRA_PERSON_ID = "EXTRA_PERSON_ID";

    @BindView(R.id.person_activity_coordinator)
    CoordinatorLayout mCoordinator;
    @BindView(R.id.person_activity_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.person_activity_house_image)
    ImageView mHeaderImage;
    @BindView(R.id.person_activity_et_words)
    EditText mEtWords;
    @BindView(R.id.person_activity_et_born)
    EditText mEtBorn;
    @BindView(R.id.person_activity_no_person_info_tv)
    TextView mErrorTextView;
    @BindView(R.id.person_activity_titles_list)
    ListView mTitlesListView;
    @BindView(R.id.person_activity_aliases_list)
    ListView mAliasesListView;
    @BindView(R.id.person_activity_bt_father)
    Button mButtonFather;
    @BindView(R.id.person_activity_bt_mother)
    Button mButtonMother;
    @BindView(R.id.person_activity_father_no_info_tv)
    TextView mFatherNoInfo;
    @BindView(R.id.person_activity_mother_no_info_tv)
    TextView mMotherNoInfo;
    @BindView(R.id.person_activity_scroll)
    NestedScrollView mNestedScroll;

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
        setupToolbar();
        mDataManager = DataManager.getInstance();
        Long personId = getIntent().getLongExtra(EXTRA_PERSON_ID, -1);
        if (personId < 0) {
            mErrorTextView.setVisibility(View.VISIBLE);
        } else {
            mPerson = mDataManager.getPersonFromDb(personId);
            updateUi();
        }

    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.finishAfterTransition(PersonInfoActivity.this);
                }
            });
        }
    }

    void updateUi(Long id) {
        mPerson = mDataManager.getPersonFromDb(id);
        if (mPerson == null) {
            mErrorTextView.setVisibility(View.VISIBLE);
            return;
        }
        updateUi();
    }

    void updateUi() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mPerson.getName());
        }

        try {
            int headerDrawable = 0;
            switch (mPerson.getPersonHouseRemoteId().intValue()) {
                case ConstantManager.LANNISTER_HOUSE_ID:
                    headerDrawable = R.drawable.person_lannister;
                    break;
                case ConstantManager.STARK_HOUSE_ID:
                    headerDrawable = R.drawable.person_stark;
                    break;
                case ConstantManager.TARGARYEN_HOUSE_ID:
                    headerDrawable = R.drawable.person_targarien;
                    break;
            }
            if (headerDrawable != 0) {
                mHeaderImage.setImageDrawable(ContextCompat.getDrawable(this, headerDrawable));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(TAG, "Выбран пользователь с неизвестным домом", e);
        }
        mEtWords.setText(mDataManager.getHouseFromDb(mPerson.getPersonHouseRemoteId()).getWords());
        mEtBorn.setText(mPerson.getBorn().isEmpty() ? "no info" : mPerson.getBorn());


        List<String> titles = mDataManager.getPersonTitles(mPerson.getPersonRemoteId());
        if (titles.size() == 0) {
            titles.add("no info");
        }
        PersonListAdapter titleAdapter = new PersonListAdapter(this, titles);
        mTitlesListView.setAdapter(titleAdapter);
        setListViewHeightBasedOnChildren(mTitlesListView);
        mTitlesListView.setEnabled(false);

        List<String> aliases = mDataManager.getPersonAliases(mPerson.getPersonRemoteId());
        if (aliases.size() == 0) {
            aliases.add("no info");
        }
        PersonListAdapter aliasesAdapter = new PersonListAdapter(this, aliases);
        mAliasesListView.setAdapter(aliasesAdapter);
        setListViewHeightBasedOnChildren(mAliasesListView);
        mAliasesListView.setEnabled(false);
        processParents();
    }

    private void processParents() {
        if (mPerson.getFather() == null) {
            mButtonFather.setVisibility(View.GONE);
            mFatherNoInfo.setVisibility(View.VISIBLE);

        } else {
            Person father = mDataManager.getPersonFromDb(mPerson.getFather());
            if (father == null) {
                mButtonFather.setVisibility(View.GONE);
                mFatherNoInfo.setVisibility(View.VISIBLE);
                processPersonUrl(String.valueOf(mPerson.getFather()));
                return;
            }
            mButtonFather.setText(father.getName());
            mFatherNoInfo.setVisibility(View.GONE);
            mButtonFather.setVisibility(View.VISIBLE);
        }

        if (mPerson.getMother() == null) {
            mButtonMother.setVisibility(View.GONE);
            mMotherNoInfo.setVisibility(View.VISIBLE);
        } else {
            Person mother = mDataManager.getPersonFromDb(mPerson.getMother());
            if (mother == null) {
                mButtonMother.setVisibility(View.GONE);
                mMotherNoInfo.setVisibility(View.VISIBLE);
                processPersonUrl(String.valueOf(mPerson.getMother()));
                return;
            }
            mButtonMother.setText(mother.getName());
            mMotherNoInfo.setVisibility(View.GONE);
            mButtonMother.setVisibility(View.VISIBLE);
        }
    }

    private void processPersonUrl(final String personId) {
        Call<PersonModelRes> call = mDataManager.getPerson(personId);
        call.enqueue(new Callback<PersonModelRes>() {
            @Override
            public void onResponse(Call<PersonModelRes> call, Response<PersonModelRes> response) {
                if (response.code() == 200) {
                    Person person = new Person(mPerson.getPersonHouseRemoteId(), Long.valueOf(personId), response.body());
                    mDataManager.getDaoSession().getPersonDao().insertOrReplace(person);
                    List<String> ttls = response.body().getTitles();
                    List<String> alses = response.body().getAliases();
                    for (String title : ttls) {
                        Titles t = new Titles(Long.valueOf(personId), true, title);
                        mDataManager.getDaoSession().getTitlesDao().insertOrReplace(t);
                    }
                    for (String alias : alses) {
                        Titles t = new Titles(Long.valueOf(personId), false, alias);
                        mDataManager.getDaoSession().getTitlesDao().insertOrReplace(t);
                    }
                }
                processParents();
            }

            @Override
            public void onFailure(Call<PersonModelRes> call, Throwable t) {

            }
        });
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        int px = PersonInfoActivity.this.getResources().getDimensionPixelSize(R.dimen.size_list);
        totalHeight = px * listAdapter.getCount();
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @OnClick(R.id.person_activity_bt_father)
    public void fatherClicked() {
        Intent i = PersonInfoActivity.newIntent(this, mPerson.getFather());
        startActivity(i);
        ActivityCompat.finishAfterTransition(this);
    }

    @OnClick(R.id.person_activity_bt_mother)
    public void motherClicked() {
        Intent i = PersonInfoActivity.newIntent(this, mPerson.getMother());
        startActivity(i);
        ActivityCompat.finishAfterTransition(this);
    }

}
