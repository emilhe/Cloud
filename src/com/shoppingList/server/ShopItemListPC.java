package com.shoppingList.server;

import com.google.appengine.api.datastore.Key;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.ArrayList;

@PersistenceCapable
public class ShopItemListPC {

    @PrimaryKey
    @Persistent
    private Key key;
    @Persistent
    private ArrayList<ShopItemPC> items;

    public ShopItemListPC(Key key) {
        this.key = key;
        items = new ArrayList<ShopItemPC>();
    }

    public ArrayList<ShopItemPC> getItems(){
        return items;
    };

    public void addItem(ShopItemPC item){
        items.add(item);
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }
}