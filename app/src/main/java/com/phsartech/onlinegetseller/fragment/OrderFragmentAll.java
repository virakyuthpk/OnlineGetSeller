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
import com.phsartech.onlinegetseller.adapter.AllOrderAdapter;
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

public class OrderFragmentAll extends Fragment implements CallBackFunctionOnItemClick {

    private RecyclerView recycler_all;
    private SwipeRefreshLayout swipeRefreshLayout_all;
    private EndlessRecyclerViewScrollListener listener;
    private SpinKitView spinKitView_all;
    private TextView textView_all;
    private SwipeRefreshLayout.OnRefreshListener refreshEvent = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            isSwapRefresh = true;
            listener.resetState();
            getData(LocalDataStore.getID(getActivity()), LocalDataStore.getToken(getActivity()));
        }
    };
    private List<OrderModel.Data> list = new ArrayList<>();
    private List<OrderModel.Data> listClear = new ArrayList<>();
    private AllOrderAdapter allOrderAdapter;


    private boolean isSwapRefresh;
    private LinearLayoutManager manager;
    private String TAG = "OrderFragmentAll";

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

        swipeRefreshLayout_all.setOnRefreshListener(refreshEvent);

        textView_all.setVisibility(View.GONE);

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
        spinKitView_all.setVisibility(View.VISIBLE);
        ApiHelper.getService().getProductAllNext(id, item).enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                spinKitView_all.setVisibility(View.GONE);
                listClear.addAll(response.body().getDataList());
                allOrderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                Log.e("onFailure: ", t.getMessage());
            }
        });
    }

    private void getData(int id, String token) {
        list.clear();
        ApiHelper.getService().getProductAll(id, token).enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                if (response.body().getDataList().toString() == "[]") {
                    spinKitView_all.setVisibility(View.GONE);
//                    textView_all.setVisibility(View.VISIBLE);
                    swipeRefreshLayout_all.setRefreshing(false);
                } else {
                    list = response.body().getDataList();
                    spinKitView_all.setVisibility(View.GONE);
                    swipeRefreshLayout_all.setRefreshing(false);
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
                swipeRefreshLayout_all.setRefreshing(false);
                spinKitView_all.setVisibility(View.GONE);
                textView_all.setText("Sorry Something happen to your internet!");
                textView_all.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setView() {
        allOrderAdapter = new AllOrderAdapter(getContext(), listClear, this);
        recycler_all.setAdapter(allOrderAdapter);
        recycler_all.setLayoutManager(manager);
        recycler_all.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }

    private void registerComponent(View view) {
        recycler_all = view.findViewById(R.id.recycler);
        swipeRefreshLayout_all = view.findViewById(R.id.swipe_refresh);
        spinKitView_all = view.findViewById(R.id.spin_kit);
        textView_all = view.findViewById(R.id.txt_status);
    }

    @Override
    public void onItemClick(int shop_id, int user_id) {
        ItemOrderDialog.display(getFragmentManager(), shop_id, user_id);
    }
}
