package com.shoppingList.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.shoppingList.shared.ShopItem;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShoppingList implements EntryPoint {

    // GUI elements.
    private TextBox inputBox = new TextBox();
    private Button addBtn = new Button("Add");
    private ShopItemCell cell = new ShopItemCell();
    private CellList<ShopItem> shoppingListView = new CellList<ShopItem>(cell);
    // Business elements.
    private ArrayList<ShopItem> shoppingItemList = new ArrayList<ShopItem>();
    private Logger logger = Logger.getLogger("MyLogger");
    private ShoppingListServiceAsync service = (ShoppingListServiceAsync) GWT.create(ShoppingListService.class);

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        doGoogleLogin();
    }

    //<editor-fold desc="Remote Procedure Calls">

    private void doGoogleLogin() {
        AsyncCallback callback = new AsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Login failed.");
            }

            @Override
            public void onSuccess(Object object) {
                String result = (String) object;
                if(result.equals("LOGGED_IN")) {
                    // Logged in; proceed go GUI.
                    setupGUI();
                    loadItems();
                }
                else{
                    // Not logged in; forward to google login.
                    Window.open((String) result, "_self", "");
                }
            }
        };
        // Get login URL; after login, return to current page.
        service.checkLogin(GWT.getHostPageBaseURL(), callback);
    }

    private void loadItems() {
        service.loadItems(new AsyncCallback<ArrayList<ShopItem>>() {

            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.INFO, "No items were loaded.");
            }

            @Override
            public void onSuccess(ArrayList<ShopItem> result) {
                if(result == null) return;
                // A match was found; load it.
                shoppingItemList = result;
                onDataChange();
            }
        });
    }

    private void saveItems(){
        service.saveItems(shoppingItemList, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.WARNING, "Save failed.");
            }

            @Override
            public void onSuccess(Void result) {
                logger.log(Level.INFO, "Save success.");
            }
        });
    }

    //</editor-fold>

    //<editor-fold desc="GUI setup and manipulation">

    private void setupGUI() {
        // Add cell event handler.
        cell.addClickListener(new ClickListener<ShopItem>() {
            @Override
            public void onClick(ShopItem source) {
                source.togglePurchased();
                onDataChange();
            }
        });

        // Input panel.
        inputBox.setText("Item to add");
        HorizontalPanel addPanel = new HorizontalPanel();
        addPanel.add(inputBox);

        // Add input field event handler.
        inputBox.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                // Add item on "enter" (=13) key down.
                if (event.getNativeKeyCode() != 13) return;
                shoppingItemList.add(new ShopItem(inputBox.getText(), false));
                onDataChange();
            }
        });
        addPanel.add(addBtn);

        // Add button event handler.
        addBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                shoppingItemList.add(new ShopItem(inputBox.getText(), false));
                onDataChange();
            }
        });

        // List panel.
        VerticalPanel displayPanel = new VerticalPanel();
        displayPanel.add(shoppingListView);

        // Perform bindings.
        RootPanel.get("input").add(addPanel);
        RootPanel.get("display").add(displayPanel);
    }

    private void onDataChange(){
        // Update view and save changes.
        shoppingListView.setRowData(shoppingItemList);
        saveItems();
    }

    //</editor-fold>

}
