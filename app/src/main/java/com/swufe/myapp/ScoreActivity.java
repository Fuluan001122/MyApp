package com.swufe.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    TextView score;
    TextView score2;

    public void btnAdd2(View view) {
    }

    public void btnAdd1(View view) {
    }

    public void btnAdd3(View view) {
    }


    public class SecondActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_score);

            score = (TextView) findViewById(R.id.score);
        }


        public void btnAdd1(View btn) {
            if(btn.getId()==R.id.btn1a) {
                showScore(1);
            }else{
                showScore2(1);
            }
        }

        public void btnAdd2(View btn) {
            if(btn.getId()==R.id.btn1a) {
                showScore(2);
            }else{
                showScore2(2);
            }
        }

        public void btnAdd3(View btn) {
            if(btn.getId()==R.id.btn1a) {
                showScore(3);
            }else{
                showScore2(3);
            }
        }

        public void btnReset(View btn) {
            score.setText("0");
            score2.setText("0");
        }

        private void showScore(int inc) {
            Log.i("show", "inc=" + inc);
            String oldScore = (String) score.getText();
            int newScore = Integer.parseInt(oldScore) + inc;
            score.setText("" + newScore);
        }

        private void showScore2(int inc) {
            Log.i("show", "inc=" + inc);
            String oldScore = (String) score2.getText();
            int newScore = Integer.parseInt(oldScore) + inc;
            score2.setText("" + newScore);
        }
    }

}
