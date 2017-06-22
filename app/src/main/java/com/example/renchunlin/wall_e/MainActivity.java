package com.example.renchunlin.wall_e;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.renchunlin.wall_e.fragment.FraEncounter;
import com.example.renchunlin.wall_e.fragment.FraEve;
import com.example.renchunlin.wall_e.fragment.FraRear;
import com.example.renchunlin.wall_e.fragment.FraWall_e;
import com.example.renchunlin.wall_e.ui.SettingActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //TabLayout
    private TabLayout mTabLayout;
    //ViewPager
    private ViewPager mViewPager;
    //Title
    private List<String> mTtile;
    //Fragment
    private List<Fragment> mFragent;
    //FloatingActionButton
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //去掉阴影
        getSupportActionBar().setElevation(0);

        initData();
        initView();
    }
    //初始化数据
    private void initData() {
        mTtile=new ArrayList<>();
        mTtile.add(getString(R.string.app_name));
        mTtile.add("偶遇");
        mTtile.add("夏娃");
        mTtile.add("后");

        mFragent=new ArrayList<>();
        mFragent.add(new FraWall_e());
        mFragent.add(new FraEncounter());
        mFragent.add(new FraEve());
        mFragent.add(new FraRear());
    }

    //初始化View
    private void initView() {
        mFab= (FloatingActionButton) findViewById(R.id.mFab);
        mFab.setOnClickListener(this);
        mFab.setVisibility(View.GONE);

        mTabLayout= (TabLayout) findViewById(R.id.mTabLayout);
        mViewPager= (ViewPager) findViewById(R.id.mViewPager);

        //预加载
        mViewPager.setOffscreenPageLimit(mFragent.size());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    mFab.setVisibility(View.GONE);
                }else {
                    mFab.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //设置适配器
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            //选中的Item
            @Override
            public Fragment getItem(int position) {
                return mFragent.get(position);
            }

            //返回的Item的个数
            @Override
            public int getCount() {
                return mFragent.size();
            }

            //设置标题
            @Override
            public CharSequence getPageTitle(int position) {
                return mTtile.get(position);
            }
        });

        //绑定
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mFab:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }
}
