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
import com.phsartech.onlinegetseller.adapter.ShippingOrderAdapter;
import com.phsartech.onlinegetseller.callback.CallBackFunctionOnItemClick;
import com.phsartech.onlinegetseller.callback.EndlessRecyclerViewScrollListener;
import com.phsartech.onlinegetseller.dialog.ItemOrderDialog;
import com.phsartech.onlinegetseller.model.OrderModel;
import com.phsartech.onlinegetseller.retrofit.ApiHelper;
import com.phsartech.onlinegetseller.util.LocalDataStore;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragmentShipping extends Fragment implements CallBackFunctionOnItemClick {

    private RecyclerView recycler_shipping;
    private SwipeRefreshLayout swipeRefreshLayout_shipping;
    private EndlessRecyclerViewScrollListener listener;
    private SpinKitView spinKitView_shipping;
    private TextView textView_shipping;
    private SwipeRefreshLayout.OnRefreshListener refreshEvent = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            isSwapRefresh = true;
            listener.resetState();
            getData(LocalDataStore.getID(getActivity()), LocalDataStore.getToken(getActivity()));
        }
    };
    private List<OrderModel.Data> list = new ArrayList<>();
    private List<OrderModel.Data> listClear = new ArrayList<OrderModel.Data>();
    private ShippingOrderAdapter shippingOrderAdapter;


    private boolean isSwapRefresh;
    private LinearLayoutManager manager;

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

        swipeRefreshLayout_shipping.setOnRefreshListener(refreshEvent);

        textView_shipping.setVisibility(View.GONE);

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
        spinKitView_shipping.setVisibility(View.VISIBLE);
        ApiHelper.getService().getProductShippingNext(id, item).enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                spinKitView_shipping.setVisibility(View.GONE);
                listClear.addAll(response.body().getDataList());
                shippingOrderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                Log.e("onFailure: ", t.getMessage());
            }
        });
    }

    private void getData(int id, String token) {
        list.clear();
        ApiHelper.getService().getProductShipping(id, token).enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                if (response.body().getDataList().toString() == "[]") {
                    spinKitView_shipping.setVisibility(View.GONE);
//                    textView_shipping.setVisibility(View.VISIBLE);
                    swipeRefreshLayout_shipping.setRefreshing(false);
                } else {
                    list = response.body().getDataList();
                    spinKitView_shipping.setVisibility(View.GONE);
                    swipeRefreshLayout_shipping.setRefreshing(false);
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
                swipeRefreshLayout_shipping.setRefreshing(false);
                spinKitView_shipping.setVisibility(View.GONE);
                textView_shipping.setText("Sorry Something happen to your internet!");
                textView_shipping.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setView() {
        shippingOrderAdapter = new ShippingOrderAdapter(getContext(), listClear, this);
        recycler_shipping.setAdapter(shippingOrderAdapter);
        recycler_shipping.setLayoutManager(manager);
        recycler_shipping.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }

    private void registerComponent(View view) {
        recycler_shipping = view.findViewById(R.id.recycler);
        swipeRefreshLayout_shipping = view.findViewById(R.id.swipe_refresh);
        spinKitView_shipping = view.findViewById(R.id.spin_kit);
        textView_shipping = view.findViewById(R.id.txt_status);
    }

    @Override
    public void onItemClick(int shop_id, int user_id, String image, String name, String email) {
        ItemOrderDialog.display(getFragmentManager(), shop_id, user_id, "all", image, name, email);
    }
}