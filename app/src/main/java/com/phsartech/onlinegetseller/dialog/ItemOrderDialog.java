package com.phsartech.onlinegetseller.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.adapter.ItemAllOrderAdapter;
import com.phsartech.onlinegetseller.model.OrderModel;
import com.phsartech.onlinegetseller.retrofit.ApiHelper;
import com.phsartech.onlinegetseller.util.LocalDataStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemOrderDialog extends DialogFragment {

    private static String TAG = "ItemOrderDialog";
    private CircleImageView circleImageView;
    private TextView textView_buyer_name, textView_buyer_email;
    private RecyclerView recyclerView;
    private int shop_id, buyer_id;
    private List<OrderModel.Data> list;
    private ItemAllOrderAdapter itemAllOrderAdapter;

    public ItemOrderDialog(int shop_id, int buyer_id) {
        this.shop_id = shop_id;
        this.buyer_id = buyer_id;
    }

    public static ItemOrderDialog display(FragmentManager fragmentManager, int shop_id, int buyer_id) {
        ItemOrderDialog exampleDialog = new ItemOrderDialog(shop_id, buyer_id);
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_order, container, false);
        registerComponent(view);
        getData(LocalDataStore.getToken(getActivity()), shop_id, buyer_id);
        return view;
    }

    private void getData(String token, int shop_id, int buyer_id) {
        ApiHelper.getService().getItemOrder(token, shop_id, buyer_id).enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                list = response.body().getDataList();
                setView();
                itemAllOrderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void setView() {
        itemAllOrderAdapter = new ItemAllOrderAdapter(getContext(), list);
        recyclerView.setAdapter(itemAllOrderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }

    private void registerComponent(View view) {
        circleImageView = view.findViewById(R.id.image_profile_buyer);
        textView_buyer_name = view.findViewById(R.id.text_username_buyer);
        textView_buyer_email = view.findViewById(R.id.text_email_buyer);
        recyclerView = view.findViewById(R.id.recycler_item_order);
    }
}

