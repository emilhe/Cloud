package com.shoppingList.server;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.shoppingList.client.ShoppingListService;
import com.shoppingList.shared.ShopItem;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShoppingListServiceImpl extends RemoteServiceServlet implements ShoppingListService {

    private static PersistenceManagerFactory pmfInstance;
    static {
        Map props = new HashMap();
        props.put("javax.jdo.PersistenceManagerFactoryClass", "org.datanucleus.store.appengine.jdo.DatastoreJDOPersistenceManagerFactory");
        props.put("javax.jdo.option.ConnectionURL", "appengine");
        props.put("javax.jdo.option.NontransactionalRead", "true");
        props.put("javax.jdo.option.NontransactionalWrite", "true");
        props.put("javax.jdo.option.RetainValues", "true");
        props.put("datanucleus.appengine.autoCreateDatastoreTxns", "true");
        pmfInstance = JDOHelper.getPersistenceManagerFactory(props);
    }

    @Override
    public String checkLogin(String returnURL) {
        UserService userService = UserServiceFactory.getUserService();
        if(userService.isUserLoggedIn()) return "LOGGED_IN";
        return userService.createLoginURL(returnURL);
    }

    @Override
    public void saveItems(ArrayList<ShopItem> items) {
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            pm.makePersistent(MapToPC(items));
        } finally {
            pm.close();
        }
    }

    @Override
    public ArrayList<ShopItem> loadItems() {
        ArrayList<ShopItem> result;
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            User user = UserServiceFactory.getUserService().getCurrentUser();
            Key key = KeyFactory.createKey(ShopItemListPC.class.getSimpleName(), user.getUserId());
            result = MapFromPC(pm.getObjectById(ShopItemListPC.class, key));
        } finally {
            pm.close();
        }
        return result;
    }

    // Map from persistence capable objects to client side object.
    private static ArrayList<ShopItem> MapFromPC(ShopItemListPC listPC) {
        ArrayList<ShopItem> result = new ArrayList<ShopItem>();
        for (ShopItemPC itemPC : listPC.getItems()) {
            result.add(new ShopItem(itemPC.getName(), itemPC.isPurchased()));
        }
        return result;
    }

    // Map from client side object to persistence capable objects.
    private static ShopItemListPC MapToPC(ArrayList<ShopItem> list) {
        User user = UserServiceFactory.getUserService().getCurrentUser();
        Key key = KeyFactory.createKey(ShopItemListPC.class.getSimpleName(), user.getUserId());
        ShopItemListPC listPC = new ShopItemListPC(key);
        for (ShopItem item : list) {
            listPC.addItem(new ShopItemPC(item));
        }
        return listPC;
    }

}