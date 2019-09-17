package com.phsartech.onlinegetseller.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.phsartech.onlinegetseller.R;


public class ProductFragment extends Fragment {

    private TabLayout tabLayout_product;
    private ViewPager viewPager_product;
    private String TAG = "ProductFragment";

    public static Fragment newInstance() {
        return new ProductFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerComponent(view);

        ProductFragmentAdapter productFragmentAdapter = new ProductFragmentAdapter(getChildFragmentManager());

        viewPager_product.setAdapter(productFragmentAdapter);
        viewPager_product.setCurrentItem(0);
        viewPager_product.setOffscreenPageLimit(productFragmentAdapter.getCount() - 1);

        tabLayout_product.setupWithViewPager(viewPager_product);
    }

    private void registerComponent(View view) {
        tabLayout_product = view.findViewById(R.id.tab_product);
        viewPager_product = view.findViewById(R.id.view_pager_product);
    }

    private class ProductFragmentAdapter extends FragmentPagerAdapter {

        private String[] title_product = new String[]{
                "On Sale",
                "Sold"
        };

        public ProductFragmentAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return ProductFragmentonsale.newInstance(position);
                case 1:
                    return ProductFragmentsold.newInstance(position);
            }
            return null;
        }

        @Override
        public int getCount() {
            return title_product.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return title_product[position];
        }
    }
}
