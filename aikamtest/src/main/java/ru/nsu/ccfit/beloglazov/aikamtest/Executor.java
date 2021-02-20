package ru.nsu.ccfit.beloglazov.aikamtest;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedList;
import javafx.util.Pair;
import ru.nsu.ccfit.beloglazov.aikamtest.customer.*;
import ru.nsu.ccfit.beloglazov.aikamtest.item.*;
import ru.nsu.ccfit.beloglazov.aikamtest.sale.*;
import ru.nsu.ccfit.beloglazov.aikamtest.search.*;

public class Executor {
    private Connection connection;
    private final JsonParser parser;
    private final String outputFileName;

    public Executor(String url, String user, String pass, String outputFileName) {
        parser = new JsonParser();
        this.outputFileName = outputFileName;
        try {
            connection = ConnectionFactory.getConnection(url, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
            parser.printError(e.getMessage(), outputFileName);
        }
    }

    public void execute(String command, String inputFileName) {
        switch (command) {
            case "search":
                search(inputFileName);
                break;
            case "stat":
                stat(inputFileName);
                break;
            default:
                parser.printError("Command " + command + " is unknown", outputFileName);
        }
    }

    private void search(String inputFileName) {
        try {
            CustomerDAOJDBCImpl c_dao = new CustomerDAOJDBCImpl(connection);
            ItemDAOImpl i_dao = new ItemDAOImpl(connection);
            SaleDAOImpl s_dao = new SaleDAOImpl(connection);

            LinkedList<SearchArg> args = parser.parseForSearch(inputFileName);
            LinkedList<SearchResult> results = new LinkedList<>();

            for (SearchArg arg : args) {
                SearchResult result = new SearchResult();
                result.setArg(arg);
                LinkedList<Customer> customers;

                switch (arg.getType()) {
                    case CUSTOMERS_WITH_LAST_NAME:
                        customers = c_dao.getCustomersByLastName(arg.getCriteria().toString());
                        break;
                    case CUSTOMERS_WHICH_BOUGHT:
                        Pair<String, Integer> criteria_1 = (Pair<String, Integer>) arg.getCriteria();
                        Item item = i_dao.getItemByName(criteria_1.getKey());
                        LinkedList<Integer> c_ids_1 = s_dao.getCustomersWhichBought(item.getID(), criteria_1.getValue());
                        customers = new LinkedList<>();
                        for (Integer id : c_ids_1) {
                            customers.add(c_dao.getCustomerByID(id));
                        }
                        break;
                    case CUSTOMERS_WITH_EXPENSES:
                        Pair<Integer, Integer> criteria_2 = (Pair<Integer, Integer>) arg.getCriteria();
                        LinkedList<Integer> c_ids_2 = s_dao.getCustomersWithExpensesBetween(criteria_2.getKey(), criteria_2.getValue());
                        customers = new LinkedList<>();
                        for (Integer id : c_ids_2) {
                            customers.add(c_dao.getCustomerByID(id));
                        }
                        break;
                    case BAD_CUSTOMERS:
                        Integer number = (Integer) arg.getCriteria();
                        LinkedList<Integer> c_ids_3 = s_dao.getBadCustomers(number);
                        customers = new LinkedList<>();
                        for (Integer id: c_ids_3) {
                            customers.add(c_dao.getCustomerByID(id));
                        }
                        break;
                    default:
                        parser.printError("Unknown type of search argument: " + arg.getType(), outputFileName);
                        return;
                }

                result.setCustomers(customers);
                results.add(result);
            }

            parser.printForSearch(results, outputFileName);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            parser.printError(e.getMessage(), outputFileName);
        }
    }

    private void stat(String inputFileName) {
        try {
            CustomerDAOJDBCImpl c_dao = new CustomerDAOJDBCImpl(connection);
            ItemDAOImpl i_dao = new ItemDAOImpl(connection);
            SaleDAOImpl s_dao = new SaleDAOImpl(connection);

            Pair<Date, Date> args = parser.parseForStat(inputFileName);
            Date date1 = args.getKey();
            Date date2 = args.getValue();
            LocalDate ld1 = date1.toLocalDate();
            LocalDate ld2 = date2.toLocalDate();
            long days_between = ChronoUnit.DAYS.between(LocalDate.parse(ld1.toString()), LocalDate.parse(ld2.toString()));

            LinkedList<Sale> sales = s_dao.getSalesBetweenDates(date1, date2);
            HashMap<Customer, LinkedList<Item>> salesMap = new HashMap<>();
            for (Sale sale : sales) {
                int c_id = sale.getCustomerID();
                Customer customer = c_dao.getCustomerByID(c_id);
                if (salesMap.get(customer) == null) {
                    salesMap.put(customer, new LinkedList<>());
                }
                int i_id = sale.getItemID();
                Item item = i_dao.getItemByID(i_id);
                salesMap.get(customer).add(item);
            }
            parser.printForStat(days_between, salesMap, outputFileName);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            parser.printError(e.getMessage(), outputFileName);
        }
    }

    public void finish() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            parser.printError(e.getMessage(), outputFileName);
        }
    }
}