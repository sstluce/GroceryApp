package com.grocery.groceryapp;

import com.grocery.groceryapp.data.GroceryItem;
import com.grocery.groceryapp.dba.DBAdapter;
import com.grocery.groceryapp.dba.GroceryItemDBAdapter;
import com.grocery.groceryapp.util.SystemUiHider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

    private Button addItemBtn;//, historyBtn, sortBtn;
    private EditText itemInput;
    private TextView emptyListText;
    private ListView groceryList;
    private ArrayList<GroceryItem> groceries;
    private DBAdapter db;
    private GroceryItemDBAdapter gdb;
    private GroceryItemAdapter gAdapter;

    private long id = -1L;


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
//        historyBtn = (Button) findViewById(R.id.historyBtn);
//        sortBtn = (Button) findViewById(R.id.sortBtn);
        emptyListText = (TextView) findViewById(R.id.empty_list_text);
        itemInput = (EditText) findViewById(R.id.itemInput);
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
                hideKeyboard();
            }
        });
        groceryList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                editItem(position);
                return true;
            }
        });
        findViewById(R.id.frameList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });
        itemInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Log.d("Input", "Focused: "+findViewById(R.id.headerTxt).getY());
                    Log.d("Input", "Focused: "+findViewById(R.id.headerTxt).getY());
                    Log.d("Input", "Focused: "+findViewById(R.id.headerTxt).getY());
                    Log.d("Input", "Focused: "+findViewById(R.id.headerTxt).getY());
                }else{
                    Log.d("Input", "Unfocused: "+findViewById(R.id.headerTxt).getY());
                    Log.d("Input", "Unfocused: "+findViewById(R.id.headerTxt).getY());
                    Log.d("Input", "Unfocused: "+findViewById(R.id.headerTxt).getY());
                    Log.d("Input", "Unfocused: "+findViewById(R.id.headerTxt).getY());
                }
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
//        Intent intent = new Intent(this, AddItemActivity.class);
//        startActivityForResult(intent, 0);
        gdb.open();
        GroceryItem item = new GroceryItem(itemInput.getText().toString(), 1);
        item.setFrom("");
        if(id == -1L) {
            long id = gdb.insertGroceryItem(item);
        }else{
            item.set_id(id);
            gdb.updateGroceryItem(item);
            hideKeyboard();
        }
        item.set_id(id);

        id = -1L;
        updateGroceries();
        groceryList.setStackFromBottom(true);
        itemInput.setText("");
        addItemBtn.setText(R.string.add_item);
        gdb.close();
    }

    public void editItem(int pos){
//        Log.d("GroceryList", "Item clicked: "+pos);
//        Intent intent = new Intent(this, AddItemActivity.class);
//        intent.putExtra(AddItemActivity.EXTRA_ITEM_ID, groceries.get(pos).getId());
//        startActivityForResult(intent, 0);
        GroceryItem item = groceries.get(pos);
        itemInput.append(item.getName());
        addItemBtn.setText(R.string.update_item);
        id = item.getId();
        showKeyboard();
    }

    private void showKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(itemInput, 0);
        }
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        itemInput.clearFocus();
        if(itemInput == this.getCurrentFocus()){
            Log.d("Focus", "itemInput");
            Log.d("Focus", "itemInput");
            Log.d("Focus", "itemInput");
        }else{
            Log.d("Focus", "not itemInput");
            Log.d("Focus", "not itemInput");
            Log.d("Focus", "not itemInput");
        }
        itemInput.setText("");
        groceryList.setStackFromBottom(false);
    }
}
