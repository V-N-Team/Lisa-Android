package ht.lisa.app.ui.wallet.games;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ht.lisa.app.R;
import ht.lisa.app.model.Draw;
import ht.lisa.app.model.Ticket;

public class RecentNumbersAdapter extends RecyclerView.Adapter<RecentNumbersAdapter.ViewHolder> {

    private ArrayList<Ticket> tickets;
    private Context context;

    public RecentNumbersAdapter(ArrayList<Ticket> tickets, Context context) {
        this.tickets = tickets;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecentNumbersAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_numbers_ticket, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);
        String gameName = ticket.getName();
        if (gameName == null || gameName.isEmpty()) return;
        Drawable numberBall = AppCompatResources.getDrawable(holder.itemView.getContext(), Draw.getTicketBall(ticket.getName() == null ? "" : ticket.getName()));

        holder.gameTicketSubscribe.setChecked(ticket.isAuto());
        if (gameName.toLowerCase().equals(Draw.LOTTO3)) {
            Log.d("LOGTAGUFU", String.valueOf(ticket.isAuto()));
        }
        if (gameName.toLowerCase().equals(Draw.BOLOTO) || gameName.toLowerCase().equals(Draw.LOTTO3) || gameName.toLowerCase().equals(Draw.LOTTO5JR)) {
            holder.gameTicketSubscribe.setVisibility(View.VISIBLE);
        } else {
            holder.gameTicketSubscribe.setVisibility(View.GONE);
        }

        holder.gamesTicketName.setText(position == 0 ? (gameName.equals(Draw.MARIAGE) ? context.getText(R.string.maryage) : gameName) : "");
        holder.gamesTicketName.setVisibility(position == 0 ? View.VISIBLE : View.GONE);

        holder.gamesTicketNameBottom.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        holder.gamesTicketBottom.setVisibility(position == tickets.size() - 1 ? View.VISIBLE : View.GONE);
        holder.gameTicketCmbCard.setVisibility(gameName.equals(Draw.BOLOTO) || ticket.getType() > 0 ? View.VISIBLE : View.GONE);


        holder.gameTicketFirstNumber.setText(ticket.getDrawArray().length > 0 ? ticket.getDrawArray()[0] : "");
        holder.gameTicketSecondNumber.setVisibility(ticket.getDrawArray().length > 1 ? View.VISIBLE : View.INVISIBLE);
        holder.gameTicketSecondNumber.setText(ticket.getDrawArray().length > 1 ? ticket.getDrawArray()[1] : "");
        holder.gameTicketThirdNumber.setVisibility(ticket.getDrawArray().length == 3 ? View.VISIBLE : View.INVISIBLE);
        holder.gameTicketThirdNumber.setText(ticket.getDrawArray().length == 3 ? ticket.getDrawArray()[2] : "");

        changeLottoSize(holder.gameTicketFirstNumber, numberBall);
        changeLottoSize(holder.gameTicketSecondNumber, numberBall);
        changeLottoSize(holder.gameTicketThirdNumber, numberBall);

        holder.gameTicketCost.setText(String.format(context.getString(R.string.HTG_d), ticket.getCost() / 100));
        holder.gameTicketCmbLayout.setVisibility(ticket.isCombo() || ticket.getType() > 0 ? View.VISIBLE : View.GONE);
        holder.gameTicketCmbText.setText(ticket.isCombo() ? context.getString(R.string.cmb) : context.getString(R.string.opt));
        holder.gameTicketCmbCount.setText(ticket.getType() > 0 ? String.valueOf(ticket.getType()) : gameName.equals(Draw.BOLOTO) ? context.getString(R.string._6) : "");

    }

    private void changeLottoSize(View v, Drawable numberBall) {
        v.post(() -> {
            v.requestLayout();
            v.setBackground(numberBall);
            v.getLayoutParams().height = v.getWidth();
        });
    }

    @Override
    public int getItemCount() {
        return tickets.size();
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
        @BindView(R.id.game_ticket_cmb_card)
        CardView gameTicketCmbCard;
        @BindView(R.id.game_ticket_cost)
        TextView gameTicketCost;
        @BindView(R.id.games_ticket_bottom)
        View gamesTicketBottom;
        @BindView(R.id.game_ticket_subscribe)
        CheckBox gameTicketSubscribe;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
