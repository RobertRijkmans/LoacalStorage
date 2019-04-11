package com.example.reminder.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.reminder.R;
import com.example.reminder.model.Reminder;

public class UpdateActivity extends AppCompatActivity {

    private EditText mReminderView;
    private EditText mDescription;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Init local variables
        mReminderView = findViewById(R.id.editText_update);
        mDescription = findViewById(R.id.Description);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sendBack();
            }
        });
    }
    void sendBack(){
        Intent intent = getIntent();
        String string = mReminderView.getText().toString()+"-"+mDescription.getText().toString()+"-F";
        intent.putExtra(MainActivity.EXTRA_REMINDER,string);
        setResult(RESULT_OK,intent);
        finish();
    }

}
