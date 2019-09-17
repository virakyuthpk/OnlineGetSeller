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
import com.phsartech.onlinegetseller.adapter.PendingOrderAdapter;
import com.phsartech.onlinegetseller.callback.CallBackFucntionRefreshData;
import com.phsartech.onlinegetseller.callback.CallBackFunctionOnItemClickPending;
import com.phsartech.onlinegetseller.dialog.ItemOrderDialogPending;
import com.phsartech.onlinegetseller.model.OrderModel;
import com.phsartech.onlinegetseller.retrofit.ApiHelper;
import com.phsartech.onlinegetseller.util.LocalDataStore;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragmentPending extends Fragment implements CallBackFunctionOnItemClickPending,
        CallBackFucntionRefreshData {

    private RecyclerView recycler_pending;
    private SpinKitView spinKitView_pending;
    private TextView textView_pending;
    private List<OrderModel.Data> list = new ArrayList<>();
    private List<OrderModel.Data> listClear = new ArrayList<OrderModel.Data>();
    private PendingOrderAdapter pendingOrderAdapter;
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
        OrderFragmentPending fragment = new OrderFragmentPending();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        registerComponent(view);
        swipeRefreshLayout.setOnRefreshListener(refreshEvent);
        textView_pending.setVisibility(View.GONE);
        getData(LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getToken(getActivity()));
        return view;
    }

    private void getData(int id, String token) {
        list.clear();
        ApiHelper.getService().getProductPending(id, token).enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                spinKitView_pending.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                if (response.body().getDataList().toString() == "[]") {
                    textView_pending.setVisibility(View.VISIBLE);
                } else {
                    list = response.body().getDataList();
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
                swipeRefreshLayout.setRefreshing(false);
                spinKitView_pending.setVisibility(View.GONE);
                textView_pending.setText("Sorry Something happen to your internet!");
                textView_pending.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setView() {
        pendingOrderAdapter = new PendingOrderAdapter(getContext(), listClear, this);
        recycler_pending.setAdapter(pendingOrderAdapter);
        recycler_pending.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_pending.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }

    private void registerComponent(View view) {
        recycler_pending = view.findViewById(R.id.recycler);
        spinKitView_pending = view.findViewById(R.id.spin_kit);
        textView_pending = view.findViewById(R.id.txt_status);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_header);
        swipeRefreshLayout.setColorSchemeColors(Color.rgb(21, 112, 191));
    }

    @Override
    public void onItemClickPending(int shop_id, int user_id, String image, String name, String email) {
        ItemOrderDialogPending.display(getFragmentManager(), shop_id, user_id, "all", image, name, email, this);
    }

    @Override
    public void refresh() {
        getData(LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getToken(getActivity()));
    }
}
