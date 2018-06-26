package com.example.kychow.simpletodo;

import android.os.Bundle;
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
        //initialize adapter using items list
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        // wire the adapter to the view
        lvItems.setAdapter(itemsAdapter);

        //add mock items
        items.add("First todo item");
        items.add("Second todo item");

        // setup the listener on creation
        setupListViewListener();
    }

    public void onAddItem(View v) {
        // obtain a reference to the EditText created with the layout
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        // grab the EditText's content as a String
        String itemText = etNewItem.getText().toString();
        // add the item to the list via the adapter
        itemsAdapter.add(itemText);
        // clear the EditText by setting it to an empty string
        etNewItem.setText("");

        // display a notification to the user
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
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
                // return true to tell framework that the long click was consumed
                return true;
            }
        });
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
}