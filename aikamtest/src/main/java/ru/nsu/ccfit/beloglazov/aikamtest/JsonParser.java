package ru.nsu.ccfit.beloglazov.aikamtest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.*;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.nsu.ccfit.beloglazov.aikamtest.customer.Customer;
import ru.nsu.ccfit.beloglazov.aikamtest.item.Item;
import ru.nsu.ccfit.beloglazov.aikamtest.search.*;

public class JsonParser {
    private final String outputFileName;

    public JsonParser(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public List<SearchArg> parseForSearch(String fileName) throws IOException {
        JSONObject obj = getJsonObjectFromFile(fileName);
        JSONArray criterias = obj.getJSONArray("criterias");
        List<SearchArg> args = new LinkedList<>();

        for (int i = 0; i < criterias.length(); i++) {
            JSONObject criteria = criterias.getJSONObject(i);
            SearchArg searchArg = new SearchArg();

            if (criteria.has("lastName")) {
                searchArg.setType(SearchArgType.CUSTOMERS_WITH_LAST_NAME);
                searchArg.setCriteria(criteria.get("lastName"));
            } else if (criteria.has("productName")) {
                String productName = criteria.getString("productName");
                int minTimes = criteria.getInt("minTimes");
                searchArg.setType(SearchArgType.CUSTOMERS_WHICH_BOUGHT);
                searchArg.setCriteria(new Pair<>(productName, minTimes));
            } else if (criteria.has("minExpenses")) {
                int min = criteria.getInt("minExpenses");
                int max = criteria.getInt("maxExpenses");
                searchArg.setType(SearchArgType.CUSTOMERS_WITH_EXPENSES);
                searchArg.setCriteria(new Pair<>(min, max));
            } else if (criteria.has("badCustomers")) {
                searchArg.setType(SearchArgType.BAD_CUSTOMERS);
                searchArg.setCriteria(criteria.getInt("badCustomers"));
            }

            args.add(searchArg);
        }

        return args;
    }

    public Pair<Date, Date> parseForStat(String fileName) throws IOException {
        JSONObject obj = getJsonObjectFromFile(fileName);
        Date date1 = Date.valueOf(obj.getString("startDate"));
        Date date2 = Date.valueOf(obj.getString("endDate"));
        return new Pair<>(date1, date2);
    }

    public void printForSearch(List<SearchResult> results) {
        JSONArray jAllResults = new JSONArray();

        for (SearchResult result : results) {
            SearchArg arg = result.getArg();

            switch (arg.getType()) {
                case CUSTOMERS_WITH_LAST_NAME:
                    String lastName = arg.getCriteria().toString();
                    JSONObject jCriteria1 = new JSONObject();
                    jCriteria1.put("lastName", lastName);
                    JSONArray jResults1Arr = new JSONArray();
                    for (Customer customer : result.getCustomers()) {
                        JSONObject ln_fn = new JSONObject();
                        ln_fn.put("lastName", customer.getLastName());
                        ln_fn.put("firstName", customer.getFirstName());
                        jResults1Arr.put(ln_fn);
                    }
                    JSONObject jResults1 = new JSONObject();
                    jResults1.put("criteria", jCriteria1);
                    jResults1.put("results", jResults1Arr);
                    jAllResults.put(jResults1);
                    break;
                case CUSTOMERS_WHICH_BOUGHT:
                    Pair<Integer, Integer> criteria1 = (Pair<Integer, Integer>) arg.getCriteria();
                    JSONObject jCriteria2 = new JSONObject();
                    jCriteria2.put("productName", criteria1.getKey());
                    jCriteria2.put("minTimes", criteria1.getValue());
                    JSONArray jResults2Arr = new JSONArray();
                    for (Customer customer : result.getCustomers()) {
                        JSONObject ln_fn = new JSONObject();
                        ln_fn.put("lastName", customer.getLastName());
                        ln_fn.put("firstName", customer.getFirstName());
                        jResults2Arr.put(ln_fn);
                    }
                    JSONObject jResults2 = new JSONObject();
                    jResults2.put("criteria", jCriteria2);
                    jResults2.put("results", jResults2Arr);
                    jAllResults.put(jResults2);
                    break;
                case CUSTOMERS_WITH_EXPENSES:
                    Pair<Integer, Integer> criteria2 = (Pair<Integer, Integer>) arg.getCriteria();
                    JSONObject jCriteria3 = new JSONObject();
                    jCriteria3.put("minExpenses", criteria2.getKey());
                    jCriteria3.put("maxExpenses", criteria2.getValue());
                    JSONArray jResults3Arr = new JSONArray();
                    for (Customer customer : result.getCustomers()) {
                        JSONObject ln_fn = new JSONObject();
                        ln_fn.put("lastName", customer.getLastName());
                        ln_fn.put("firstName", customer.getFirstName());
                        jResults3Arr.put(ln_fn);
                    }
                    JSONObject jResults3 = new JSONObject();
                    jResults3.put("criteria", jCriteria3);
                    jResults3.put("results", jResults3Arr);
                    jAllResults.put(jResults3);
                    break;
                case BAD_CUSTOMERS:
                    int number = (Integer) arg.getCriteria();
                    JSONObject jCriteria4 = new JSONObject();
                    jCriteria4.put("badCustomers", number);
                    JSONArray jResults4Arr = new JSONArray();
                    for (Customer customer : result.getCustomers()) {
                        JSONObject ln_fn = new JSONObject();
                        ln_fn.put("lastName", customer.getLastName());
                        ln_fn.put("firstName", customer.getFirstName());
                        jResults4Arr.put(ln_fn);
                    }
                    JSONObject jResults4 = new JSONObject();
                    jResults4.put("criteria", jCriteria4);
                    jResults4.put("results", jResults4Arr);
                    jAllResults.put(jResults4);
                    break;
                default:
                    printError("Unknown type of search argument: " + result.getArg().getType());
                    return;
            }
        }

        JSONObject jObj = new JSONObject();
        jObj.put("type", "search");
        jObj.put("results", jAllResults);
        printJsonObjectInFile(jObj);
    }

    public void printForStat(long daysBetween, Map<Customer, List<Item>> salesMap) {
        JSONArray jAllCustomers = new JSONArray();
        int totalExpenses = 0;

        for (Customer customer : salesMap.keySet()) {
            JSONObject jCustomer = new JSONObject();
            jCustomer.put("name", customer.getLastName() + " " + customer.getFirstName());
            JSONArray jPurchases = new JSONArray();
            int totalExpensesForCurrent = 0;

            for (Item item : salesMap.get(customer)) {
                JSONObject jItem = new JSONObject();
                jItem.put("name", item.getName());
                jItem.put("expenses", item.getCost());
                jPurchases.put(jItem);
                totalExpensesForCurrent += item.getCost();
            }

            jCustomer.put("purchases", jPurchases);
            jCustomer.put("totalExpenses", totalExpensesForCurrent);
            jAllCustomers.put(jCustomer);
            totalExpenses += totalExpensesForCurrent;
        }

        JSONObject jObj = new JSONObject();
        jObj.put("type", "stat");
        jObj.put("totalDays", daysBetween);
        jObj.put("customers", jAllCustomers);
        jObj.put("totalExpenses", totalExpenses);
        jObj.put("avgExpenses", (double) totalExpenses / (double) salesMap.size());
        printJsonObjectInFile(jObj);
    }

    public void printError(String message) {
        JSONObject jObj = new JSONObject();
        jObj.put("type", "error");
        jObj.put("message", message);
        printJsonObjectInFile(jObj);
    }

    private JSONObject getJsonObjectFromFile(String fileName) throws IOException {
        String jsonString = new String(Files.readAllBytes(Paths.get(fileName)));
        return new JSONObject(jsonString);
    }

    private void printJsonObjectInFile(JSONObject jObj) {
        try(FileOutputStream fos = new FileOutputStream(outputFileName)) {
            fos.write(jObj.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}