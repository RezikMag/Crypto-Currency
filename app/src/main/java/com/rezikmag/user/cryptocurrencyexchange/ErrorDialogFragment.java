package com.rezikmag.user.cryptocurrencyexchange;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ErrorDialogFragment extends DialogFragment {

   public static DialogFragment newInstance(){
        ErrorDialogFragment fragment = new ErrorDialogFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       View v= inflater.inflate(R.layout.fragment_error_dialog,container,false);

        Button button = v.findViewById(R.id.btn_finish_dialog);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dismiss();
            }
        });
        return v;
    }
}
