package com.paypalsku.paypalsku.View;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Item {

    private String id;
    private String title;
    private String brand;
    private String color;
    private String size;
    private String category;

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id=id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}
