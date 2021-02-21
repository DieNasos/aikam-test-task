package ru.nsu.ccfit.beloglazov.aikamtest.customer;

import java.sql.SQLException;
import java.util.List;

public interface CustomerDAO {
    void addCustomer(Customer customer) throws SQLException;
    void deleteCustomer(int id) throws SQLException;
    List<Customer> getAllCustomers() throws SQLException;
    Customer getCustomerByID(int id) throws SQLException;
    List<Customer> getCustomersByLastName(String lastName) throws SQLException;
}