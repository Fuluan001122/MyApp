package com.swufe.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RateActivity extends AppCompatActivity implements Runnable{

    private final String TAG = "Rate";

    private float dollarRate = 0.1f;
    private float euroRate = 0.0f;
    private float wonRate = 0.0f;

    EditText rmb;
    TextView show;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb = (EditText) findViewById(R.id.rmb);
        show = (TextView) findViewById(R.id.show);

        //获取SP里保存的数据
        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);//推荐方法，版本>24
        dollarRate = sharedPreferences.getFloat("dollar_rate",0.0f);
        euroRate = sharedPreferences.getFloat("euro_rate",0.0f);
        wonRate = sharedPreferences.getFloat("won_rate",0.0f);
        //defValue设置默认值
        Log.i(TAG, "onCreate: sp dollarRate=" + dollarRate);
        Log.i(TAG, "onCreate: sp euroRate=" + euroRate);
        Log.i(TAG, "onCreate: sp wonRate=" + wonRate);

        //开启子线程
        Thread t = new Thread(this);//要加上当前对象使用接口，避免冲突
        t.start();

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what==5){
                    String str = (String) msg.obj;//强制转型
                    Log.i(TAG, "handleMessage: getMessage msg = " + str);
                    show.setText(str);
                }
                super.handleMessage(msg);
            }
        };//类和类方法，重写

    }

    @SuppressLint("DefaultLocale")
    public void onClick(View btn) {
        //获取用户输入内容
        Log.i(TAG, "onClick: ");
        String str = rmb.getText().toString();
        Log.i(TAG, "onClick: get str=" + str);

        float r = 0;
        if(str.length()>0){
            r = Float.parseFloat(str);
        }else{
            //用户没有输入内容
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i(TAG, "onClick: r=" + r);


        //计算1：常量计算
//        float val=0;
//        if(btn.getId()==R.id.btn_dollar){
//            val = r*(1/6.7f);
//            show.setText(String.valueOf(val));
//        }else if(btn.getId()==R.id.btn_euro){
//            val = r*(1/11f);
//            show.setText(val+"");
//        }else{
//            val = r*(1/500f);
//        }
//        show.setText(String.valueOf(val));
//    }

        //计算2：数值直接计算
        if (btn.getId() == R.id.btn_dollar) {
            show.setText(String.format("%.2f",r * dollarRate));
        } else if (btn.getId() == R.id.btn_euro) {
            show.setText(String.format("%.2f",r * euroRate));
        } else {
            show.setText(String.format("%.2f",r * wonRate));
        }
    }
//    public void openOne(View btn){
//        //打开一个页面activity
//        Log.i("open","openOne");
//        Intent hello = new Intent(this,Week4Activity.class);
//        Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jd.com"));
//        Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:87092"));
//        //startActivity(hello);
//        //startActivity(web);
//        //startActivity(dial);
//
//        finish();
//    }

    public void openConfig(){
        openConfig();
    }

    public void openConfig(View btn){
        Intent config = new Intent(this,ConfigActivity.class);
        //传值
        config.putExtra("dollar_rate_key",dollarRate);
        config.putExtra("euro_rate_key",euroRate);
        config.putExtra("won_rate_key",wonRate);

        //输出
        Log.i(TAG, "openOne: dollarRate=" + dollarRate);
        Log.i(TAG, "openOne: euroRate=" + euroRate);
        Log.i(TAG, "openOne: wonRate=" + wonRate);

        //startActivity(config);
        startActivityForResult(config,1);//带回数据(打开对象，整数)
        }

    @Override
    //添加菜单
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_set){
           openConfig();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            /*
            bdl.putFloat("key_dollar", newDollar);
         bdl.putFloat("key_euro", newEuroi);
         bdl.putFloat("key_won", newWon);
             */
            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("key_dollar",0.1f);
            euroRate = bundle.getFloat("key_euro",0.1f);
            wonRate = bundle.getFloat("key_won",0.1f);
            Log.i(TAG, "onActivityResult: dollarResult" + dollarRate);
            Log.i(TAG, "onActivityResult: euroResult" + euroRate);
            Log.i(TAG, "onActivityResult: wonResult" + wonRate);

            //将新设置的汇率写到SP里
            SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("dollar_rate",dollarRate);
            editor.putFloat("euro_rate",euroRate);
            editor.putFloat("won_rate",wonRate);
            editor.commit();
            Log.i(TAG, "onActivityResult: 数据已保存到sharedPreferences");
        }

    }

    @Override
    public void run() {
        Log.i(TAG, "run: run()......");
        Log.i(TAG, "run: run()......");
        for(int i=1;i<6;i++){
            Log.i(TAG, "run: i=" + i);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }//当前停止两秒钟
        }

        //获取Msg对象，用于返回主线程
        Message msg = handler.obtainMessage(5);
        //msg.what = 5;
        msg.obj = "Hello from run()";
        handler.sendMessage(msg);

        //获取网络数据
        URL url = null;
        try {
            url = new URL("http://www.usd-cny.com/icbc.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();//强制转型
            InputStream in = http.getInputStream();//获得输入流

            String html = inputStream2String(in);
            Log.i(TAG, "run: html=" + html);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");//转中文字符串方式
        while (true) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }
}


