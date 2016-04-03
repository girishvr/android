package com.bitjini.myfirstprogram;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button myButton = (Button) findViewById(R.id.clickButton);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this,"button clicked",Toast.LENGTH_LONG).show();
                Intent freshIntent = new Intent(MainActivity.this, FreshPage.class);
                startActivity(freshIntent);
            }
        });
    }
}















