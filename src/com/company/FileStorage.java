package com.company;

import java.io.*;
import java.util.Scanner;

public class FileStorage implements Storage {
    private File file;
    private Scanner reader;
    private PrintWriter writer;

    @Override
    public void open(String path) throws IOException {
        file = new File(path);
        reader = new Scanner(file);
        writer = new PrintWriter(file);
    }

    @Override
    public String readAsString() {
        return reader.nextLine();
    }

    @Override
    public Integer readAsInteger() {
        return reader.nextInt();
    }

    @Override
    public Double readAsDouble() {
        return reader.nextDouble();
    }

    @Override
    public void setAsString(String val) {
        writer.println(val);
    }

    @Override
    public void setAsInteger(int val) {
        writer.println(val);
    }

    @Override
    public void setAsDouble(double val) {
        writer.println(val);
    }

    @Override
    public Boolean hasData() {
        return sc.hasNextLine();
    }

    @Override
    public void close() {
        sc.close();
    }
}
