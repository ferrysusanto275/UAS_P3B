package com.example.contract;

import android.os.Bundle;

public interface HomeUI {
    public void changePage(String page, Bundle bundle);
    public void closeApp();
    public String getToken();
    public String getRole();
    public String getEmail();
}
