package com.phsartech.onlinegetseller.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
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
import com.phsartech.onlinegetseller.callback.CallBackFucntionSetNewPassword;
import com.phsartech.onlinegetseller.retrofit.ApiHelper;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetPasswordDialog extends DialogFragment {

    private static String TAG = "VerifyEmailDialog";
    private final int user_id;
    private TextInputEditText textInputEditText_new, textInputEditText_confirm;
    private TextInputLayout textInputLayout_new, textInputLayout_confirm;
    private MaterialButton materialButton;
    private Toolbar toolbar;
    private CallBackFucntionSetNewPassword callBackFucntionSetNewPassword;

    public SetPasswordDialog(int user_id, CallBackFucntionSetNewPassword callBackFucntionSetNewPassword) {
        this.user_id = user_id;
        this.callBackFucntionSetNewPassword = callBackFucntionSetNewPassword;
    }

    public static SetPasswordDialog display(FragmentManager fragmentManager, int user_id, CallBackFucntionSetNewPassword callBackFucntionSetNewPassword) {
        SetPasswordDialog exampleDialog = new SetPasswordDialog(user_id, callBackFucntionSetNewPassword);
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
        setupToolbar();
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(textInputEditText_new.getText())) {
                    textInputLayout_new.setError("Please input your password!");
                } else if (TextUtils.isEmpty(textInputEditText_confirm.getText())) {
                    textInputLayout_confirm.setError("Please input your confirm password!");
                } else if (!textInputEditText_new.getText().toString().equalsIgnoreCase(textInputEditText_confirm.getText().toString())) {
                    textInputLayout_confirm.setError("Your password does not match!");
                } else {
                    setNewPassword(user_id, textInputEditText_new.getText().toString());
                }
            }
        });
    }

    private void setNewPassword(int user_id, String password) {
        ApiHelper.getService().setNewPassword(user_id, password).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("success") != "false") {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage("Password change successful!");
                        alertDialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                alertDialog.dismiss();
                                dismiss();
                                callBackFucntionSetNewPassword.Dismiss();
                            }
                        }, 1000);
                    } else {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Sorry");
                        alertDialog.setMessage(jsonObject.getString("data"));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void setupToolbar() {
        if (toolbar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                toolbar.setTitle("Set New Password");
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
        View view = inflater.inflate(R.layout.fragment_setpassword, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        textInputEditText_new = view.findViewById(R.id.edit_text_password);
        textInputLayout_new = view.findViewById(R.id.layout_password);
        textInputEditText_confirm = view.findViewById(R.id.edit_text_password_confirm);
        textInputLayout_confirm = view.findViewById(R.id.layout_password_confirm);
        materialButton = view.findViewById(R.id.button_save);
        return view;
    }
}

