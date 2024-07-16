package ht.lisa.app.ui.wallet.transactionhistory;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import ht.lisa.app.R;
import ht.lisa.app.model.Phone;
import ht.lisa.app.model.response.TransactionHistoryResponse;
import ht.lisa.app.ui.wallet.BaseWalletFragment;
import ht.lisa.app.util.SharedPreferencesUtil;

public class TransactionHistoryFragment extends BaseWalletFragment implements TransactionHistoryAdapter.OnLastItemReachedListener {
    public static final String SCREEN_NAME = "TransactionHistoryScreen";

    @BindView(R.id.transaction_history_recyclerview)
    RecyclerView transactionHistoryRecyclerview;
    @BindView(R.id.transaction_history_no_transactions_layout)
    ConstraintLayout transactionHistoryNoTransactionsLayout;

    private TransactionHistoryAdapter transactionHistoryAdapter;
    private int next;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaction_history, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerview();
        walletPresenter.getTransactionsHistory(new Phone(SharedPreferencesUtil.getPhone()), next);
    }

    private void setupRecyclerview() {
        transactionHistoryRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        transactionHistoryAdapter = new TransactionHistoryAdapter(this);
        transactionHistoryRecyclerview.setAdapter(transactionHistoryAdapter);
    }

    @Override
    public void getData(Object object) {
        if (object instanceof TransactionHistoryResponse) {
            TransactionHistoryResponse transactionHistoryResponse = (TransactionHistoryResponse) object;
            next = transactionHistoryResponse.getNext();
            if (transactionHistoryResponse.getTransactionHistory() != null && transactionHistoryResponse.getTransactionHistory().size() > 0) {
                transactionHistoryRecyclerview.setVisibility(View.VISIBLE);
                transactionHistoryNoTransactionsLayout.setVisibility(View.GONE);
                transactionHistoryAdapter.addTransactionHistoryList(transactionHistoryResponse.getTransactionHistory());
            } else {
                transactionHistoryRecyclerview.setVisibility(View.GONE);
                transactionHistoryNoTransactionsLayout.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void onLastItemAttached() {
        if (next > 0) {
            walletPresenter.getTransactionsHistory(new Phone(SharedPreferencesUtil.getPhone()), next);
        }
    }
}
