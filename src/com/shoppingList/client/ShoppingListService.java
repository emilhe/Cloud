package com.shoppingList.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.shoppingList.shared.ShopItem;

import java.util.ArrayList;

@RemoteServiceRelativePath("ShoppingListService")
public interface ShoppingListService extends RemoteService {

    String checkLogin(String returnURL);

    void saveItems(ArrayList<ShopItem> items);

    ArrayList<ShopItem> loadItems();

    public static class App {
        private static ShoppingListServiceAsync ourInstance = GWT.create(ShoppingListService.class);

        public static synchronized ShoppingListServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
