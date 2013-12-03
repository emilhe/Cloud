package com.shoppingList.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.shoppingList.shared.ShopItem;

import java.util.ArrayList;

public interface ShoppingListServiceAsync {

    void loadItems(AsyncCallback<ArrayList<ShopItem>> async);

    void saveItems(ArrayList<ShopItem> items, AsyncCallback<Void> async);

    void checkLogin(String returnURL, AsyncCallback<String> async);
}
