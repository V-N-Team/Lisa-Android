package ht.lisa.app.ui.gamesubscription;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ht.lisa.app.R;
import ht.lisa.app.model.Ticket;

public class GamesSubscriptionTicketAdapter extends RecyclerView.Adapter<GamesSubscriptionTicketAdapter.ViewHolder> {

    private ArrayList<Ticket> tickets;
    private OnItemClickListener onItemClickListener;
    private String name;

    public GamesSubscriptionTicketAdapter(ArrayList<Ticket> tickets, OnItemClickListener onItemClickListener, String game) {
        this.tickets = tickets == null ? new ArrayList<>() : tickets;
        this.onItemClickListener = onItemClickListener;
        this.name = game;
    }

    @NonNull
    @Override
    public GamesSubscriptionTicketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GamesSubscriptionTicketAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_subscription_numbers, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GamesSubscriptionTicketAdapter.ViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);
        holder.number1.setText(ticket.getDrawArray().length > 0 ? ticket.getDrawArray()[0] : "");
        holder.number2.setText(ticket.getDrawArray().length > 1 ? ticket.getDrawArray()[1] : "");
        holder.number2.setVisibility(ticket.getDrawArray().length > 1 ? View.VISIBLE : View.GONE);
        holder.number3.setText(ticket.getDrawArray().length == 3 ? ticket.getDrawArray()[2] : "");
        holder.number3.setVisibility(ticket.getDrawArray().length == 3 ? View.VISIBLE : View.GONE);
        holder.gameSubscriptionCheckbox.setBackground(AppCompatResources.getDrawable(holder.itemView.getContext(), R.drawable.check_box_button_selected));

        holder.number1.setTextSize(holder.number1.length() == 3 ? 10.5f : 13.5f);
        holder.number1.setBackground(AppCompatResources.getDrawable(holder.itemView.getContext(), R.drawable.white_circle));
        holder.number2.setBackground(AppCompatResources.getDrawable(holder.itemView.getContext(), R.drawable.white_circle));
        holder.number3.setBackground(AppCompatResources.getDrawable(holder.itemView.getContext(), R.drawable.white_circle));

        holder.ticketDrawType.setText(ticket.getRegion());
        holder.ticketDrawType.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),
                ticket.getRegion().equals("NY") ? R.color.drawDay : R.color.drawNight));

        holder.gameSubscriptionCheckbox.setOnClickListener(v ->
                holder.showUnsubscribeDialog(holder.itemView.getContext(), ticket));

        holder.gameSubscriptionCheckbox.setOnCheckedChangeListener((buttonView, isChecked) ->
                holder.gameSubscriptionCheckbox.setBackground(AppCompatResources.getDrawable(holder.itemView.getContext(),
                        isChecked ? R.drawable.check_box_button_selected : R.drawable.check_box_button_unselected)));

        if (position % 2 == 0) {
            holder.gameSubscriptionNumbersCardview.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.defaultBgHalfTransparent));
        } else {
            holder.gameSubscriptionNumbersCardview.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.transparent));
        }

    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.game_subscription_number_1)
        TextView number1;
        @BindView(R.id.game_subscription_number_2)
        TextView number2;
        @BindView(R.id.ticket_draw_type)
        TextView ticketDrawType;
        @BindView(R.id.game_subscription_number_3)
        TextView number3;
        @BindView(R.id.game_subscription_checkbox)
        CheckBox gameSubscriptionCheckbox;
        @BindView(R.id.game_subscription_numbers_cardview)
        CardView gameSubscriptionNumbersCardview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int height = gameSubscriptionCheckbox.getMeasuredHeight();
            gameSubscriptionCheckbox.setWidth(height);
        }

        private void showUnsubscribeDialog(Context context, Ticket ticket) {
            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setMessage(context.getText(R.string.are_you_sure_you_want_unsubscribe))
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        gameSubscriptionCheckbox.setChecked(false);
                        onUnsubscribeClick(ticket);
                    })
                    .setNegativeButton(R.string.no, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        gameSubscriptionCheckbox.setChecked(true);
                    })
                    .setOnDismissListener(dialog ->
                            gameSubscriptionCheckbox.setChecked(true))
                    .show();
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GREEN);
        }
    }


    private void onUnsubscribeClick(Ticket ticket) {
        ticket.setName(name);
        onItemClickListener.onItemClick(ticket);
        notifyItemRemoved(tickets.indexOf(ticket));
        notifyItemRangeChanged(tickets.indexOf(ticket), tickets.size());
        tickets.remove(ticket);
    }

    public interface OnItemClickListener {
        void onItemClick(Ticket ticket);
    }
}
