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
import com.phsartech.onlinegetseller.adapter.PendingOrderAdapter;
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

public class OrderFragmentPending extends Fragment implements CallBackFunctionOnItemClick {

    private RecyclerView recycler_pending;
    private SwipeRefreshLayout swipeRefreshLayout_pending;
    private EndlessRecyclerViewScrollListener listener;
    private SpinKitView spinKitView_pending;
    private TextView textView_pending;
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
    private PendingOrderAdapter pendingOrderAdapter;


    private boolean isSwapRefresh;
    private LinearLayoutManager manager;

    public static Fragment newInstance(int position) {

        Bundle args = new Bundle();
        args.putInt("argument_position", position);
        OrderFragmentPending fragment = new OrderFragmentPending();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);

        registerComponent(view);

        swipeRefreshLayout_pending.setOnRefreshListener(refreshEvent);

        textView_pending.setVisibility(View.GONE);

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
        spinKitView_pending.setVisibility(View.VISIBLE);
        ApiHelper.getService().getProductShippingNext(id, item).enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                spinKitView_pending.setVisibility(View.GONE);
                listClear.addAll(response.body().getDataList());
                pendingOrderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                Log.e("onFailure: ", t.getMessage());
            }
        });
    }

    private void getData(int id, String token) {
        list.clear();
        ApiHelper.getService().getProductPending(id, token).enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                if (response.body().getDataList().toString() == "[]") {
                    spinKitView_pending.setVisibility(View.GONE);
//                    textView_pending.setVisibility(View.VISIBLE);
                    swipeRefreshLayout_pending.setRefreshing(false);
                } else {
                    list = response.body().getDataList();
                    spinKitView_pending.setVisibility(View.GONE);
                    swipeRefreshLayout_pending.setRefreshing(false);
                    if (isSwapRefresh) {
                        isSwapRefresh = false;
                        listClear.clear();
                    }
                    listClear.addAll(list);
                    setView();
                    pendingOrderAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                Log.e("onFailure: ", t.getMessage());
                swipeRefreshLayout_pending.setRefreshing(false);
                spinKitView_pending.setVisibility(View.GONE);
                textView_pending.setText("Sorry Something happen to your internet!");
                textView_pending.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setView() {
        pendingOrderAdapter = new PendingOrderAdapter(getContext(), listClear, this);
        recycler_pending.setAdapter(pendingOrderAdapter);
        recycler_pending.setLayoutManager(manager);
        recycler_pending.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }

    private void registerComponent(View view) {
        recycler_pending = view.findViewById(R.id.recycler);
        swipeRefreshLayout_pending = view.findViewById(R.id.swipe_refresh);
        spinKitView_pending = view.findViewById(R.id.spin_kit);
        textView_pending = view.findViewById(R.id.txt_status);
    }

    @Override
    public void onItemClick(int shop_id, int user_id, String image, String name, String email) {
        ItemOrderDialog.display(getFragmentManager(), shop_id, user_id, "all", image, name, email);
    }
}
