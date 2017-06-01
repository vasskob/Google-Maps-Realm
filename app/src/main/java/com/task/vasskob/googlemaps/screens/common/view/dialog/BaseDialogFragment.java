package com.task.vasskob.googlemaps.screens.common.view.dialog;

import android.content.Context;
import android.support.v4.app.DialogFragment;


public abstract class BaseDialogFragment<T> extends DialogFragment {
    private T mCallback;

    public final T getCallback() {
        return mCallback;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (T) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }
}