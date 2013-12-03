package com.shoppingList.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ShopItem implements IsSerializable {

    private String name;
    private boolean purchased;

    public ShopItem() {
    }

    public void togglePurchased() {
        purchased = !purchased;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public ShopItem(String name, boolean purchased) {
        this.name = name;
        this.purchased = purchased;
    }

}