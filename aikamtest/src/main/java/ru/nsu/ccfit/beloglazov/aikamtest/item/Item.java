package ru.nsu.ccfit.beloglazov.aikamtest.item;

public class Item {
    private int id;
    private String name;
    private int cost;

    public Item(int id, String name, int cost) {
        this.id = id;
        this.name = name;
        this.cost = cost;
    }

    public Item(String name, int cost) {
        this(-1, name, cost);
    }

    public int getID() { return id; }
    public String getName() { return name; }
    public int getCost() { return cost; }

    public void setID(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCost(int cost) { this.cost = cost; }

    @Override
    public String toString() {
        return "Item{id = " + id + ", name = " + name + ", cost = " + cost + '}';
    }
}