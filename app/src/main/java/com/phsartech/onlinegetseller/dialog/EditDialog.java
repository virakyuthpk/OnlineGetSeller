package com.phsartech.onlinegetseller.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.callback.CallBackFucntionAfterEdit;
import com.phsartech.onlinegetseller.fragment.SettingFragment;
import com.phsartech.onlinegetseller.retrofit.ApiHelper;
import com.phsartech.onlinegetseller.util.LocalDataStore;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditDialog extends DialogFragment {

    private static String TAG = "AboutDialog";
    private final CallBackFucntionAfterEdit callBackFucntionAfterEdit;
    private TextInputLayout textInputLayout_edit;
    private TextInputEditText textInputEditText_edit;
    private MaterialButton materialButton_done;
    private String control;

    public EditDialog(String control, CallBackFucntionAfterEdit callBackFucntionAfterEdit) {
        this.control = control;
        this.callBackFucntionAfterEdit = callBackFucntionAfterEdit;
    }


    public static EditDialog display(FragmentManager fragmentManager, String control, CallBackFucntionAfterEdit callBackFucntionAfterEdit) {
        EditDialog exampleDialog = new EditDialog(control, callBackFucntionAfterEdit);
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
        if (control == "username") {
            textInputLayout_edit.setHint("New Username");
        } else if (control == "email") {
            textInputLayout_edit.setHint("New Email");
        } else if (control == "phone") {
            textInputLayout_edit.setHint("New Phone");
        } else if (control == "address") {
            textInputLayout_edit.setHint("New Address");
        } else if (control == "des") {
            textInputLayout_edit.setHint("New Description");
            textInputLayout_edit.setCounterEnabled(true);
        } else if (control == "shop_name") {
            textInputLayout_edit.setHint("New Shop name");
        } else if (control == "shop_email") {
            textInputLayout_edit.setHint("New Shop email");
        } else if (control == "shop_phone") {
            textInputLayout_edit.setHint("New Shop phone");
        } else if (control == "shop_address") {
            textInputLayout_edit.setHint("New Shop address");
        } else if (control == "shop_detail") {
            textInputLayout_edit.setHint("New Shop detail");
            textInputLayout_edit.setCounterEnabled(true);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        materialButton_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (control == "username") {
                    editUsername(textInputEditText_edit.getText().toString(), LocalDataStore.getID(getActivity()), LocalDataStore.getToken(getActivity()));
                } else if (control == "email") {
                    editEmail(textInputEditText_edit.getText().toString(), LocalDataStore.getID(getActivity()), LocalDataStore.getToken(getActivity()));
                } else if (control == "phone") {
                    editPhone(textInputEditText_edit.getText().toString(), LocalDataStore.getID(getActivity()), LocalDataStore.getToken(getActivity()));
                } else if (control == "address") {
                    editAddress(textInputEditText_edit.getText().toString(), LocalDataStore.getID(getActivity()), LocalDataStore.getToken(getActivity()));
                } else if (control == "des") {
                    editDes(textInputEditText_edit.getText().toString(), LocalDataStore.getID(getActivity()), LocalDataStore.getToken(getActivity()));
                } else if (control == "shop_name") {
                    editShopName(textInputEditText_edit.getText().toString(), LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getToken(getActivity()));
                } else if (control == "shop_email") {
                    editShopEmail(textInputEditText_edit.getText().toString(), LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getToken(getActivity()));
                } else if (control == "shop_phone") {
                    editShopPhone(textInputEditText_edit.getText().toString(), LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getToken(getActivity()));
                } else if (control == "shop_address") {
                    editShopAddress(textInputEditText_edit.getText().toString(), LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getToken(getActivity()));
                } else if (control == "shop_detail") {
                    editShopDetail(textInputEditText_edit.getText().toString(), LocalDataStore.getSHOPID(getActivity()), LocalDataStore.getToken(getActivity()));
                }
            }
        });
    }

    private void editShopName(final String name, int id, String token) {
        ApiHelper.getService().editShopName(token, id, name).enqueue(new Callback<JsonObject>() {
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
                                callBackFucntionAfterEdit.afterEdit(control, name);
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 2000);
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
        ApiHelper.getService().editShopEmail(token, id, email).enqueue(new Callback<JsonObject>() {
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
                                callBackFucntionAfterEdit.afterEdit(control, email);
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 2000);
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
        ApiHelper.getService().editShopPhone(token, id, phone).enqueue(new Callback<JsonObject>() {
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
                                callBackFucntionAfterEdit.afterEdit(control, phone);
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 2000);
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
        ApiHelper.getService().editShopAddress(token, id, address).enqueue(new Callback<JsonObject>() {
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
                                callBackFucntionAfterEdit.afterEdit(control, address);
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 2000);
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
        ApiHelper.getService().editShopDetail(token, id, detail).enqueue(new Callback<JsonObject>() {
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
                                callBackFucntionAfterEdit.afterEdit(control, detail);
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 2000);
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
        ApiHelper.getService().editBio(token, id, bio).enqueue(new Callback<JsonObject>() {
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
                                callBackFucntionAfterEdit.afterEdit(control, bio);
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 2000);
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
        ApiHelper.getService().editAddress(token, id, address).enqueue(new Callback<JsonObject>() {
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
                                callBackFucntionAfterEdit.afterEdit(control, address);
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 2000);
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
        ApiHelper.getService().editPhone(token, id, phone).enqueue(new Callback<JsonObject>() {
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
                                callBackFucntionAfterEdit.afterEdit(control, phone);
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 2000);
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
        ApiHelper.getService().editEmail(token, id, email).enqueue(new Callback<JsonObject>() {
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
                                callBackFucntionAfterEdit.afterEdit(control, email);
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 2000);
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
        ApiHelper.getService().editUsername(token, id, username).enqueue(new Callback<JsonObject>() {
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
                                callBackFucntionAfterEdit.afterEdit(control, username);
                                alertDialog.dismiss();
                                dismiss();
                            }
                        }, 2000);
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
    }
}

