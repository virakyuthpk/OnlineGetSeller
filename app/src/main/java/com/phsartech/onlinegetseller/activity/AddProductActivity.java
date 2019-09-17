package com.phsartech.onlinegetseller.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.adapter.ImageAdapter;
import com.phsartech.onlinegetseller.callback.CallBackFucntionOnButtonCategoryClick;
import com.phsartech.onlinegetseller.callback.CallBackFucntionOnButtonCategoryParentClick;
import com.phsartech.onlinegetseller.callback.CallBackFucntionOnButtonCategorySubClick;
import com.phsartech.onlinegetseller.callback.CallBackFunctionOnButtonBrandClick;
import com.phsartech.onlinegetseller.callback.CallBackFunctionOnButtonSupplierClick;
import com.phsartech.onlinegetseller.callback.CallBackFunctionOnButtonUnitClick;
import com.phsartech.onlinegetseller.dialog.BrandDialog;
import com.phsartech.onlinegetseller.dialog.CategoryDialog;
import com.phsartech.onlinegetseller.dialog.CategoryParentDialog;
import com.phsartech.onlinegetseller.dialog.CategorySubDialog;
import com.phsartech.onlinegetseller.dialog.SupplierDialog;
import com.phsartech.onlinegetseller.dialog.UnitDialog;
import com.phsartech.onlinegetseller.model.BrandModel;
import com.phsartech.onlinegetseller.model.CategoryModel;
import com.phsartech.onlinegetseller.model.SupplierModel;
import com.phsartech.onlinegetseller.model.UnitModel;
import com.phsartech.onlinegetseller.retrofit.ApiHelper;
import com.phsartech.onlinegetseller.util.LocalDataStore;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity implements
        CallBackFucntionOnButtonCategoryClick,
        CallBackFucntionOnButtonCategoryParentClick,
        CallBackFucntionOnButtonCategorySubClick,
        CallBackFunctionOnButtonBrandClick,
        CallBackFunctionOnButtonSupplierClick,
        CallBackFunctionOnButtonUnitClick {

    String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";
    private TextInputLayout textInputLayout_name,
            textInputLayout_qty,
            textInputLayout_price,
            textInputLayout_video,
            textInputLayout_description;
    private MaterialButton materialButton_post,
            materialButton_category,
            materialButton_brand,
            materialButton_photo,
            materialButton_supplier,
            materialButton_unit;
    private TextInputEditText textInputEditText_name,
            textInputEditText_price,
            textInputEditText_des,
            textInputEditText_video,
            textInputEditText_qty;
    private String TAG = "AddProductActivity";
    private ArrayList<String> mPaths_img;
    private CategoryModel.DataEntity item_category;
    private CategoryModel.DataEntity item_parent_category;
    private CategoryModel.DataEntity item_sub_category;
    private UnitModel.DataEntity item_unit;
    private SupplierModel.DataEntity item_supplier;
    private BrandModel.DataEntity item_brand;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        registerComponent();
        setupToolbar();

        materialButton_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryDialog.display(AddProductActivity.this, getSupportFragmentManager());
            }
        });
        materialButton_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrandDialog.display(getSupportFragmentManager(), AddProductActivity.this);
            }
        });
        materialButton_supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupplierDialog.display(getSupportFragmentManager(), AddProductActivity.this);
            }
        });
        materialButton_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnitDialog.display(getSupportFragmentManager(), AddProductActivity.this);
            }
        });
        materialButton_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(textInputEditText_name.getText()) ||
                        TextUtils.isEmpty(textInputEditText_price.getText()) ||
                        TextUtils.isEmpty(textInputEditText_des.getText()) ||
                        TextUtils.isEmpty(textInputEditText_qty.getText()) ||
                        TextUtils.isEmpty(textInputEditText_video.getText()) ||
                        mPaths_img.isEmpty() ||
                        item_category == null ||
                        item_parent_category == null ||
                        item_sub_category == null ||
                        item_brand == null ||
                        item_supplier == null ||
                        item_unit == null) {
                    AlertDialog alertDialog = new AlertDialog.Builder(AddProductActivity.this).create();
                    alertDialog.setTitle("Sorry");
                    alertDialog.setMessage("Please insert all input!");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                    if (TextUtils.isEmpty(textInputEditText_name.getText())) {
                        textInputLayout_name.setError("Please input product name!");
                    }
                    if (TextUtils.isEmpty(textInputEditText_price.getText())) {
                        textInputLayout_name.setError("Please input product price!");
                    }
                    if (TextUtils.isEmpty(textInputEditText_des.getText())) {
                        textInputLayout_name.setError("Please input product description!");
                    }
                    if (TextUtils.isEmpty(textInputEditText_qty.getText())) {
                        textInputLayout_name.setError("Please input product qty!");
                    }
                    if (TextUtils.isEmpty(textInputEditText_video.getText())) {
                        textInputLayout_name.setError("Please input product video!");
                    }
                    if (mPaths_img == null) {
                        materialButton_photo.setError("");
                    }
                    if (item_category == null ||
                            item_parent_category == null ||
                            item_sub_category == null) {
                        materialButton_category.setError("");
                    }
                    if (item_brand == null) {
                        materialButton_brand.setError("");
                    }
                    if (item_unit == null) {
                        materialButton_unit.setError("");
                    }
                } else {
                    if (textInputEditText_video.getText().toString().matches(pattern)) {
                        addProduct(
                                textInputEditText_name.getText().toString(),
                                Integer.parseInt(textInputEditText_qty.getText().toString()),
                                Integer.parseInt(textInputEditText_price.getText().toString()),
                                textInputEditText_video.getText().toString(),
                                textInputEditText_des.getText().toString(),
                                mPaths_img,
                                item_category.getId(),
                                item_parent_category.getId(),
                                item_sub_category.getId(),
                                item_brand.getId(),
                                item_supplier.getId(),
                                item_unit.getId()
                        );
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(AddProductActivity.this).create();
                        alertDialog.setTitle("Sorry");
                        alertDialog.setMessage("Video Url is not valid!");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                        textInputLayout_video.setError("Not valid url!");
                    }
                }
            }
        });
        materialButton_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImagePicker.Builder(AddProductActivity.this)
                        .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                        .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                        .directory(ImagePicker.Directory.DEFAULT)
                        .extension(ImagePicker.Extension.PNG)
                        .extension(ImagePicker.Extension.JPG)
                        .scale(600, 600)
                        .allowOnlineImages(true)
                        .allowMultipleImages(true)
                        .enableDebuggingMode(true)
                        .build();
            }
        });
    }

    private void addProduct(String name_en,
                            int qty,
                            int sell_price,
                            String video,
                            String des_en,
                            ArrayList<String> image,
                            int category_id,
                            int parent_category,
                            int sub_id,
                            int braind_id,
                            int supplier_id,
                            int unit_id) {


        RequestBody requestBody_userId = RequestBody.create(MediaType.parse("text/plain"), LocalDataStore.getID(AddProductActivity.this) + "");
        RequestBody requestBody_name = RequestBody.create(MediaType.parse("text/plain"), name_en + "");
        RequestBody requestBody_qty = RequestBody.create(MediaType.parse("text/plain"), qty + "");
        RequestBody requestBody_sell_price = RequestBody.create(MediaType.parse("text/plain"), sell_price + "");
        RequestBody requestBody_video = RequestBody.create(MediaType.parse("text/plain"), video + "");
        RequestBody requestBody_desEn = RequestBody.create(MediaType.parse("text/plain"), des_en + "");
        RequestBody requestBody_categoryId = RequestBody.create(MediaType.parse("text/plain"), category_id + "");
        RequestBody requestBody_parentCategory = RequestBody.create(MediaType.parse("text/plain"), parent_category + "");
        RequestBody requestBody_subId = RequestBody.create(MediaType.parse("text/plain"), sub_id + "");
        RequestBody requestBody_brandId = RequestBody.create(MediaType.parse("text/plain"), braind_id + "");
        RequestBody requestBody_supplierId = RequestBody.create(MediaType.parse("text/plain"), supplier_id + "");
        RequestBody requestBody_unitId = RequestBody.create(MediaType.parse("text/plain"), unit_id + "");

        MultipartBody.Part[] parts = new MultipartBody.Part[image.size()];

        for (int i = 0; i < image.size(); i++) {

            String filePath = getRealPathFromURIPath(Uri.parse(image.get(i)), AddProductActivity.this);
            File file = new File(filePath);
            RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image[]", file.getName(), mFile);

            parts[i] = fileToUpload;
        }
        final ProgressDialog progressDialog = ProgressDialog.show(AddProductActivity.this, "",
                "Loading, Please wait...", true);

        ApiHelper.getService().addProduct(
                LocalDataStore.getToken(AddProductActivity.this),
                requestBody_userId, requestBody_name, requestBody_qty, requestBody_sell_price,
                requestBody_video, requestBody_desEn, requestBody_categoryId, requestBody_parentCategory,
                requestBody_subId, requestBody_brandId, requestBody_supplierId, requestBody_unitId, parts
        ).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    Log.e(TAG, "onResponse: " + jsonObject.getString("success"));
                    AlertDialog alertDialog = new AlertDialog.Builder(AddProductActivity.this).create();
                    alertDialog.setTitle("Product Added!");
                    alertDialog.setMessage("Want to add more product!");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivity(new Intent(AddProductActivity.this, AddProductActivity.class));
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivity(new Intent(AddProductActivity.this, MainActivity.class));
                        }
                    });
                    alertDialog.show();
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

    private void registerComponent() {
        materialButton_supplier = findViewById(R.id.button_supplier);
        materialButton_unit = findViewById(R.id.button_unit);
        materialButton_post = findViewById(R.id.button_post);
        materialButton_category = findViewById(R.id.button_category);
        materialButton_brand = findViewById(R.id.button_brand);
        materialButton_photo = findViewById(R.id.button_photo);
        materialButton_post = findViewById(R.id.button_post);

        textInputLayout_name = findViewById(R.id.text_layout_name);
        textInputLayout_price = findViewById(R.id.text_layout_price);
        textInputLayout_description = findViewById(R.id.text_layout_des);
        textInputLayout_qty = findViewById(R.id.text_layout_qty);
        textInputLayout_video = findViewById(R.id.text_layout_video);

        textInputEditText_name = findViewById(R.id.text_p_name);
        textInputEditText_video = findViewById(R.id.text_p_video);
        textInputEditText_des = findViewById(R.id.text_p_des);
        textInputEditText_price = findViewById(R.id.text_p_price);
        textInputEditText_qty = findViewById(R.id.text_p_qty);

        toolbar = findViewById(R.id.toolbar);
    }

    private void setupToolbar() {
        if (toolbar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                toolbar.setTitle("Add Product");
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        startActivity(new Intent(AddProductActivity.this, MainActivity.class));
                        finish();
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddProductActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            mPaths_img = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
            setImage();
        }
    }

    private void setImage() {
        RecyclerView recyclerView = findViewById(R.id.recycler_photo);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        ImageAdapter imgAdapter = new ImageAdapter(this, mPaths_img);
        recyclerView.setAdapter(imgAdapter);
        imgAdapter.notifyDataSetChanged();
    }

    @Override
    public void onButtonCategoryClick(CategoryModel.DataEntity item) {
        CategoryParentDialog.display(AddProductActivity.this, getSupportFragmentManager(), item);
    }

    @Override
    public void onButtonBrandClick(BrandModel.DataEntity item) {
        materialButton_brand.setText(item.getNameEn());
        materialButton_brand.setIcon(null);
        this.item_brand = item;
    }

    @Override
    public void onButtonSupplierClick(SupplierModel.DataEntity item) {
        materialButton_supplier.setText(item.getNameEn());
        materialButton_supplier.setIcon(null);
        this.item_supplier = item;
    }

    @Override
    public void onButtonUnitClick(UnitModel.DataEntity item) {
        materialButton_unit.setText(item.getNameEn());
        materialButton_unit.setIcon(null);
        this.item_unit = item;
    }

    @Override
    public void onButtonCategoryParentClick(CategoryModel.DataEntity item, CategoryModel.DataEntity item_parent) {
        CategorySubDialog.display(AddProductActivity.this, getSupportFragmentManager(), item, item_parent);
    }

    @Override
    public void onButtonCategorySubClick(CategoryModel.DataEntity item, CategoryModel.DataEntity item_parent, CategoryModel.DataEntity item_sub) {
        materialButton_category.setText(
                item.getTitleEn()
                        + " > " +
                        item_parent.getTitleEn()
                        + " > " +
                        item_sub.getTitleEn()
        );
        materialButton_category.setIcon(null);
        this.item_category = item;
        this.item_parent_category = item_parent;
        this.item_sub_category = item_sub;
    }
}