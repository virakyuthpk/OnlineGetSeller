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
import com.phsartech.onlinegetseller.adapter.AllOrderAdapter;
import com.phsartech.onlinegetseller.callback.CallBackFucntionRefreshData;
import com.phsartech.onlinegetseller.callback.CallBackFunctionOnItemClickAll;
import com.phsartech.onlinegetseller.dialog.ItemOrderDialogAll;
import com.phsartech.onlinegetseller.model.OrderModel;
import com.phsartech.onlinegetseller.retrofit.ApiHelper;
import com.phsartech.onlinegetseller.util.LocalDataStore;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragmentAll extends Fragment
        implements CallBackFunctionOnItemClickAll,
        CallBackFucntionRefreshData {

    private RecyclerView recycler_all;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SpinKitView spinKitView_all;
    private TextView textView_all;
    private List<OrderModel.Data> list = new ArrayList<>();
    private List<OrderModel.Data> listClear = new ArrayList<>();
    private AllOrderAdapter allOrderAdapter;
    private String TAG = "OrderFragmentAll";
    private boolean isSwapRefresh;
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
        OrderFragmentAll fragment = new OrderFragmentAll();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        registerComponent(view);
        swipeRefreshLayout.setOnRefreshListener(refreshEvent);
        textView_all.setVisibility(View.GONE);
        getData(LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getToken(getActivity()));
        return view;
    }

    private void getData(int id, String token) {
        list.clear();
        ApiHelper.getService().getProductAll(id, token).enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                swipeRefreshLayout.setRefreshing(false);
                spinKitView_all.setVisibility(View.GONE);
                if (response.body().getDataList().toString() == "[]") {
                    textView_all.setVisibility(View.VISIBLE);
                } else {
                    list = response.body().getDataList();
                    spinKitView_all.setVisibility(View.GONE);
                    if (isSwapRefresh) {
                        isSwapRefresh = false;
                        listClear.clear();
                    }
                    listClear.addAll(list);
                    setView();
                    allOrderAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                Log.e("onFailure: ", t.getMessage());
                spinKitView_all.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                textView_all.setText("Sorry Something happen to your internet!");
                textView_all.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setView() {
        allOrderAdapter = new AllOrderAdapter(getContext(), listClear, this);
        recycler_all.setAdapter(allOrderAdapter);
        recycler_all.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_all.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }

    private void registerComponent(View view) {
        recycler_all = view.findViewById(R.id.recycler);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_header);
        spinKitView_all = view.findViewById(R.id.spin_kit);
        textView_all = view.findViewById(R.id.txt_status);
        swipeRefreshLayout.setColorSchemeColors(Color.rgb(21, 112, 191));
    }

    @Override
    public void onItemClickAll(int shop_id, int user_id, String image, String name, String email) {
        ItemOrderDialogAll.display(getFragmentManager(), shop_id, user_id, "all", image, name, email, this);
    }

    @Override
    public void refresh() {
        getData(LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getToken(getActivity()));
    }
}
