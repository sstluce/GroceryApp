package com.grocery.groceryapp.data;

/**
 * Created by shanestluce on 3/7/15.
 */
public class GroceryItem {
    private long _id;
    private String name, from;
    private int quantity;
    private boolean history, isChecked;

    public GroceryItem(){}
    public GroceryItem(String n, int count){
        name = n;
        quantity = count;
        history = false;
        isChecked = false;
    }

    public long getId() {return _id;}
    public int getQuantity() {return quantity;}
    public String getName() {return name;}
    public String getFrom() {return from;}
    public boolean isHistory() {return history;}
    public boolean isChecked() {return isChecked;}

    public void set_id(long value) {_id = value;}
    public void setFrom(String value) {from = value;}
    public void setHistory(boolean value) {history = value;}
    public void setChecked(boolean value) {isChecked = value;}

    public void selected() {isChecked = !isChecked;}
}
