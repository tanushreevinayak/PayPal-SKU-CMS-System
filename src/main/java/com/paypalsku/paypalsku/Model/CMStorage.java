package com.paypalsku.paypalsku.Model;

import com.paypalsku.paypalsku.Utility.Action;
import com.paypalsku.paypalsku.View.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Component
@TestPropertySource("/application.properties")
public class CMStorage {
    Map<String,Map<String, Map<String,Item>>> storeUnitsMaps;
    Map<String,Item> idToItemMap;
    @Value("#{'${skus}'.split(',')}")
    private List<String> skus;

    @PostConstruct
    private void postConstruct() {
        idToItemMap=new HashMap<>();

        storeUnitsMaps= new HashMap<>();
        for (int i = 0; i < skus.size(); i++) {
            storeUnitsMaps.put(skus.get(i),new HashMap<>());
        }
    }

    public Item insertNewSkuItemMapping(Item item)  {
            item.setId(UUID.randomUUID().toString());
            idToItemMap.put(item.getId(),item);
            hashmapTraversalUtility(item, Action.Insert);
            return item;
    }

    private void hashmapTraversalUtility(Item item, Action action){
        for(int i=0;i<skus.size();i++) {
            Map<String, Map<String,Item>> mapForSku = storeUnitsMaps.get(skus.get(i));
            try {
                Method getSkuMethod
                        = Item.class.getDeclaredMethod("get"+skus.get(i));
                String[] tokenizeValueForSku = getTokenizeValue(getSkuMethod.invoke(item).toString());

                for(int j=0;j<tokenizeValueForSku.length;j++) {
                    switch(action) {
                        case Insert:
                            insertTokensIntoHashmap(tokenizeValueForSku[j], mapForSku, item);
                            break;
                        case Delete:
                            deleteItemMappings(tokenizeValueForSku[j], mapForSku, item);
                            break;
                        case Update:
                            updateItemMappings(tokenizeValueForSku[j], mapForSku, item);
                            break;
                        default: break;
                        }
                    }
                }

             catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
    }

    private String[] getTokenizeValue(String keywords) {
        String value=keywords.trim().replaceAll("\\s+", " ");
        return value.split(" ");
    }


    private void insertTokensIntoHashmap(String value,Map<String,Map<String, Item>> skuList,Item item){
        Map<String, Item> idToItemMap = skuList.get(value);
        String id=item.getId();

        if(idToItemMap==null){
            idToItemMap = new HashMap<>();
            skuList.put(value, idToItemMap);
        }
        idToItemMap.put(id, item);
    }

    private void deleteItemMappings(String value,Map<String,Map<String, Item>> skuList,Item item){
        Map<String, Item> itemMapFetched = skuList.get(value);
        if(itemMapFetched !=null && itemMapFetched.get(item.getId())!=null) {
            itemMapFetched.remove(item.getId());
        }
    }

    private void updateItemMappings(String value, Map<String,Map<String, Item>> skuList, Item item){
            insertTokensIntoHashmap(value,skuList,item);
    }

//    public List<Item> getItemListFromSku(String sku, String value){
//        Map<String, List<Item> mapForSku = storeUnitsMaps.get(sku);
//        return mapForSku.get(value);
//    }

    public Item getItemFromId(String id){
        return idToItemMap.get(id);
    }

    public void deleteItem(String id){
        Item item=idToItemMap.get(id);
        hashmapTraversalUtility(item,Action.Delete);
        idToItemMap.remove(item.getId());
    }

    public Item updateItem(String id, Item item){
        hashmapTraversalUtility(idToItemMap.get(id),Action.Delete);
        hashmapTraversalUtility(item,Action.Update);
        idToItemMap.put(id, item);
        return item;
    }

    public List<Item> searchKeyword(String sku, String keyword){
        Map<String, Map<String,Item>> mapForSku = storeUnitsMaps.get(sku);
        if(mapForSku==null) return null;
        Map<String, Item> itemMapFetched = mapForSku.get(keyword);
        if(itemMapFetched==null) return null;
        return new ArrayList<>(itemMapFetched.values());
    }

}
