package ht.lisa.app.ui.mytickets;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import ht.lisa.app.LisaApp;
import ht.lisa.app.R;
import ht.lisa.app.model.Phone;
import ht.lisa.app.model.Ticket;
import ht.lisa.app.model.response.TicketsResponse;
import ht.lisa.app.ui.datepicker.DatePickerDialog;
import ht.lisa.app.ui.datepicker.SpinnerDatePickerDialogBuilder;
import ht.lisa.app.ui.main.MainActivity;
import ht.lisa.app.ui.wallet.BaseWalletFragment;
import ht.lisa.app.ui.wallet.gamelist.GameListFragment;
import ht.lisa.app.util.DateTimeUtil;
import ht.lisa.app.util.FragmentUtil;
import ht.lisa.app.util.OnFragmentVisibleListener;
import ht.lisa.app.util.SharedPreferencesUtil;

import static ht.lisa.app.model.Ticket.BOLOTO_GAME;
import static ht.lisa.app.model.Ticket.BORLET_GAME;
import static ht.lisa.app.model.Ticket.LOTTO3_GAME;
import static ht.lisa.app.model.Ticket.LOTTO4_GAME;
import static ht.lisa.app.model.Ticket.LOTTO5_5G_GAME;
import static ht.lisa.app.model.Ticket.LOTTO5_GAME;
import static ht.lisa.app.model.Ticket.LOTTO5_ROYAL_GAME;
import static ht.lisa.app.model.Ticket.MARIAGE_GAME;

public class MyTicketsFragment extends BaseWalletFragment implements FilterTicketsDialog.OnFilterListener, TicketsAdapter.OnTicketClickListener {

    public static final String SCREEN_NAME = "MyTicketsScreen";

    public static final int TOP_SHEET_DURATION = 5000;
    private static final String DATE_PATTERN = "dd.MM.yy";
    private static final int EVENING = 16;


