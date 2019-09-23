package com.phsartech.onlinegetseller.dialog;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.adapter.ItemPendingOrderAdapter;
import com.phsartech.onlinegetseller.callback.CallBackFucntionAcceptPending;
import com.phsartech.onlinegetseller.callback.CallBackFucntionDeniedPending;
import com.phsartech.onlinegetseller.callback.CallBackFucntionRefreshData;
import com.phsartech.onlinegetseller.model.OrderModel;
import com.phsartech.onlinegetseller.retrofit.ApiHelper;
import com.phsartech.onlinegetseller.util.LocalDataStore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemOrderDialogPending extends DialogFragment implements CallBackFucntionAcceptPending, CallBackFucntionDeniedPending {

    private static String TAG = "ItemOrderDialogAll";
    private final String control, image, name, email;
    private final CallBackFucntionRefreshData callBackFucntionRefreshData;
    private CircleImageView circleImageView;
    private TextView textView_buyer_name, textView_buyer_email;
    private RecyclerView recyclerView;
    private int shop_id, buyer_id;
    private List<OrderModel.Data> list;
    private ItemPendingOrderAdapter itemPendingOrderAdapter;
    private Toolbar toolbar;

    public ItemOrderDialogPending(int shop_id, int buyer_id, String control, String image, String name, String email, CallBackFucntionRefreshData callBackFucntionRefreshData) {
        this.shop_id = shop_id;
        this.control = control;
        this.image = image;
        this.name = name;
        this.email = email;
        this.buyer_id = buyer_id;
        this.callBackFucntionRefreshData = callBackFucntionRefreshData;
    }

    public static ItemOrderDialogPending display(FragmentManager fragmentManager, int shop_id, int buyer_id, String control, String image, String name, String email, CallBackFucntionRefreshData callBackFucntionRefreshData) {
        ItemOrderDialogPending exampleDialog = new ItemOrderDialogPending(shop_id, buyer_id, control, image, name, email, callBackFucntionRefreshData);
        exampleDialog.show(fragmentManager, TAG);
        return exampleDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    private void setupToolbar() {
        if (toolbar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                toolbar.setTitle("Order Pending");
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dismiss();
                        callBackFucntionRefreshData.refresh();
                    }
                });
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_order, container, false);
        registerComponent(view);
        textView_buyer_name.setText(name);
        textView_buyer_email.setText("Email : " + email);
        if (image != null) {
            Glide.with(this)
                    .load(image)
                    .placeholder(R.drawable.noimg)
                    .into(circleImageView);
        }
        getData(LocalDataStore.getToken(getActivity()), shop_id, buyer_id);
        return view;
    }

    private void getData(String token, int shop_id, int buyer_id) {
        ApiHelper.getService().getItemOrderPending(token, shop_id, buyer_id).enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                list = response.body().getDataList();
                setView();
                itemPendingOrderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void setView() {
        itemPendingOrderAdapter = new ItemPendingOrderAdapter(getContext(), list, this, this);
        recyclerView.setAdapter(itemPendingOrderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }

    private void registerComponent(View view) {
        circleImageView = view.findViewById(R.id.image_profile_buyer);
        textView_buyer_name = view.findViewById(R.id.text_username_buyer);
        textView_buyer_email = view.findViewById(R.id.text_email_buyer);
        recyclerView = view.findViewById(R.id.recycler_item_order);
        toolbar = view.findViewById(R.id.toolbar);
    }

    @Override
    public void acceptPending(OrderModel.Data item) {
        ApiHelper.getService().acceptPending(LocalDataStore.getToken(getActivity()), item.getId()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setMessage("Status change Successful!");
                alertDialog.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        alertDialog.dismiss();
                    }
                }, 1000);
                getData(LocalDataStore.getToken(getActivity()), shop_id, buyer_id);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void deniedPending(OrderModel.Data item) {
        ApiHelper.getService().deniedPending(LocalDataStore.getToken(getActivity()), item.getId()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setMessage("Status change Successful!");
                alertDialog.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        alertDialog.dismiss();
                    }
                }, 1000);
                getData(LocalDataStore.getToken(getActivity()), shop_id, buyer_id);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}

