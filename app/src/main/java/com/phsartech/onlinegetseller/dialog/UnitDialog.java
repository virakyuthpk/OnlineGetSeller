package com.phsartech.onlinegetseller.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.adapter.UnitAdapter;
import com.phsartech.onlinegetseller.callback.CallBackFunctionOnButtonUnitClick;
import com.phsartech.onlinegetseller.callback.CallBackFunctionOnButtonUnitDialogClick;
import com.phsartech.onlinegetseller.model.UnitModel;
import com.phsartech.onlinegetseller.retrofit.ApiHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnitDialog extends DialogFragment implements CallBackFunctionOnButtonUnitDialogClick {

    private CallBackFunctionOnButtonUnitClick callBackFunctionOnButtonUnitClick;
    private RecyclerView recyclerView;
    private List<UnitModel.DataEntity> list;
    private UnitAdapter unitAdapter;
    private static String TAG = "UnitDialog";

    public UnitDialog(CallBackFunctionOnButtonUnitClick callBackFunctionOnButtonUnitClick) {
        this.callBackFunctionOnButtonUnitClick = callBackFunctionOnButtonUnitClick;
    }

    public static UnitDialog display(FragmentManager fragmentManager, CallBackFunctionOnButtonUnitClick callBackFunctionOnButtonUnitClick) {
        UnitDialog exampleDialog = new UnitDialog(callBackFunctionOnButtonUnitClick);
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
        View view = inflater.inflate(R.layout.fragment_item_recycler, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        getData();
        return view;
    }

    private void getData() {
        ApiHelper.getService().getUnit().enqueue(new Callback<UnitModel>() {
            @Override
            public void onResponse(Call<UnitModel> call, Response<UnitModel> response) {
                if (response.body().getData() != null) {
                    list = response.body().getData();
                    setView();
                }
            }

            @Override
            public void onFailure(Call<UnitModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void setView() {
        unitAdapter = new UnitAdapter(getActivity(), list, this);
        recyclerView.setAdapter(unitAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }
    @Override
    public void onButtonUnitClick(UnitModel.DataEntity item) {
        callBackFunctionOnButtonUnitClick.onButtonUnitClick(item);
        dismiss();
    }
}
