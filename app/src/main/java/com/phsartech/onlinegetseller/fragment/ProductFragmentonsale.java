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
import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.adapter.OnsaleProductAdapter;
import com.phsartech.onlinegetseller.callback.CallBackFucntionOnItemOnSaleClick;
import com.phsartech.onlinegetseller.dialog.ProductDialog;
import com.phsartech.onlinegetseller.model.ProductModelOnSale;
import com.phsartech.onlinegetseller.retrofit.ApiHelper;
import com.phsartech.onlinegetseller.util.LocalDataStore;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragmentonsale extends Fragment implements CallBackFucntionOnItemOnSaleClick {

    private static String TAG = "ProductFragmentonsale";
    private RecyclerView recycler_onsale;
    private SpinKitView spinKitView_onsale;
    private TextView textView_onsale;
    private List<ProductModelOnSale.Data> list = new ArrayList<>();
    private List<ProductModelOnSale.Data> listClear = new ArrayList<>();
    private OnsaleProductAdapter onsaleProductAdapter;
    private boolean isSwapRefresh;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener refreshEvent = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            isSwapRefresh = true;
            getData(LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getToken(getActivity()));
        }
    };

    public static Fragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("argument_position", position);
        ProductFragmentonsale fragment = new ProductFragmentonsale();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        registerComponent(view);
        swipeRefreshLayout.setOnRefreshListener(refreshEvent);
        textView_onsale.setVisibility(View.GONE);
        getData(LocalDataStore.getID(getActivity()), LocalDataStore.getToken(getActivity()));
        return view;
    }

    private void getData(int id, String token) {
        list.clear();
        ApiHelper.getService().getProductOnSale(id, token).enqueue(new Callback<ProductModelOnSale>() {
            @Override
            public void onResponse(Call<ProductModelOnSale> call, Response<ProductModelOnSale> response) {
                spinKitView_onsale.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                if (response.body().getDataList().toString() == "[]") {
                    textView_onsale.setVisibility(View.VISIBLE);
                } else {
                    list = response.body().getDataList();
                    if (isSwapRefresh) {
                        isSwapRefresh = false;
                        listClear.clear();
                    }
                    listClear.addAll(list);
                    setView();
                    onsaleProductAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ProductModelOnSale> call, Throwable t) {
                Log.e("onFailure: ", t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
                spinKitView_onsale.setVisibility(View.GONE);
                textView_onsale.setText("Sorry Something happen to your internet!");
                textView_onsale.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setView() {
        onsaleProductAdapter = new OnsaleProductAdapter(getContext(), listClear, this);
        recycler_onsale.setAdapter(onsaleProductAdapter);
        recycler_onsale.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_onsale.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }

    private void registerComponent(View view) {
        recycler_onsale = view.findViewById(R.id.recycler);
        spinKitView_onsale = view.findViewById(R.id.spin_kit);
        textView_onsale = view.findViewById(R.id.txt_status);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_header);
        swipeRefreshLayout.setColorSchemeColors(Color.rgb(21, 112, 191));
    }

    @Override
    public void OnClickItem(ProductModelOnSale.Data item) {
        ProductDialog.display(getFragmentManager(), item);
    }
}