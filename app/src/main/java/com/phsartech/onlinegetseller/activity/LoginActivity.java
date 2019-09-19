package com.phsartech.onlinegetseller.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.dialog.VerifyEmailDialog;
import com.phsartech.onlinegetseller.retrofit.ApiHelper;
import com.phsartech.onlinegetseller.util.LocalDataStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText inputEditText_email, inputEditText_password;
    private MaterialButton materialButton_login, materialButton_forgetpassword;
    private TextView textView_or;
    private LoginButton loginButton_facebook;
    private TextInputLayout textInputLayout_email, textInputLayout_password;
    private CallbackManager callBackManager;
    private String TAG = "LoginActivity";

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callBackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void registerComponent() {
        textInputLayout_email = findViewById(R.id.layout_email);
        textInputLayout_password = findViewById(R.id.layout_password);
        inputEditText_email = findViewById(R.id.edit_text_email);
        inputEditText_password = findViewById(R.id.edit_text_password);
        materialButton_login = findViewById(R.id.button_login);
        loginButton_facebook = findViewById(R.id.login_button);
        textView_or = findViewById(R.id.text_or);
        materialButton_forgetpassword = findViewById(R.id.material_text_button_forget_password);
        inputEditText_email.setSingleLine(true);
    }

    private void login_facebook(String id, String first_name, String
            last_name, String
                                        name, String email, String image_path) {
        final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "",
                "Loading, Please wait...", true);
        ApiHelper.getService().login_fb(id, first_name, last_name, name, email, image_path).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                if (response.body() == null) {
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("Sorry");
                    alertDialog.setMessage("Something Bad Happened!");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        LocalDataStore.setToken(LoginActivity.this, "Bearer " + jsonObject.getString("token"));
                        LocalDataStore.setID(LoginActivity.this, jsonObject.getInt("user_id"));
                        LocalDataStore.setLogin(LoginActivity.this, true);
                        LocalDataStore.setSHOPID(LoginActivity.this, jsonObject.getInt("shop_id"));
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "onFailure: " + t.getMessage());
            }

        });
    }

    private boolean isEmptyValid(Editable text) {
        return text != null && text.length() > 0;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(ContextCompat.getColor(LoginActivity.this, R.color.colorGrey50Transparent));
        }
        registerComponent();

        callBackManager = CallbackManager.Factory.create();
        loginButton_facebook.setReadPermissions("public_profile", "email");
        loginButton_facebook.registerCallback(callBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String accessToken = loginResult.getAccessToken().getToken();
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            URL profile_pic = new URL(
                                    "https:graph.facebook.com/" +
                                            object.getString("id") +
                                            "/picture?width=250&height=250");
                            login_facebook(
                                    object.getString("id"),
                                    object.getString("first_name"),
                                    object.getString("last_name"),
                                    object.getString("name"),
                                    object.getString("email"),
                                    profile_pic.toString()
                            );
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                //Graph API
                Bundle bundle = new Bundle();
                bundle.putString("fields", "id,first_name,last_name,name,email");
                graphRequest.setParameters(bundle);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "onCancel: true");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "onError: " + error);
            }
        });

        materialButton_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(inputEditText_email.getText())) {
                    textInputLayout_email.setError("Please input Email!");
                } else if (TextUtils.isEmpty(inputEditText_password.getText())) {
                    textInputLayout_password.setError("Please Input Password!");
                } else {
                    getLogin(inputEditText_email.getText().toString(), inputEditText_password.getText().toString());
                }
            }
        });

        inputEditText_password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (isEmptyValid(inputEditText_password.getText())) {
                    textInputLayout_password.setError(null); //Clear the error
                }
                return false;
            }
        });

        inputEditText_email.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (isEmptyValid(inputEditText_email.getText())) {
                    textInputLayout_email.setError(null); //Clear the error
                }
                return false;
            }
        });
        materialButton_forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifyEmailDialog.display(getSupportFragmentManager());
            }
        });
    }

    private void getLogin(String email, final String password) {
        final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "",
                "Loading, Please wait...", true);
        ApiHelper.getService().getLogin(email, password).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();

                try {
                    if (response.body() == null) {

                        final AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                        alertDialog.setTitle("Sorry");
                        alertDialog.setMessage("Username or Password isn't correct!");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        LocalDataStore.setLogin(LoginActivity.this, true);
                        LocalDataStore.setSHOPID(LoginActivity.this, jsonObject.getInt("shop_id"));
                        LocalDataStore.setID(LoginActivity.this, jsonObject.getInt("user_id"));
                        LocalDataStore.setToken(LoginActivity.this, "Bearer " + jsonObject.getString("token"));
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                        startActivity(intent);

                        finish();
                    }
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
}




