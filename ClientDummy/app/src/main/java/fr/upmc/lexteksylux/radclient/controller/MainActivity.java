package fr.upmc.lexteksylux.radclient.controller;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fr.upmc.lexteksylux.radclient.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int[] value = {0};
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setBackgroundColor(Color.GRAY);

        final Button buttontap = (Button) findViewById(R.id.ButtonTap);
        final TextView countview = (TextView) findViewById(R.id.CountView);

        buttontap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                countview.setText(Integer.toString(++value[0]));
            }
        });

    }
}
