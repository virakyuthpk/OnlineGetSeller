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
import com.phsartech.onlinegetseller.adapter.ShippingOrderAdapter;
import com.phsartech.onlinegetseller.callback.CallBackFucntionRefreshData;
import com.phsartech.onlinegetseller.callback.CallBackFunctionOnItemClickShipping;
import com.phsartech.onlinegetseller.dialog.ItemOrderDialogShipping;
import com.phsartech.onlinegetseller.model.OrderModel;
import com.phsartech.onlinegetseller.retrofit.ApiHelper;
import com.phsartech.onlinegetseller.util.LocalDataStore;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragmentShipping extends Fragment implements CallBackFunctionOnItemClickShipping, CallBackFucntionRefreshData {

    private RecyclerView recycler_shipping;
    private SpinKitView spinKitView_shipping;
    private TextView textView_shipping;
    private boolean isSwapRefresh;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener refreshEvent = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            isSwapRefresh = true;
            getData(LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getToken(getActivity()));
        }
    };
    private List<OrderModel.Data> list = new ArrayList<>();
    private List<OrderModel.Data> listClear = new ArrayList<OrderModel.Data>();
    private ShippingOrderAdapter shippingOrderAdapter;

    public static Fragment newInstance(int position) {

        Bundle args = new Bundle();
        args.putInt("argument_position", position);
        OrderFragmentShipping fragment = new OrderFragmentShipping();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        registerComponent(view);
        swipeRefreshLayout.setOnRefreshListener(refreshEvent);
        textView_shipping.setVisibility(View.GONE);
        getData(LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getToken(getActivity()));
        return view;
    }

    private void getData(int id, String token) {
        list.clear();
        ApiHelper.getService().getProductShipping(id, token).enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                spinKitView_shipping.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                if (response.body().getDataList().toString() == "[]") {
                    textView_shipping.setVisibility(View.VISIBLE);
                } else {
                    list = response.body().getDataList();
                    if (isSwapRefresh) {
                        isSwapRefresh = false;
                        listClear.clear();
                    }
                    listClear.addAll(list);
                    setView();
                    shippingOrderAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                Log.e("onFailure: ", t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
                spinKitView_shipping.setVisibility(View.GONE);
                textView_shipping.setText("Sorry Something happen to your internet!");
                textView_shipping.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setView() {
        shippingOrderAdapter = new ShippingOrderAdapter(getContext(), listClear, this);
        recycler_shipping.setAdapter(shippingOrderAdapter);
        recycler_shipping.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_shipping.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }

    private void registerComponent(View view) {
        recycler_shipping = view.findViewById(R.id.recycler);
        spinKitView_shipping = view.findViewById(R.id.spin_kit);
        textView_shipping = view.findViewById(R.id.txt_status);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_header);
        swipeRefreshLayout.setColorSchemeColors(Color.rgb(21, 112, 191));
    }

    @Override
    public void onItemClickShipping(int shop_id, int user_id, String image, String name, String email) {
        ItemOrderDialogShipping.display(getFragmentManager(), shop_id, user_id, "all", image, name, email, this);
    }

    @Override
    public void refresh() {
        getData(LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getToken(getActivity()));
    }
}