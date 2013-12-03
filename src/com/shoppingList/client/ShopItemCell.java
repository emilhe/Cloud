package com.shoppingList.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.shoppingList.shared.ShopItem;

import java.util.ArrayList;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

public class ShopItemCell extends AbstractCell<ShopItem> {

    //<editor-fold desc="Click event forwarding (OBSERVER)">

    ArrayList<ClickListener> clickListeners = new ArrayList<ClickListener>();

    public ShopItemCell() {
        super(CLICK);
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, ShopItem value,
                               NativeEvent event, ValueUpdater<ShopItem> valueUpdater) {
        super.onBrowserEvent(context, parent, value, event, valueUpdater);

        // Forward click event.
        if ((CLICK.equals(event.getType()))) signalClick(value);
    }

    public void addClickListener(ClickListener clickListener) {
        clickListeners.add(clickListener);
    }

    private void signalClick(ShopItem source) {
        for (ClickListener clickListener : clickListeners) {
            clickListener.onClick(source);
        }
    }

    //</editor-fold>

    // Custom rendering to allow strike out on purchase.
    @Override
    public void render(Context context, ShopItem value, SafeHtmlBuilder sb) {
        if (value == null) return;
        sb.appendHtmlConstant(value.isPurchased() ? "<div style=\"text-decoration:line-through;\">" : "<div>");
        sb.appendEscaped(value.getName());
        sb.appendHtmlConstant("</div>");
    }

}

