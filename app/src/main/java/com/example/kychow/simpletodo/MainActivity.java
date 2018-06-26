package com.example.kychow.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // numeric code to identify edit activity
    public static final int EDIT_REQUEST_CODE = 20;
    // keys used for passing data between activities
    public static final String ITEM_TEXT = "itemTEXT";
    public static final String ITEM_POSITION = "itemPosition";

    // declaring stateful objects here; these will be null before OnCreate is called
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // obtain a reference ListView created by layout
        lvItems = (ListView) findViewById(R.id.lvItems);
        //initialize items list
        items = new ArrayList<>();
        readItems();
        //initialize adapter using items list
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        // wire the adapter to the view
        lvItems.setAdapter(itemsAdapter);

        //add mock items
        //items.add("First todo item");
        //items.add("Second todo item");

        // setup the listener on creation
        setupListViewListener();

    }

    private void setupListViewListener() {
        // set the ListView's itemLongClickListener
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                // remove the item in the list at the index given by position
                items.remove(position);
                // notify the adapter that the underlying dataset changed
                itemsAdapter.notifyDataSetChanged();
                Log.i("MainActivity", "Removed item " + position);
                writeItems();
                // return true to tell framework that the long click was consumed
                return true;
            }
        });

        // set ListView's regular click listener
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> prent, View view, int position, long id) {
                // parameters: context, class of launched activity
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                // put extras into bundle for access in edit activity
                i.putExtra(ITEM_TEXT, items.get(position));
                i.putExtra(ITEM_POSITION, position);
                // brings up edit activity (we expect a result)
                startActivityForResult(i, EDIT_REQUEST_CODE);
            }
        });
    }

    public void onAddItem(View v) {
        // obtain a reference to the EditText created with the layout
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        // grab the EditText's content as a String
        String itemText = etNewItem.getText().toString();
        // add the item to the list via the adapter
        itemsAdapter.add(itemText);
        writeItems();
        // clear the EditText by setting it to an empty string
        etNewItem.setText("");

        // display a notification to the user
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
    }

    // returns file where data is stored
    private File getDataFile(){
        return new File(getFilesDir(), "todo.txt");
    }

    // read items from file system
    private void readItems() {
        try {
            // create array using content in file
            items = new ArrayList<String> (FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            // print error to console
            e.printStackTrace();
            // load empty list
            items = new ArrayList<>();
        }
    }

    // write items to filesystem
    private void writeItems() {
        try {
            // save item list a line-delimited text file
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            // print error to console
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // EDIT_REQUEST_CODE defined with constants
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            // extract updated item value from result extras
            String updatedItem = data.getExtras().getString(ITEM_TEXT);
            // get position of edited item
            int position = data.getExtras().getInt(ITEM_POSITION, 0);
            // updated model with new item text at edited position
            items.set(position, updatedItem);
            // notify adapter with changed model
            itemsAdapter.notifyDataSetChanged();
            // store updated items back to disk
            writeItems();
            // notify user operation completed OK
            Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
        }
    }
}