    @BindView(R.id.my_tickets_date)
    TextView myTicketsDate;
    @BindView(R.id.my_tickets_no_ticket_layout)
    LinearLayout myTicketsNoTicketLayout;
    @BindView(R.id.my_tickets_layout)
    LinearLayout myTicketsLayout;
    @BindView(R.id.boloto_fl_noon_tickets_recycler)
    RecyclerView bolotoFlNoonTicketsRecycler;
    @BindView(R.id.boloto_fl_evening_tickets_recycler)
    RecyclerView bolotoFlEveningTicketsRecycler;
    @BindView(R.id.boloto_ny_noon_tickets_recycler)
    RecyclerView bolotoNyNoonTicketsRecycler;
    @BindView(R.id.boloto_ny_evening_tickets_recycler)
    RecyclerView bolotoNyEveningTicketsRecycler;
    @BindView(R.id.boloto_layout)
    View bolotoLayout;
    @BindView(R.id.boloto_fl_noon_layout)
    View bolotoFlNoonLayout;
    @BindView(R.id.boloto_fl_evening_layout)
    View bolotoFlEveningLayout;
    @BindView(R.id.borlet_fl_noon_tickets_recycler)
    RecyclerView borletFlNoonTicketsRecycler;
    @BindView(R.id.borlet_fl_evening_tickets_recycler)
    RecyclerView borletFlEveningTicketsRecycler;
    @BindView(R.id.boloto_ny_noon_layout)
    View bolotoNyNoonLayout;
    @BindView(R.id.boloto_ny_evening_layout)
    View bolotoNyEveningLayout;
    @BindView(R.id.borlet_ny_noon_tickets_recycler)
    RecyclerView borletNyNoonTicketsRecycler;
    @BindView(R.id.borlet_ny_evening_tickets_recycler)
    RecyclerView borletNyEveningTicketsRecycler;
    @BindView(R.id.borlet_layout)
    View borletLayout;
    @BindView(R.id.borlet_fl_noon_layout)
    View borletFlNoonLayout;
    @BindView(R.id.borlet_fl_evening_layout)
    View borletFlEveningLayout;
    @BindView(R.id.borlet_ny_noon_layout)
    View borletNyNoonLayout;
    @BindView(R.id.borlet_ny_evening_layout)
    View borletNyEveningLayout;
    @BindView(R.id.maryage_fl_noon_tickets_recycler)
    RecyclerView mariageFlNoonTicketsRecycler;
    @BindView(R.id.maryage_ny_evening_tickets_recycler)
    RecyclerView mariageNyEveningTicketsRecycler;
    @BindView(R.id.maryage_ny_noon_tickets_recycler)
    RecyclerView mariageNyNoonTicketsRecycler;
    @BindView(R.id.maryage_fl_evening_tickets_recycler)
    RecyclerView mariageFlEveningTicketsRecycler;
    @BindView(R.id.maryage_layout)
    View mariageLayout;
    @BindView(R.id.maryage_fl_noon_layout)
    View mariageFlNoonLayout;
    @BindView(R.id.maryage_fl_evening_layout)
    View mariageFlEveningLayout;
    @BindView(R.id.maryage_ny_noon_layout)
    View mariageNyNoonLayout;
    @BindView(R.id.maryage_ny_evening_layout)
    View mariageNyEveningLayout;
    @BindView(R.id.lotto3_fl_noon_tickets_recycler)
    RecyclerView lotto3FlNoonTicketsRecycler;
    @BindView(R.id.lotto3_fl_evening_tickets_recycler)
    RecyclerView lotto3FlEveningTicketsRecycler;
    @BindView(R.id.lotto3_ny_noon_tickets_recycler)
    RecyclerView lotto3NyNoonTicketsRecycler;
    @BindView(R.id.lotto3_ny_evening_tickets_recycler)
    RecyclerView lotto3NyEveningTicketsRecycler;
    @BindView(R.id.lotto3_layout)
    View lotto3Layout;
    @BindView(R.id.lotto3_fl_noon_layout)
    View lotto3FlNoonLayout;
    @BindView(R.id.lotto3_fl_evening_layout)
    View lotto3FlEveningLayout;
    @BindView(R.id.lotto3_ny_noon_layout)
    View lotto3NyNoonLayout;
    @BindView(R.id.lotto3_ny_evening_layout)
    View lotto3NyEveningLayout;
    @BindView(R.id.lotto4_fl_noon_tickets_recycler)
    RecyclerView lotto4FlNoonTicketsRecycler;
    @BindView(R.id.lotto4_fl_evening_tickets_recycler)
    RecyclerView lotto4FlEveningTicketsRecycler;
    @BindView(R.id.lotto4_ny_noon_tickets_recycler)
    RecyclerView lotto4NyNoonTicketsRecycler;
    @BindView(R.id.lotto4_ny_evening_tickets_recycler)
    RecyclerView lotto4NyEveningTicketsRecycler;
    @BindView(R.id.lotto4_layout)
    View lotto4Layout;
    @BindView(R.id.lotto4_fl_noon_layout)
    View lotto4FlNoonLayout;
    @BindView(R.id.lotto4_fl_evening_layout)
    View lotto4FlEveningLayout;
    @BindView(R.id.lotto4_ny_noon_layout)
    View lotto4NyNoonLayout;
    @BindView(R.id.lotto4_ny_evening_layout)
    View lotto4NyEveningLayout;
    @BindView(R.id.lotto5_fl_noon_tickets_recycler)
    RecyclerView lotto5FlNoonTicketsRecycler;
    @BindView(R.id.lotto5_fl_evening_tickets_recycler)
    RecyclerView lotto5FlEveningTicketsRecycler;
    @BindView(R.id.lotto5_ny_noon_tickets_recycler)
    RecyclerView lotto5NyNoonTicketsRecycler;
    @BindView(R.id.lotto5_ny_evening_tickets_recycler)
    RecyclerView lotto5NyEveningTicketsRecycler;
    @BindView(R.id.lotto5_layout)
    View lotto5Layout;
    @BindView(R.id.lotto5_fl_noon_layout)
    View lotto5FlNoonLayout;
    @BindView(R.id.lotto5_fl_evening_layout)
    View lotto5FlEveningLayout;
    @BindView(R.id.lotto5_ny_noon_layout)
    View lotto5NyNoonLayout;
    @BindView(R.id.lotto5_ny_evening_layout)
    View lotto5NyEveningLayout;
    @BindView(R.id.lotto5_5g_fl_noon_tickets_recycler)
    RecyclerView lotto55GFlNoonTicketsRecycler;
    @BindView(R.id.lotto5_5g_fl_evening_tickets_recycler)
    RecyclerView lotto55GFlEveningTicketsRecycler;
    @BindView(R.id.lotto5_5g_ny_noon_tickets_recycler)
    RecyclerView lotto55GNyNoonTicketsRecycler;
    @BindView(R.id.lotto5_5g_ny_evening_tickets_recycler)
    RecyclerView lotto55GNyEveningTicketsRecycler;
    @BindView(R.id.lotto5_5g_layout)
    View lotto55gLayout;
    @BindView(R.id.lotto5_5g_ny_noon_layout)
    View lotto55gNyNoonLayout;
    @BindView(R.id.lotto5_5g_ny_evening_layout)
    View lotto55gNyEveningLayout;
    @BindView(R.id.lotto5_5g_fl_noon_layout)
    View lotto55gFlNoonLayout;
    @BindView(R.id.lotto5_5g_fl_evening_layout)
    View lotto55gFlEveningLayout;
    @BindView(R.id.lotto5_royal_layout)
    View lotto5RoyalLayout;
    @BindView(R.id.lotto5_royal_ny_noon_layout)
    View lotto5RoyalNyNoonLayout;
    @BindView(R.id.lotto5_royal_ny_evening_layout)
    View lotto5RoyalNyEveningLayout;
    @BindView(R.id.lotto5_royal_fl_noon_layout)
    View lotto5RoyalFlNoonLayout;
    @BindView(R.id.lotto5_royal_fl_evening_layout)
    View lotto5RoyalFlEveningLayout;
    @BindView(R.id.lotto5_royal_fl_noon_tickets_recycler)
    RecyclerView lotto5RoyalFlNoonTicketsRecycler;
    @BindView(R.id.lotto5_royal_fl_evening_tickets_recycler)
    RecyclerView lotto5RoyalFlEveningTicketsRecycler;
    @BindView(R.id.lotto5_royal_ny_noon_tickets_recycler)
    RecyclerView lotto5RoyalNyNoonTicketsRecycler;
    @BindView(R.id.lotto5_royal_ny_evening_tickets_recycler)
    RecyclerView lotto5RoyalNyEveningTicketsRecycler;


    @BindView(R.id.boloto_ga_noon_tickets_recycler)
    RecyclerView bolotoGaNoonTicketsRecycler;
    @BindView(R.id.boloto_ga_evening_tickets_recycler)
    RecyclerView bolotoGaEveningTicketsRecycler;
    @BindView(R.id.boloto_ga_noon_layout)
    View bolotoGaNoonLayout;
    @BindView(R.id.boloto_ga_evening_layout)
    View bolotoGaEveningLayout;


    @BindView(R.id.borlet_ga_noon_tickets_recycler)
    RecyclerView borletGaNoonTicketsRecycler;
    @BindView(R.id.borlet_ga_evening_tickets_recycler)
    RecyclerView borletGaEveningTicketsRecycler;
    @BindView(R.id.borlet_ga_noon_layout)
    View borletGaNoonLayout;
    @BindView(R.id.borlet_ga_evening_layout)
    View borletGaEveningLayout;


