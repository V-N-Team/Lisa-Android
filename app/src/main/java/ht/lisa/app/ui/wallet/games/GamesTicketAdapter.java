package ht.lisa.app.ui.wallet.games;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ht.lisa.app.R;
import ht.lisa.app.model.Draw;
import ht.lisa.app.model.Ticket;
import ht.lisa.app.util.DateTimeUtil;

public class GamesTicketAdapter extends RecyclerView.Adapter<GamesTicketAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Ticket> tickets;
    private final OnTicketElementClickListener onTicketElementClickListener;

    GamesTicketAdapter(Context context, ArrayList<Ticket> tickets, OnTicketElementClickListener onTicketElementClickListener) {
        this.tickets = tickets;
        this.onTicketElementClickListener = onTicketElementClickListener;
        this.context = context;
    }

    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
        notifyDataSetChanged();
    }

    public void unsubscribeTicket(Ticket ticket) {
        tickets.get(tickets.indexOf(ticket)).setSubscribe(false);
        notifyDataSetChanged();
    }

    public void removeAllTickets() {
        if (tickets != null && !tickets.isEmpty()) {
            tickets.clear();
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_ticket_2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);
        String gameName = ticket.getName();
        if (gameName == null || gameName.isEmpty()) return;
        Drawable numberBall = AppCompatResources.getDrawable(holder.itemView.getContext(), Draw.getTicketBall(ticket.getName() == null ? "" : ticket.getName()));

        holder.gamesTicketName.setText((position == 0 ? (gameName.equals(Draw.MARIAGE) ? context.getString(R.string.maryage) : gameName.toLowerCase().equals(Draw.LOTTO5P5) ? context.getString(R.string.lotto5jr) : gameName) : ""));
        holder.gamesTicketName.setVisibility(position == 0 ? View.VISIBLE : View.GONE);

        holder.gameTicketSubscribe.setChecked(ticket.isSubscribe());
        holder.gamesTicketNameBottom.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        holder.gamesTicketBottom.setVisibility(position == tickets.size() - 1 ? View.VISIBLE : View.GONE);
        holder.gameTicketCmbCard.setVisibility(gameName.equals(Draw.BOLOTO) || ticket.getType() > 0 ? View.VISIBLE : View.GONE);
        holder.gameTicketSubscribe.setVisibility((gameName.equals(Draw.LOTTO3) || gameName.equals(Draw.LOTTO5JR) || gameName.equals(Draw.LOTTO5JR_NEW) || gameName.equals(Draw.LOTTO5P5) || gameName.equals(Draw.BOLOTO) || gameName.equals(Draw.ROYAL5)) && ticket.getNum() != null ? View.VISIBLE : View.GONE);

        int endFirstNumber = ticket.getName().toLowerCase().equals(Draw.LOTTO5P5) || ticket.getName().equals(Draw.LOTTO5JR) || ticket.getName().equals(Draw.LOTTO3) || ticket.getName().equals(Draw.LOTTO5) || ticket.getName().equals(Draw.ROYAL5) ? 3 : 2;
        holder.gameTicketFirstNumber.setText(ticket.getNum() == null ? context.getString(R.string.qp) : ticket.getNum().substring(0, endFirstNumber));
        changeLottoSize(holder.gameTicketFirstNumber, numberBall);
        if (!gameName.equals(Draw.BOLET) && !gameName.equals(Draw.LOTTO3)) {
            holder.gameTicketSecondNumber.setText(ticket.getNum() == null ? context.getString(R.string.qp) : ticket.getNum().substring(endFirstNumber, endFirstNumber + 2));
            holder.gameTicketSecondNumber.setVisibility(View.VISIBLE);
            changeLottoSize(holder.gameTicketSecondNumber, numberBall);
        } else {
            holder.gameTicketSecondNumber.setVisibility(View.INVISIBLE);
        }
        if (gameName.equals(Draw.BOLOTO) || (gameName.equals(Draw.MARIAGE) && ticket.isCombo())) {
            holder.gameTicketThirdNumber.setText(ticket.getNum() == null ? context.getString(R.string.qp) : ticket.getNum().substring(4));
            holder.gameTicketThirdNumber.setVisibility(View.VISIBLE);

            changeLottoSize(holder.gameTicketThirdNumber, numberBall);
        } else {
            holder.gameTicketThirdNumber.setVisibility(View.INVISIBLE);
        }
        holder.ticketDrawType.setText(ticket.isNy() ? R.string.ny :
                ticket.isFl() ? R.string.fl : R.string.ga);
        holder.ticketDrawType.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), DateTimeUtil.isDayDraw(ticket.getDraw(), holder.itemView.getContext()) ? R.color.drawDay : R.color.drawNight));

        holder.gameTicketCost.setText(String.format(context.getString(R.string.HTG_d), ticket.getComboCost()));
        holder.gameTicketCmbLayout.setVisibility(ticket.isCombo() || ticket.getType() > 0 ? View.VISIBLE : View.GONE);
        holder.gameTicketCmbText.setText(ticket.isCombo() ? context.getString(R.string.cmb) : context.getString(R.string.opt));
        holder.gameTicketCmbCount.setText(ticket.getType() > 0 ? String.valueOf(ticket.getType()) : gameName.equals(Draw.BOLOTO) ? context.getString(R.string._6) : "");
        holder.gameTicketDelete.setOnClickListener(v -> onTicketElementClickListener.onDeleteClick(ticket));

        holder.gameTicketSubscribe.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ticket.setSubscribe(isChecked);
            onTicketElementClickListener.onSubscribeClick(ticket, isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }


    private void changeLottoSize(View v, Drawable numberBall) {
        v.post(() -> {
            v.requestLayout();
            v.setBackground(numberBall);
            v.getLayoutParams().height = v.getWidth();
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.games_ticket_name)
        TextView gamesTicketName;
        @BindView(R.id.games_ticket_name_bottom)
        View gamesTicketNameBottom;
        @BindView(R.id.game_ticket_first_number)
        TextView gameTicketFirstNumber;
        @BindView(R.id.game_ticket_second_number)
        TextView gameTicketSecondNumber;
        @BindView(R.id.game_ticket_third_number)
        TextView gameTicketThirdNumber;
        @BindView(R.id.game_ticket_cmb_layout)
        ConstraintLayout gameTicketCmbLayout;
        @BindView(R.id.game_ticket_cmb_text)
        TextView gameTicketCmbText;
        @BindView(R.id.game_ticket_cmb_count)
        TextView gameTicketCmbCount;
        @BindView(R.id.game_ticket_subscribe)
        CheckBox gameTicketSubscribe;
        @BindView(R.id.game_ticket_cmb_card)
        CardView gameTicketCmbCard;
        @BindView(R.id.game_ticket_cost)
        TextView gameTicketCost;
        @BindView(R.id.game_ticket_delete)
        ImageButton gameTicketDelete;
        @BindView(R.id.games_ticket_bottom)
        View gamesTicketBottom;
        @BindView(R.id.ticket_draw_type)
        TextView ticketDrawType;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnTicketElementClickListener {
        void onDeleteClick(Ticket ticket);

        void onSubscribeClick(Ticket ticket, boolean subscribe);
    }
}