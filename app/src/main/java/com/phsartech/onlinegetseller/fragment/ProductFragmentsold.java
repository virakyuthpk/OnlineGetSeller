package com.phsartech.onlinegetseller.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.ybq.android.spinkit.SpinKitView;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;
import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.adapter.SoldProductAdapter;
import com.phsartech.onlinegetseller.model.ProductModelSold;
import com.phsartech.onlinegetseller.retrofit.ApiHelper;
import com.phsartech.onlinegetseller.util.LocalDataStore;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragmentsold extends Fragment {

    private static String TAG = "ProductFragmentsold";
    private RecyclerView recycler_sold;
    private SpinKitView spinKitView_sold;
    private TextView textView_sold;
    private boolean isSwapRefresh;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener refreshEvent = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            isSwapRefresh = true;
            getData(LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getToken(getActivity()));
        }
    };
    private List<ProductModelSold.Data> list = new ArrayList<>();
    private List<ProductModelSold.Data> listClear = new ArrayList<>();
    private SoldProductAdapter soldProductAdapter;

    public static Fragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("argument_position", position);
        ProductFragmentsold fragment = new ProductFragmentsold();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        registerComponent(view);
        swipeRefreshLayout.setOnRefreshListener(refreshEvent);
        textView_sold.setVisibility(View.GONE);
        getData(LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getToken(getActivity()));
        return view;
    }

    private void getData(int shop_id, String token) {
        list.clear();
        ApiHelper.getService().getProductSold(shop_id, token).enqueue(new Callback<ProductModelSold>() {
            @Override
            public void onResponse(Call<ProductModelSold> call, Response<ProductModelSold> response) {
                spinKitView_sold.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                if (response.body().getDataList().toString() == "[]") {
                    textView_sold.setVisibility(View.VISIBLE);
                } else {
                    list = response.body().getDataList();
                    if (isSwapRefresh) {
                        isSwapRefresh = false;
                        listClear.clear();
                    }
                    listClear.addAll(list);
                    setView();
                    soldProductAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ProductModelSold> call, Throwable t) {
                Log.e("onFailure: ", t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
                spinKitView_sold.setVisibility(View.GONE);
                textView_sold.setText("Sorry Something happen to your internet!");
                textView_sold.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setView() {
        soldProductAdapter = new SoldProductAdapter(getContext(), listClear);
        recycler_sold.setAdapter(soldProductAdapter);
        recycler_sold.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_sold.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }

    private void registerComponent(View view) {
        recycler_sold = view.findViewById(R.id.recycler);
        spinKitView_sold = view.findViewById(R.id.spin_kit);
        textView_sold = view.findViewById(R.id.txt_status);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_header);
        swipeRefreshLayout.setColorSchemeColors(Color.rgb(21, 112, 191));
    }
}
