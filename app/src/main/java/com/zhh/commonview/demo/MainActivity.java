package com.zhh.commonview.demo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

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
        }else if(id == R.id.btn_chart_view){
            startActivity(new Intent(this,ChartActivity.class));
        }else if(id == R.id.iv_menu){
            //执行删除操作逻辑
            showToast("删除逻辑");
        }
    }


    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
