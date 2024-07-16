package ht.lisa.app.model.response;

import java.util.ArrayList;

import ht.lisa.app.model.Ticket;

public class Lotto5jrSubscribeListResponse extends BaseResponse {
    private ArrayList<Ticket> dataset;

    public ArrayList<Ticket> getDataset() {
        return dataset;
    }

    public void setDataset(ArrayList<Ticket> dataset) {
        this.dataset = dataset;
    }

}
