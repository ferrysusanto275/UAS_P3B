package com.example.contract;

import android.content.Context;

public interface MainUI {
    public void loginBerhasil(String token);
    public void loginGagal(String error);
    public Context getContext();
    public void disabledInput();
    public void enabledInput();
}
