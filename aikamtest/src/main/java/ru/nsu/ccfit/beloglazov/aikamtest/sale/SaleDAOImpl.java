package ru.nsu.ccfit.beloglazov.aikamtest.sale;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class SaleDAOImpl implements SaleDAO {
    private final Connection connection;

    public SaleDAOImpl(Connection connection) throws SQLException {
        this.connection = connection;
        this.connection.setAutoCommit(false);
    }

    @Override
    public void addSale(Sale sale) throws SQLException {
        int customerID = sale.getCustomerID();
        int itemID = sale.getItemID();
        Date date = sale.getDate();
        String sql = "INSERT INTO sale(customer_id, item_id, date) VALUES(?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, customerID);
        ps.setInt(2, itemID);
        ps.setDate(3, date);
        ps.executeUpdate();
        connection.commit();
        ps.close();
    }

    @Override
    public void deleteSale(int id) throws SQLException {
        String sql = "DELETE FROM sale WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        connection.commit();
        ps.close();
    }

    @Override
    public List<Sale> getAllSales() throws SQLException {
        List<Sale> sales = new LinkedList<>();
        String sql = "SELECT * FROM sale";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            int customerID = rs.getInt("customer_id");
            int itemID = rs.getInt("item_id");
            Date date = rs.getDate("date");
            Sale sale = new Sale(id, customerID, itemID, date);
            sales.add(sale);
        }
        ps.close();
        rs.close();
        return sales;
    }

    @Override
    public List<Integer> getCustomersWhichBought(int itemID, int minTimes) throws SQLException {
        List<Integer> customers = new LinkedList<>();
        String sql = "WITH T1 AS (SELECT * FROM sale WHERE item_id = ?)," +
                "T2 AS (SELECT COUNT(*) AS COUNT, customer_id FROM T1 GROUP BY customer_id)" +
                "SELECT * FROM T2 WHERE count >= ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, itemID);
        ps.setInt(2, minTimes);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int customerID = rs.getInt("customer_id");
            customers.add(customerID);
        }
        ps.close();
        rs.close();
        return customers;
    }

    @Override
    public List<Integer> getCustomersWithExpensesBetween(int minExpenses, int maxExpenses) throws SQLException {
        List<Integer> customers = new LinkedList<>();
        String sql = "WITH T1 AS (SELECT * FROM sale)," +
                "T2 AS (SELECT id, cost FROM item)," +
                "T3 AS (SELECT * FROM T1 FULL JOIN T2 ON T1.item_id = T2.id)," +
                "T4 AS (SELECT customer_id, item_id, cost FROM T3)," +
                "T5 AS (SELECT customer_id, SUM(cost) AS SUM FROM T4 GROUP BY customer_id ORDER BY customer_id)" +
                "SELECT * FROM T5 WHERE sum >= ? AND sum <= ?;";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, minExpenses);
        ps.setInt(2, maxExpenses);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int customerID = rs.getInt("customer_id");
            customers.add(customerID);
        }
        ps.close();
        rs.close();
        return customers;
    }

    @Override
    public List<Integer> getBadCustomers(int number) throws SQLException {
        List<Integer> customers = new LinkedList<>();
        String sql = "SELECT customer_id, COUNT(*) AS COUNT FROM sale GROUP BY customer_id ORDER BY COUNT;";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int customerID = rs.getInt("customer_id");
            customers.add(customerID);
        }
        ps.close();
        rs.close();
        int cSize = customers.size();
        for (int i = 1; i <= cSize - number; i++) {
            customers.remove(cSize - i);
        }
        return customers;
    }

    @Override
    public List<Sale> getSalesBetweenDates(Date date1, Date date2) throws SQLException {
        List<Sale> sales = new LinkedList<>();
        String sql = "SELECT * FROM sale WHERE date >= ? AND date <= ?;";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setDate(1, date1);
        ps.setDate(2, date2);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            int customerID = rs.getInt("customer_id");
            int itemID = rs.getInt("item_id");
            Date date = rs.getDate("date");
            Sale sale = new Sale(id, customerID, itemID, date);
            sales.add(sale);
        }
        ps.close();
        rs.close();
        return sales;
    }
}