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
import com.phsartech.onlinegetseller.adapter.DeliveryOrderAdapter;
import com.phsartech.onlinegetseller.callback.CallBackFunctionOnItemClickDelivery;
import com.phsartech.onlinegetseller.dialog.ItemOrderDialogDelivery;
import com.phsartech.onlinegetseller.model.OrderModel;
import com.phsartech.onlinegetseller.retrofit.ApiHelper;
import com.phsartech.onlinegetseller.util.LocalDataStore;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragmentDelivery extends Fragment implements CallBackFunctionOnItemClickDelivery {

    private RecyclerView recycler_delivered;
    private SpinKitView spinKitView_delivered;
    private TextView textView_delivered;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<OrderModel.Data> list = new ArrayList<>();
    private List<OrderModel.Data> listClear = new ArrayList<>();
    private DeliveryOrderAdapter deliveredOrderAdapter;
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
        OrderFragmentDelivery fragment = new OrderFragmentDelivery();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        registerComponent(view);
        swipeRefreshLayout.setOnRefreshListener(refreshEvent);
        textView_delivered.setVisibility(View.GONE);
        getData(LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getToken(getActivity()));
        return view;
    }

    private void getData(int id, String token) {
        list.clear();
        ApiHelper.getService().getProductDelivery(id, token).enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                spinKitView_delivered.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                if (response.body().getDataList().toString() == "[]") {
                    textView_delivered.setVisibility(View.VISIBLE);
                } else {
                    list = response.body().getDataList();
                    if (isSwapRefresh) {
                        isSwapRefresh = false;
                        listClear.clear();
                    }
                    listClear.addAll(list);
                    setView();
                    deliveredOrderAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                Log.e("onFailure: ", t.getMessage());
                spinKitView_delivered.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                textView_delivered.setText("Sorry Something happen to your internet!");
                textView_delivered.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setView() {
        deliveredOrderAdapter = new DeliveryOrderAdapter(getContext(), listClear, this);
        recycler_delivered.setAdapter(deliveredOrderAdapter);
        recycler_delivered.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_delivered.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }

    private void registerComponent(View view) {
        recycler_delivered = view.findViewById(R.id.recycler);
        spinKitView_delivered = view.findViewById(R.id.spin_kit);
        textView_delivered = view.findViewById(R.id.txt_status);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_header);
        swipeRefreshLayout.setColorSchemeColors(Color.rgb(21, 112, 191));
    }

    @Override
    public void onItemClickDelivery(int shop_id, int user_id, String image, String name, String email) {
        ItemOrderDialogDelivery.display(getFragmentManager(), shop_id, user_id, "all", image, name, email);
    }
}
