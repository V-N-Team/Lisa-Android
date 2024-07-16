package ht.lisa.app.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ht.lisa.app.model.Transaction;

public class TransactionHistoryResponse extends BaseResponse {

    private int next;
    @SerializedName("dataset")
    private List<Transaction> transactionHistory;

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public int getNext() {
        return next;
    }
}
