package ru.nsu.ccfit.beloglazov.aikamtest.customer;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class CustomerDAOJDBCImpl implements CustomerDAO {
    private final Connection connection;

    public CustomerDAOJDBCImpl(Connection connection) throws SQLException {
        this.connection = connection;
        this.connection.setAutoCommit(false);
    }

    @Override
    public void addCustomer(Customer customer) throws SQLException {
        String firstName = customer.getFirstName();
        String lastName = customer.getLastName();
        String sql = "INSERT INTO customer(first_name, last_name) VALUES(?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, firstName);
        ps.setString(2, lastName);
        ps.executeUpdate();
        connection.commit();
        ps.close();
    }

    @Override
    public void deleteCustomer(int id) throws SQLException {
        String sql = "DELETE FROM customer WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        connection.commit();
        ps.close();
    }

    @Override
    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new LinkedList<>();
        String sql = "SELECT * FROM customer";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            Customer customer = new Customer(id, firstName, lastName);
            customers.add(customer);
        }
        ps.close();
        rs.close();
        return customers;
    }

    @Override
    public Customer getCustomerByID(int id) throws SQLException {
        Customer customer = null;
        String sql = "SELECT * FROM customer WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            customer = new Customer(id, firstName, lastName);
        }
        ps.close();
        rs.close();
        return customer;
    }

    @Override
    public List<Customer> getCustomersByLastName(String lastName) throws SQLException {
        List<Customer> customers = new LinkedList<>();
        String sql = "SELECT * FROM customer WHERE last_name = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, lastName);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String firstName = rs.getString("first_name");
            Customer customer = new Customer(id, firstName, lastName);
            customers.add(customer);
        }
        ps.close();
        rs.close();
        return customers;
    }
}