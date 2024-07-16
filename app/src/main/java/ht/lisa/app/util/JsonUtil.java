package ht.lisa.app.util;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import ht.lisa.app.model.Ticket;

public class JsonUtil {
    public static ArrayList<Ticket> getTicketsFromJson(String json) {
        ArrayList<Ticket> ticketsArrayList = new ArrayList<>();
        try {
            Gson gson = new Gson();
            Ticket[] tickets = gson.fromJson(json, Ticket[].class);
            ticketsArrayList = new ArrayList<>(Arrays.asList(tickets));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ticketsArrayList;
    }
}
