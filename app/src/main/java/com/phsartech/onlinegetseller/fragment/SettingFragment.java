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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.callback.CallBackFucntionAfterEdit;
import com.phsartech.onlinegetseller.callback.CallBackFucntionOnButtonLogoutClick;
import com.phsartech.onlinegetseller.dialog.AboutDialog;
import com.phsartech.onlinegetseller.dialog.ChangePassWordDialog;
import com.phsartech.onlinegetseller.dialog.EditDialog;
import com.phsartech.onlinegetseller.dialog.PolicyDialog;
import com.phsartech.onlinegetseller.dialog.SaleOnOnlineGetDialog;
import com.phsartech.onlinegetseller.dialog.ShopDialog;
import com.phsartech.onlinegetseller.retrofit.ApiHelper;
import com.phsartech.onlinegetseller.util.LocalDataStore;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingFragment extends Fragment implements CallBackFucntionAfterEdit {

    private View view;
    private MaterialButton materialButton_shop,
            materialButton_username, materialButton_email,
            materialButton_phone, materialButton_address,
            materialButton_password, materialButton_policy,
            materialButton_des,
            materialButton_sale_on_onlineget, materialButton_about,
            materialButton_logout, materialButton_change_password;
    private TextView textView_username;
    private FloatingActionButton floatingActionButton_camera;
    private String TAG = "SettingFragment";
    private JSONObject jsonOject_shop;
    private CallBackFucntionOnButtonLogoutClick callBackFucntionOnButtonLogoutClick;

    public SettingFragment(CallBackFucntionOnButtonLogoutClick callBackFucntionOnButtonLogoutClick) {
        this.callBackFucntionOnButtonLogoutClick = callBackFucntionOnButtonLogoutClick;
    }

    public static Fragment newInstance(CallBackFucntionOnButtonLogoutClick callBackFucntionOnButtonLogoutClick) {
        return new SettingFragment(callBackFucntionOnButtonLogoutClick);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_setting, container, false);
        registerComponent(view);
        getDataUser(LocalDataStore.getID(getActivity()), LocalDataStore.getToken(getActivity()));
        getDataShop(LocalDataStore.getID(getActivity()), LocalDataStore.getToken(getActivity()));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        materialButton_sale_on_onlineget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaleOnOnlineGetDialog.display(getFragmentManager());
            }
        });
        materialButton_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PolicyDialog.display(getFragmentManager());
            }
        });
        materialButton_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutDialog.display(getFragmentManager());
            }
        });
        materialButton_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePassWordDialog.display(getFragmentManager());
            }
        });
        materialButton_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopDialog.display(getFragmentManager(), jsonOject_shop);
            }
        });
        materialButton_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackFucntionOnButtonLogoutClick.onButtonLogoutClick();
            }
        });
        materialButton_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfo("username");
            }
        });
        materialButton_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfo("email");
            }
        });
        materialButton_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfo("phone");
            }
        });
        materialButton_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfo("address");
            }
        });
        materialButton_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfo("des");
            }
        });
    }

    private void editInfo(String value) {
        EditDialog.display(getFragmentManager(), value, this);
    }

    private void getDataShop(int id, String token) {
        ApiHelper.getService().getShopDetail(id, token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject object = new JSONObject(response.body().toString());
                    JSONObject jsonObject = new JSONObject(String.valueOf(object.getJSONObject("data")));
                    setViewShop(jsonObject);
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

    private void getDataUser(int id, String token) {
        ApiHelper.getService().getProfileDetail(id, token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject object = new JSONObject(response.body().toString());
                    JSONObject jsonObject = new JSONObject(String.valueOf(object.getJSONObject("data")));
                    setViewUser(jsonObject);
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

    private void setViewUser(JSONObject jsonObject) {
        try {
            materialButton_username.setText(jsonObject.getString("username") + "");
            materialButton_email.setText(jsonObject.getString("email") + "");
            materialButton_des.setText(jsonObject.getString("bio") + "");
            materialButton_phone.setText(jsonObject.getString("phone") + "");
            materialButton_address.setText(jsonObject.getString("address") + "");
            textView_username.setText(jsonObject.getString("username") + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setViewShop(JSONObject jsonObject) {
        try {
            this.jsonOject_shop = jsonObject;
            materialButton_shop.setText(jsonObject.getString("shop_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void registerComponent(View view) {
        materialButton_change_password = view.findViewById(R.id.button_change_password);
        textView_username = view.findViewById(R.id.text_username);
        materialButton_shop = view.findViewById(R.id.button_shop);
        floatingActionButton_camera = view.findViewById(R.id.button_camera);
        materialButton_username = view.findViewById(R.id.button_username);
        materialButton_email = view.findViewById(R.id.button_email);
        materialButton_phone = view.findViewById(R.id.button_phone);
        materialButton_address = view.findViewById(R.id.button_address);
        materialButton_password = view.findViewById(R.id.button_change_password);
        materialButton_policy = view.findViewById(R.id.button_policy);
        materialButton_sale_on_onlineget = view.findViewById(R.id.button_sale_on_onlineget);
        materialButton_about = view.findViewById(R.id.button_about);
        materialButton_des = view.findViewById(R.id.button_des);
        materialButton_logout = view.findViewById(R.id.button_logout);
    }

    @Override
    public void afterEdit(String control, String value) {
        if (control == "username") {
            textView_username.setText(value);
            materialButton_username.setText(value);
        } else if (control == "email") {
            materialButton_email.setText(value);
        } else if (control == "phone") {
            materialButton_phone.setText(value);
        } else if (control == "address") {
            materialButton_address.setText(value);
        } else if (control == "des") {
            materialButton_des.setText(value);
        }
    }
}
