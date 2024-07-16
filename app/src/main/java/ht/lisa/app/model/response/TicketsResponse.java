package ht.lisa.app.model.response;

import java.util.ArrayList;

import ht.lisa.app.model.Ticket;

public class TicketsResponse extends BaseResponse {
    private int next;
    private ArrayList<Ticket> dataset;

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public ArrayList<Ticket> getDataset() {
        return dataset;
    }

    public void setDataset(ArrayList<Ticket> dataset) {
        this.dataset = dataset;
    }
}
