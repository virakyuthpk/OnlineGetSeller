package com.phsartech.onlinegetseller.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;
import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.callback.CallBackFucntionAfterEdit;

import org.json.JSONException;
import org.json.JSONObject;

public class ShopDialog extends DialogFragment implements CallBackFucntionAfterEdit {

    private static String TAG = "ShopDialog";
    private JSONObject jsonObject;
    private MaterialButton
            materialButton_shopname,
            materialButton_email,
            materialButton_phone,
            materialButton_address,
            materialButton_description;
    private TextView textView_shopname;

    public ShopDialog(JSONObject jsonOject) {
        this.jsonObject = jsonOject;
    }

    public static ShopDialog display(FragmentManager fragmentManager, JSONObject jsonOject) {
        ShopDialog exampleDialog = new ShopDialog(jsonOject);
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
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        registerComponent(view);
        return view;
    }

    private void registerComponent(View view) {
        textView_shopname = view.findViewById(R.id.text_shopname);
        materialButton_shopname = view.findViewById(R.id.button_shopname);
        materialButton_email = view.findViewById(R.id.button_shopemail);
        materialButton_phone = view.findViewById(R.id.button_shopphone);
        materialButton_address = view.findViewById(R.id.button_shopaddress);
        materialButton_description = view.findViewById(R.id.button_shopdes);

        setView();
    }

    private void setView() {
        try {
            textView_shopname.setText(jsonObject.getString("shop_name"));
            materialButton_shopname.setText(jsonObject.getString("shop_name"));
            materialButton_email.setText(jsonObject.getString("email"));
            materialButton_phone.setText(jsonObject.getString("phone"));
            materialButton_address.setText(jsonObject.getString("address"));
            materialButton_description.setText(jsonObject.getString("detail"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        materialButton_shopname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editShopInfo("shop_name");
            }
        });
        materialButton_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editShopInfo("shop_email");
            }
        });
        materialButton_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editShopInfo("shop_phone");
            }
        });
        materialButton_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editShopInfo("shop_address");
            }
        });
        materialButton_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editShopInfo("shop_detail");
            }
        });
    }

    private void editShopInfo(String control) {
        EditDialog.display(getFragmentManager(), control, this);
    }

    @Override
    public void afterEdit(String control, String value) {
        if (control == "shop_name") {
            materialButton_shopname.setText(value);
            textView_shopname.setText(value);
        } else if (control == "shop_email") {
            materialButton_email.setText(value);
        } else if (control == "shop_phone") {
            materialButton_phone.setText(value);
        } else if (control == "shop_address") {
            materialButton_address.setText(value);
        } else if (control == "shop_detail") {
            materialButton_description.setText(value);
        }
    }
}