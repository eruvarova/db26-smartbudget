package com.smartbudget.model;

public class Category {

    private int categoryId;
    private String name;
    private String type;          // 'INCOME' or 'EXPENSE'

    public Category() { }

    public Category(int categoryId, String name, String type) {
        this.categoryId = categoryId;
        this.name       = name;
        setType(type);            // run validation in the constructor too
    }

    public int getCategoryId()           { return categoryId; }
    public void setCategoryId(int id)    { this.categoryId = id; }

    public String getName()              { return name; }
    public void setName(String name)     { this.name = name; }

    public String getType()              { return type; }
    public void setType(String type) {
        if (!"INCOME".equals(type) && !"EXPENSE".equals(type)) {
            throw new IllegalArgumentException(
                    "type must be 'INCOME' or 'EXPENSE', got: " + type);
        }
        this.type = type;
    }

    @Override
    public String toString() {
        return "Category{id=" + categoryId
                + ", name='" + name + '\''
                + ", type='" + type + "'}";
    }
}