package com.example.pma_projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button b11, b12, b13, b21, b22, b23, b31, b32, b33;
    String xo = "X";
    int[][] Storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        b11 = findViewById(R.id.button_11);
        b12 = findViewById(R.id.button_12);
        b13 = findViewById(R.id.button_13);
        b21 = findViewById(R.id.button_21);
        b22 = findViewById(R.id.button_22);
        b23 = findViewById(R.id.button_23);
        b31 = findViewById(R.id.button_31);
        b32 = findViewById(R.id.button_32);
        b33 = findViewById(R.id.button_33);
        
        b11.setOnClickListener(this);
        b12.setOnClickListener(this);
        b13.setOnClickListener(this);
        b21.setOnClickListener(this);
        b22.setOnClickListener(this);
        b23.setOnClickListener(this);
        b31.setOnClickListener(this);
        b32.setOnClickListener(this);
        b33.setOnClickListener(this);

        Storage = new int[3][3];
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_11:
                b11.setText(xo);
                input(1,1);
                break;
            case R.id.button_12:
                b12.setText(xo);
                input(1,2);
                break;
            case R.id.button_13:
                b13.setText(xo);
                input(1,3);
                break;
            case R.id.button_21:
                b21.setText(xo);
                input(2,1);
                break;
            case R.id.button_22:
                b22.setText(xo);
                input(2,2);
                break;
            case R.id.button_23:
                b23.setText(xo);
                input(2,3);
                break;
            case R.id.button_31:
                b31.setText(xo);
                input(3,1);
                break;
            case R.id.button_32:
                b32.setText(xo);
                input(3,2);
                break;
            case R.id.button_33:
                b33.setText(xo);
                input(3,3);
                break;
        }
    }

    private void input(int x, int y) {
        x = x -1;
        y = y -1;

        if(Storage[x][y] == 0){
            if(xo.equals("X")){
                Storage[x][y] = 1;
                xo = "O";
            } else {
                Storage[x][y] = -1;
                xo = "X";
            }
        }
        if(checkEnd()){
            gameFinish();
        }
    }

    private boolean checkEnd() {
        return    (Math.abs(Storage[0][0] + Storage[0][1] + Storage[0][2]) == 3
                || Math.abs(Storage[1][0] + Storage[1][1] + Storage[1][2]) == 3
                || Math.abs(Storage[2][0] + Storage[2][1] + Storage[2][2]) == 3
                || Math.abs(Storage[0][0] + Storage[1][0] + Storage[2][0]) == 3
                || Math.abs(Storage[0][1] + Storage[1][1] + Storage[2][1]) == 3
                || Math.abs(Storage[0][2] + Storage[1][2] + Storage[2][2]) == 3
                || Math.abs(Storage[0][0] + Storage[1][1] + Storage[2][2]) == 3
                || Math.abs(Storage[0][2] + Storage[1][1] + Storage[2][0]) == 3);
    }

    private void gameFinish(){
        if(xo.equals("X")){
            Toast.makeText(getApplicationContext().getApplicationContext(), "0 gewinnt", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext().getApplicationContext(), "X gewinnt", Toast.LENGTH_LONG).show();
        }
        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);
        this.finish();

    }
}