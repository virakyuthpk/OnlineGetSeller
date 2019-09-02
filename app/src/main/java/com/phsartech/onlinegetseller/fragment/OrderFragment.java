package com.phsartech.onlinegetseller.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.phsartech.onlinegetseller.R;


public class OrderFragment extends Fragment {

    private TabLayout tabLayout_order;
    private ViewPager viewPager_order;

    public static Fragment newInstance() {
        return new OrderFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerComponent(view);

        OrderFragmentAdapter orderFragmentAdapter = new OrderFragmentAdapter(getChildFragmentManager());

        viewPager_order.setAdapter(orderFragmentAdapter);
        viewPager_order.setCurrentItem(0);
        viewPager_order.setOffscreenPageLimit(orderFragmentAdapter.getCount() - 1);

        tabLayout_order.setupWithViewPager(viewPager_order);
    }

    private void registerComponent(View view) {
        tabLayout_order = view.findViewById(R.id.tab_order);
        viewPager_order = view.findViewById(R.id.view_pager_order);
    }

    private class OrderFragmentAdapter extends FragmentPagerAdapter {

        private String[] title_order = new String[]{
                "All",
                "Pending",
                "Shipping",
                "Delivery",
                "Canceled"
        };

        public OrderFragmentAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return OrderFragmentAll.newInstance(position);
                case 1:
                    return OrderFragmentPending.newInstance(position);
                case 2:
                    return OrderFragmentShipping.newInstance(position);
                case 3:
                    return OrderFragmentDelivered.newInstance(position);
                case 4:
                    return OrderFragmentCanceled.newInstance(position);
            }
            return null;
        }

        @Override
        public int getCount() {
            return title_order.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return title_order[position];
        }
    }
}
