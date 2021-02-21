package ru.nsu.ccfit.beloglazov.aikamtest.item;

import java.sql.SQLException;
import java.util.List;

public interface ItemDAO {
    void addItem(Item item) throws SQLException;
    void deleteItem(int id) throws SQLException;
    List<Item> getAllItems() throws SQLException;
    Item getItemByID(int id) throws SQLException;
    Item getItemByName(String name) throws SQLException;
}