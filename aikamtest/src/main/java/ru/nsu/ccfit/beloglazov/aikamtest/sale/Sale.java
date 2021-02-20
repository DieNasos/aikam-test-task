package ru.nsu.ccfit.beloglazov.aikamtest.sale;

import java.sql.Date;

public class Sale {
    private int id;
    private int customerID;
    private int itemID;
    private Date date;

    public Sale(int id, int customerID, int itemID, Date date) {
        this.id = id;
        this.customerID = customerID;
        this.itemID = itemID;
        this.date = date;
    }

    public Sale(int customer_id, int item_id, Date date) {
        this(-1, customer_id, item_id, date);
    }

    public int getID() { return id; }
    public int getCustomerID() { return customerID; }
    public int getItemID() { return itemID; }
    public Date getDate() { return date; }

    public void setID(int id) { this.id = id; }
    public void setCustomerID(int customerID) { this.customerID = customerID; }
    public void setItemID(int item_id) { this.itemID = item_id; }
    public void setDate(Date date) { this.date = date; }

    @Override
    public String toString() {
        return "Sale{id = " + id + ", customerID = " + customerID + ", itemID = " + itemID +
                ", date = " + date + '}';
    }
}