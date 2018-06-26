package com.example.kychow.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import static com.example.kychow.simpletodo.MainActivity.ITEM_POSITION;
import static com.example.kychow.simpletodo.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {

    // text field with updated item description
    EditText etItemText;
    // track item's position in list
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // resolve text field from layout
        etItemText = (EditText) findViewById(R.id.etItemText);
        // set text field content from intent
        etItemText.setText(getIntent().getStringExtra(ITEM_TEXT));
        // track position of item in list
        position = getIntent().getIntExtra(ITEM_POSITION, 0);
        // set title bar to reflect purpose of view
        getSupportActionBar().setTitle("Edit Item");
    }

    public void onSaveItem(View v) {
        // prepare intent to pass back to MainActivity
        Intent data = new Intent();
        // pass updated item text, original position
        data.putExtra(ITEM_TEXT, etItemText.getText().toString());
        data.putExtra(ITEM_POSITION, position);
        setResult(RESULT_OK, data);
        finish();
    }
}