    @BindView(R.id.maryage_ga_noon_tickets_recycler)
    RecyclerView maryageGaNoonTicketsRecycler;
    @BindView(R.id.maryage_ga_evening_tickets_recycler)
    RecyclerView maryageGaEveningTicketsRecycler;
    @BindView(R.id.maryage_ga_noon_layout)
    View maryageGaNoonLayout;
    @BindView(R.id.maryage_ga_evening_layout)
    View maryageGaEveningLayout;


    @BindView(R.id.lotto3_ga_noon_tickets_recycler)
    RecyclerView lotto3GaNoonTicketsRecycler;
    @BindView(R.id.lotto3_ga_evening_tickets_recycler)
    RecyclerView lotto3GaEveningTicketsRecycler;
    @BindView(R.id.lotto3_ga_noon_layout)
    View lotto3GaNoonLayout;
    @BindView(R.id.lotto3_ga_evening_layout)
    View lotto3GaEveningLayout;


    @BindView(R.id.lotto4_ga_noon_tickets_recycler)
    RecyclerView lotto4GaNoonTicketsRecycler;
    @BindView(R.id.lotto4_ga_evening_tickets_recycler)
    RecyclerView lotto4GaEveningTicketsRecycler;
    @BindView(R.id.lotto4_ga_noon_layout)
    View lotto4GaNoonLayout;
    @BindView(R.id.lotto4_ga_evening_layout)
    View lotto4GaEveningLayout;


    @BindView(R.id.lotto5_ga_noon_tickets_recycler)
    RecyclerView lotto5GaNoonTicketsRecycler;
    @BindView(R.id.lotto5_ga_evening_tickets_recycler)
    RecyclerView lotto5GaEveningTicketsRecycler;
    @BindView(R.id.lotto5_ga_noon_layout)
    View lotto5GaNoonLayout;
    @BindView(R.id.lotto5_ga_evening_layout)
    View lotto5GaEveningLayout;


    @BindView(R.id.lotto5_5g_ga_noon_tickets_recycler)
    RecyclerView lotto55GGaNoonTicketsRecycler;
    @BindView(R.id.lotto5_5g_ga_evening_tickets_recycler)
    RecyclerView lotto55GGaEveningTicketsRecycler;
    @BindView(R.id.lotto5_5g_ga_noon_layout)
    View lotto55GGaNoonLayout;
    @BindView(R.id.lotto5_5g_ga_evening_layout)
    View lotto55GGaEveningLayout;


    @BindView(R.id.lotto5_royal_ga_noon_tickets_recycler)
    RecyclerView lotto5RoyalGaNoonTicketsRecycler;
    @BindView(R.id.lotto5_royal_ga_evening_tickets_recycler)
    RecyclerView lotto5RoyalGaEveningTicketsRecycler;
    @BindView(R.id.lotto5_royal_ga_noon_layout)
    View lotto5RoyalGaNoonLayout;
    @BindView(R.id.lotto5_royal_ga_evening_layout)
    View lotto5RoyalGaEveningLayout;


    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;


    private OnNavigateToGamesListListener gamesListClickListener;
    private OnFragmentVisibleListener onFragmentVisibleListener;
    boolean filterByBoloto = true;
    boolean filterByBorlet = true;
    boolean filterByMariage = true;
    boolean filterByLotto3 = true;
    boolean filterByLotto4 = true;
    boolean filterByLotto5 = true;
    boolean filterByLotto55g = true;
    boolean filterByLotto5Royal = true;
    boolean filterByWin = true;
    boolean filterByNotAccepted = true;
    boolean filterByClosed = true;
    boolean filterByPending = true;

    Calendar dateAndTime = Calendar.getInstance();

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_tickets, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSwipeRefreshLayout();
        walletPresenter.getUserTickets(new Phone(SharedPreferencesUtil.getPhone()), DateTimeUtil.getDobFormat(dateAndTime.getTimeInMillis()));
        myTicketsDate.setText(DateTimeUtil.getTicketDateFormatTime(dateAndTime.getTimeInMillis() / DateTimeUtil.SECOND, DATE_PATTERN, getActivity()));

