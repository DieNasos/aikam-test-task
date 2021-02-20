package ru.nsu.ccfit.beloglazov.aikamtest.sale;

import java.sql.Date;
import java.sql.SQLException;
import java.util.LinkedList;

public interface SaleDAO {
    void addSale(Sale sale) throws SQLException;
    void deleteSale(int id) throws SQLException;
    LinkedList<Sale> getAllSales() throws SQLException;
    LinkedList<Integer> getCustomersWhichBought(int item_id, int min_times) throws SQLException;
    LinkedList<Integer> getCustomersWithExpensesBetween(int minExpenses, int maxExpenses) throws SQLException;
    LinkedList<Integer> getBadCustomers(int number) throws SQLException;
    LinkedList<Sale> getSalesBetweenDates(Date date1, Date date2) throws SQLException;
}