package com.codepathgroup5.listeners;


import android.support.v4.app.Fragment;

import com.codepathgroup5.models.Message;

public interface ChildFragmentListener {
    void nextQuestion(Fragment fragment,Message message);
    void lastQuestion(Fragment fragment);
}
