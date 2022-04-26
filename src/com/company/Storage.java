package com.company;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Storage {
    void open(String path) throws IOException;
    String readAsString();
    Integer readAsInteger();
    Double readAsDouble();
    void setAsString(String val);
    void setAsInteger(int val);
    void setAsDouble(double val);
    Boolean hasData();
    void reset();
    void close();
}