        resizeNoTicketsLayout();
        Alerter alerter = Alerter.create(getActivity(), R.layout.dialog_error_top_sheet);
        alerter.setDuration(TOP_SHEET_DURATION);
        RelativeLayout layout = (RelativeLayout) alerter.getLayoutContainer();
        if (layout != null) {
            ((TextView) alerter.getLayoutContainer().findViewById(R.id.top_sheet_error_message)).setText(R.string.unsuccessful_transaction_no_funds_available);
            alerter.getLayoutContainer().findViewById(R.id.top_sheet_error_close_icon).setOnClickListener(v ->
                    Alerter.hide());
            alerter.setBackgroundColorRes(R.color.errorBgColor);
            // alerter.show();
        }
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            walletPresenter.getUserTickets(new Phone(SharedPreferencesUtil.getPhone()), DateTimeUtil.getDobFormat(dateAndTime.getTimeInMillis()));
        });
    }

    private void resizeNoTicketsLayout() {
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) myTicketsNoTicketLayout.getLayoutParams();
        params.height = (int) (height * 0.55);
        myTicketsNoTicketLayout.setLayoutParams(params);
    }

    @Override
    public void getData(Object object) {
        if (object instanceof TicketsResponse) {
            TicketsResponse ticketsResponse = (TicketsResponse) object;
            if (ticketsResponse.getState() == 0) {
                if (ticketsResponse.getDataset().size() == 0) {
                    myTicketsNoTicketLayout.setVisibility(View.VISIBLE);
                    myTicketsLayout.setVisibility(View.GONE);
                } else {
                    myTicketsNoTicketLayout.setVisibility(View.GONE);
                    myTicketsLayout.setVisibility(View.VISIBLE);
                    ArrayList<Ticket> bolotoNyNoonTickets = new ArrayList<>();
                    ArrayList<Ticket> borletNyNoonTickets = new ArrayList<>();
                    ArrayList<Ticket> mariageNyNoonTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto3NyNoonTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto4NyNoonTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto5NyNoonTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto55gNyNoonTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto5RoyalNyNoonTickets = new ArrayList<>();
                    ArrayList<Ticket> bolotoNyEveningTickets = new ArrayList<>();
                    ArrayList<Ticket> borletNyEveningTickets = new ArrayList<>();
                    ArrayList<Ticket> mariageNyEveningTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto3NyEveningTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto4NyEveningTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto5NyEveningTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto55gNyEveningTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto5RoyalNyEveningTickets = new ArrayList<>();
                    ArrayList<Ticket> bolotoGaNoonTickets = new ArrayList<>();
                    ArrayList<Ticket> bolotoGaEveningTickets = new ArrayList<>();
                    ArrayList<Ticket> mariageGaNoonTickets = new ArrayList<>();
                    ArrayList<Ticket> mariageGaEveningTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto3GaNoonTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto3GaEveningTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto4GaNoonTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto4GaEveningTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto5GaNoonTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto5GaEveningTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto55gGaNoonTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto55gGaEveningTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto5RoyalGaNoonTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto5RoyalGaEveningTickets = new ArrayList<>();
                    ArrayList<Ticket> borletGaNoonTickets = new ArrayList<>();
                    ArrayList<Ticket> borletGaEveningTickets = new ArrayList<>();

                    ArrayList<Ticket> bolotoFlNoonTickets = new ArrayList<>();
                    ArrayList<Ticket> borletFlNoonTickets = new ArrayList<>();
                    ArrayList<Ticket> mariageFlNoonTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto3FlNoonTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto4FlNoonTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto5FlNoonTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto55gFlNoonTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto5RoyalFlNoonTickets = new ArrayList<>();
                    ArrayList<Ticket> bolotoFlEveningTickets = new ArrayList<>();
                    ArrayList<Ticket> borletFlEveningTickets = new ArrayList<>();
                    ArrayList<Ticket> mariageFlEveningTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto3FlEveningTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto4FlEveningTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto5FlEveningTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto55gFlEveningTickets = new ArrayList<>();
                    ArrayList<Ticket> lotto5RoyalFlEveningTickets = new ArrayList<>();

                    ArrayList<Integer> filterCodes = new ArrayList<>();
                    if (filterByPending) {
                        filterCodes.add(Ticket.CODE_PENDING_TICKET);
                        filterCodes.add(Ticket.CODE_REQUEST_CREATED);
                        filterCodes.add(Ticket.CODE_ACCEPTED);
                        filterCodes.add(Ticket.CODE_PENDING_GAME);
                    }
                    if (filterByNotAccepted) {
                        filterCodes.add(Ticket.CODE_CANCELED);
                        filterCodes.add(Ticket.CODE_PAYMENT_GATEWAY_ERROR);
                        filterCodes.add(Ticket.CODE_POSTPAID_SETTLEMENT_SYSTEM);
                        filterCodes.add(Ticket.CODE_LACK_OF_FUNDS);
                        filterCodes.add(Ticket.CODE_RISK_CONTROL_SYSTEM);
                        filterCodes.add(Ticket.CODE_INCORRECT_TIME);
                        filterCodes.add(Ticket.CODE_BLOCKED);
                    }

                    if (filterByWin) {
                        filterCodes.add(Ticket.CODE_WIN);
                        filterCodes.add(Ticket.CODE_WIN_JACKPOT);
                    }
                    if (filterByClosed) {
                        filterCodes.add(Ticket.CODE_LOST);
                    }


                    for (Ticket ticket : ticketsResponse.getDataset()) {
                        Log.d("LOGTAGDRAWOO", ticket.getCode() == Ticket.CODE_WIN_JACKPOT ? "JACKPOT" : "");
                        if (filterCodes.contains(ticket.getCode())) {
                            switch (ticket.getGame()) {
                                case BOLOTO_GAME:
                                    if (filterByBoloto) {
                                        if (isNoonDraw(ticket)) {
                                            addTicketToList(ticket, ticket.isNy() ? bolotoNyNoonTickets : ticket.isFl() ? bolotoFlNoonTickets : bolotoGaNoonTickets);
                                        } else {
                                            addTicketToList(ticket, ticket.isNy() ? bolotoNyEveningTickets : ticket.isFl() ? bolotoFlEveningTickets : bolotoGaEveningTickets);
                                        }
                                    }
                                    break;
                                case BORLET_GAME:
                                    if (filterByBorlet) {
                                        if (isNoonDraw(ticket)) {
                                            addTicketToList(ticket, ticket.isNy() ? borletNyNoonTickets : ticket.isFl() ? borletFlNoonTickets : borletGaNoonTickets);
                                        } else {
                                            addTicketToList(ticket, ticket.isNy() ? borletNyEveningTickets : ticket.isFl() ? borletFlEveningTickets : borletGaEveningTickets);
                                        }
                                    }
                                    break;
                                case MARIAGE_GAME:
                                    if (filterByMariage) {
                                        if (isNoonDraw(ticket)) {
                                            addTicketToList(ticket, ticket.isNy() ? mariageNyNoonTickets : ticket.isFl() ? mariageFlNoonTickets : mariageGaNoonTickets);
                                        } else {
                                            addTicketToList(ticket, ticket.isNy() ? mariageNyEveningTickets : ticket.isFl() ? mariageFlEveningTickets : mariageGaEveningTickets);
                                        }
                                    }
                                    break;
                                case LOTTO3_GAME:
                                    if (filterByLotto3) {
                                        if (isNoonDraw(ticket)) {
                                            addTicketToList(ticket, ticket.isNy() ? lotto3NyNoonTickets : ticket.isFl() ? lotto3FlNoonTickets : lotto3GaNoonTickets);
                                        } else {
                                            addTicketToList(ticket, ticket.isNy() ? lotto3NyEveningTickets : ticket.isFl() ? lotto3FlEveningTickets : lotto3GaEveningTickets);
                                        }
                                    }
                                    break;
                                case LOTTO4_GAME:
                                    if (filterByLotto4) {
                                        if (isNoonDraw(ticket)) {
                                            addTicketToList(ticket, ticket.isNy() ? lotto4NyNoonTickets : ticket.isFl() ? lotto4FlNoonTickets : lotto4GaNoonTickets);
                                        } else {
                                            addTicketToList(ticket, ticket.isNy() ? lotto4NyEveningTickets : ticket.isFl() ? lotto4FlEveningTickets : lotto4GaEveningTickets);
                                        }
                                    }
                                    break;
                                case LOTTO5_GAME:
                                    if (filterByLotto5) {
                                        if (isNoonDraw(ticket)) {
                                            addTicketToList(ticket, ticket.isNy() ? lotto5NyNoonTickets : ticket.isFl() ? lotto5FlNoonTickets : lotto5GaNoonTickets);
                                        } else {
                                            addTicketToList(ticket, ticket.isNy() ? lotto5NyEveningTickets : ticket.isFl() ? lotto5FlEveningTickets : lotto5GaEveningTickets);
                                        }
                                    }
                                    break;
                                case LOTTO5_5G_GAME:
                                    if (filterByLotto55g) {
                                        if (isNoonDraw(ticket)) {
                                            addTicketToList(ticket, ticket.isNy() ? lotto55gNyNoonTickets : ticket.isFl() ? lotto55gFlNoonTickets : lotto55gGaNoonTickets);
                                        } else {
                                            addTicketToList(ticket, ticket.isNy() ? lotto55gNyEveningTickets : ticket.isFl() ? lotto55gFlEveningTickets : lotto55gGaEveningTickets);
                                        }
                                    }
                                    break;
                                case LOTTO5_ROYAL_GAME:
                                    if (filterByLotto55g) {
                                        if (isNoonDraw(ticket)) {
                                            addTicketToList(ticket, ticket.isNy() ? lotto5RoyalNyNoonTickets : ticket.isFl() ? lotto5RoyalFlNoonTickets : lotto5RoyalGaNoonTickets);
                                        } else {
                                            addTicketToList(ticket, ticket.isNy() ? lotto5RoyalNyEveningTickets : ticket.isFl() ? lotto5RoyalFlEveningTickets : lotto5RoyalGaEveningTickets);
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                    if (bolotoNyNoonTickets.size() == 0 && bolotoNyEveningTickets.size() == 0
                            && borletNyNoonTickets.size() == 0 && borletNyEveningTickets.size() == 0
                            && mariageNyNoonTickets.size() == 0 && mariageNyEveningTickets.size() == 0
                            && lotto3NyNoonTickets.size() == 0 && lotto3NyEveningTickets.size() == 0
                            && lotto4NyNoonTickets.size() == 0 && lotto4NyEveningTickets.size() == 0
                            && lotto5NyNoonTickets.size() == 0 && lotto5NyEveningTickets.size() == 0
                            && lotto55gNyNoonTickets.size() == 0 && lotto55gNyEveningTickets.size() == 0
                            && lotto5RoyalNyNoonTickets.size() == 0 && lotto5RoyalNyEveningTickets.size() == 0
                            && bolotoFlNoonTickets.size() == 0 && bolotoFlEveningTickets.size() == 0
                            && borletFlNoonTickets.size() == 0 && borletFlEveningTickets.size() == 0
                            && mariageFlNoonTickets.size() == 0 && mariageFlEveningTickets.size() == 0
                            && lotto3FlNoonTickets.size() == 0 && lotto3FlEveningTickets.size() == 0
                            && lotto4FlNoonTickets.size() == 0 && lotto4FlEveningTickets.size() == 0
                            && lotto5FlNoonTickets.size() == 0 && lotto5FlEveningTickets.size() == 0
                            && lotto55gFlNoonTickets.size() == 0 && lotto55gFlEveningTickets.size() == 0
                            && lotto5RoyalFlNoonTickets.size() == 0 && lotto5RoyalFlEveningTickets.size() == 0
                            && bolotoGaNoonTickets.size() == 0 && bolotoGaEveningTickets.size() == 0
                            && borletGaNoonTickets.size() == 0 && borletGaEveningTickets.size() == 0
                            && mariageGaNoonTickets.size() == 0 && mariageGaEveningTickets.size() == 0
                            && lotto3GaNoonTickets.size() == 0 && lotto3GaEveningTickets.size() == 0
                            && lotto4GaNoonTickets.size() == 0 && lotto4GaEveningTickets.size() == 0
                            && lotto5GaNoonTickets.size() == 0 && lotto5GaEveningTickets.size() == 0
                            && lotto55gGaNoonTickets.size() == 0 && lotto55gGaEveningTickets.size() == 0
                            && lotto5RoyalGaNoonTickets.size() == 0 && lotto5RoyalGaEveningTickets.size() == 0) {
                        myTicketsNoTicketLayout.setVisibility(View.VISIBLE);
                        myTicketsLayout.setVisibility(View.GONE);
                    } else {
                        myTicketsNoTicketLayout.setVisibility(View.GONE);
                        myTicketsLayout.setVisibility(View.VISIBLE);

                        bolotoFlNoonLayout.setVisibility(bolotoFlNoonTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        mariageFlNoonLayout.setVisibility(mariageFlNoonTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        borletFlNoonLayout.setVisibility(borletFlNoonTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto3FlNoonLayout.setVisibility(lotto3FlNoonTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto4FlNoonLayout.setVisibility(lotto4FlNoonTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto5FlNoonLayout.setVisibility(lotto5FlNoonTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto55gFlNoonLayout.setVisibility(lotto55gFlNoonTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto5RoyalFlNoonLayout.setVisibility(lotto5RoyalFlNoonTickets.size() == 0 ? View.GONE : View.VISIBLE);


                        bolotoFlEveningLayout.setVisibility(bolotoFlEveningTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        mariageFlEveningLayout.setVisibility(mariageFlEveningTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        borletFlEveningLayout.setVisibility(borletFlEveningTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto3FlEveningLayout.setVisibility(lotto3FlEveningTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto4FlEveningLayout.setVisibility(lotto4FlEveningTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto5FlEveningLayout.setVisibility(lotto5FlEveningTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto55gFlEveningLayout.setVisibility(lotto55gFlEveningTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto5RoyalFlEveningLayout.setVisibility(lotto5RoyalFlEveningTickets.size() == 0 ? View.GONE : View.VISIBLE);


                        bolotoNyNoonLayout.setVisibility(bolotoNyNoonTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        mariageNyNoonLayout.setVisibility(mariageNyNoonTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        borletNyNoonLayout.setVisibility(borletNyNoonTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto3NyNoonLayout.setVisibility(lotto3NyNoonTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto4NyNoonLayout.setVisibility(lotto4NyNoonTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto5NyNoonLayout.setVisibility(lotto5NyNoonTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto55gNyNoonLayout.setVisibility(lotto55gNyNoonTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto5RoyalNyNoonLayout.setVisibility(lotto5RoyalNyNoonTickets.size() == 0 ? View.GONE : View.VISIBLE);

                        bolotoNyEveningLayout.setVisibility(bolotoNyEveningTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        mariageNyEveningLayout.setVisibility(mariageNyEveningTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        borletNyEveningLayout.setVisibility(borletNyEveningTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto3NyEveningLayout.setVisibility(lotto3NyEveningTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto4NyEveningLayout.setVisibility(lotto4NyEveningTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto5NyEveningLayout.setVisibility(lotto5NyEveningTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto55gNyEveningLayout.setVisibility(lotto55gNyEveningTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto5RoyalNyEveningLayout.setVisibility(lotto5RoyalNyEveningTickets.size() == 0 ? View.GONE : View.VISIBLE);

                        bolotoGaNoonLayout.setVisibility(bolotoGaNoonTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        maryageGaNoonLayout.setVisibility(mariageGaNoonTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        borletGaNoonLayout.setVisibility(borletGaNoonTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto3GaNoonLayout.setVisibility(lotto3GaNoonTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto4GaNoonLayout.setVisibility(lotto4GaNoonTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto5GaNoonLayout.setVisibility(lotto5GaNoonTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto55GGaNoonLayout.setVisibility(lotto55gGaNoonTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto5RoyalGaNoonLayout.setVisibility(lotto5RoyalGaNoonTickets.size() == 0 ? View.GONE : View.VISIBLE);


                        bolotoGaEveningLayout.setVisibility(bolotoGaEveningTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        maryageGaEveningLayout.setVisibility(mariageGaEveningTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        borletGaEveningLayout.setVisibility(borletGaEveningTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto3GaEveningLayout.setVisibility(lotto3GaEveningTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto4GaEveningLayout.setVisibility(lotto4GaEveningTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto5GaEveningLayout.setVisibility(lotto5GaEveningTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto55GGaEveningLayout.setVisibility(lotto55gGaEveningTickets.size() == 0 ? View.GONE : View.VISIBLE);
                        lotto5RoyalGaEveningLayout.setVisibility(lotto5RoyalGaEveningTickets.size() == 0 ? View.GONE : View.VISIBLE);


                        if (bolotoFlNoonTickets.size() != 0 && filterByBoloto)
                            initRecycler(bolotoFlNoonTicketsRecycler, bolotoFlNoonTickets);
                        if (borletFlNoonTickets.size() != 0 && filterByBorlet)
                            initRecycler(borletFlNoonTicketsRecycler, borletFlNoonTickets);
                        if (mariageFlNoonTickets.size() != 0 && filterByMariage)
                            initRecycler(mariageFlNoonTicketsRecycler, mariageFlNoonTickets);
                        if (lotto3FlNoonTickets.size() != 0 && filterByLotto3)
                            initRecycler(lotto3FlNoonTicketsRecycler, lotto3FlNoonTickets);
                        if (lotto4FlNoonTickets.size() != 0 && filterByLotto4)
                            initRecycler(lotto4FlNoonTicketsRecycler, lotto4FlNoonTickets);
                        if (lotto5FlNoonTickets.size() != 0 && filterByLotto5)
                            initRecycler(lotto5FlNoonTicketsRecycler, lotto5FlNoonTickets);
                        if (lotto55gFlNoonTickets.size() != 0 && filterByLotto55g)
                            initRecycler(lotto55GFlNoonTicketsRecycler, lotto55gFlNoonTickets);
                        if (lotto5RoyalFlNoonTickets.size() != 0 && filterByLotto5Royal)
                            initRecycler(lotto5RoyalFlNoonTicketsRecycler, lotto5RoyalFlNoonTickets);
                        if (bolotoNyEveningTickets.size() != 0 && filterByBoloto)
                            initRecycler(bolotoNyEveningTicketsRecycler, bolotoNyEveningTickets);
                        if (borletNyEveningTickets.size() != 0 && filterByBorlet)
                            initRecycler(borletNyEveningTicketsRecycler, borletNyEveningTickets);
                        if (mariageNyEveningTickets.size() != 0 && filterByMariage)
                            initRecycler(mariageNyEveningTicketsRecycler, mariageNyEveningTickets);
                        if (lotto3NyEveningTickets.size() != 0 && filterByLotto3)
                            initRecycler(lotto3NyEveningTicketsRecycler, lotto3NyEveningTickets);
                        if (lotto4NyEveningTickets.size() != 0 && filterByLotto4)
                            initRecycler(lotto4NyEveningTicketsRecycler, lotto4NyEveningTickets);
                        if (lotto5NyEveningTickets.size() != 0 && filterByLotto5)
                            initRecycler(lotto5NyEveningTicketsRecycler, lotto5NyEveningTickets);
                        if (lotto55gNyEveningTickets.size() != 0 && filterByLotto55g)
                            initRecycler(lotto55GNyEveningTicketsRecycler, lotto55gNyEveningTickets);
                        if (lotto5RoyalNyEveningTickets.size() != 0 && filterByLotto5Royal)
                            initRecycler(lotto5RoyalNyEveningTicketsRecycler, lotto5RoyalNyEveningTickets);
                        if (bolotoNyNoonTickets.size() != 0 && filterByBoloto)
                            initRecycler(bolotoNyNoonTicketsRecycler, bolotoNyNoonTickets);
                        if (borletNyNoonTickets.size() != 0 && filterByBorlet)
                            initRecycler(borletNyNoonTicketsRecycler, borletNyNoonTickets);
                        if (mariageNyNoonTickets.size() != 0 && filterByMariage)
                            initRecycler(mariageNyNoonTicketsRecycler, mariageNyNoonTickets);
                        if (lotto3NyNoonTickets.size() != 0 && filterByLotto3)
                            initRecycler(lotto3NyNoonTicketsRecycler, lotto3NyNoonTickets);
                        if (lotto4NyNoonTickets.size() != 0 && filterByLotto4)
                            initRecycler(lotto4NyNoonTicketsRecycler, lotto4NyNoonTickets);
                        if (lotto5NyNoonTickets.size() != 0 && filterByLotto5)
                            initRecycler(lotto5NyNoonTicketsRecycler, lotto5NyNoonTickets);
                        if (lotto55gNyNoonTickets.size() != 0 && filterByLotto55g)
                            initRecycler(lotto55GNyNoonTicketsRecycler, lotto55gNyNoonTickets);
                        if (lotto5RoyalNyNoonTickets.size() != 0 && filterByLotto5Royal)
                            initRecycler(lotto5RoyalNyNoonTicketsRecycler, lotto5RoyalNyNoonTickets);
                        if (bolotoFlEveningTickets.size() != 0 && filterByBoloto)
                            initRecycler(bolotoFlEveningTicketsRecycler, bolotoFlEveningTickets);
                        if (borletFlEveningTickets.size() != 0 && filterByBorlet)
                            initRecycler(borletFlEveningTicketsRecycler, borletFlEveningTickets);
                        if (mariageFlEveningTickets.size() != 0 && filterByMariage)
                            initRecycler(mariageFlEveningTicketsRecycler, mariageFlEveningTickets);
                        if (lotto3FlEveningTickets.size() != 0 && filterByLotto3)
                            initRecycler(lotto3FlEveningTicketsRecycler, lotto3FlEveningTickets);
                        if (lotto4FlEveningTickets.size() != 0 && filterByLotto4)
                            initRecycler(lotto4FlEveningTicketsRecycler, lotto4FlEveningTickets);
                        if (lotto5FlEveningTickets.size() != 0 && filterByLotto5)
                            initRecycler(lotto5FlEveningTicketsRecycler, lotto5FlEveningTickets);
                        if (lotto55gFlEveningTickets.size() != 0 && filterByLotto55g)
                            initRecycler(lotto55GFlEveningTicketsRecycler, lotto55gFlEveningTickets);
                        if (lotto5RoyalFlEveningTickets.size() != 0 && filterByLotto5Royal)
                            initRecycler(lotto5RoyalFlEveningTicketsRecycler, lotto5RoyalFlEveningTickets);

                        if (bolotoGaNoonTickets.size() != 0 && filterByBoloto)
                            initRecycler(bolotoGaNoonTicketsRecycler, bolotoGaNoonTickets);
                        if (borletGaNoonTickets.size() != 0 && filterByBorlet)
                            initRecycler(borletGaNoonTicketsRecycler, borletGaNoonTickets);
                        if (mariageGaNoonTickets.size() != 0 && filterByMariage)
                            initRecycler(maryageGaNoonTicketsRecycler, mariageGaNoonTickets);
                        if (lotto3GaNoonTickets.size() != 0 && filterByLotto3)
                            initRecycler(lotto3GaNoonTicketsRecycler, lotto3GaNoonTickets);
                        if (lotto4GaNoonTickets.size() != 0 && filterByLotto4)
                            initRecycler(lotto4GaNoonTicketsRecycler, lotto4GaNoonTickets);
                        if (lotto5GaNoonTickets.size() != 0 && filterByLotto5)
                            initRecycler(lotto5GaNoonTicketsRecycler, lotto5GaNoonTickets);
                        if (lotto55gGaNoonTickets.size() != 0 && filterByLotto55g)
                            initRecycler(lotto55GGaNoonTicketsRecycler, lotto55gGaNoonTickets);
                        if (lotto5RoyalGaNoonTickets.size() != 0 && filterByLotto5Royal)
                            initRecycler(lotto5RoyalGaNoonTicketsRecycler, lotto5RoyalGaNoonTickets);

                        if (bolotoGaEveningTickets.size() != 0 && filterByBoloto)
                            initRecycler(bolotoGaEveningTicketsRecycler, bolotoGaEveningTickets);
                        if (borletGaEveningTickets.size() != 0 && filterByBorlet)
                            initRecycler(borletGaEveningTicketsRecycler, borletGaEveningTickets);
                        if (mariageGaEveningTickets.size() != 0 && filterByMariage)
                            initRecycler(maryageGaEveningTicketsRecycler, mariageGaEveningTickets);
                        if (lotto3GaEveningTickets.size() != 0 && filterByLotto3)
                            initRecycler(lotto3GaEveningTicketsRecycler, lotto3GaEveningTickets);
                        if (lotto4GaEveningTickets.size() != 0 && filterByLotto4)
                            initRecycler(lotto4GaEveningTicketsRecycler, lotto4GaEveningTickets);
                        if (lotto5GaEveningTickets.size() != 0 && filterByLotto5)
                            initRecycler(lotto5GaEveningTicketsRecycler, lotto5GaEveningTickets);
                        if (lotto55gGaEveningTickets.size() != 0 && filterByLotto55g)
                            initRecycler(lotto55GGaEveningTicketsRecycler, lotto55gGaEveningTickets);
                        if (lotto5RoyalGaEveningTickets.size() != 0 && filterByLotto5Royal)
                            initRecycler(lotto5RoyalGaEveningTicketsRecycler, lotto5RoyalGaEveningTickets);
                    }
                }
            } else {
                walletPresenter.getUserTickets(new Phone(SharedPreferencesUtil.getPhone()), DateTimeUtil.getDobFormat(dateAndTime.getTimeInMillis()));
            }
        }
    }

    private boolean isNoonDraw(Ticket ticket) {
        Log.d("LOGTAGDRAWTIME", String.valueOf(Integer.parseInt(DateTimeUtil.getTicketDateFormatTime(ticket.getDraw(), DateTimeUtil.HR, getActivity()))));
        return Integer.parseInt(DateTimeUtil.getTicketDateFormatTime(ticket.getDraw(), DateTimeUtil.HR, getActivity())) <= EVENING;
    }

    private void addTicketToList(Ticket ticket, ArrayList<Ticket> unsorted) {
        if (ticket.isGameWinJackpot() || ticket.isGameWin()) {
            unsorted.add(0, ticket);
        } else {
            unsorted.add(ticket);
        }
    }

    private void initRecycler(RecyclerView recyclerView, ArrayList<Ticket> tickets) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TicketsAdapter ticketsAdapter = new TicketsAdapter(tickets, this);
        recyclerView.setAdapter(ticketsAdapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            gamesListClickListener = (OnNavigateToGamesListListener) context;
            onFragmentVisibleListener = (OnFragmentVisibleListener) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (onFragmentVisibleListener != null) {
            onFragmentVisibleListener.onFragmentShowListener(MyTicketsFragment.class.getSimpleName(), true);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (onFragmentVisibleListener != null) {
            onFragmentVisibleListener.onFragmentShowListener(MyTicketsFragment.class.getSimpleName(), false);
            onFragmentVisibleListener = null;
        }
        if (gamesListClickListener != null) {
            gamesListClickListener = null;
        }
    }

    @OnClick(R.id.my_tickets_filter)
    void onFilterClick() {
        FilterTicketsDialog filterTicketsDialog = FilterTicketsDialog.newInstance(filterByBoloto,
                filterByBorlet,
                filterByMariage,
                filterByLotto3,
                filterByLotto4,
                filterByLotto5,
                filterByLotto55g,
                filterByLotto5Royal,
                filterByWin,
                filterByNotAccepted,
                filterByClosed,
                filterByPending);
        filterTicketsDialog.show(getChildFragmentManager(), FilterTicketsDialog.class.getSimpleName());
        filterTicketsDialog.setOnFilterListener(this);
    }


    @OnClick(R.id.select_game)
    void onSelectGameClick() {
        if (gamesListClickListener != null)
            gamesListClickListener.onNavigateToGamesList();
        else FragmentUtil.replaceFragment(getFragmentManager(), new GameListFragment(), true);
    }

    @OnClick(R.id.my_tickets_date)
    void onTicketsDateClick() {
        Locale.setDefault(new Locale(LisaApp.getInstance().getLanguage(getActivity())));
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        DatePickerDialog datePickerDialog = new SpinnerDatePickerDialogBuilder()
                .context(getContext())
                .callback((view, year, monthOfYear, dayOfMonth) -> {
                    dateAndTime.set(year, monthOfYear, dayOfMonth);
                    myTicketsDate.setText(DateTimeUtil.getTicketDateFormatTime(dateAndTime.getTimeInMillis() / DateTimeUtil.SECOND, DATE_PATTERN, getActivity()));
                    walletPresenter.getUserTickets(new Phone(SharedPreferencesUtil.getPhone()), DateTimeUtil.getDobFormat(dateAndTime.getTimeInMillis()));
                })
                .showTitle(false)
                .defaultDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE))
                .maxDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE))
                .showDaySpinner(true)
                .build();
        datePickerDialog.show();
        datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.GREEN);
        datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
    }

    @Override
    public void onTicketClick(Ticket ticket) {
        TicketDetailsDialog ticketDetailsDialog = TicketDetailsDialog.newInstance(ticket);
        ticketDetailsDialog.show(getChildFragmentManager(), TicketDetailsDialog.class.getSimpleName());
    }

    @Override
    public void onApply(boolean filterByBoloto, boolean filterByBorlet, boolean filterByMariage, boolean filterByLotto3,
                        boolean filterByLotto4, boolean filterByLotto5, boolean filterByLotto55g, boolean filterByLotto5Royal,
                        boolean filterByWin, boolean filterByNotAccepted, boolean filterByClosed, boolean filterByPending) {
        this.filterByBoloto = filterByBoloto;
        this.filterByBorlet = filterByBorlet;
        this.filterByMariage = filterByMariage;
        this.filterByLotto3 = filterByLotto3;
        this.filterByLotto4 = filterByLotto4;
        this.filterByLotto5 = filterByLotto5;
        this.filterByLotto55g = filterByLotto55g;
        this.filterByLotto5Royal = filterByLotto5Royal;
        this.filterByClosed = filterByClosed;
        this.filterByPending = filterByPending;
        this.filterByWin = filterByWin;
        this.filterByNotAccepted = filterByNotAccepted;
        walletPresenter.getUserTickets(new Phone(SharedPreferencesUtil.getPhone()), DateTimeUtil.getDobFormat(dateAndTime.getTimeInMillis()));
    }

    public interface OnNavigateToGamesListListener {
        void onNavigateToGamesList();
    }
}
