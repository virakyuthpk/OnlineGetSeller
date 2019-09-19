package com.phsartech.onlinegetseller.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;
import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.callback.CallBackFucntionAfterEdit;
import com.phsartech.onlinegetseller.dialog.EditDialog;
import com.phsartech.onlinegetseller.fragment.SettingFragment;
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

public class ShopActivity extends AppCompatActivity implements CallBackFucntionAfterEdit {

    private static String TAG = "ShopActivity";
    private JSONObject jsonObject = SettingFragment.getJsonOject_shop();
    private MaterialButton
            materialButton_shopname,
            materialButton_email,
            materialButton_phone,
            materialButton_address,
            materialButton_description,
            materialButton_logo,
            materialButton_cover;
    private TextView textView_shopname;
    private ImageView imageView_cover;
    private CircleImageView circleImageView_logo;
    private Toolbar toolbar;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ShopActivity.this, MainActivity.class).putExtra("shop", true));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                Uri returnUri = data.getData();
                editLogo(returnUri);
            } else if (requestCode == 2000) {
                Uri returnUri = data.getData();
                editCover(returnUri);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_shop);
        registerComponent();
        setupToolbar();
        materialButton_shopname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editShopInfo("Shop name");
            }
        });
        materialButton_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editShopInfo("Shop email");
            }
        });
        materialButton_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editShopInfo("Shop phone");
            }
        });
        materialButton_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editShopInfo("Shop address");
            }
        });
        materialButton_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editShopInfo("Shop detail");
            }
        });
        materialButton_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ShopActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                2000);
                    }
                } else {
                    startGallery("logo");
                }
            }
        });
        materialButton_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ShopActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                2000);
                    }
                } else {
                    startGallery("cover");
                }
            }
        });
    }

    private void startGallery(String control) {
        if (control == "logo") {
            Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            cameraIntent.setType("image/*");
            if (cameraIntent.resolveActivity(ShopActivity.this.getPackageManager()) != null) {
                startActivityForResult(cameraIntent, 1000);
            }
        } else if (control == "cover") {
            Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            cameraIntent.setType("image/*");
            if (cameraIntent.resolveActivity(ShopActivity.this.getPackageManager()) != null) {
                startActivityForResult(cameraIntent, 2000);
            }
        }
    }

    private void editLogo(final Uri returnUri) {

        String filePath = getRealPathFromURIPath(returnUri, ShopActivity.this);
        File file = new File(filePath);
        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), mFile);

        int id = LocalDataStore.getSHOPID(ShopActivity.this);
        String token = LocalDataStore.getToken(ShopActivity.this);

        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), id + "");
        final ProgressDialog progressDialog = ProgressDialog.show(ShopActivity.this, "",
                "Loading, Please wait...", true);
        ApiHelper.getService().editLogo(token, fileToUpload, requestBody).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    final AlertDialog alertDialog = new AlertDialog.Builder(ShopActivity.this).create();
                    alertDialog.setMessage("Change Successful!");
                    alertDialog.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            alertDialog.dismiss();
                        }
                    }, 1000);
                    Glide.with(ShopActivity.this)
                            .load(returnUri)
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(circleImageView_logo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
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

    private void editCover(final Uri returnUri) {

        String filePath = getRealPathFromURIPath(returnUri, ShopActivity.this);
        File file = new File(filePath);
        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), mFile);

        int id = LocalDataStore.getSHOPID(ShopActivity.this);
        String token = LocalDataStore.getToken(ShopActivity.this);

        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), id + "");
        final ProgressDialog progressDialog = ProgressDialog.show(ShopActivity.this, "",
                "Loading, Please wait...", true);
        ApiHelper.getService().editCover(token, fileToUpload, requestBody).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    final AlertDialog alertDialog = new AlertDialog.Builder(ShopActivity.this).create();
                    alertDialog.setMessage("Change Successful!");
                    alertDialog.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            alertDialog.dismiss();
                        }
                    }, 1000);
                    Glide.with(ShopActivity.this)
                            .load(returnUri)
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(imageView_cover);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void registerComponent() {
        textView_shopname = findViewById(R.id.text_shopname);
        materialButton_shopname = findViewById(R.id.button_shopname);
        materialButton_email = findViewById(R.id.button_shopemail);
        materialButton_phone = findViewById(R.id.button_shopphone);
        materialButton_address = findViewById(R.id.button_shopaddress);
        materialButton_description = findViewById(R.id.button_shopdes);
        circleImageView_logo = findViewById(R.id.img_shop);
        imageView_cover = findViewById(R.id.cover_shop);
        toolbar = findViewById(R.id.toolbar);
        materialButton_logo = findViewById(R.id.button_logo);
        materialButton_cover = findViewById(R.id.button_cover);
        setView();
    }

    private void editShopInfo(String control) {
        EditDialog.display(getSupportFragmentManager(), control, this);
    }

    private void setupToolbar() {
        if (toolbar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                toolbar.setTitle("Shop Info");
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        startActivity(new Intent(ShopActivity.this, MainActivity.class).putExtra("shop", true));
                        finish();
                    }
                });
            }
        }
    }

    private void setView() {
        try {
            if (jsonObject != null) {
                if (jsonObject.getString("shop_name") != "null") {
                    textView_shopname.setText(jsonObject.getString("shop_name"));
                    materialButton_shopname.setText("Shop name : " + jsonObject.getString("shop_name"));
                } else {
                    textView_shopname.setText("Shop name");
                    materialButton_shopname.setText("Click to edit shop name");
                }
                if (jsonObject.getString("email") != "null") {
                    materialButton_email.setText("Shop email : " + jsonObject.getString("email"));
                } else {
                    materialButton_email.setText("Click to edit shop email");
                }
                if (jsonObject.getString("phone") != "null") {
                    materialButton_phone.setText("Shop phone : " + jsonObject.getString("phone"));
                } else {
                    materialButton_phone.setText("Click to edit shop phone");
                }
                if (jsonObject.getString("address") != "null") {
                    materialButton_address.setText("Shop address : " + jsonObject.getString("address"));
                } else {
                    materialButton_address.setText("Click to edit shop address");
                }
                if (jsonObject.getString("detail") != "null") {
                    materialButton_description.setText("Shop description : " + jsonObject.getString("detail"));
                } else {
                    materialButton_description.setText("Click to edit shop description");
                }
                if (jsonObject.getString("pic") != "null") {
                    Glide.with(this)
                            .load(jsonObject.getString("pic"))
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(circleImageView_logo);
                }
                if (jsonObject.getString("shop_cover") != "null") {
                    Glide.with(this)
                            .load(jsonObject.getString("shop_cover"))
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(imageView_cover);
                }
            } else {
                textView_shopname.setText("Shop Name");
                materialButton_shopname.setText("Click Edit Shop name");
                materialButton_email.setText("Click Edit Shop email");
                materialButton_phone.setText("Click Edit Shop phone");
                materialButton_address.setText("Click Edit Shop address");
                materialButton_description.setText("Click Edit Shop Description");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterEdit(String control, String value) {
        if (control == "Shop name") {
            materialButton_shopname.setText("Shop name : " + value);
            textView_shopname.setText(value);
        } else if (control == "Shop email") {
            materialButton_email.setText("Shop email : " + value);
        } else if (control == "Shop phone") {
            materialButton_phone.setText("Shop phone : " + value);
        } else if (control == "Shop address") {
            materialButton_address.setText("Shop address : " + value);
        } else if (control == "Shop detail") {
            materialButton_description.setText("Shop description : " + value);
        }
    }
}
