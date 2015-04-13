package com.grocery.groceryapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.grocery.groceryapp.data.GroceryItem;
import com.grocery.groceryapp.dba.GroceryItemDBAdapter;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by shanestluce on 3/7/15.
 */
public class GroceryItemAdapter extends ArrayAdapter<GroceryItem> {
    private Holder holder;
    GroceryItemDBAdapter gdb;
    GroceryListActivity gla;

    public GroceryItemAdapter(Context context, List<GroceryItem> list, GroceryItemDBAdapter dba, GroceryListActivity groceryListActivity) {
        super(context, 0, list);
        gdb = dba;
        gla = groceryListActivity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.grocery_item, parent, false);

            holder = new Holder();
            holder.GroceryItemNameTxt = (TextView) view.findViewById(R.id.itemName);
            holder.GroceryItemQuantityTxt = (TextView) view.findViewById(R.id.itemQuantity);
            holder.GroceryItemFromTxt = (TextView) view.findViewById(R.id.itemFrom);
            holder.GroceryItemFav = (ImageView) view.findViewById(R.id.itemFav);
//            holder.GroceryItemCheckBox = (CheckBox) view.findViewById(R.id.itemCheckBox);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        final GroceryItem groceryItem = getItem(position);

        holder.GroceryItemNameTxt.setText(groceryItem.getName());
        holder.GroceryItemQuantityTxt.setText("Quantity: "+groceryItem.getQuantity());
        if(!groceryItem.getFrom().isEmpty()){
            holder.GroceryItemFromTxt.setText("From: "+groceryItem.getFrom());
            holder.GroceryItemQuantityTxt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        }else{
            holder.GroceryItemFromTxt.setText("");
            holder.GroceryItemFromTxt.setVisibility(View.GONE);
            holder.GroceryItemQuantityTxt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        }
//        holder.GroceryItemCheckBox.setFocusable(false);
//        holder.GroceryItemCheckBox.setClickable(false);
//        holder.GroceryItemCheckBox.setChecked(groceryItem.isChecked());
//        Log.d("gItemAdapter", position + ": " + holder.GroceryItemCheckBox.isChecked());
        holder.GroceryItemFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("Adapter","clicked");
                if(!gdb.db.isOpen()) {
                    gdb.open();
                }
                gdb.deleteGroceryItem(groceryItem.getId());
//                notifyDataSetChanged();
                gla.updateGroceries();
//                groceryItem.setHistory(!groceryItem.isHistory());
//                if(groceryItem.isHistory())
//                    v.setBackgroundColor(Color.BLUE);
//                else
//                    v.setBackgroundColor(Color.BLACK);
            }
        });
        if(groceryItem.isChecked()){
//            view.setBackgroundColor(Color.parseColor("#0586ff"));
            holder.GroceryItemNameTxt.setPaintFlags(holder.GroceryItemNameTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
//            view.setBackgroundColor(Color.parseColor("#0099cc"));
            holder.GroceryItemNameTxt.setPaintFlags(holder.GroceryItemNameTxt.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
//        if(groceryItem.isHistory())
//            holder.GroceryItemFav.setBackgroundColor(Color.BLUE);
//        else
//            holder.GroceryItemFav.setBackgroundColor(Color.BLACK);

        return view;
    }
/*
    public void toggle(){
        holder.GroceryItemCheckBox.setSelected(!holder.GroceryItemCheckBox.isSelected());
    }*/

    private static class Holder {
        TextView GroceryItemNameTxt;
        TextView GroceryItemQuantityTxt;
        TextView GroceryItemFromTxt;
        ImageView GroceryItemFav;
//        CheckBox GroceryItemCheckBox;
    }
}