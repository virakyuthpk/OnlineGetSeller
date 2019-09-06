package com.phsartech.onlinegetseller.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.phsartech.onlinegetseller.retrofit.ApiHelper;
import com.phsartech.onlinegetseller.util.LocalDataStore;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassWordDialog extends DialogFragment {

    private static String TAG = "ChangePassWordDialog";
    private TextInputLayout textInputLayout_password_new, textInputLayout_password_confirm, textInputLayout_password_old;
    private TextInputEditText textInputEditText_password_new, textInputEditText_password_confirm, textInputEditText_password_old;
    private MaterialButton materialButton_save;
    private Toolbar toolbar;

    public static ChangePassWordDialog display(FragmentManager fragmentManager) {
        ChangePassWordDialog exampleDialog = new ChangePassWordDialog();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        materialButton_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(textInputEditText_password_new.getText())) {
                    textInputLayout_password_new.setError("Please Input password!");
                } else if (TextUtils.isEmpty(textInputEditText_password_confirm.getText())) {
                    textInputLayout_password_confirm.setError("Please Input password!");
                } else if (TextUtils.isEmpty(textInputEditText_password_old.getText())) {
                    textInputLayout_password_old.setError("Please Input password!");
                } else if (!textInputEditText_password_new.getText().toString().equalsIgnoreCase(textInputEditText_password_confirm.getText().toString())) {
                    textInputLayout_password_confirm.setError("Confirm password doesn't match!");
                    Log.e(TAG, "onClick: " + textInputEditText_password_new.getText().toString() + textInputEditText_password_confirm.getText().toString());
                } else {
                    changePassword(LocalDataStore.getID(getActivity()), LocalDataStore.getToken(getActivity()), textInputEditText_password_old.getText().toString(), textInputEditText_password_new.getText().toString());
                }
            }
        });
    }

    private void changePassword(final int id, String token, String oldP, String newP) {
        ApiHelper.getService().changePassword(id, token, oldP, newP).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (!jsonObject.getString("data").equalsIgnoreCase("Password changed successfully !")) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setTitle("Sorry");
                        alertDialog.setMessage(jsonObject.getString("data"));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage(jsonObject.getString("data"));
                        alertDialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
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

    private void setupToolbar() {
        if (toolbar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                toolbar.setTitle("Change Password");
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
        View view = inflater.inflate(R.layout.fragment_changepassword, container, false);
        registerComponent(view);
        setupToolbar();
        return view;
    }

    private void registerComponent(View view) {
        materialButton_save = view.findViewById(R.id.button_save);
        textInputEditText_password_new = view.findViewById(R.id.edit_text_password_change);
        textInputEditText_password_confirm = view.findViewById(R.id.edit_text_password_confirm);
        textInputEditText_password_old = view.findViewById(R.id.edit_text_password_old);
        textInputLayout_password_old = view.findViewById(R.id.layout_password_old);
        textInputLayout_password_new = view.findViewById(R.id.layout_password_change);
        textInputLayout_password_confirm = view.findViewById(R.id.layout_password_confirm);
        toolbar = view.findViewById(R.id.toolbar);
    }
}
