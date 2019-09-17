package com.phsartech.onlinegetseller.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;
import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.activity.MainActivity;
import com.phsartech.onlinegetseller.adapter.AdapterSlideView;
import com.phsartech.onlinegetseller.callback.CallBackFucntionAfterEdit;
import com.phsartech.onlinegetseller.callback.CallBackFucntionOnButtonCategoryClick;
import com.phsartech.onlinegetseller.callback.CallBackFucntionOnButtonCategoryParentClick;
import com.phsartech.onlinegetseller.callback.CallBackFucntionOnButtonCategorySubClick;
import com.phsartech.onlinegetseller.callback.CallBackFunctionOnButtonBrandClick;
import com.phsartech.onlinegetseller.callback.CallBackFunctionOnButtonSupplierClick;
import com.phsartech.onlinegetseller.callback.CallBackFunctionOnButtonUnitClick;
import com.phsartech.onlinegetseller.model.BrandModel;
import com.phsartech.onlinegetseller.model.CategoryModel;
import com.phsartech.onlinegetseller.model.GalleryModel;
import com.phsartech.onlinegetseller.model.ProductModelOnSale;
import com.phsartech.onlinegetseller.model.SupplierModel;
import com.phsartech.onlinegetseller.model.UnitModel;
import com.phsartech.onlinegetseller.retrofit.ApiHelper;
import com.phsartech.onlinegetseller.util.LocalDataStore;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDialog extends DialogFragment implements
        CallBackFucntionAfterEdit,
        CallBackFucntionOnButtonCategoryClick,
        CallBackFucntionOnButtonCategoryParentClick,
        CallBackFucntionOnButtonCategorySubClick,
        CallBackFunctionOnButtonBrandClick,
        CallBackFunctionOnButtonSupplierClick,
        CallBackFunctionOnButtonUnitClick {

    private static String TAG = "ProductDialog";
    private final ProductModelOnSale.Data item;
    private Toolbar toolbar;
    private MaterialButton materialButton_productName,
            materialButton_productQty, materialButton_productPrice,
            materialButton_ProductCategory, materialButton_productBrand,
            materialButton_productSupplier, materialButton_productUnit,
            materialButton_productVideo, materialButton_productDescription,
            materialButton_delete;
    private List<GalleryModel.DataEntity> galleryList;
    private SliderView sliderView;
    private CallBackFucntionOnButtonCategoryClick callBackFucntionOnButtonCategoryClick = this;
    private CallBackFunctionOnButtonBrandClick callBackFunctionOnButtonBrandClick = this;
    private CallBackFunctionOnButtonSupplierClick callBackFunctionOnButtonSupplierClick = this;
    private CallBackFunctionOnButtonUnitClick callBackFunctionOnButtonUnitClick = this;

    public ProductDialog(ProductModelOnSale.Data item) {
        this.item = item;
    }

    public static ProductDialog display(FragmentManager fragmentManager, ProductModelOnSale.Data item) {
        ProductDialog exampleDialog = new ProductDialog(item);
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
                toolbar.setTitle("Product");
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_product, container, false);
        registerComponent(view);
        setupToolbar();
        setView();
        return view;
    }

    private void getDataCategory() {
        ApiHelper.getService().showCategory(LocalDataStore.getToken(getActivity()), item.getCategory_id(),
                item.getParent_category(), item.getSub_id()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    materialButton_ProductCategory.setText(jsonObject.getString("category") + " > " +
                            jsonObject.getString("parent") + " > " +
                            jsonObject.getString("sub"));
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

    private void setView() {
        materialButton_productName.setText(item.getName_en() + "");
        materialButton_productQty.setText(item.getQty() + "");
        materialButton_productPrice.setText(item.getSell_price() + "");
        materialButton_productVideo.setText(item.getVideo() + "");
        materialButton_productDescription.setText(item.getDes_en() + "");
        getDataCategory();
        getDataBrand();
        getDataSupplier();
        getUnit();
        getDataGallery();
    }

    private void getDataGallery() {
        ApiHelper.getService().getGallery(LocalDataStore.getToken(getActivity()), item.getId()).enqueue(new Callback<GalleryModel>() {
            @Override
            public void onResponse(Call<GalleryModel> call, Response<GalleryModel> response) {
                galleryList = response.body().getGallery();
                setSlide();
            }

            @Override
            public void onFailure(Call<GalleryModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void setSlide() {
        AdapterSlideView adapter = new AdapterSlideView(galleryList, getContext());
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setScrollTimeInSec(3); //set scroll delay in seconds :
        sliderView.startAutoCycle();
    }

    private void getUnit() {
        ApiHelper.getService().showUnit(LocalDataStore.getToken(getActivity()), item.getUnit_id()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    materialButton_productUnit.setText(jsonObject.getString("unit"));
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

    private void getDataSupplier() {
        ApiHelper.getService().showSupplier(LocalDataStore.getToken(getActivity()), item.getSupplier_id()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    materialButton_productSupplier.setText(jsonObject.getString("supplier"));
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

    private void getDataBrand() {
        ApiHelper.getService().showBrand(LocalDataStore.getToken(getActivity()), item.getBraind_id()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    materialButton_productBrand.setText(jsonObject.getString("brand") + "");
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        materialButton_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setMessage("Do you want to delete this product?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteProduct(item.getId());
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
        materialButton_productName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfo("Product name");
            }
        });
        materialButton_productQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfo("Product qty");
            }
        });
        materialButton_productPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfo("Product price");
            }
        });
        materialButton_ProductCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryDialog.display(callBackFucntionOnButtonCategoryClick, getFragmentManager());
            }
        });
        materialButton_productBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrandDialog.display(getFragmentManager(), callBackFunctionOnButtonBrandClick);
            }
        });
        materialButton_productSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupplierDialog.display(getFragmentManager(), callBackFunctionOnButtonSupplierClick);
            }
        });
        materialButton_productUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnitDialog.display(getFragmentManager(), callBackFunctionOnButtonUnitClick);
            }
        });
        materialButton_productVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfo("Product video");
            }
        });
        materialButton_productDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfo("Product description");
            }
        });
    }

    private void editInfo(String value) {
        EditDialog.display(getFragmentManager(), value, this, item.getId());
    }

    private void deleteProduct(int id) {
        ApiHelper.getService().deleteProduct(LocalDataStore.getToken(getActivity()), id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("success") == "true") {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage("Delete Successful!");
                        alertDialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 1000);
                        dismiss();
                        getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
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

    private void registerComponent(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        materialButton_productName = view.findViewById(R.id.button_product_name);
        materialButton_productQty = view.findViewById(R.id.button_product_qty);
        materialButton_productPrice = view.findViewById(R.id.button_product_price);
        materialButton_ProductCategory = view.findViewById(R.id.button_product_category);
        materialButton_productBrand = view.findViewById(R.id.button_product_brand);
        materialButton_productSupplier = view.findViewById(R.id.button_product_supplier);
        materialButton_productUnit = view.findViewById(R.id.button_product_unit);
        materialButton_productVideo = view.findViewById(R.id.button_product_video);
        materialButton_productDescription = view.findViewById(R.id.button_product_des);
        sliderView = view.findViewById(R.id.imageSlider);
        materialButton_delete = view.findViewById(R.id.button_delete);
    }

    @Override
    public void afterEdit(String control, String value) {
        if (control == "Product name") {
            materialButton_productName.setText(value);
        } else if (control == "Product qty") {
            materialButton_productQty.setText(value);
        } else if (control == "Product price") {
            materialButton_productPrice.setText(value);
        } else if (control == "Product video") {
            materialButton_productVideo.setText(value);
        } else if (control == "Product description") {
            materialButton_productDescription.setText(value);
        }
    }


    @Override
    public void onButtonCategoryClick(CategoryModel.DataEntity item) {
        CategoryParentDialog.display(this, getFragmentManager(), item);
    }

    @Override
    public void onButtonBrandClick(BrandModel.DataEntity item) {
        editBrand(item);
    }

    private void editBrand(final BrandModel.DataEntity item) {
        ApiHelper.getService().editProductBrand(LocalDataStore.getToken(getActivity()), this.item.getId(), item.getId()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage("Change Successful!");
                        alertDialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 1000);
                        materialButton_productBrand.setText(item.getNameEn());
                        materialButton_productBrand.setIcon(null);
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

    @Override
    public void onButtonSupplierClick(SupplierModel.DataEntity item) {
        editSupplier(item);
    }

    private void editSupplier(final SupplierModel.DataEntity item) {
        ApiHelper.getService().editProductSupplier(LocalDataStore.getToken(getActivity()), this.item.getId(), item.getId()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage("Change Successful!");
                        alertDialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 1000);
                        materialButton_productSupplier.setText(item.getNameEn());
                        materialButton_productSupplier.setIcon(null);
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

    @Override
    public void onButtonUnitClick(UnitModel.DataEntity item) {
        editUnit(item);
    }

    private void editUnit(final UnitModel.DataEntity item) {
        ApiHelper.getService().editProductUnit(LocalDataStore.getToken(getActivity()), this.item.getId(), item.getId()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage("Change Successful!");
                        alertDialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 1000);
                        materialButton_productUnit.setText(item.getNameEn());
                        materialButton_productUnit.setIcon(null);
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

    @Override
    public void onButtonCategoryParentClick(CategoryModel.DataEntity item, CategoryModel.DataEntity item_parent) {
        CategorySubDialog.display(this, getFragmentManager(), item, item_parent);
    }

    @Override
    public void onButtonCategorySubClick(CategoryModel.DataEntity item, CategoryModel.DataEntity item_parent, CategoryModel.DataEntity item_sub) {
        editCategory(item, item_parent, item_sub);
    }

    private void editCategory(final CategoryModel.DataEntity item, final CategoryModel.DataEntity item_parent, final CategoryModel.DataEntity item_sub) {
        ApiHelper.getService().editProductCategory(LocalDataStore.getToken(getActivity()),
                this.item.getId(), item.getId(), item_parent.getId(), item_sub.getId()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage("Change Successful!");
                        alertDialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 1000);
                        materialButton_ProductCategory.setText(
                                item.getTitleEn()
                                        + " > " +
                                        item_parent.getTitleEn()
                                        + " > " +
                                        item_sub.getTitleEn()
                        );
                        materialButton_ProductCategory.setIcon(null);
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
}

