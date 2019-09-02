package com.phsartech.onlinegetseller.fragment;

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
import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.adapter.SoldProductAdapter;
import com.phsartech.onlinegetseller.callback.EndlessRecyclerViewScrollListener;
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
    private SwipeRefreshLayout swipeRefreshLayout_sold;
    private EndlessRecyclerViewScrollListener listener;
    private SpinKitView spinKitView_sold;
    private TextView textView_sold;
    private SwipeRefreshLayout.OnRefreshListener refreshEvent = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            isSwapRefresh = true;
            listener.resetState();
            getData(LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getToken(getActivity()));
        }
    };
    private List<ProductModelSold.Data> list = new ArrayList<>();
    private List<ProductModelSold.Data> listClear = new ArrayList<>();
    private SoldProductAdapter soldProductAdapter;


    private boolean isSwapRefresh;
    private LinearLayoutManager manager;

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

        swipeRefreshLayout_sold.setOnRefreshListener(refreshEvent);

        textView_sold.setVisibility(View.GONE);

        setData();
        return view;
    }

    private void setData() {
        manager = new LinearLayoutManager(getActivity());

        getData(LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getToken(getActivity()));

        listener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int item, int totalItemsCount, RecyclerView view) {
                item = +10;
                loadNext(LocalDataStore.getSHOPID(getActivity()), item);
            }
        };

    }

    private void loadNext(int id, int item) {
        spinKitView_sold.setVisibility(View.VISIBLE);
        ApiHelper.getService().getProductSoldNext(id, item).enqueue(new Callback<ProductModelSold>() {
            @Override
            public void onResponse(Call<ProductModelSold> call, Response<ProductModelSold> response) {
                spinKitView_sold.setVisibility(View.GONE);
                listClear.addAll(response.body().getDataList());
                soldProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ProductModelSold> call, Throwable t) {
                Log.e("onFailure: ", t.getMessage());
            }
        });
    }

    private void getData(int shop_id, String token) {
        list.clear();
        ApiHelper.getService().getProductSold(shop_id, token).enqueue(new Callback<ProductModelSold>() {
            @Override
            public void onResponse(Call<ProductModelSold> call, Response<ProductModelSold> response) {
                if (response.body().getDataList().toString() == "[]") {
                    spinKitView_sold.setVisibility(View.GONE);
//                    textView_sold.setVisibility(View.VISIBLE);
                    swipeRefreshLayout_sold.setRefreshing(false);
                } else {
                    list = response.body().getDataList();
                    spinKitView_sold.setVisibility(View.GONE);
                    swipeRefreshLayout_sold.setRefreshing(false);
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
                swipeRefreshLayout_sold.setRefreshing(false);
                spinKitView_sold.setVisibility(View.GONE);
                textView_sold.setText("Sorry Something happen to your internet!");
                textView_sold.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setView() {
        soldProductAdapter = new SoldProductAdapter(getContext(), listClear);
        recycler_sold.setAdapter(soldProductAdapter);
        recycler_sold.setLayoutManager(manager);
        recycler_sold.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }

    private void registerComponent(View view) {
        recycler_sold = view.findViewById(R.id.recycler);
        swipeRefreshLayout_sold = view.findViewById(R.id.swipe_refresh);
        spinKitView_sold = view.findViewById(R.id.spin_kit);
        textView_sold = view.findViewById(R.id.txt_status);
    }
}
