package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class StockService {

    private String path;
    HashMap<String, Stock> stocks;

    public StockService(String path) throws FileNotFoundException {
        this.path = path;
        this.stocks = new HashMap<String, Stock>();
        getAllStocks();
    }
//
//    public String getStock(String itemName, double itemCosts, double itemPrice, int itemQt) throws FileNotFoundException, UserException {
//        for (Stock stock : getAllStocks()) {
//
//            if (stock.getItemName().equalsIgnoreCase(itemName)) {
//                return itemName;
//            }
//        }
//        throw new UserException("Neteisingi prisijungimo duomenys");
//    }




    public ArrayList<Stock> getAllStocks() throws FileNotFoundException {
        File file = new File(path);
        Scanner sc = new Scanner(file);
        ArrayList<Stock> stocks = new ArrayList<>();

        if(this.stocks.isEmpty()) {
            while (sc.hasNextLine()) {
                String itemId = sc.nextLine();
                String itemName = sc.nextLine();
                double itemCosts = sc.nextDouble();
                int itemQt = sc.nextInt();
                sc.nextLine();
                sc.nextLine();

                Stock stock = new Stock(itemId, itemName, itemCosts, itemQt);
                stocks.add(stock);
                this.stocks.put(itemId, stock);
            }
        } else {
            for (Stock s : this.stocks.values()) {
                stocks.add(s);
            }
        }
        return stocks;
    }

    public void addStock(Stock stock) throws IOException {

        if(this.stocks.containsKey(stock.getItemId())) {
            this.stocks.get(stock.getItemId()).incrementQt(stock.getItemQt());
        } else {
            this.stocks.put(stock.getItemId(), stock);
        }

        FileWriter fw = new FileWriter(path);
        PrintWriter writer = new PrintWriter(fw);
        for (Stock s : this.stocks.values()) {
            writeStock(writer, s);
        }
        writer.close();
    }

    public Stock getStockbyId(String id)
    {
        if(stocks.containsKey(id)) {
            return stocks.get(id);
        }
        return null;
    }

    private void writeStock(PrintWriter writer, Stock item) {
        writer.println(item.getItemId());
        writer.println(item.getItemName());
        writer.println(item.getItemCosts());
        writer.println(item.getItemQt());
        writer.println();
    }

}

