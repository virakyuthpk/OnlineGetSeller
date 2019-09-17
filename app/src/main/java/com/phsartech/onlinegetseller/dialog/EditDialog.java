package com.phsartech.onlinegetseller.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.callback.CallBackFucntionAfterEdit;
import com.phsartech.onlinegetseller.retrofit.ApiHelper;
import com.phsartech.onlinegetseller.util.LocalDataStore;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditDialog extends DialogFragment {

    private static String TAG = "EditDialog";
    private final CallBackFucntionAfterEdit callBackFucntionAfterEdit;
    private int productId;
    private TextInputLayout textInputLayout_edit;
    private TextInputEditText textInputEditText_edit;
    private MaterialButton materialButton_done;
    private String control;
    private Toolbar toolbar;
    String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";

    public EditDialog(String control, CallBackFucntionAfterEdit callBackFucntionAfterEdit) {
        this.control = control;
        this.callBackFucntionAfterEdit = callBackFucntionAfterEdit;
    }

    public EditDialog(String control, CallBackFucntionAfterEdit callBackFucntionAfterEdit, int productId) {
        this.productId = productId;
        this.control = control;
        this.callBackFucntionAfterEdit = callBackFucntionAfterEdit;
    }


    public static EditDialog display(FragmentManager fragmentManager, String control, CallBackFucntionAfterEdit callBackFucntionAfterEdit) {
        EditDialog exampleDialog = new EditDialog(control, callBackFucntionAfterEdit);
        exampleDialog.show(fragmentManager, TAG);
        return exampleDialog;
    }

    public static EditDialog display(FragmentManager fragmentManager, String control, CallBackFucntionAfterEdit callBackFucntionAfterEdit, int productId) {
        EditDialog exampleDialog = new EditDialog(control, callBackFucntionAfterEdit, productId);
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
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        registerComponent(view);
        setupToolbar();
        if (control == "Username") {
            textInputLayout_edit.setHint("New Username");
            textInputEditText_edit.setSingleLine(true);
        } else if (control == "Email") {
            textInputLayout_edit.setHint("New Email");
            textInputEditText_edit.setSingleLine(true);
        } else if (control == "Phone") {
            textInputLayout_edit.setHint("New Phone");
            textInputEditText_edit.setSingleLine(true);
            textInputEditText_edit.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (control == "Address") {
            textInputLayout_edit.setHint("New Address");
            textInputEditText_edit.setSingleLine(true);
        } else if (control == "Description") {
            textInputLayout_edit.setHint("New Description");
            textInputLayout_edit.setCounterEnabled(true);
        } else if (control == "Shop name") {
            textInputLayout_edit.setHint("New Shop name");
            textInputEditText_edit.setSingleLine(true);
        } else if (control == "Shop email") {
            textInputLayout_edit.setHint("New Shop email");
            textInputEditText_edit.setSingleLine(true);
        } else if (control == "Shop phone") {
            textInputLayout_edit.setHint("New Shop phone");
            textInputEditText_edit.setSingleLine(true);
            textInputEditText_edit.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (control == "Shop address") {
            textInputLayout_edit.setHint("New Shop address");
            textInputEditText_edit.setSingleLine(true);
        } else if (control == "Shop detail") {
            textInputLayout_edit.setHint("New Shop detail");
            textInputLayout_edit.setCounterEnabled(true);
        } else if (control == "Product name") {
            textInputLayout_edit.setHint("New Product name");
            textInputEditText_edit.setSingleLine(true);
        } else if (control == "Product qty") {
            textInputLayout_edit.setHint("New Product Qty");
            textInputEditText_edit.setSingleLine(true);
            textInputEditText_edit.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        } else if (control == "Product price") {
            textInputLayout_edit.setHint("New Product sell price");
            textInputEditText_edit.setSingleLine(true);
            textInputEditText_edit.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        } else if (control == "Product video") {
            textInputLayout_edit.setHint("New Product Video Url");
            textInputEditText_edit.setSingleLine(true);
            textInputEditText_edit.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        } else if (control == "Product description") {
            textInputLayout_edit.setHint("New Product description");
            textInputLayout_edit.setCounterEnabled(true);
        }
        return view;
    }

    private void setupToolbar() {
        if (toolbar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                toolbar.setTitle("Edit " + control);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        materialButton_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (control == "Username") {
                    if (TextUtils.isEmpty(textInputEditText_edit.getText())) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Sorry");
                        alertDialog.setMessage("Please input new username!");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {
                        editUsername(textInputEditText_edit.getText().toString(), LocalDataStore.getID(getActivity()), LocalDataStore.getToken(getActivity()));
                    }
                } else if (control == "Email") {
                    if (TextUtils.isEmpty(textInputEditText_edit.getText())) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Sorry");
                        alertDialog.setMessage("Please input new email!");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {
                        editEmail(textInputEditText_edit.getText().toString(), LocalDataStore.getID(getActivity()), LocalDataStore.getToken(getActivity()));
                    }
                } else if (control == "Phone") {
                    if (textInputEditText_edit.getText().length() < 10) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Sorry");
                        alertDialog.setMessage("Please input new phone number!");
                        textInputLayout_edit.setError("please input phone");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {
                        editPhone(textInputEditText_edit.getText().toString(), LocalDataStore.getID(getActivity()), LocalDataStore.getToken(getActivity()));
                    }
                } else if (control == "Address") {
                    if (TextUtils.isEmpty(textInputEditText_edit.getText())) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Sorry");
                        alertDialog.setMessage("Please input new address!");
                        textInputLayout_edit.setError("please input address");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {
                        editAddress(textInputEditText_edit.getText().toString(), LocalDataStore.getID(getActivity()), LocalDataStore.getToken(getActivity()));
                    }
                } else if (control == "Description") {
                    if (TextUtils.isEmpty(textInputEditText_edit.getText())) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Sorry");
                        alertDialog.setMessage("Please input new description!");
                        textInputLayout_edit.setError("please input description");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {
                        editDes(textInputEditText_edit.getText().toString(), LocalDataStore.getID(getActivity()), LocalDataStore.getToken(getActivity()));
                    }
                } else if (control == "Shop name") {
                    if (TextUtils.isEmpty(textInputEditText_edit.getText())) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Sorry");
                        alertDialog.setMessage("Please input new shop name!");
                        textInputLayout_edit.setError("please input name");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {
                        editShopName(textInputEditText_edit.getText().toString(), LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getToken(getActivity()));
                    }
                } else if (control == "Shop email") {
                    if (TextUtils.isEmpty(textInputEditText_edit.getText())) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Sorry");
                        alertDialog.setMessage("Please input new shop email!");
                        textInputLayout_edit.setError("please input email");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {
                        editShopEmail(textInputEditText_edit.getText().toString(), LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getToken(getActivity()));
                    }
                } else if (control == "Shop phone") {
                    if (textInputEditText_edit.getText().length() < 10) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Sorry");
                        alertDialog.setMessage("Please input new shop phone!");
                        textInputLayout_edit.setError("please input phone");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {
                        editShopPhone(textInputEditText_edit.getText().toString(), LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getToken(getActivity()));
                    }
                } else if (control == "Shop address") {
                    if (TextUtils.isEmpty(textInputEditText_edit.getText())) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Sorry");
                        alertDialog.setMessage("Please input new shop address!");
                        textInputLayout_edit.setError("please input address");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {
                        editShopAddress(textInputEditText_edit.getText().toString(), LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getToken(getActivity()));
                    }
                } else if (control == "Shop detail") {
                    if (TextUtils.isEmpty(textInputEditText_edit.getText())) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Sorry");
                        alertDialog.setMessage("Please input new shop description!");
                        textInputLayout_edit.setError("please input description");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {
                        editShopDetail(textInputEditText_edit.getText().toString(), LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getToken(getActivity()));
                    }
                } else if (control == "Product name") {
                    if (TextUtils.isEmpty(textInputEditText_edit.getText())) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Sorry");
                        alertDialog.setMessage("Please input new product name!");
                        textInputLayout_edit.setError("please input name");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {
                        editProductName(textInputEditText_edit.getText().toString(), productId, LocalDataStore.getToken(getActivity()));
                    }
                } else if (control == "Product qty") {
                    if (TextUtils.isEmpty(textInputEditText_edit.getText())) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Sorry");
                        alertDialog.setMessage("Please input new product qty!");
                        textInputLayout_edit.setError("please input qty");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {
                        editProductQty(textInputEditText_edit.getText().toString(), productId, LocalDataStore.getToken(getActivity()));
                    }
                } else if (control == "Product price") {
                    if (TextUtils.isEmpty(textInputEditText_edit.getText())) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Sorry");
                        alertDialog.setMessage("Please input new product price!");
                        textInputLayout_edit.setError("please input price");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {
                        editProductPrice(textInputEditText_edit.getText().toString(), productId, LocalDataStore.getToken(getActivity()));
                    }
                } else if (control == "Product video") {
                    if (TextUtils.isEmpty(textInputEditText_edit.getText())) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Sorry");
                        alertDialog.setMessage("Please input new product video!");
                        textInputLayout_edit.setError("please input video url");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {
                        if (textInputEditText_edit.getText().toString().matches(pattern)) {
                            editProductVideo(textInputEditText_edit.getText().toString(), productId, LocalDataStore.getToken(getActivity()));
                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                            alertDialog.setTitle("Sorry");
                            alertDialog.setMessage("Video Url is not valid!");
                            textInputLayout_edit.setError("url is not valid");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alertDialog.show();
                        }
                    }
                } else if (control == "Product description") {
                    if (TextUtils.isEmpty(textInputEditText_edit.getText())) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Sorry");
                        alertDialog.setMessage("Please input new product description!");
                        textInputLayout_edit.setError("please input description");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {
                        editProductDescription(textInputEditText_edit.getText().toString(), productId, LocalDataStore.getToken(getActivity()));
                    }
                }
            }
        });
    }

    private void editProductPrice(final String price, int productId, String token) {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "",
                "Loading, Please wait...", true);
        ApiHelper.getService().editProductSellPrice(token, productId, Integer.parseInt(price)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage("Change Successful!");
                        alertDialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                callBackFucntionAfterEdit.afterEdit(control, price);
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 1000);
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

    private void editProductName(final String name, int productId, String token) {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "",
                "Loading, Please wait...", true);
        ApiHelper.getService().editProductName(token, productId, name).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage("Change Successful!");
                        alertDialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                callBackFucntionAfterEdit.afterEdit(control, name);
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 1000);
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

    private void editProductQty(final String qty, int productId, String token) {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "",
                "Loading, Please wait...", true);
        ApiHelper.getService().editProductQty(token, productId, Integer.parseInt(qty)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage("Change Successful!");
                        alertDialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                callBackFucntionAfterEdit.afterEdit(control, qty);
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 1000);
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

    private void editProductVideo(final String video, int productId, String token) {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "",
                "Loading, Please wait...", true);
        ApiHelper.getService().editProductVideo(token, productId, video).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage("Change Successful!");
                        alertDialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                callBackFucntionAfterEdit.afterEdit(control, video);
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 1000);
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

    private void editProductDescription(final String des, int productId, String token) {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "",
                "Loading, Please wait...", true);
        ApiHelper.getService().editProductDes(token, productId, des).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage("Change Successful!");
                        alertDialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                callBackFucntionAfterEdit.afterEdit(control, des);
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 1000);
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

    private void editShopName(final String name, int id, String token) {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "",
                "Loading, Please wait...", true);
        ApiHelper.getService().editShopName(token, id, name).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage("Change Successful!");
                        alertDialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                callBackFucntionAfterEdit.afterEdit(control, name);
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 1000);
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

    private void editShopEmail(final String email, int id, String token) {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "",
                "Loading, Please wait...", true);
        ApiHelper.getService().editShopEmail(token, id, email).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage("Change Successful!");
                        alertDialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                callBackFucntionAfterEdit.afterEdit(control, email);
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 1000);
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

    private void editShopPhone(final String phone, int id, String token) {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "",
                "Loading, Please wait...", true);
        ApiHelper.getService().editShopPhone(token, id, phone).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage("Change Successful!");
                        alertDialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                callBackFucntionAfterEdit.afterEdit(control, phone);
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 1000);
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

    private void editShopAddress(final String address, int id, String token) {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "",
                "Loading, Please wait...", true);
        ApiHelper.getService().editShopAddress(token, id, address).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage("Change Successful!");
                        alertDialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                callBackFucntionAfterEdit.afterEdit(control, address);
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 1000);
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

    private void editShopDetail(final String detail, int id, String token) {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "",
                "Loading, Please wait...", true);
        ApiHelper.getService().editShopDetail(token, id, detail).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage("Change Successful!");
                        alertDialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                callBackFucntionAfterEdit.afterEdit(control, detail);
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 1000);
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

    private void editDes(final String bio, int id, String token) {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "",
                "Loading, Please wait...", true);
        ApiHelper.getService().editBio(token, id, bio).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage("Change Successful!");
                        alertDialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                callBackFucntionAfterEdit.afterEdit(control, bio);
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 1000);
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

    private void editAddress(final String address, int id, String token) {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "",
                "Loading, Please wait...", true);
        ApiHelper.getService().editAddress(token, id, address).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage("Change Successful!");
                        alertDialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                callBackFucntionAfterEdit.afterEdit(control, address);
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 1000);
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

    private void editPhone(final String phone, int id, String token) {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "",
                "Loading, Please wait...", true);
        ApiHelper.getService().editPhone(token, id, phone).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage("Change Successful!");
                        alertDialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                callBackFucntionAfterEdit.afterEdit(control, phone);
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 1000);
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

    private void editEmail(final String email, int id, String token) {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "",
                "Loading, Please wait...", true);
        ApiHelper.getService().editEmail(token, id, email).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage("Change Successful!");
                        alertDialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                callBackFucntionAfterEdit.afterEdit(control, email);
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 1000);
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

    private void editUsername(final String username, int id, String token) {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "",
                "Loading, Please wait...", true);
        ApiHelper.getService().editUsername(token, id, username).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage("Change Successful!");
                        alertDialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                callBackFucntionAfterEdit.afterEdit(control, username);
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 1000);
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
        textInputLayout_edit = view.findViewById(R.id.layout_edit);
        textInputEditText_edit = view.findViewById(R.id.text_edit);
        materialButton_done = view.findViewById(R.id.button_done);
        toolbar = view.findViewById(R.id.toolbar);
    }
}

