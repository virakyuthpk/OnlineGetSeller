package com.phsartech.onlinegetseller.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.activity.ShopActivity;
import com.phsartech.onlinegetseller.callback.CallBackFucntionAfterEdit;
import com.phsartech.onlinegetseller.callback.CallBackFucntionOnButtonLogoutClick;
import com.phsartech.onlinegetseller.dialog.AboutDialog;
import com.phsartech.onlinegetseller.dialog.ChangePassWordDialog;
import com.phsartech.onlinegetseller.dialog.EditDialog;
import com.phsartech.onlinegetseller.dialog.PolicyDialog;
import com.phsartech.onlinegetseller.dialog.SaleOnOnlineGetDialog;
import com.phsartech.onlinegetseller.retrofit.ApiHelper;
import com.phsartech.onlinegetseller.util.LocalDataStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class SettingFragment extends Fragment implements CallBackFucntionAfterEdit {

    private View view;
    private CircleImageView circleImageView_user;
    private MaterialButton materialButton_shop,
            materialButton_username, materialButton_email,
            materialButton_phone, materialButton_address,
            materialButton_policy, materialButton_des,
            materialButton_sale_on_onlineget, materialButton_about,
            materialButton_logout, materialButton_change_password;
    private TextView textView_username;
    private FloatingActionButton floatingActionButton_camera;
    private String TAG = "SettingFragment";
    private static JSONObject jsonOject_shop;
    private CallBackFucntionOnButtonLogoutClick callBackFucntionOnButtonLogoutClick;
    private Fragment fragment = this;

    public static JSONObject getJsonOject_shop() {
        return jsonOject_shop;
    }

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
        getDataShop(LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getID(getActivity()), LocalDataStore.getToken(getActivity()));
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
                startActivity(new Intent(getActivity(), ShopActivity.class));
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
                editInfo("Username");
            }
        });
        materialButton_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfo("Email");
            }
        });
        materialButton_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfo("Phone");
            }
        });
        materialButton_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfo("Address");
            }
        });
        materialButton_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfo("Description");
            }
        });
        floatingActionButton_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            2000);
                } else {
                    startGallery();
                }
            }
        });
    }

    private void startGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1000);
        }

    }

    private void editInfo(String value) {
        EditDialog.display(getFragmentManager(), value, this);
    }

    private void getDataShop(int id, int user_id, String token) {
        ApiHelper.getService().getShopDetail(id, user_id, token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject object = new JSONObject(response.body().toString());
                    JSONObject jsonObject = new JSONObject(String.valueOf(object.getJSONObject("data")));
                    jsonOject_shop = jsonObject;
                    if (jsonObject.getString("shop_name") != "null") {
                        materialButton_shop.setText(jsonObject.getString("shop_name"));
                    } else {
                        materialButton_shop.setText("Click to edit shop!");
                    }
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
            if (jsonObject.getString("username") != "null") {
                materialButton_username.setText("Username : " + jsonObject.getString("username") + "");
                textView_username.setText(jsonObject.getString("username") + "");
            } else {
                textView_username.setText("Username");
                materialButton_username.setText("Click to edit username");
            }
            if (jsonObject.getString("email") != "null") {
                materialButton_email.setText("Email : " + jsonObject.getString("email") + "");
            } else {
                materialButton_email.setText("Click to edit email");
            }
            if (jsonObject.getString("bio") != "null") {
                materialButton_des.setText("Description : " + jsonObject.getString("bio") + "");
            } else {
                materialButton_des.setText("Click to edit Description");
            }
            if (jsonObject.getString("phone") != "null") {
                materialButton_phone.setText("Phone : " + jsonObject.getString("phone") + "");
            } else {
                materialButton_phone.setText("Click to edit phone");
            }
            if (jsonObject.getString("address") != "null") {
                materialButton_address.setText("Address : " + jsonObject.getString("address") + "");
            } else {
                materialButton_address.setText("Click to edit address");
            }
            Glide.with(this)
                    .load(jsonObject.getString("image_path"))
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(circleImageView_user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void registerComponent(View view) {
        circleImageView_user = view.findViewById(R.id.image_profile_user);
        materialButton_change_password = view.findViewById(R.id.button_change_password);
        textView_username = view.findViewById(R.id.text_username);
        materialButton_shop = view.findViewById(R.id.button_shop);
        floatingActionButton_camera = view.findViewById(R.id.button_camera);
        materialButton_username = view.findViewById(R.id.button_username);
        materialButton_email = view.findViewById(R.id.button_email);
        materialButton_phone = view.findViewById(R.id.button_phone);
        materialButton_address = view.findViewById(R.id.button_address);
        materialButton_policy = view.findViewById(R.id.button_policy);
        materialButton_sale_on_onlineget = view.findViewById(R.id.button_sale_on_onlineget);
        materialButton_about = view.findViewById(R.id.button_about);
        materialButton_des = view.findViewById(R.id.button_des);
        materialButton_logout = view.findViewById(R.id.button_logout);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                Uri returnUri = data.getData();
                editProfile(returnUri);
            }
        }
    }

    private void editProfile(final Uri returnUri) {

        String filePath = getRealPathFromURIPath(returnUri, getActivity());
        File file = new File(filePath);
        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), mFile);

        int id = LocalDataStore.getID(getActivity());
        String token = LocalDataStore.getToken(getActivity());

        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), id + "");
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "",
                "Loading, Please wait...", true);
        ApiHelper.getService().editProfile(token, fileToUpload, requestBody).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setMessage("Change Successful!");
                    alertDialog.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            alertDialog.dismiss();
                        }
                    }, 1000);
                    Glide.with(fragment)
                            .load(returnUri)
                            .into(circleImageView_user);
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

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    @Override
    public void afterEdit(String control, String value) {
        if (control == "Username") {
            textView_username.setText(value);
            materialButton_username.setText("Username : " + value);
        } else if (control == "Email") {
            materialButton_email.setText("Email : " + value);
        } else if (control == "Phone") {
            materialButton_phone.setText("Phone : " + value);
        } else if (control == "Address") {
            materialButton_address.setText("Address : " + value);
        } else if (control == "Description") {
            materialButton_des.setText("Description : " + value);
        }
    }
}
