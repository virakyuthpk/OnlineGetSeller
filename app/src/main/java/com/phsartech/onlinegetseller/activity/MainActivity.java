package com.phsartech.onlinegetseller.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.callback.CallBackFucntionOnButtonLogoutClick;
import com.phsartech.onlinegetseller.fragment.OrderFragment;
import com.phsartech.onlinegetseller.fragment.ProductFragment;
import com.phsartech.onlinegetseller.fragment.ReportFragment;
import com.phsartech.onlinegetseller.fragment.SettingFragment;
import com.phsartech.onlinegetseller.util.LocalDataStore;

public class MainActivity extends AppCompatActivity implements
        CallBackFucntionOnButtonLogoutClick {

    private final String TAG = "MainActivity";
    private ViewPager viewPager_home;
    private MaterialToolbar materialToolbar;
    private BottomNavigationView bottomNavigationView_home;
    private BottomNavigationView.OnNavigationItemSelectedListener seftOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_order:
                    materialToolbar.setVisibility(View.VISIBLE);
                    viewPager_home.setCurrentItem(0);
                    bottomNavigationView_home.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_product:
                    materialToolbar.setVisibility(View.VISIBLE);
                    viewPager_home.setCurrentItem(1);
                    bottomNavigationView_home.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_addproduct:
                    Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_report:
                    materialToolbar.setVisibility(View.VISIBLE);
                    viewPager_home.setCurrentItem(2);
                    bottomNavigationView_home.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_setting:
                    materialToolbar.setVisibility(View.VISIBLE);
                    viewPager_home.setCurrentItem(3);
                    bottomNavigationView_home.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerComponent();

        bottomNavigationView_home.setOnNavigationItemSelectedListener(seftOnNavigationItemSelectedListener);

        HomeFragmentAdapter homeFragmentAdapter = new HomeFragmentAdapter(getSupportFragmentManager());

        viewPager_home.setAdapter(homeFragmentAdapter);
        viewPager_home.setCurrentItem(0);
        viewPager_home.setOffscreenPageLimit(homeFragmentAdapter.getCount() - 1);

        if (getIntent().hasExtra("shop") == true) {
            bottomNavigationView_home.setSelectedItemId(R.id.navigation_setting);
        }
    }

    private void registerComponent() {
        viewPager_home = findViewById(R.id.view_pager_home);
        bottomNavigationView_home = findViewById(R.id.bottom_home);
        materialToolbar = findViewById(R.id.toolbar);
    }

    @Override
    public void onButtonLogoutClick() {
        LocalDataStore.setID(MainActivity.this, 0);
        LocalDataStore.setLogin(MainActivity.this, false);
        LocalDataStore.setSHOPID(MainActivity.this, 0);
        LocalDataStore.setToken(MainActivity.this, "");
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    private class HomeFragmentAdapter extends FragmentPagerAdapter {

        public HomeFragmentAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return OrderFragment.newInstance();
                case 1:
                    return ProductFragment.newInstance();
                case 2:
                    return ReportFragment.newInstance();
                case 3:
                    return SettingFragment.newInstance(MainActivity.this);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
