package com.paypalsku.paypalsku.Controller;

import com.paypalsku.paypalsku.Model.CMStorage;
import com.paypalsku.paypalsku.View.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SkuApis {

    @Autowired
    CMStorage cmStorage;

    @PostMapping(path = "/item", consumes = "application/json", produces = "application/json")
    public Item addItem(@RequestBody Item item) {
            return cmStorage.insertNewSkuItemMapping(item);
    }

    @DeleteMapping(path = "/item/{id}", consumes = "application/json")
    public void deleteItem(@PathVariable String id) {
        cmStorage.deleteItem(id);
    }

    @PostMapping(path = "/item/{id}", consumes = "application/json")
    public Item updateItem(@RequestBody Item item, @PathVariable String id) {
       return cmStorage.updateItem(id, item);
    }

    @GetMapping(path = "/item/{id}")
    public Item updateItem(@PathVariable("id") String id) {
        return cmStorage.getItemFromId(id);
    }

    @GetMapping(path = "/item/search/{sku}/{keyword}")
    public List<Item> updateItem(@PathVariable("sku") String sku, @PathVariable("keyword") String keyword) {
        return cmStorage.searchKeyword(sku,keyword);
    }

}
