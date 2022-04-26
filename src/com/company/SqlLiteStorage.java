package com.company;

import java.io.FileNotFoundException;

public class SqlLiteStorage implements Storage {
    @Override
    public void open(String path) throws FileNotFoundException {

    }

    @Override
    public String readAsString() {
        return null;
    }

    @Override
    public Integer readAsInteger() {
        return null;
    }

    @Override
    public Double readAsDouble() {
        return null;
    }

    @Override
    public Boolean hasData() {
        return false;
    }

    @Override
    public void close() {

    }
}
