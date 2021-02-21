package ru.nsu.ccfit.beloglazov.aikamtest.search;

import java.util.List;
import ru.nsu.ccfit.beloglazov.aikamtest.customer.Customer;

public class SearchResult {
    private SearchArg arg;
    private List<Customer> customers;

    public SearchArg getArg() { return arg; }
    public List<Customer> getCustomers() { return customers; }

    public void setArg(SearchArg arg) { this.arg = arg; }
    public void setCustomers(List<Customer> customers) { this.customers = customers; }
}