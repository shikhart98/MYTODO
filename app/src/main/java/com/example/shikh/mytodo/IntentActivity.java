package com.example.shikh.mytodo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class IntentActivity extends AppCompatActivity {
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);
        tv = findViewById(R.id.tv);
        String res = getIntent().getStringExtra("str");
        tv.setText(res);
    }
}
