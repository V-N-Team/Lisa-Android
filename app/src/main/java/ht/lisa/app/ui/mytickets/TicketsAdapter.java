package ht.lisa.app.ui.mytickets;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ht.lisa.app.R;
import ht.lisa.app.model.Ticket;
import ht.lisa.app.util.TextUtil;

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.ViewHolder> {

    private ArrayList<Ticket> tickets;
    private OnTicketClickListener onTicketClickListener;

    TicketsAdapter(ArrayList<Ticket> tickets, OnTicketClickListener onTicketClickListener) {
        this.tickets = tickets;
        this.onTicketClickListener = onTicketClickListener;
    }

    @NonNull
    @Override
    public TicketsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TicketsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TicketsAdapter.ViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);

        if (ticket.isBeforeDraw() && ticket.getType() != 0) {
            holder.ticketOptionsCount.setVisibility(View.VISIBLE);
            holder.ticketOptionsOptText.setVisibility(View.VISIBLE);
            holder.ticketOptionsCount.setText(String.valueOf(ticket.getType()));
        } else {
            holder.ticketOptionsCount.setVisibility(View.GONE);
            holder.ticketOptionsOptText.setVisibility(View.GONE);
        }
        holder.quickpick.setVisibility(ticket.getQuick() == 1 ? View.VISIBLE : View.GONE);
        holder.lottoBall1Number.setText(ticket.getDrawArray().length > 0 ? ticket.getDrawArray()[0] : "");
        holder.lottoBall2Number.setText(ticket.getDrawArray().length > 1 ? ticket.getDrawArray()[1] : "");
        holder.lottoBall2Layout.setVisibility(ticket.getDrawArray().length > 1 ? View.VISIBLE : View.GONE);
        holder.lottoBall3Number.setText(ticket.getDrawArray().length == 3 ? ticket.getDrawArray()[2] : "");
        holder.lottoBall3Layout.setVisibility(ticket.getDrawArray().length == 3 ? View.VISIBLE : View.GONE);

        holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), ticket.isGameWin() ? R.color.lightGreen : R.color.white));
        holder.ticketNormalLayout.setVisibility(View.VISIBLE);
        holder.ticketJackpotLayout.setVisibility(View.GONE);
        if (ticket.isGameWin()) {
            Drawable greenBall = AppCompatResources.getDrawable(holder.itemView.getContext(), R.drawable.ic_green_lotto_ball);
            holder.lottoBall1Number.setBackground(greenBall);
            holder.lottoBall2Number.setBackground(greenBall);
            holder.lottoBall3Number.setBackground(greenBall);
            holder.quickpick.setImageResource(R.drawable.ic_quickpick_green);
            holder.ticketWinAmountLayout.setVisibility(View.VISIBLE);
            holder.ticketWinAmount.setText(TextUtil.getDecimalString(Integer.parseInt(ticket.getWin())).replaceAll("[^0-9.G]", ","));
            holder.ticketStatusImage.setVisibility(View.VISIBLE);
            holder.ticketStatusImage.setImageResource(R.drawable.ic_win_ticket_icon);
            holder.ticketWinAmount.setTextColor(ContextCompat.getColor(holder.ticketOptionsOptText.getContext(), R.color.greenJackpotColor));
        } else if (ticket.isGameWinJackpot()) {
            Drawable yellowBall = AppCompatResources.getDrawable(holder.itemView.getContext(), R.drawable.ic_yellow_lotto_ball);
            holder.ticketNormalLayout.setVisibility(View.GONE);
            holder.ticketJackpotLayout.setVisibility(View.VISIBLE);
            holder.ticketJackpotWinAmount.setText(TextUtil.getDecimalString(Integer.parseInt(ticket.getWin())).replaceAll("[^0-9.G]", ","));

            holder.lottoJackpotBall1Number.setText(ticket.getDrawArray().length > 0 ? ticket.getDrawArray()[0] : "");
            holder.lottoJackpotBall2Number.setText(ticket.getDrawArray().length > 1 ? ticket.getDrawArray()[1] : "");
            holder.lottoJackpotBall2Layout.setVisibility(ticket.getDrawArray().length > 1 ? View.VISIBLE : View.GONE);
            holder.lottoJackpotBall3Number.setText(ticket.getDrawArray().length == 3 ? ticket.getDrawArray()[2] : "");
            holder.lottoJackpotBall3Layout.setVisibility(ticket.getDrawArray().length == 3 ? View.VISIBLE : View.GONE);
            holder.lottoJackpotBall1Number.setBackground(yellowBall);
            holder.lottoJackpotBall2Number.setBackground(yellowBall);
            holder.lottoJackpotBall3Number.setBackground(yellowBall);

        } else if (ticket.isTicketAccepted()) {
            Drawable orangeBall = AppCompatResources.getDrawable(holder.itemView.getContext(), R.drawable.ic_light_orange_lotto_ball);
            holder.lottoBall1Number.setBackground(orangeBall);
            holder.lottoBall2Number.setBackground(orangeBall);
            holder.lottoBall3Number.setBackground(orangeBall);

            holder.ticketWinAmountLayout.setVisibility(View.GONE);
            holder.quickpick.setImageResource(R.drawable.ic_quickpick_orange);
            holder.ticketStatusImage.setVisibility(View.VISIBLE);
            holder.ticketOptionsOptText.setBackgroundResource(R.drawable.ic_opt_orange);
            holder.ticketOptionsCount.setBackgroundColor(ContextCompat.getColor(holder.ticketOptionsOptText.getContext(), R.color.buttonLightOrange));
            holder.ticketStatusImage.setImageResource(R.drawable.ic_accepted);
        } else {
            Drawable greyBall = AppCompatResources.getDrawable(holder.itemView.getContext(), R.drawable.ic_grey_lotto_ball);
            holder.lottoBall1Number.setBackground(greyBall);
            holder.lottoBall2Number.setBackground(greyBall);
            holder.lottoBall3Number.setBackground(greyBall);

            holder.ticketOptionsOptText.setBackgroundResource(R.drawable.ic_opt_grey);
            holder.ticketOptionsCount.setBackgroundColor(ContextCompat.getColor(holder.ticketOptionsOptText.getContext(), R.color.lightGray));
            holder.quickpick.setImageResource(R.drawable.ic_quickpick_gray);
            holder.ticketWinAmountLayout.setVisibility(View.GONE);
            holder.ticketStatusImage.setImageResource((ticket.isPendingTicket() || ticket.isRequestCreated() || ticket.isPendingGame()) ? R.drawable.ic_processing : R.drawable.ic_error_mark);
            holder.ticketStatusImage.setVisibility(ticket.isGameLost() ? View.GONE : View.VISIBLE);
        }

        holder.itemView.setOnClickListener(view -> onTicketClickListener.onTicketClick(ticket));
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    public interface OnTicketClickListener {
        void onTicketClick(Ticket ticket);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.lotto_ball_1)
        LinearLayout lottoBall1Layout;
        @BindView(R.id.lotto_ball_1_number)
        TextView lottoBall1Number;
        @BindView(R.id.lotto_jackpot_ball_1_number)
        TextView lottoJackpotBall1Number;
        @BindView(R.id.lotto_ball_2)
        LinearLayout lottoBall2Layout;
        @BindView(R.id.lotto_ball_2_number)
        TextView lottoBall2Number;
        @BindView(R.id.lotto_ball_3)
        LinearLayout lottoBall3Layout;
        @BindView(R.id.lotto_ball_3_number)
        TextView lottoBall3Number;
        @BindView(R.id.lotto_jackpot_ball_2)
        LinearLayout lottoJackpotBall2Layout;
        @BindView(R.id.lotto_jackpot_ball_2_number)
        TextView lottoJackpotBall2Number;
        @BindView(R.id.lotto_jackpot_ball_3)
        LinearLayout lottoJackpotBall3Layout;
        @BindView(R.id.lotto_jackpot_ball_3_number)
        TextView lottoJackpotBall3Number;
        @BindView(R.id.quickpick)
        ImageView quickpick;
        @BindView(R.id.ticket_options_count)
        TextView ticketOptionsCount;
        @BindView(R.id.ticket_options_opt_text)
        TextView ticketOptionsOptText;
        @BindView(R.id.ticket_status_image)
        ImageView ticketStatusImage;
        @BindView(R.id.ticket_win_amount)
        TextView ticketWinAmount;
        @BindView(R.id.ticket_jackpot_win_amount)
        TextView ticketJackpotWinAmount;
        @BindView(R.id.ticket_win_amount_layout)
        View ticketWinAmountLayout;
        @BindView(R.id.ticket_normal_layout)
        View ticketNormalLayout;
        @BindView(R.id.ticket_jackpot_layout)
        View ticketJackpotLayout;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}