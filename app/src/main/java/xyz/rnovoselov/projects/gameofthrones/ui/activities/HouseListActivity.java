package xyz.rnovoselov.projects.gameofthrones.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.rnovoselov.projects.gameofthrones.R;
import xyz.rnovoselov.projects.gameofthrones.ui.fragments.HouseFragment;

import static xyz.rnovoselov.projects.gameofthrones.utils.ConstantManager.*;

/**
 * Created by novoselov on 13.10.2016.
 */

public class HouseListActivity extends BaseActivity {

    private static final String EXTRA_VIEW_PAGER_TAB = "EXTRA_VIEW_PAGER_TAB";
    private static final String EXTRA_SYNC_ERROR = "EXTRA_SYNC_ERROR";

    @BindView(R.id.house_activity_coordinator)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.house_activity_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.house_activity_tabs)
    TabLayout mTabLayout;
    @BindView(R.id.house_activity_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.house_activity_drawer)
    DrawerLayout mNavigationDrawer;
    @BindView(R.id.house_activity_navigation_view)
    NavigationView mNavigationView;

    private int[] tabIcons = {
            R.drawable.lanister_icon,
            R.drawable.stark_icon,
            R.drawable.targarien_icon
    };
    private int[] tabText = {
            R.string.lannister_home_title,
            R.string.stark_home_title,
            R.string.targariens_home_title
    };
    private HouseFragment lannisterFragment;
    private HouseFragment starksFragment;
    private HouseFragment targariensFragment;
    SearchView searchView;

    private MenuItem mSearchItem;

    public static Intent newIntent(Context context, SYNC_DATA_ERRORS error) {
        Intent intent = new Intent(context, HouseListActivity.class);
        intent.putExtra(EXTRA_SYNC_ERROR, error);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_list);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.app_developer);
        }
        setupDrawer();

        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
        setupTabIcon(LANNISTER_HOUSE_TAB_ID);
        setupTabIcon(STARKS_HOUSE_TAB_ID);
        setupTabIcon(TARGARYENS_HOUSE_TAB_ID);

        if (savedInstanceState != null) {
            int currentVpTab = savedInstanceState.getInt(EXTRA_VIEW_PAGER_TAB, 0);
            mViewPager.setCurrentItem(currentVpTab);
        } else {
            try {
                SYNC_DATA_ERRORS error = ((SYNC_DATA_ERRORS) getIntent().getSerializableExtra(EXTRA_SYNC_ERROR));
                if (error != SYNC_DATA_ERRORS.NO_ERROR) {
                    showSnackbar(error.toStringValue());
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_VIEW_PAGER_TAB, mViewPager.getCurrentItem());
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        mSearchItem = menu.findItem(R.id.menu_search_action);
        searchView = ((SearchView) MenuItemCompat.getActionView(mSearchItem));
        searchView.setQueryHint(getString(R.string.menu_search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                HouseFragment currFragment;
                switch (mViewPager.getCurrentItem()) {
                    case 0:
                        currFragment = lannisterFragment;
                        break;
                    case 1:
                        currFragment = starksFragment;
                        break;
                    default:
                        currFragment = targariensFragment;
                }
                if (newText.isEmpty()) {
                    currFragment.showPersons();
                } else {
                    currFragment.showPersons(newText);
                }
                return false;
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

    private void setupDrawer() {
        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.drawer_menu_lannisters_item:
                        mViewPager.setCurrentItem(0, true);
                        break;
                    case R.id.drawer_menu_starks_item:
                        mViewPager.setCurrentItem(1, true);
                        break;
                    case R.id.drawer_menu_targariens_item:
                        mViewPager.setCurrentItem(2, true);
                        break;
                }
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void setupViewPager(ViewPager vp) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        lannisterFragment = HouseFragment.newInstance(LANNISTER_HOUSE_ID);
        starksFragment = HouseFragment.newInstance(STARK_HOUSE_ID);
        targariensFragment = HouseFragment.newInstance(TARGARYEN_HOUSE_ID);
        adapter.addFragment(lannisterFragment, getResources().getString(R.string.lannister_home_title));
        adapter.addFragment(starksFragment, getString(R.string.stark_home_title));
        adapter.addFragment(targariensFragment, getString(R.string.targariens_home_title));
        mViewPager.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mNavigationView.getMenu().getItem(0).setChecked(false);
                mNavigationView.getMenu().getItem(1).setChecked(false);
                mNavigationView.getMenu().getItem(2).setChecked(false);
                mNavigationView.getMenu().getItem(position).setChecked(true);
                searchView.setQuery("", false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupTabIcon(int tabAtId) {
        View view = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView tv = ((TextView) view.findViewById(R.id.tab));
        ImageView iv = ((ImageView) view.findViewById(R.id.tab_icon));
        int px = getResources().getDimensionPixelSize(R.dimen.size_custom_tab_icon);
        Picasso.with(this)
                .load(tabIcons[tabAtId])
                .fit()
                .centerCrop()
                .into(iv);
        tv.setText(getString(tabText[tabAtId]));
        mTabLayout.getTabAt(tabAtId).setCustomView(view);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }
}
