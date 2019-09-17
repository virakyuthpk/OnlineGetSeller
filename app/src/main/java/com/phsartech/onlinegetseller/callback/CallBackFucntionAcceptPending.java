package com.phsartech.onlinegetseller.callback;

import com.phsartech.onlinegetseller.model.OrderModel;

public interface CallBackFucntionAcceptPending {
    void acceptPending(OrderModel.Data item);
}
