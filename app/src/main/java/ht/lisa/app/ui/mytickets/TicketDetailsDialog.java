package ht.lisa.app.ui.mytickets;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ht.lisa.app.R;
import ht.lisa.app.model.Ticket;
import ht.lisa.app.util.DateTimeUtil;
import ht.lisa.app.util.TextUtil;

public class TicketDetailsDialog extends DialogFragment {

    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final String MINUTES_FORMAT = "mma";
    private static final String TICKET_KEY = "ticketKey";

    @BindView(R.id.lotto_ball_1_number)
    TextView lottoBall1Number;
    @BindView(R.id.lotto_ball_2)
    LinearLayout lottoBall2Layout;
    @BindView(R.id.lotto_ball_2_number)
    TextView lottoBall2Number;
    @BindView(R.id.lotto_ball_3)
    LinearLayout lottoBall3Layout;
    @BindView(R.id.lotto_ball_3_number)
    TextView lottoBall3Number;
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
    @BindView(R.id.ticket_win_amount_layout)
    View ticketWinAmountLayout;
    @BindView(R.id.ticket_accepted_layout)
    View ticketAcceptedLayout;
    @BindView(R.id.ticket_not_accepted_reason)
    TextView ticketNotAcceptedReason;
    @BindView(R.id.ticket_win_amount_bottom)
    TextView ticketWinAmountBottom;
    @BindView(R.id.ticket_date)
    TextView ticketDate;
    @BindView(R.id.ticket_time)
    TextView ticketTime;
    @BindView(R.id.ticket_purchased_with_details)
    TextView ticketPurchasedWithDetails;

    public static TicketDetailsDialog newInstance(Ticket ticket) {
        TicketDetailsDialog dialog = new TicketDetailsDialog();
        Bundle args = new Bundle();
        args.putSerializable(TICKET_KEY, ticket);
        dialog.setArguments(args);
        return dialog;
    }


    private Unbinder unbinder;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_ticket_details, container);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            Ticket ticket = (Ticket) getArguments().getSerializable(TICKET_KEY);
            if (ticket == null) return;

            initView(ticket);
        }
    }

    private void initView(Ticket ticket) {
        if (ticket.isBeforeDraw() && ticket.getType() != 0) {
            ticketOptionsCount.setVisibility(View.VISIBLE);
            ticketOptionsOptText.setVisibility(View.VISIBLE);
            ticketOptionsCount.setText(String.valueOf(ticket.getType()));
        } else {
            ticketOptionsCount.setVisibility(View.GONE);
            ticketOptionsOptText.setVisibility(View.GONE);
        }

        ticketPurchasedWithDetails.setText(String.format("%sG BONIS, %sG LISA, %sG DIGICEL", TextUtil.getFormattedDecimalString(ticket.getBonus()), TextUtil.getFormattedDecimalString((ticket.getCost() - (ticket.getBonus() + ticket.getDigicel()))), TextUtil.getFormattedDecimalString(ticket.getDigicel())));
        quickpick.setVisibility(ticket.getQuick() == 1 ? View.VISIBLE : View.GONE);
        lottoBall1Number.setText(ticket.getDrawArray().length > 0 ? ticket.getDrawArray()[0] : "");
        lottoBall2Number.setText(ticket.getDrawArray().length > 1 ? ticket.getDrawArray()[1] : "");
        lottoBall2Layout.setVisibility(ticket.getDrawArray().length > 1 ? View.VISIBLE : View.GONE);
        lottoBall3Number.setText(ticket.getDrawArray().length == 3 ? ticket.getDrawArray()[2] : "");
        lottoBall3Layout.setVisibility(ticket.getDrawArray().length == 3 ? View.VISIBLE : View.GONE);

        ticketDate.setText(DateTimeUtil.getTicketDateFormatTime(ticket.getDate(), DATE_FORMAT, getActivity()));
        ticketTime.setText(String.format("%sh %s", DateTimeUtil.getTicketDateFormatTime(ticket.getDate(), DateTimeUtil.HR_AM_PM, getActivity()), DateTimeUtil.getTicketDateFormatTime(ticket.getDate(), MINUTES_FORMAT, getActivity())));
        if (ticket.isGameWin() || ticket.isGameWinJackpot()) {
            Drawable greenBall = AppCompatResources.getDrawable(getContext(), R.drawable.ic_green_lotto_ball);
            lottoBall1Number.setBackground(greenBall);
            lottoBall2Number.setBackground(greenBall);
            lottoBall3Number.setBackground(greenBall);

            quickpick.setImageResource(R.drawable.ic_quickpick_green);
            ticketAcceptedLayout.setVisibility(View.VISIBLE);
            ticketWinAmount.setText(TextUtil.getDecimalString(Integer.parseInt(ticket.getWin())).replaceAll("[^0-9.G]", ","));
            ticketWinAmountBottom.setText(String.format(getString(R.string.win_), TextUtil.getDecimalString(Integer.parseInt(ticket.getWin())).replaceAll("[^0-9.G]", ",")));
            ticketStatusImage.setVisibility(View.VISIBLE);
            ticketStatusImage.setImageResource(R.drawable.ic_win_ticket_icon);
            ticketNotAcceptedReason.setVisibility(View.GONE);
        } else if (ticket.isTicketAccepted()) {
            Drawable orangeBall = AppCompatResources.getDrawable(getContext(), R.drawable.ic_light_orange_lotto_ball);
            lottoBall1Number.setBackground(orangeBall);
            lottoBall2Number.setBackground(orangeBall);
            lottoBall3Number.setBackground(orangeBall);

            ticketWinAmountLayout.setVisibility(View.GONE);
            ticketWinAmountBottom.setVisibility(View.GONE);
            quickpick.setImageResource(R.drawable.ic_quickpick_orange);
            ticketStatusImage.setVisibility(View.VISIBLE);
            ticketOptionsOptText.setBackgroundResource(R.drawable.ic_opt_orange);
            ticketOptionsCount.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.buttonLightOrange));
            ticketStatusImage.setImageResource(R.drawable.ic_accepted);
            ticketNotAcceptedReason.setVisibility(View.GONE);
        } else {
            Drawable greyBall = AppCompatResources.getDrawable(getContext(), R.drawable.ic_grey_lotto_ball);
            lottoBall1Number.setBackground(greyBall);
            lottoBall2Number.setBackground(greyBall);
            lottoBall3Number.setBackground(greyBall);
            ticketWinAmountLayout.setVisibility(View.GONE);
            ticketWinAmountBottom.setVisibility(View.GONE);
            ticketNotAcceptedReason.setVisibility(View.GONE);
            ticketOptionsOptText.setBackgroundResource(R.drawable.ic_opt_grey);
            ticketOptionsCount.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightGray));
            quickpick.setImageResource(R.drawable.ic_quickpick_gray);
            ticketStatusImage.setImageResource((ticket.isPendingTicket() || ticket.isRequestCreated() || ticket.isPendingGame()) ? R.drawable.ic_processing : R.drawable.ic_error_mark);
            if (ticket.isNotAccepted()) {
                ticketNotAcceptedReason.setVisibility(View.VISIBLE);
                ticketNotAcceptedReason.setText(String.format(getString(R.string.ticket_not_accepted), ticket.getNotAcceptedReason(getActivity())));
                ticketAcceptedLayout.setVisibility(View.GONE);
            }
            ticketStatusImage.setVisibility(ticket.isGameLost() ? View.GONE : View.VISIBLE);
        }
    }

    @OnClick(R.id.ticket_details_close_button)
    void onCloseClick() {
        dismiss();
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            dialog.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}
