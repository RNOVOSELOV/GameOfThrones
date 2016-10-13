package xyz.rnovoselov.projects.gameofthrones.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.rnovoselov.projects.gameofthrones.R;
import xyz.rnovoselov.projects.gameofthrones.ui.fragments.HouseFragment;
import xyz.rnovoselov.projects.gameofthrones.utils.ConstantManager;

/**
 * Created by novoselov on 13.10.2016.
 */

public class HouseListActivity extends BaseActivity {

    @BindView(R.id.house_activity_coordinator)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.house_activity_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.house_activity_tabs)
    TabLayout mTabLayout;
    @BindView(R.id.house_activity_view_pager)
    ViewPager mViewPager;
    private int[] tabIcons = {
            R.drawable.lanister_icon,
            R.drawable.stark_icon,
            R.drawable.targarien_icon
    };
    private String[] tabText = {
            ConstantManager.LANNISTER_HOUSE_TITLE,
            ConstantManager.STARKS_HOUSE_TITLE,
            ConstantManager.TARGARYENS_HOUSE_TITLE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_list);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
        setupTabIcon(ConstantManager.LANNISTER_HOUSE_TAB_ID);
        setupTabIcon(ConstantManager.STARKS_HOUSE_TAB_ID);
        setupTabIcon(ConstantManager.TARGARYENS_HOUSE_TAB_ID);
    }

    private void setupViewPager(ViewPager vp) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HouseFragment(), ConstantManager.LANNISTER_HOUSE_TITLE);
        adapter.addFragment(new HouseFragment(), ConstantManager.STARKS_HOUSE_TITLE);
        adapter.addFragment(new HouseFragment(), ConstantManager.TARGARYENS_HOUSE_TITLE);
        mViewPager.setAdapter(adapter);
    }

    private void setupTabIcon(int tabAtId) {
        View view = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView tv = ((TextView) view.findViewById(R.id.tab));
        ImageView iv = ((ImageView) view.findViewById(R.id.tab_icon));
        int px = getResources().getDimensionPixelSize(R.dimen.custom_tab_icon_size);
        Picasso.with(this)
                .load(tabIcons[tabAtId])
                .resize(px, px)
                .centerCrop()
                .into(iv);
        tv.setText(tabText[tabAtId]);
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
}
