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

import com.google.gson.JsonObject;
import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.retrofit.ApiHelper;
import com.phsartech.onlinegetseller.util.LocalDataStore;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReportFragment extends Fragment {

    private String TAG = "ReportFragment";
    private TextView textView_pending, textView_shipping, textView_delivery, textView_canceled,
            textView_instock, textView_low, textView_outstock;
    private View view;

    public static Fragment newInstance() {
        return new ReportFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_report, container, false);
        getData(LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getID(getActivity()), LocalDataStore.getToken(getActivity()));
        registerComponent(view);
        return view;
    }

    private void registerComponent(View view) {
        textView_pending = view.findViewById(R.id.text_pending);
        textView_shipping = view.findViewById(R.id.text_shipping);
        textView_delivery = view.findViewById(R.id.text_delivery);
        textView_canceled = view.findViewById(R.id.text_canceled);
        textView_instock = view.findViewById(R.id.text_instock);
        textView_low = view.findViewById(R.id.text_low);
        textView_outstock = view.findViewById(R.id.text_outstock);
    }

    private void getData(int shopid, int id, String token) {
        ApiHelper.getService().getOrderReport(shopid, token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    setViewOrder(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        ApiHelper.getService().getProductReport(id, token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    setViewProduct(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void setViewProduct(JSONObject jsonObject) {
        try {
            textView_instock.setText(jsonObject.getInt("instock") + "");
            textView_low.setText(jsonObject.getInt("low") + "");
            textView_outstock.setText(jsonObject.getInt("outstock") + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setViewOrder(JSONObject jsonObject) {
        try {
            textView_pending.setText(jsonObject.getInt("pending") + "");
            textView_shipping.setText(jsonObject.getInt("shipping") + "");
            textView_delivery.setText(jsonObject.getInt("delivery") + "");
            textView_canceled.setText(jsonObject.getInt("cancel") + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
