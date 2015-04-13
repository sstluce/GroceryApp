package com.grocery.groceryapp;

import com.grocery.groceryapp.data.GroceryItem;
import com.grocery.groceryapp.dba.GroceryItemDBAdapter;
import com.grocery.groceryapp.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class AddItemActivity extends Activity {
    public static final String EXTRA_ITEM_ID = "ITEM_ID";
    Button saveBtn, cancelBtn;
    EditText nameTxt, quantityTxt, fromTxt;
    GroceryItemDBAdapter gdb;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        init();
        gdb.open();
        initListeners();
        setEditTextFields();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gdb.close();
    }

    private void setEditTextFields() {
        if(id == -1L){
            return;
        }

        GroceryItem gItem = gdb.getGroceryItem(id);

        nameTxt.setText(gItem.getName());
        quantityTxt.setText(String.valueOf(gItem.getQuantity()));
        fromTxt.setText(gItem.getFrom());
        saveBtn.setEnabled(true);
    }

    private void initListeners(){
        findViewById(R.id.itemLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            hideKeyboard();
            }
        });

        nameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                updateSaveBtnEnable();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        quantityTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                updateSaveBtnEnable();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroceryItem item = new GroceryItem(nameTxt.getText().toString(), Integer.parseInt(quantityTxt.getText().toString()));
                item.setFrom(fromTxt.getText().toString());
                if(id == -1L) {
                    long id = gdb.insertGroceryItem(item);
                }else{
                    item.set_id(id);
                    gdb.updateGroceryItem(item);
                }
                item.set_id(id);
                finish();
            }
        });
    }
    private void init(){
        gdb = GroceryItemDBAdapter.getSingleton(this);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);

        nameTxt = (EditText) findViewById(R.id.nameInput);
        quantityTxt = (EditText) findViewById(R.id.quantityInput);
        fromTxt = (EditText) findViewById(R.id.fromInput);

        id = getIntent().getLongExtra(EXTRA_ITEM_ID, -1L);
    }
    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    private void updateSaveBtnEnable() {
        saveBtn.setEnabled(!nameTxt.toString().isEmpty() && !quantityTxt.toString().isEmpty());
    }
}
