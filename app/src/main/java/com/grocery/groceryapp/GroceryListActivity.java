package com.grocery.groceryapp;

import com.grocery.groceryapp.data.GroceryItem;
import com.grocery.groceryapp.dba.DBAdapter;
import com.grocery.groceryapp.dba.GroceryItemDBAdapter;
import com.grocery.groceryapp.util.SystemUiHider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class GroceryListActivity extends Activity {

    private Button addItemBtn, historyBtn, sortBtn;
    private TextView emptyListText;
    private ListView groceryList;
    private ArrayList<GroceryItem> groceries;
    private DBAdapter db;
    private GroceryItemDBAdapter gdb;
    private GroceryItemAdapter gAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);
        init();
        initListeners();
        updateGroceries();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        updateGroceries();
    }

    private void init(){
        db = DBAdapter.getSingleton(this);
        gdb = GroceryItemDBAdapter.getSingleton(this);
        groceryList = (ListView) findViewById(R.id.groceryList);
        addItemBtn = (Button) findViewById(R.id.addItemBtn);
        historyBtn = (Button) findViewById(R.id.historyBtn);
        sortBtn = (Button) findViewById(R.id.sortBtn);
        emptyListText = (TextView) findViewById(R.id.empty_list_text);
        db.open();
    }

    private void initListeners() {
        Log.d("GroceryList", "Init listeners");
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewItem();
            }
        });
        groceryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroceryItem gItem = (GroceryItem) parent.getItemAtPosition(position);
                gItem.selected();
                gAdapter.notifyDataSetChanged();
            }
        });
        groceryList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                editItem(position);
                return true;
            }
        });
    }

    public void updateGroceries(){
        gdb.open();

        groceries = gdb.getAllGroceryItems();
        gAdapter = new GroceryItemAdapter(this, groceries, gdb, this);
        groceryList.setAdapter(gAdapter);

        gAdapter.notifyDataSetChanged();
        updateListView();

        gdb.close();
    }

    private void updateListView(){
        emptyListText.setVisibility(groceryList.getCount() > 0 ? View.GONE : View.VISIBLE);
        groceryList.setVisibility(emptyListText.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    public void addNewItem(){
        Intent intent = new Intent(this, AddItemActivity.class);
        startActivityForResult(intent, 0);
    }

    public void editItem(int pos){
        Log.d("GroceryList", "Item clicked: "+pos);
        Intent intent = new Intent(this, AddItemActivity.class);
        intent.putExtra(AddItemActivity.EXTRA_ITEM_ID, groceries.get(pos).getId());
        startActivityForResult(intent, 0);
    }
}
