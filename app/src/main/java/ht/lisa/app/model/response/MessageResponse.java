package ht.lisa.app.model.response;

import java.util.List;

import ht.lisa.app.model.Message;

public class MessageResponse extends BaseResponse {

    private List<Message> dataset;

    public List<Message> getDataset() {
        return dataset;
    }
}
