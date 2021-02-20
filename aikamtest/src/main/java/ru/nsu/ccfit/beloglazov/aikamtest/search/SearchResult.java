package ru.nsu.ccfit.beloglazov.aikamtest.search;

import java.util.LinkedList;
import ru.nsu.ccfit.beloglazov.aikamtest.customer.Customer;

public class SearchResult {
    private SearchArg arg;
    private LinkedList<Customer> customers;

    public SearchArg getArg() { return arg; }
    public LinkedList<Customer> getCustomers() { return customers; }

    public void setArg(SearchArg arg) { this.arg = arg; }
    public void setCustomers(LinkedList<Customer> customers) { this.customers = customers; }
}