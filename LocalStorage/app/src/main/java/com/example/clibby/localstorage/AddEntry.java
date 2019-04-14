package com.example.clibby.localstorage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddEntry extends AppCompatActivity {

    private  EditText mText;
    private  EditText mDescription;
    private Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        mText = findViewById(R.id.editTitle);
        mDescription = findViewById(R.id.editDescription);
        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonClicked();
            }
        });

    }

    void ButtonClicked(){
        Intent intent = getIntent();
        if(intent == null)return;
        intent.putExtra(MainActivity.INTENT_TITLE,mText.getText().toString());
        intent.putExtra(MainActivity.INTENT_DESCRIPTION,mDescription.getText().toString());
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}
