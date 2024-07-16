package ht.lisa.app.ui.wallet.transactionhistory;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ht.lisa.app.R;
import ht.lisa.app.model.Transaction;
import ht.lisa.app.util.DateTimeUtil;
import ht.lisa.app.util.TextUtil;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder> {

    private ArrayList<Transaction> transactionHistories;
    private OnLastItemReachedListener onLastItemAttachedListener;

    TransactionHistoryAdapter(OnLastItemReachedListener onLastItemAttachedListener) {
        transactionHistories = new ArrayList<>();
        this.onLastItemAttachedListener = onLastItemAttachedListener;
    }

    void addTransactionHistoryList(List<Transaction> transactionHistories) {
        this.transactionHistories.addAll(transactionHistories);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactionHistories.get(position);
        holder.transactionHistoryName.setText(Transaction.getTransactionMessage(holder.itemView.getContext(), transaction.getType()));
        holder.transactionHistoryDate.setText(DateTimeUtil.getTransactionDateFormat(transaction.getDate(), holder.itemView.getContext()));
        Log.d("WOROEF", DateTimeUtil.getTransactionDateFormat(transaction.getDate(), holder.itemView.getContext()) + "\n" + transaction.getType() + "\n" + transaction.getId() + "\n pos: " + position);
        holder.transactionHistoryAmount.setText(String.format(holder.itemView.getContext().getString(R.string.G), (transaction.getAmount() > 0 ? "+" : ""), TextUtil.getDecimalString(transaction.getAmount())));
        holder.transactionHistoryIcon.setImageResource(transaction.getAmount() > 0 ? R.drawable.ic_top_up : R.drawable.ic_cash_out_grey);
        holder.transactionHistoryAmount.setTextColor(holder.itemView.getContext().getResources().getColor(transaction.getAmount() > 0 ? R.color.accent : R.color.grey));
        if (onLastItemAttachedListener != null) {
            if (position == getItemCount() - 1) {
                onLastItemAttachedListener.onLastItemAttached();
            }
        }
    }

    @Override
    public int getItemCount() {
        return transactionHistories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.transaction_history_name)
        TextView transactionHistoryName;
        @BindView(R.id.transaction_history_date)
        TextView transactionHistoryDate;
        @BindView(R.id.transaction_history_amount)
        TextView transactionHistoryAmount;
        @BindView(R.id.transaction_history_icon)
        ImageView transactionHistoryIcon;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnLastItemReachedListener {
        void onLastItemAttached();
    }
}