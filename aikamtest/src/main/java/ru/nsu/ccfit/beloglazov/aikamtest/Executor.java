package ru.nsu.ccfit.beloglazov.aikamtest;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import javafx.util.Pair;
import ru.nsu.ccfit.beloglazov.aikamtest.customer.*;
import ru.nsu.ccfit.beloglazov.aikamtest.item.*;
import ru.nsu.ccfit.beloglazov.aikamtest.sale.*;
import ru.nsu.ccfit.beloglazov.aikamtest.search.*;

public class Executor implements Closeable {
    private Connection connection;
    private JsonParser parser;

    public Executor() {
        try {
            Properties properties = new Properties();
            properties.load(this.getClass().getResourceAsStream("/connection.properties"));
            String url = properties.getProperty("URL");
            String user = properties.getProperty("USER");
            String pass = properties.getProperty("PASS");
            connection = ConnectionFactory.getConnection(url, user, pass);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void execute(String command, String inputFileName, String outputFileName) {
        parser = new JsonParser(outputFileName);
        switch (command) {
            case "search":
                search(inputFileName);
                break;
            case "stat":
                stat(inputFileName);
                break;
            default:
                parser.printError("Command " + command + " is unknown");
        }
    }

    private void search(String inputFileName) {
        try {
            CustomerDAOJDBCImpl cDAO = new CustomerDAOJDBCImpl(connection);
            ItemDAOImpl iDAO = new ItemDAOImpl(connection);
            SaleDAOImpl sDAO = new SaleDAOImpl(connection);

            List<SearchArg> args = parser.parseForSearch(inputFileName);
            List<SearchResult> results = new LinkedList<>();

            for (SearchArg arg : args) {
                SearchResult result = new SearchResult();
                result.setArg(arg);
                List<Customer> customers;

                switch (arg.getType()) {
                    case CUSTOMERS_WITH_LAST_NAME:
                        customers = cDAO.getCustomersByLastName(arg.getCriteria().toString());
                        break;
                    case CUSTOMERS_WHICH_BOUGHT:
                        Pair<String, Integer> criteria1 = (Pair<String, Integer>) arg.getCriteria();
                        Item item = iDAO.getItemByName(criteria1.getKey());
                        List<Integer> cIDs1 = sDAO.getCustomersWhichBought(item.getID(), criteria1.getValue());
                        customers = new LinkedList<>();
                        for (Integer id : cIDs1) {
                            customers.add(cDAO.getCustomerByID(id));
                        }
                        break;
                    case CUSTOMERS_WITH_EXPENSES:
                        Pair<Integer, Integer> criteria2 = (Pair<Integer, Integer>) arg.getCriteria();
                        List<Integer> cIDs2 = sDAO.getCustomersWithExpensesBetween(criteria2.getKey(), criteria2.getValue());
                        customers = new LinkedList<>();
                        for (Integer id : cIDs2) {
                            customers.add(cDAO.getCustomerByID(id));
                        }
                        break;
                    case BAD_CUSTOMERS:
                        Integer number = (Integer) arg.getCriteria();
                        List<Integer> cIDs3 = sDAO.getBadCustomers(number);
                        customers = new LinkedList<>();
                        for (Integer id: cIDs3) {
                            customers.add(cDAO.getCustomerByID(id));
                        }
                        break;
                    default:
                        parser.printError("Unknown type of search argument: " + arg.getType());
                        return;
                }

                result.setCustomers(customers);
                results.add(result);
            }

            parser.printForSearch(results);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            parser.printError(e.getMessage());
        }
    }

    private void stat(String inputFileName) {
        try {
            CustomerDAOJDBCImpl cDAO = new CustomerDAOJDBCImpl(connection);
            ItemDAOImpl iDAO = new ItemDAOImpl(connection);
            SaleDAOImpl sDAO = new SaleDAOImpl(connection);

            Pair<Date, Date> args = parser.parseForStat(inputFileName);
            Date date1 = args.getKey();
            Date date2 = args.getValue();
            LocalDate ld1 = date1.toLocalDate();
            LocalDate ld2 = date2.toLocalDate();
            long days_between = ChronoUnit.DAYS.between(LocalDate.parse(ld1.toString()), LocalDate.parse(ld2.toString()));

            List<Sale> sales = sDAO.getSalesBetweenDates(date1, date2);
            Map<Customer, List<Item>> salesMap = new HashMap<>();
            for (Sale sale : sales) {
                int cID = sale.getCustomerID();
                Customer customer = cDAO.getCustomerByID(cID);
                salesMap.computeIfAbsent(customer, k -> new LinkedList<>());
                int iID = sale.getItemID();
                Item item = iDAO.getItemByID(iID);
                salesMap.get(customer).add(item);
            }
            parser.printForStat(days_between, salesMap);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            parser.printError(e.getMessage());
        }
    }

    @Override
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            parser.printError(e.getMessage());
        }
    }
}