package com.example.aokly.samsungfragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.aokly.samsungfragments.defaultClasses.MainActivity;
import com.example.aokly.samsungfragments.simpleData.Vector3;

/*
 Активность, с которой нам надо работать
 */
public class CustomAvtivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button btnCenter = (Button)findViewById(R.id.btnCenter);
        btnCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renderer.trianglePos = new Vector3();
            }
        });
    }
}
