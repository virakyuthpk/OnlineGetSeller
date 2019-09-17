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
import com.phsartech.onlinegetseller.adapter.CanceledOrderAdapter;
import com.phsartech.onlinegetseller.callback.CallBackFunctionOnItemClickCanceled;
import com.phsartech.onlinegetseller.dialog.ItemOrderDialogCanceled;
import com.phsartech.onlinegetseller.model.OrderModel;
import com.phsartech.onlinegetseller.retrofit.ApiHelper;
import com.phsartech.onlinegetseller.util.LocalDataStore;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragmentCanceled extends Fragment implements CallBackFunctionOnItemClickCanceled {

    private RecyclerView recycler_canceled;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SpinKitView spinKitView_canceled;
    private TextView textView_canceled;
    private List<OrderModel.Data> list = new ArrayList<>();
    private List<OrderModel.Data> listClear = new ArrayList<>();
    private CanceledOrderAdapter canceledOrderAdapter;
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
        OrderFragmentCanceled fragment = new OrderFragmentCanceled();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        registerComponent(view);
        swipeRefreshLayout.setOnRefreshListener(refreshEvent);
        textView_canceled.setVisibility(View.GONE);
        getData(LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getToken(getActivity()));
        return view;
    }

    private void getData(int id, String token) {
        list.clear();
        ApiHelper.getService().getProductCanceled(id, token).enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                swipeRefreshLayout.setRefreshing(false);
                spinKitView_canceled.setVisibility(View.GONE);
                if (response.body().getDataList().toString() == "[]") {
                    textView_canceled.setVisibility(View.VISIBLE);
                } else {
                    list = response.body().getDataList();
                    spinKitView_canceled.setVisibility(View.GONE);
                    if (isSwapRefresh) {
                        isSwapRefresh = false;
                        listClear.clear();
                    }
                    listClear.addAll(list);
                    setView();
                    canceledOrderAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                Log.e("onFailure: ", t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
                spinKitView_canceled.setVisibility(View.GONE);
                textView_canceled.setText("Sorry Something happen to your internet!");
                textView_canceled.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setView() {
        canceledOrderAdapter = new CanceledOrderAdapter(getContext(), listClear, this);
        recycler_canceled.setAdapter(canceledOrderAdapter);
        recycler_canceled.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_canceled.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }

    private void registerComponent(View view) {
        recycler_canceled = view.findViewById(R.id.recycler);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_header);
        spinKitView_canceled = view.findViewById(R.id.spin_kit);
        textView_canceled = view.findViewById(R.id.txt_status);
        swipeRefreshLayout.setColorSchemeColors(Color.rgb(21, 112, 191));
    }

    @Override
    public void onItemClickCanceled(int shop_id, int user_id, String image, String name, String email) {
        ItemOrderDialogCanceled.display(getFragmentManager(), shop_id, user_id, "all", image, name, email);
    }
}
