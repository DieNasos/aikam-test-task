package ru.nsu.ccfit.beloglazov.aikamtest.item;

import java.sql.*;
import java.util.LinkedList;

public class ItemDAOImpl implements ItemDAO {
    private final Connection connection;

    public ItemDAOImpl(Connection connection) throws SQLException {
        this.connection = connection;
        this.connection.setAutoCommit(false);
    }

    @Override
    public void addItem(Item item) throws SQLException {
        String name = item.getName();
        int cost = item.getCost();
        String sql = "INSERT INTO item(name, cost) VALUES(?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, name);
        ps.setInt(2, cost);
        ps.executeUpdate();
        connection.commit();
        ps.close();
    }

    @Override
    public void deleteItem(int id) throws SQLException {
        String sql = "DELETE FROM item WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        connection.commit();
        ps.close();
    }

    @Override
    public LinkedList<Item> getAllItems() throws SQLException {
        LinkedList<Item> items = new LinkedList<>();
        String sql = "SELECT * FROM item";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int cost = rs.getInt("cost");
            Item item = new Item(id, name, cost);
            items.add(item);
        }
        ps.close();
        rs.close();
        return items;
    }

    @Override
    public Item getItemByID(int id) throws SQLException {
        Item item = null;
        String sql = "SELECT * FROM item WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String name = rs.getString("name");
            int cost = rs.getInt("cost");
            item = new Item(id, name, cost);
        }
        ps.close();
        rs.close();
        return item;
    }

    @Override
    public Item getItemByName(String name) throws SQLException {
        Item item = null;
        String sql = "SELECT * FROM item WHERE name = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            int cost = rs.getInt("cost");
            item = new Item(id, name, cost);
        }
        ps.close();
        rs.close();
        return item;
    }
}