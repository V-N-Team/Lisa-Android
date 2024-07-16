package ht.lisa.app.ui.wallet.messagecenter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ht.lisa.app.R;
import ht.lisa.app.model.Message;
import ht.lisa.app.util.DateTimeUtil;
import ht.lisa.app.util.SharedPreferencesUtil;
import ht.lisa.app.util.TextUtil;

public class MessageCenterAdapter extends RecyclerView.Adapter<MessageCenterAdapter.ViewHolder> {

    private final ArrayList<Message> messages;
    private final OnMessageClickListener onMessageClickListener;

    MessageCenterAdapter(OnMessageClickListener onMessageClickListener) {
        this.onMessageClickListener = onMessageClickListener;
        messages = new ArrayList<>();
    }

    void addMessages(List<Message> messages) {
        this.messages.clear();
        ArrayList<Message> addMessages = new ArrayList<>();
        for (Message message : messages) {
            if (message.getMsgType() != Message.MSG_TYPE_PAYMENT_SUCCESS) {
                addMessages.add(message);
            }
        }
        this.messages.addAll(addMessages);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_center, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (SharedPreferencesUtil.getReadMessages() != null && SharedPreferencesUtil.getReadMessages().contains(message.getMsgId())) {
            holder.unreadMessage.setVisibility(View.GONE);
        } else {
            holder.unreadMessage.setVisibility(View.VISIBLE);
        }

        holder.messageTitle.setText(message.getMessageTile(holder.itemView.getContext()));
        holder.messageText.setText(message.getMessageText(holder.itemView.getContext()));

        holder.messageDate.setText(DateTimeUtil.getTransactionDateFormat(String.valueOf(message.getMsgTime()), holder.itemView.getContext()));
        holder.itemView.setOnClickListener(view -> onMessageClickListener.onItemClick(message));

        holder.messageTitle.setText(TextUtil.capitalize(holder.messageTitle.getText()));
        holder.messageTrash.setOnClickListener(view -> onMessageClickListener.onDeleteClick(message.getMsgId()));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public interface OnMessageClickListener {
        void onItemClick(Message message);

        void onDeleteClick(String messageId);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.message_title)
        TextView messageTitle;
        @BindView(R.id.message_text)
        TextView messageText;
        @BindView(R.id.message_date)
        TextView messageDate;
        @BindView(R.id.message_trash)
        FrameLayout messageTrash;
        @BindView(R.id.unread_message)
        View unreadMessage;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}