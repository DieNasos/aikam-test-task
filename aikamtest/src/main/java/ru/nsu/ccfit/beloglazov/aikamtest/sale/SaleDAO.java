package ru.nsu.ccfit.beloglazov.aikamtest.sale;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface SaleDAO {
    void addSale(Sale sale) throws SQLException;
    void deleteSale(int id) throws SQLException;
    List<Sale> getAllSales() throws SQLException;
    List<Integer> getCustomersWhichBought(int item_id, int min_times) throws SQLException;
    List<Integer> getCustomersWithExpensesBetween(int minExpenses, int maxExpenses) throws SQLException;
    List<Integer> getBadCustomers(int number) throws SQLException;
    List<Sale> getSalesBetweenDates(Date date1, Date date2) throws SQLException;
}