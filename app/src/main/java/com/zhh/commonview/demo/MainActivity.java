package com.zhh.commonview.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View view) {
        int id = view.getId();
        if(id == R.id.btn_more_fragments){
            startActivity(new Intent(this,MoreFragmentTestActivity.class));
        }
    }
}
