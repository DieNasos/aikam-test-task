package ru.nsu.ccfit.beloglazov.aikamtest.search;

public class SearchArg {
    private SearchArgType type;
    private Object criteria;

    public SearchArgType getType() { return type; }
    public Object getCriteria() { return criteria; }

    public void setType(SearchArgType type) { this.type = type; }
    public void setCriteria(Object criteria) { this.criteria = criteria; }
}