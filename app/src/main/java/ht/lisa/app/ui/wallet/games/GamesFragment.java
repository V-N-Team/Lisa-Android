package ht.lisa.app.ui.wallet.games;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.GsonBuilder;
import com.matthewtamlin.sliding_intro_screen_library.indicators.DotIndicator;
import com.tapadoo.alerter.Alerter;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ht.lisa.app.LisaApp;
import ht.lisa.app.R;
import ht.lisa.app.model.Draw;
import ht.lisa.app.model.Phone;
import ht.lisa.app.model.Ticket;
import ht.lisa.app.model.response.DrawResponse;
import ht.lisa.app.model.response.RegionDrawResponse;
import ht.lisa.app.model.response.SubscribeResponse;
import ht.lisa.app.model.response.TicketsPurchaseResponse;
import ht.lisa.app.ui.main.MainActivity;
import ht.lisa.app.ui.main.WebViewActivity;
import ht.lisa.app.ui.wallet.BaseWalletFragment;
import ht.lisa.app.util.DateTimeUtil;
import ht.lisa.app.util.EditTextUtil;
import ht.lisa.app.util.FragmentUtil;
import ht.lisa.app.util.JsonUtil;
import ht.lisa.app.util.PinViewTextWatcher;
import ht.lisa.app.util.RxUtil;
import ht.lisa.app.util.SharedPreferencesUtil;
import ht.lisa.app.util.TextUtil;

public class GamesFragment extends BaseWalletFragment {
    public static final String SCREEN_NAME = "GameScreen";

    public static final int VIEWPAGER_MID = 176;

    public static final String GAME = "game";
    public static final String DRAW_SEC = "draw";
    public static final String DRAW_DATE = "drawDate";
    public static final String FL_DRAW = "flDraw";
    public static final String GA_DRAW = "gaDraw";
    public static final String NY_DRAW = "nyDraw";
    public static final String STOP_BEFORE_DRAW = "stopBeforeDraw";
    public static final String JACK_POT = "jackPot";
    public static final String PROGRESSIVE_JACK_POT = "progressiveJackPot";
    public static final String ROYAL5_JACK_POT = "royal5JackPot";

    private static final int COLLAPSED = 0;
    private static final int GAME_RULES = 1;
    private static final int BOLOTO_TICKET_COST = 25;
    private static final int COMBO_SIX = 6;
    private static final int COMBO_TWO = 2;
    private static final int LOTTO5JR_TICKET_COST = 5;
    private static final int ROYAL5_TICKET_COST = 25;

    //games view pager
    @BindView(R.id.games_viewpager)
    ViewPager gamesViewpager;
    @BindView(R.id.games_tabs)
    DotIndicator gamesTabs;
    @BindView(R.id.ny_games_date)
    TextView gamesDateNy;
    @BindView(R.id.fl_games_date)
    TextView gamesDateFl;
    @BindView(R.id.ga_games_date)
    TextView gamesDateGa;
    //next draw
    @BindView(R.id.ny_games_next_hour)
    TextView nyGamesNextHour;
    @BindView(R.id.ny_games_next_min)
    TextView nyGamesNextMin;
    @BindView(R.id.ny_games_next_sec)
    TextView nyGamesNextSec;

    @BindView(R.id.ga_games_next_hour)
    TextView gaGamesNextHour;
    @BindView(R.id.ga_games_next_min)
    TextView gaGamesNextMin;
    @BindView(R.id.ga_games_next_sec)
    TextView gaGamesNextSec;

    @BindView(R.id.fl_games_next_hour)
    TextView flGamesNextHour;
    @BindView(R.id.fl_games_next_min)
    TextView flGamesNextMin;
    @BindView(R.id.fl_games_next_sec)
    TextView flGamesNextSec;
    //ticket header
    @BindView(R.id.games_ticket_header_bg)
    View gamesTicketHeaderBg;
    @BindView(R.id.add_image)
    ImageView addImage;
    @BindView(R.id.games_first_number)
    EditText gamesFirstNumber;
    @BindView(R.id.games_second_number)
    EditText gamesSecondNumber;
    @BindView(R.id.games_third_number)
    EditText gamesThirdNumber;
    @BindView(R.id.games_ticket_bet)
    EditText gamesTicketBet;
    @BindView(R.id.games_ticket_option1)
    CheckBox gamesTicketOption1;
    @BindView(R.id.games_ticket_option2)
    CheckBox gamesTicketOption2;
    @BindView(R.id.games_ticket_option3)
    CheckBox gamesTicketOption3;
    @BindView(R.id.games_combo_rb)
    CheckBox gamesComboRb;
    //tickets list
    @BindView(R.id.tickets_layout)
    LinearLayout ticketsLayout;
    @BindView(R.id.empty_list_layout)
    View emptyListLayout;
    @BindView(R.id.tickets_layout_full)
    LinearLayout ticketsLayoutFull;
    @BindView(R.id.games_total_price_value)
    TextView gamesTotalPriceValue;
    //rules and numbers
    @BindView(R.id.games_rules)
    View gamesRules;
    @BindView(R.id.recent_numbers)
    View gamesNumbers;
    @BindView(R.id.expandable_layout)
    ExpandableLayout expandableLayout;
    @BindView(R.id.game_rules_bottom)
    View gameRulesBottom;
    @BindView(R.id.recent_numbers_bottom)
    View recentNumbersBottom;
    @BindView(R.id.game_rules_text)
    TextView gameRulesText;
    @BindView(R.id.recent_numbers_text)
    TextView recentNumbersText;
    @BindView(R.id.add_button_text)
    TextView addButtonText;
    @BindView(R.id.games_qp)
    ImageView gamesQp;
    @BindView(R.id.ticket_options_layout)
    View ticketOptionsLayout;
    @BindView(R.id.ny_draw_checkbox)
    CheckBox nyDrawCheckbox;
    @BindView(R.id.ga_draw_checkbox)
    CheckBox gaDrawCheckbox;
    @BindView(R.id.fl_draw_checkbox)
    CheckBox flDrawCheckbox;
    @BindView(R.id.fl_draw)
    View flDraw;
    @BindView(R.id.ny_draw)
    View nyDraw;
    @BindView(R.id.ga_draw)
    View gaDraw;

    private ArrayList<String> games;
    private LinkedHashMap<String, ArrayList<Ticket>> allGamesTickets;
    private ArrayList<Ticket> tickets;
    private CountDownTimer nyTimer;
    private CountDownTimer flTimer;
    private CountDownTimer gaTimer;
    private GamesItemAdapter gamesItemAdapter;
    private String game;
    int comboRequestCount;
    private ArrayList<CheckBox> optionCheckBoxes;
    private ArrayList<EditText> numbersEditTexts;
    private ArrayList<String> comboNumList;
    private int editTextSize;
    private Ticket editedTicket;
    private String maryajComboNum;
    private RegionDrawResponse flDrawResponse;
    private RegionDrawResponse nyDrawResponse;
    private RegionDrawResponse gaDrawResponse;

    private GamesTicketAdapter gamesTicketAdapter;
    private int informationState;
    private String jackPot;
    private String progressiveJackPot;
    private String royal5JackPot;
    private long playableFlDrawTime;
    private long playableGaDrawTime;
    private long playableNyDrawTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allGamesTickets = new LinkedHashMap<>();
        informationState = COLLAPSED;
        if (getArguments() != null) {
            game = getArguments().getString(GAME).toLowerCase();
            flDrawResponse = (RegionDrawResponse) getArguments().getSerializable(FL_DRAW);
            nyDrawResponse = (RegionDrawResponse) getArguments().getSerializable(NY_DRAW);
            gaDrawResponse = (RegionDrawResponse) getArguments().getSerializable(GA_DRAW);
            jackPot = getArguments().getString(JACK_POT);
            progressiveJackPot = getArguments().getString(PROGRESSIVE_JACK_POT);
            royal5JackPot = getArguments().getString(ROYAL5_JACK_POT);
            if (flDrawResponse != null && nyDrawResponse != null && gaDrawResponse != null) {
                playableFlDrawTime = isCurrentTimeBigger(flDrawResponse.getLastStop()) ? flDrawResponse.getNext() : flDrawResponse.getDraw();
                playableNyDrawTime = isCurrentTimeBigger(nyDrawResponse.getLastStop()) ? nyDrawResponse.getNext() : nyDrawResponse.getDraw();
                playableGaDrawTime = isCurrentTimeBigger(gaDrawResponse.getLastStop()) ? gaDrawResponse.getNext() : gaDrawResponse.getDraw();
            }
        }
    }

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_games, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextSize = gamesFirstNumber.getLayoutParams().height;
        initNextDrawObjects();
        initTicketHeaderObjects();
        initPager();
        setRadioGroupListener();
        initOptionEditTextArray();
        setOptionCheckListener();
        showGameGuideDialog();
        getTicketsFromMemory();
        initDrawTypeCheckboxes();
        //setMinMaxBetByGame();
    }

    private boolean isCurrentTimeBigger(long time) {
        return System.currentTimeMillis() >= time;
    }

    private void initDrawTypeCheckbox(CheckBox checkBox) {
        Drawable chbDrawable = AppCompatResources.getDrawable(getContext(), R.drawable.check_box_button_selected);
        Drawable chbDrawableUnchecked = AppCompatResources.getDrawable(getContext(), R.drawable.check_box_button_unselected);

        long drawTime = checkBox.getId() == R.id.ny_draw_checkbox ?
                playableNyDrawTime :
                checkBox.getId() == R.id.fl_draw_checkbox ? playableFlDrawTime : playableGaDrawTime;

        Drawable drawTimeDrawable = AppCompatResources.getDrawable(getContext(),
                DateTimeUtil.isDayDraw(drawTime, getActivity()) ?
                        R.drawable.ic_sun_white :
                        DateTimeUtil.isEveningDraw(drawTime, getActivity()) ? R.drawable.ic_evening_draw_14 :
                                R.drawable.ic_white_moon);

        checkBox.setCompoundDrawablesWithIntrinsicBounds(drawTimeDrawable, null, chbDrawable, null);
        flDrawCheckbox.setCompoundDrawablesWithIntrinsicBounds(drawTimeDrawable, null, checkBox.isChecked() ? chbDrawable : chbDrawableUnchecked, null);
        gaDrawCheckbox.setCompoundDrawablesWithIntrinsicBounds(drawTimeDrawable, null, checkBox.isChecked() ? chbDrawable : chbDrawableUnchecked, null);

        if (checkBox.getId() == R.id.ny_draw_checkbox) {
            nyDraw.setBackground(AppCompatResources.getDrawable(getContext(), DateTimeUtil.isDayDraw(playableNyDrawTime, getActivity()) ? R.drawable.day_gradient_bg : R.drawable.night_gradient_bg));
        } else if (checkBox.getId() == R.id.ga_draw_checkbox) {
            gaDraw.setBackground(AppCompatResources.getDrawable(getContext(), DateTimeUtil.isDayDraw(playableGaDrawTime, getActivity()) ? R.drawable.day_gradient_bg : R.drawable.night_gradient_bg));
        } else {
            flDraw.setBackground(AppCompatResources.getDrawable(getContext(), DateTimeUtil.isDayDraw(playableFlDrawTime, getActivity()) ? R.drawable.day_gradient_bg : R.drawable.night_gradient_bg));
        }
        checkBox.setOnCheckedChangeListener(drawTypeCheckedChangeListener);
    }

    private void initDrawTypeCheckboxes() {
        initDrawTypeCheckbox(nyDrawCheckbox);
        initDrawTypeCheckbox(flDrawCheckbox);
        initDrawTypeCheckbox(gaDrawCheckbox);
    }


    @OnClick({R.id.ny_draw, R.id.fl_draw, R.id.ga_draw})
    void onDrawClick(View v) {
        if ((v.getId() == R.id.ny_draw && nyDrawCheckbox.isChecked() && !flDrawCheckbox.isChecked() && !gaDrawCheckbox.isChecked())
                || (v.getId() == R.id.ga_draw && gaDrawCheckbox.isChecked() && !nyDrawCheckbox.isChecked() && !flDrawCheckbox.isChecked())
                || (v.getId() == R.id.fl_draw && flDrawCheckbox.isChecked() && !nyDrawCheckbox.isChecked() && !gaDrawCheckbox.isChecked()))
            return;

        if (v.getId() == R.id.ny_draw) {
            nyDrawCheckbox.setChecked(!nyDrawCheckbox.isChecked());
        } else if (v.getId() == R.id.ga_draw) {
            gaDrawCheckbox.setChecked(!gaDrawCheckbox.isChecked());
        } else {
            flDrawCheckbox.setChecked(!flDrawCheckbox.isChecked());
        }
    }


    private void initNextDrawObjects() {
        if (getContext() == null) return;

        if (flDrawResponse != null && nyDrawResponse != null) {
            gamesDateFl.setText(String.format("%s FL", DateTimeUtil.getGameListDateFormat(flDrawResponse.getDraw(), getActivity())));
            gamesDateNy.setText(String.format("%s NY", DateTimeUtil.getGameListDateFormat(nyDrawResponse.getDraw(), getActivity())));
            gamesDateGa.setText(String.format("%s GA", DateTimeUtil.getGameListDateFormat(gaDrawResponse.getDraw(), getActivity())));

            startCountDownTimers(flDrawResponse.getLastLeft(), nyDrawResponse.getLastLeft(), gaDrawResponse.getLastLeft());
            flGamesNextHour.setBackground(AppCompatResources.getDrawable(getContext(), DateTimeUtil.isDayDraw(flDrawResponse.getDraw(), getActivity()) ? R.drawable.ic_day_draw_time_frame : R.drawable.ic_night_draw_time_frame));
            flGamesNextMin.setBackground(AppCompatResources.getDrawable(getContext(), DateTimeUtil.isDayDraw(flDrawResponse.getDraw(), getActivity()) ? R.drawable.ic_day_draw_time_frame : R.drawable.ic_night_draw_time_frame));
            flGamesNextSec.setBackground(AppCompatResources.getDrawable(getContext(), DateTimeUtil.isDayDraw(flDrawResponse.getDraw(), getActivity()) ? R.drawable.ic_day_draw_time_frame : R.drawable.ic_night_draw_time_frame));

            nyGamesNextHour.setBackground(AppCompatResources.getDrawable(getContext(), DateTimeUtil.isDayDraw(nyDrawResponse.getDraw(), getActivity()) ? R.drawable.ic_day_draw_time_frame : R.drawable.ic_night_draw_time_frame));
            nyGamesNextMin.setBackground(AppCompatResources.getDrawable(getContext(), DateTimeUtil.isDayDraw(nyDrawResponse.getDraw(), getActivity()) ? R.drawable.ic_day_draw_time_frame : R.drawable.ic_night_draw_time_frame));
            nyGamesNextSec.setBackground(AppCompatResources.getDrawable(getContext(), DateTimeUtil.isDayDraw(nyDrawResponse.getDraw(), getActivity()) ? R.drawable.ic_day_draw_time_frame : R.drawable.ic_night_draw_time_frame));

            gaGamesNextHour.setBackground(AppCompatResources.getDrawable(getContext(), DateTimeUtil.isDayDraw(nyDrawResponse.getDraw(), getActivity()) ? R.drawable.ic_day_draw_time_frame : R.drawable.ic_night_draw_time_frame));
            gaGamesNextMin.setBackground(AppCompatResources.getDrawable(getContext(), DateTimeUtil.isDayDraw(nyDrawResponse.getDraw(), getActivity()) ? R.drawable.ic_day_draw_time_frame : R.drawable.ic_night_draw_time_frame));
            gaGamesNextSec.setBackground(AppCompatResources.getDrawable(getContext(), DateTimeUtil.isDayDraw(nyDrawResponse.getDraw(), getActivity()) ? R.drawable.ic_day_draw_time_frame : R.drawable.ic_night_draw_time_frame));


            gamesDateFl.setCompoundDrawablesWithIntrinsicBounds(getDrawTimerDrawable(flDrawResponse), null, null, null);
            gamesDateNy.setCompoundDrawablesWithIntrinsicBounds(getDrawTimerDrawable(nyDrawResponse), null, null, null);
            gamesDateGa.setCompoundDrawablesWithIntrinsicBounds(getDrawTimerDrawable(gaDrawResponse), null, null, null);
        }
    }

    private Drawable getDrawTimerDrawable(RegionDrawResponse response) {
        Drawable day = AppCompatResources.getDrawable(getContext(), R.drawable.ic_grey_sun);
        Drawable night = AppCompatResources.getDrawable(getContext(), R.drawable.ic_grey_moon);
        Drawable evening = AppCompatResources.getDrawable(getContext(), R.drawable.ic_evening_draw_14);
        evening.setTint(ContextCompat.getColor(getContext(), R.color.lightGray));

        return DateTimeUtil.isDayDraw(response.getDraw(), getActivity()) ? day :
                DateTimeUtil.isNightDraw(response.getDraw(), getActivity()) ?
                        night : evening;
    }

    private Drawable getDrawCheckboxDrawable(RegionDrawResponse response) {
        Drawable day = AppCompatResources.getDrawable(getContext(), R.drawable.ic_sun_white);
        Drawable night = AppCompatResources.getDrawable(getContext(), R.drawable.ic_white_moon);
        Drawable evening = AppCompatResources.getDrawable(getContext(), R.drawable.ic_evening_draw_14);

        return DateTimeUtil.isDayDraw(response.getDraw(), getActivity()) ? day :
                DateTimeUtil.isNightDraw(response.getDraw(), getActivity()) ?
                        night : evening;
    }

    private void initTicketHeaderObjects() {
        if (getContext() == null || game == null) return;
        gamesTicketHeaderBg.setBackgroundColor(ContextCompat.getColor(getContext(), Draw.getTicketHeaderBG(game)));
        initNumbersEditTexts();
        setTextChangeListener();
        if (LisaApp.getInstance().isKreyol(getActivity())
                || LisaApp.getInstance().isFrench(getActivity()))
            addButtonText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_smaller_9sp));
        addImage.setImageDrawable(AppCompatResources.getDrawable(getContext(), game.equals(Draw.LOTTO5) ? R.drawable.ic_add_button_orange : R.drawable.ic_add_new));
        gamesQp.setImageDrawable(AppCompatResources.getDrawable(getContext(), Draw.getQpDrawableByGame(game)));
        if (game.equals(Draw.LOTTO4) || game.equals(Draw.LOTTO5)) {
            ticketOptionsLayout.setBackgroundColor(ContextCompat.getColor(getContext(), Draw.getOptionsBgByName(game)));
            ticketOptionsLayout.setVisibility(View.VISIBLE);

            gamesTicketOption2.setChecked(false);
            gamesTicketOption3.setChecked(false);
            gamesTicketOption1.setChecked(true);
        } else {
            ticketOptionsLayout.setVisibility(View.GONE);
        }
        EditTextUtil.setFocusOnFirstEmptyPosition(numbersEditTexts, getContext(), true);
        gamesFirstNumber.setHint(game.equals(Draw.LOTTO5) || game.equals(Draw.LOTTO3) || game.equals(Draw.LOTTO5JR) || game.equals(Draw.LOTTO5P5) || game.equals(Draw.LOTTO5ROYAL) ? R.string.xxx : R.string.xx);
        gamesFirstNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(game.equals(Draw.LOTTO5) || game.equals(Draw.LOTTO3) || game.equals(Draw.LOTTO5P5) || game.equals(Draw.LOTTO5JR) || game.equals(Draw.ROYAL5) ? 3 : 2)});
        gamesSecondNumber.setVisibility(game.equals(Draw.LOTTO3) || game.equals(Draw.BOLET) ? View.GONE : View.VISIBLE);
        gamesThirdNumber.setVisibility(game.equals(Draw.BOLOTO) ? View.VISIBLE : View.GONE);
        gamesTicketBet.setVisibility(game.equals(Draw.BOLOTO) || game.equals(Draw.LOTTO5JR) || game.equals(Draw.LOTTO5P5) || game.equals(Draw.LOTTO5ROYAL) ? View.GONE : View.VISIBLE);
        gamesTicketOption1.setVisibility(game.equals(Draw.LOTTO5) || game.equals(Draw.LOTTO4) ? View.VISIBLE : View.GONE);
        gamesTicketOption2.setVisibility(game.equals(Draw.LOTTO5) || game.equals(Draw.LOTTO4) ? View.VISIBLE : View.GONE);
        gamesTicketOption3.setVisibility(game.equals(Draw.LOTTO5) || game.equals(Draw.LOTTO4) ? View.VISIBLE : View.GONE);
        gamesComboRb.setText(getStringFromResource(game.equals(Draw.BOLOTO) ? R.string.combo6 : R.string.combo_));
        gamesComboRb.setVisibility(game.equals(Draw.BOLOTO) || game.equals(Draw.LOTTO3) || game.equals(Draw.MARIAGE) ? View.VISIBLE : View.GONE);
    }

    private void showGameGuideDialog() {
        if (isGameGuideNeeded()) {
            GameGuideDialog gameGuideDialog = new GameGuideDialog(game);
            if (getFragmentManager() != null) {
                gameGuideDialog.show(getFragmentManager(), GameGuideDialog.class.getSimpleName());
            }
        }
    }

    private boolean isGameGuideNeeded() {
        if (SharedPreferencesUtil.getGameGuideSet() == null) return true;
        for (String gameName : SharedPreferencesUtil.getGameGuideSet()) {
            if (gameName.equals(game)) return false;
        }
        return true;
    }

    private void initPager() {
        games = new ArrayList<>();
        games.addAll(Draw.getDrawNameList());
        gamesItemAdapter = new GamesItemAdapter(getChildFragmentManager(), games, jackPot, progressiveJackPot, royal5JackPot);
        gamesViewpager.setAdapter(gamesItemAdapter);
        gamesViewpager.setCurrentItem(getGamePosition(game) + VIEWPAGER_MID);
        gamesTabs.setSelectedItem(getGamePosition(game), true);
        gamesItemAdapter.notifyDataSetChanged();
        gamesViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                position = position % games.size();
                gamesTabs.setSelectedItem(position, true);
                game = gamesItemAdapter.getGameName(position);
                //setMinMaxBetByGame();
                initTicketHeaderObjects();
                setTicketNumberFields("", game);
                setTicketBetField(-1);
                setOptionChecking(-1);
                editedTicket = null;
                gamesComboRb.setChecked(false);
                showGameGuideDialog();

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private boolean isBetInLimit() {
        int bet = (int) Float.parseFloat(gamesTicketBet.getText().toString());
        int messageMaxBet = 0;
        boolean inLimit = true;
        switch (game) {
            case Draw.BOLET:
                if (bet > 10000 || bet < 1) {
                    messageMaxBet = 10000;
                    inLimit = false;
                }
                break;
            case Draw.MARIAGE:
                if (bet > 5000 || bet < 1) {
                    messageMaxBet = 5000;
                    inLimit = false;
                }
                break;
            case Draw.LOTTO3:
                if (bet > 1000 || bet < 1) {
                    messageMaxBet = 1000;
                    inLimit = false;
                }
                break;
            case Draw.LOTTO4:
                if (bet > 250 || bet < 1) {
                    messageMaxBet = 250;
                    inLimit = false;
                }
                break;
            case Draw.LOTTO5:
                if (bet > 75 || bet < 1) {
                    messageMaxBet = 75;
                    inLimit = false;
                }
                break;
        }
        if (!inLimit) {
            gamesTicketBet.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.error_bet));
            new Handler().postDelayed(() -> {
                gamesTicketBet.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.ticket_bet_bg));
                gamesTicketBet.getText().clear();
            }, 700);
            callAlerter(messageMaxBet);
        }
        return inLimit;
    }

    private void callAlerter(int messageMaxBet) {
        Alerter alerter = Alerter.create(getActivity(), R.layout.dialog_error_top_sheet);
        alerter.setDuration(2000);
        RelativeLayout layout = (RelativeLayout) alerter.getLayoutContainer();
        if (layout != null) {
            alerter.getLayoutContainer().findViewById(R.id.top_sheet_error_close_icon).setOnClickListener(view ->
                    Alerter.hide());
            alerter.setBackgroundColorRes(R.color.errorBgColor);
            ((TextView) alerter.getLayoutContainer().findViewById(R.id.top_sheet_error_message)).setText(String.format(getString(R.string.bet_limits_), messageMaxBet));
        }
        alerter.show();
    }

    private void setRadioGroupListener() {
        gamesComboRb.setOnCheckedChangeListener((group, checkedId) -> {
            gamesComboRb.setCompoundDrawablesWithIntrinsicBounds(null, null, checkedId ? AppCompatResources.getDrawable(getContext(), R.drawable.check_box_button_selected) : AppCompatResources.getDrawable(getContext(), R.drawable.check_box_button_unselected), null);
        });
        gamesComboRb.setChecked(false);
    }

    private boolean isQPedited() {
        for (EditText editText : numbersEditTexts) {
            if (editText.getText().toString().equals(getStringFromResource(R.string.p)) || editText.getText().toString().equals(getStringFromResource(R.string.q)))
                return true;
        }
        return false;
    }

    private void getTicketsFromMemory() {
        if (SharedPreferencesUtil.getTickets() != null) {
            ArrayList<Ticket> tickets = JsonUtil.getTicketsFromJson(SharedPreferencesUtil.getTickets());
            for (Ticket ticket : tickets) {
                addTicket(ticket.getName(), ticket);
            }
            setGamesTicketLayout(allGamesTickets);
        }
    }

    private void setTextChangeListener() {
        for (EditText editText : numbersEditTexts) {
            editText.addTextChangedListener(new PinViewTextWatcher(numbersEditTexts, getContext()) {
                @Override
                public void afterTextChanged(Editable s) {
                    editText.setBackgroundResource(s.length() == 0 ? R.drawable.empty_number : R.drawable.filled_number);
                    editText.getLayoutParams().height = s.length() == 0 ? editTextSize : editTextSize + 10;
                    editText.getLayoutParams().width = s.length() == 0 ? editTextSize : editTextSize + 10;
                    if (s.length() == getMaxNumberCount(editText)) {
                        EditTextUtil.changeEditTextFocus(getContext(), numbersEditTexts, s.length() == 0);
                    }

                    gamesTicketBet.setEnabled(getNumCount() == Draw.getGameNumCount(game) || isQP(game));
                    if (getNumCount() != Draw.getGameNumCount(game) && !isQP(game)) {
                        gamesTicketBet.getText().clear();
                    }
                }
            });
        }
    }

    private int getMaxNumberCount(EditText editText) {
        return (Draw.LOTTO3.equals(game) || Draw.LOTTO5.equals(game) || Draw.LOTTO5P5.equals(game) || Draw.LOTTO5JR.equals(game) || Draw.LOTTO5P5.equals(game.toLowerCase()) || Draw.ROYAL5.equals(game.toLowerCase())) && editText == gamesFirstNumber ? 3 : 2;
    }

    private void startCountDownTimers(long flTime, long nyTime, long gaTime) {
        if (flTimer != null) {
            flTimer.cancel();
        }
        if (nyTimer != null) {
            nyTimer.cancel();
        }
        if (gaTimer != null) {
            gaTimer.cancel();
        }
        long fl = flTime - System.currentTimeMillis();
        long ny = nyTime - System.currentTimeMillis();
        long ga = gaTime - System.currentTimeMillis();
        flTimer = new CountDownTimer(fl, DateTimeUtil.SECOND) {
            public void onTick(long millisUntilFinished) {
                if (getContext() != null) {
                    if (isCurrentTimeBigger(flDrawResponse.getLastStop()) && playableFlDrawTime != flDrawResponse.getNext()) {
                        playableFlDrawTime = isCurrentTimeBigger(flDrawResponse.getLastStop()) ? flDrawResponse.getNext() : flDrawResponse.getDraw();
                        initDrawTypeCheckbox(flDrawCheckbox);
                    }
                    flGamesNextHour.setText(String.format(getStringFromResource(R.string.h), DateTimeUtil.getFormattedTime(millisUntilFinished, DateTimeUtil.HR)));
                    flGamesNextMin.setText(String.format(getStringFromResource(R.string.m), DateTimeUtil.getFormattedTime(millisUntilFinished, DateTimeUtil.MIN)));
                    flGamesNextSec.setText(String.format(getStringFromResource(R.string.s), DateTimeUtil.getFormattedTime(millisUntilFinished, DateTimeUtil.SEC)));
                }
            }

            public void onFinish() {
                if (getContext() != null) {
                    flGamesNextHour.setText(getStringFromResource(R.string.zero));
                    flGamesNextMin.setText(getStringFromResource(R.string.zero));
                    flGamesNextSec.setText(getStringFromResource(R.string.zero));
                }
            }
        }.start();

        gaTimer = new CountDownTimer(ga, DateTimeUtil.SECOND) {
            public void onTick(long millisUntilFinished) {
                if (getContext() != null) {
                    if (isCurrentTimeBigger(gaDrawResponse.getLastStop()) && playableGaDrawTime != gaDrawResponse.getNext()) {
                        playableGaDrawTime = isCurrentTimeBigger(gaDrawResponse.getLastStop()) ? gaDrawResponse.getNext() : gaDrawResponse.getDraw();
                        initDrawTypeCheckbox(gaDrawCheckbox);
                    }
                    gaGamesNextHour.setText(String.format(getStringFromResource(R.string.h), DateTimeUtil.getFormattedTime(millisUntilFinished, DateTimeUtil.HR)));
                    gaGamesNextMin.setText(String.format(getStringFromResource(R.string.m), DateTimeUtil.getFormattedTime(millisUntilFinished, DateTimeUtil.MIN)));
                    gaGamesNextSec.setText(String.format(getStringFromResource(R.string.s), DateTimeUtil.getFormattedTime(millisUntilFinished, DateTimeUtil.SEC)));
                }
            }

            public void onFinish() {
                if (getContext() != null) {
                    gaGamesNextHour.setText(getStringFromResource(R.string.zero));
                    gaGamesNextMin.setText(getStringFromResource(R.string.zero));
                    gaGamesNextSec.setText(getStringFromResource(R.string.zero));
                }
            }
        }.start();

        nyTimer = new CountDownTimer(ny, DateTimeUtil.SECOND) {
            public void onTick(long millisUntilFinished) {
                if (getContext() != null) {
                    if (isCurrentTimeBigger(nyDrawResponse.getLastStop()) && playableNyDrawTime != nyDrawResponse.getNext()) {
                        playableNyDrawTime = isCurrentTimeBigger(nyDrawResponse.getLastStop()) ? nyDrawResponse.getNext() : nyDrawResponse.getDraw();
                        initDrawTypeCheckbox(nyDrawCheckbox);
                    }
                    nyGamesNextHour.setText(String.format(getStringFromResource(R.string.h), DateTimeUtil.getFormattedTime(millisUntilFinished, DateTimeUtil.HR)));
                    nyGamesNextMin.setText(String.format(getStringFromResource(R.string.m), DateTimeUtil.getFormattedTime(millisUntilFinished, DateTimeUtil.MIN)));
                    nyGamesNextSec.setText(String.format(getStringFromResource(R.string.s), DateTimeUtil.getFormattedTime(millisUntilFinished, DateTimeUtil.SEC)));

                }
            }

            public void onFinish() {
                if (getContext() != null) {
                    nyGamesNextHour.setText(getStringFromResource(R.string.zero));
                    nyGamesNextMin.setText(getStringFromResource(R.string.zero));
                    nyGamesNextSec.setText(getStringFromResource(R.string.zero));
                }
            }
        }.start();
    }

    private final CompoundButton.OnCheckedChangeListener drawTypeCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton view, boolean isChecked) {
            Drawable chbDrawable = AppCompatResources.getDrawable(getContext(), isChecked ? R.drawable.check_box_button_selected : R.drawable.check_box_button_unselected);
            Drawable nyDrawDrawable = null;
            Drawable flDrawDrawable = null;
            Drawable gaDrawDrawable = null;
            if (flDrawResponse != null) {
                flDrawDrawable = getDrawCheckboxDrawable(flDrawResponse);
            }
            if (nyDrawResponse != null) {
                nyDrawDrawable = getDrawCheckboxDrawable(nyDrawResponse);
            }
            if (gaDrawResponse != null) {
                gaDrawDrawable = getDrawCheckboxDrawable(gaDrawResponse);
            }

            if (nyDrawDrawable != null && view.getId() == R.id.ny_draw_checkbox || flDrawDrawable != null) {
                view.setCompoundDrawablesWithIntrinsicBounds(view.getId() == R.id.ny_draw_checkbox ?
                                nyDrawDrawable :
                                view.getId() == R.id.fl_draw_checkbox ? flDrawDrawable : gaDrawDrawable
                        , null, chbDrawable, null);
            }
        }
    };

    private int getGamePosition(String name) {
        int position = 0;
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).equals(name)) {
                position = i;
                break;
            }
        }
        return position;
    }

    private String getNum() {
        if (game.equals(Draw.MARIAGE) && gamesComboRb.isChecked() && gamesFirstNumber.getText().toString().equals(getString(R.string.qp))) {
            gamesFirstNumber.setText(TextUtil.getRandomNumberString(2));
            gamesSecondNumber.setText(TextUtil.getRandomNumberString(2));
            if (gamesFirstNumber.getText().equals(gamesSecondNumber.getText()))
                gamesSecondNumber.setText(TextUtil.getRandomNumberString(2));
        }
        return getStringFromResource(R.string.qp).equals(gamesFirstNumber.getText().toString()) ?
                null :
                gamesFirstNumber.getText().toString() + gamesSecondNumber.getText().toString() + gamesThirdNumber.getText().toString();
    }

    private int getNumCount() {
        int numCount = 0;
        for (EditText editText : numbersEditTexts) {
            numCount += editText.getText().toString().length();
        }
        return numCount;
    }

    private int getTicketCost() {
        String numOne = gamesFirstNumber.getText().toString();
        String numTwo = gamesSecondNumber.getText().toString();
        String numThree = gamesThirdNumber.getText().toString();
        if (game.equals(Draw.BOLOTO)) {
            if (gamesComboRb.isChecked()) {
                int cost;
                try {
                    if (numOne.equals(getString(R.string.qp))) {
                        cost = BOLOTO_TICKET_COST * COMBO_SIX;
                    } else {
                        cost = BOLOTO_TICKET_COST * TextUtil.getThreePermutations(numOne, numTwo, numThree).size();
                    }
                } catch (Exception e) {
                    cost = BOLOTO_TICKET_COST * COMBO_SIX;
                }
                return cost;
            } else {
                return BOLOTO_TICKET_COST;
            }
        } else if (game.equals(Draw.LOTTO5JR) || game.equals(Draw.LOTTO5P5)) {
            return LOTTO5JR_TICKET_COST;
        } else if (game.equals(Draw.ROYAL5)) {
            return ROYAL5_TICKET_COST;
        } else if (game.equals(Draw.MARIAGE) && gamesComboRb.isChecked()) {
            int combo;
            try {
                combo = TextUtil.getTwoPermutationsMaryajNew(maryajComboNum.substring(0, 2), maryajComboNum.substring(2, 4), maryajComboNum.substring(4, 6)).size();
            } catch (Exception e) {
                combo = COMBO_SIX;
            }
            return gamesTicketBet.getText().toString().isEmpty() ? 0 : Integer.parseInt(gamesTicketBet.getText().toString()) * combo;
        } else if (game.equals(Draw.LOTTO3) && gamesComboRb.isChecked()) {
            int step = 1;
            if (numOne.equals(getString(R.string.qp)))
                return gamesTicketBet.getText().toString().isEmpty() ? 0 : Integer.parseInt(gamesTicketBet.getText().toString()) * COMBO_SIX;
            else
                return gamesTicketBet.getText().toString().isEmpty() ? 0 : Integer.parseInt(gamesTicketBet.getText().toString()) * TextUtil.getThreePermutations(TextUtil.getStringArray(numOne, step)[0],
                        TextUtil.getStringArray(numOne, step)[1], TextUtil.getStringArray(numOne, step)[2]).size();
        } else {
            return gamesTicketBet.getText().toString().isEmpty() ? 0 : Integer.parseInt(gamesTicketBet.getText().toString());
        }

    }

    private boolean isNumFull() {
        return (isQP(game)
                || getNumCount() == Draw.getGameNumCount(game))
                && (!Draw.isBetNecessary(game) || !gamesTicketBet.getText().toString().isEmpty());
    }

    private boolean isQP(String gameName) {
        int qpCount = 0;
        for (EditText editText : numbersEditTexts) {
            if (editText.getText().toString().equals(getStringFromResource(R.string.qp)))
                qpCount++;
        }
        return qpCount == Draw.getGameNumCount(gameName) / 2;
    }

    private int getTotalPrice() {
        int totalPrice = 0;
        for (Map.Entry<String, ArrayList<Ticket>> entry : allGamesTickets.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                totalPrice += entry.getValue().get(i).getComboCost();
            }
        }
        return totalPrice;
    }

    private ArrayList<Integer> getGameType() {
        ArrayList<Integer> options = new ArrayList<>();

        if (gamesTicketOption1.isChecked() || gamesTicketOption2.isChecked() || gamesTicketOption3.isChecked()) {
            if (gamesTicketOption1.isChecked()) {
                options.add(1);
            }
            if (gamesTicketOption2.isChecked()) {
                options.add(2);
            }
            if (gamesTicketOption3.isChecked()) {
                options.add(3);
            }
        } else {
            options.add(0);
        }

        return options;
    }

    private void initOptionEditTextArray() {
        optionCheckBoxes = new ArrayList<>(Arrays.asList(gamesTicketOption1, gamesTicketOption2, gamesTicketOption3));
    }

    private void initNumbersEditTexts() {
        switch (game.toLowerCase()) {
            case Draw.BOLOTO:
                numbersEditTexts = new ArrayList<>(Arrays.asList(gamesFirstNumber, gamesSecondNumber, gamesThirdNumber));
                break;

            case Draw.LOTTO3:
            case Draw.BOLET:
                numbersEditTexts = new ArrayList<>(Collections.singletonList(gamesFirstNumber));
                break;

            default:
                numbersEditTexts = new ArrayList<>(Arrays.asList(gamesFirstNumber, gamesSecondNumber));
                break;
        }
    }

    private void setOptionCheckListener() {
        gamesTicketOption1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            gamesTicketOption1.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(getContext(), isChecked ? R.drawable.check_box_button_selected : R.drawable.check_box_button_unselected), null);
        });
        gamesTicketOption2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            gamesTicketOption2.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(getContext(), isChecked ? R.drawable.check_box_button_selected : R.drawable.check_box_button_unselected), null);
        });
        gamesTicketOption3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            gamesTicketOption3.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(getContext(), isChecked ? R.drawable.check_box_button_selected : R.drawable.check_box_button_unselected), null);
        });
    }

    private void setOptionChecking(int position) {
        for (int i = 0; i < optionCheckBoxes.size(); i++) {
            optionCheckBoxes.get(i).setChecked(i == position);
        }

        if (game.equals(Draw.LOTTO5) || game.equals(Draw.LOTTO4))
            gamesTicketOption1.setChecked(true);
    }

    private void sortTickersHashMap() {
        ArrayList<String> keys = new ArrayList<>(allGamesTickets.keySet());
        Collections.sort(keys, (o1, o2) ->
                Draw.getOrderByName(o2) - Draw.getOrderByName(o1));

        LinkedHashMap<String, ArrayList<Ticket>> newHash = new LinkedHashMap<>();
        for (String key : keys) {
            newHash.put(key, allGamesTickets.get(key));
        }
        this.allGamesTickets = newHash;
    }

    private void setGamesTicketLayout(HashMap<String, ArrayList<Ticket>> allTickets) {
        ticketsLayout.removeAllViews();
        sortTickersHashMap();
        ArrayList<String> keys = new ArrayList<>(allTickets.keySet());
        for (int i = 0; i < keys.size(); i++) {
            String gameName = keys.get(i);
            ArrayList<Ticket> tickets = allTickets.get(gameName);
            if (getContext() == null) return;
            RecyclerView gamesTicketRecyclerView = new RecyclerView(getContext());
            NestedScrollView nsv = new NestedScrollView(getContext());
            nsv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            gamesTicketRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            gamesTicketAdapter = new GamesTicketAdapter(getContext(), tickets, new GamesTicketAdapter.OnTicketElementClickListener() {
                @Override
                public void onSubscribeClick(Ticket ticket, boolean subscribe) {
                    String game = ticket.getName();
                    if (game != null) {
                        if (subscribe && (game.equals(Draw.BOLOTO) || game.equals(Draw.LOTTO3) || game.equals(Draw.LOTTO5JR) || game.equals(Draw.LOTTO5P5) || game.equals(Draw.ROYAL5))) {
                            if (getFragmentManager() != null) {
                                GamesTicketDialog gamesTicketDialog = new GamesTicketDialog(ticket.getNum(), ticket.getName(), false, new GamesTicketDialog.OnConfirmButtonClickListener() {
                                    @Override
                                    public void onConfirmButtonClick() {
                                        subscribeTicket(ticket, true);
                                    }

                                    @Override
                                    public void onCancel() {
                                        subscribeTicket(ticket, false);
                                        gamesTicketAdapter.unsubscribeTicket(ticket);
                                    }
                                });
                                gamesTicketDialog.show(getFragmentManager(), GamesTicketDialog.class.getSimpleName());
                            }
                        } else {
                            subscribeTicket(ticket, false);
                        }
                    }
                }

                @Override
                public void onDeleteClick(Ticket ticket) {
                    deleteTicket(ticket);
                    setGamesTicketLayout(allTickets);
                    setTicketBetField(-1);
                }

            });
            gamesTicketRecyclerView.setAdapter(gamesTicketAdapter);
            gamesTicketRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            gamesTicketRecyclerView.setBackgroundColor(getColorFromResource(i % 2 == 0 ? R.color.white : R.color.defaultBg));
            nsv.addView(gamesTicketRecyclerView);
            ticketsLayout.addView(nsv);
        }
        gamesTotalPriceValue.setText(String.format(getStringFromResource(R.string._G_), getTotalPrice()));

        if (getTotalPrice() == 0) {
            ticketsLayout.addView(emptyListLayout);
        }
    }

    private void subscribeTicket(Ticket ticket, boolean subscribe) {
        int index = allGamesTickets.get(ticket.getName()).indexOf(ticket);
        allGamesTickets.get(ticket.getName()).get(index).setSubscribe(subscribe);
        saveTicketsToMemory();
    }


    private synchronized void addTicket(String mapKey, Ticket ticket) {
        ArrayList<Ticket> itemsList = allGamesTickets.get(mapKey);
        if (itemsList == null) {
            itemsList = new ArrayList<>();
            itemsList.add(ticket);
            allGamesTickets.put(mapKey, itemsList);
        } else {
            if (!itemsList.contains(ticket)) itemsList.add(ticket);
        }
        sortTickersHashMap();
        saveTicketsToMemory();
    }

    private void deleteTicket(Ticket ticket) {
        for (Map.Entry<String, ArrayList<Ticket>> entry : allGamesTickets.entrySet()) {
            entry.getValue().remove(ticket);
        }
        saveTicketsToMemory();
    }

    private void saveTicketsToMemory() {
        ArrayList<Ticket> tickets = new ArrayList<>();
        for (Map.Entry<String, ArrayList<Ticket>> entry : allGamesTickets.entrySet()) {
            tickets.addAll(entry.getValue());
        }
        Log.d("LOGTAGMEMORY", new GsonBuilder().create().toJson(tickets));
        SharedPreferencesUtil.setTickets(new GsonBuilder().create().toJson(tickets));
    }

    private void setQuickPickFields() {
        setTicketNumberFields(getStringFromResource(R.string.qp) + getStringFromResource(R.string.qp) + getStringFromResource(R.string.qp), game);
    }

    private void setTicketNumberFields(String number, String gameName) {
        if (number == null)
            number = getStringFromResource(R.string.qp) + getStringFromResource(R.string.qp) + getStringFromResource(R.string.qp);
        if (number.isEmpty()) {
            for (EditText editText : numbersEditTexts) {
                editText.getText().clear();
            }
            if (game.equals(Draw.MARIAGE)) {
                gamesThirdNumber.getText().clear();
            }
        } else {
            int endFirstNumber = (gameName.equals(Draw.LOTTO5JR) || game.equals(Draw.LOTTO5P5) || gameName.equals(Draw.LOTTO3) || gameName.equals(Draw.LOTTO5)) && !number.contains(getStringFromResource(R.string.qp)) ? 3 : 2;
            gamesFirstNumber.setText(number.substring(0, endFirstNumber));
            if (!gameName.equals(Draw.BOLET) && !gameName.equals(Draw.LOTTO3)) {
                gamesSecondNumber.setText(number.substring(endFirstNumber, endFirstNumber + 2));
                if (gameName.equals(Draw.BOLOTO)) {
                    gamesThirdNumber.setText(number.substring(4));
                }
            }
        }
    }

    private void setTicketBetField(int bet) {
        gamesTicketBet.setText(bet == -1 ? "" : String.valueOf(bet));
    }

    private void getRequestByName(Ticket ticket) {
        if (ticket == null || ticket.getName() == null)
            return;

        Log.d("ticketTAG", "getRequestByName  ticket.getNum() " + ticket.getNum());

        switch (ticket.getName().toLowerCase()) {
            case Draw.BOLOTO:
                if (ticket.isSubscribe()) {
                    walletPresenter.getBolotoSubscribe(new Phone(SharedPreferencesUtil.getPhone()), ticket.getNum(), ticket.getSubscribeRegion());
                } else {
                    walletPresenter.getBolotoTicket(new Phone(SharedPreferencesUtil.getPhone()), ticket);
                }
                break;

            case Draw.BOLET:
                walletPresenter.getBoletTicket(new Phone(SharedPreferencesUtil.getPhone()), ticket);
                break;

            case Draw.MARIAGE:
                walletPresenter.getMariageTicket(new Phone(SharedPreferencesUtil.getPhone()), ticket);
                break;

            case Draw.LOTTO3:
                if (ticket.isSubscribe()) {
                    walletPresenter.getLotto3Subscribe(new Phone(SharedPreferencesUtil.getPhone()), ticket.getNum(), ticket.getSubscribeRegion(), ticket.getCost());
                } else {
                    walletPresenter.getLotto3Ticket(new Phone(SharedPreferencesUtil.getPhone()), ticket);
                }
                break;

            case Draw.LOTTO4:
                walletPresenter.getLotto4Ticket(new Phone(SharedPreferencesUtil.getPhone()), ticket);
                break;

            case Draw.LOTTO5:
                walletPresenter.getLotto5Ticket(new Phone(SharedPreferencesUtil.getPhone()), ticket);
                break;
            case Draw.ROYAL5:
                if (ticket.isSubscribe()) {
                    walletPresenter.getRoyal5Subscribe(new Phone(SharedPreferencesUtil.getPhone()), ticket.getNum(), ticket.getSubscribeRegion());
                } else {
                    walletPresenter.getLotto5RoyalTicket(new Phone(SharedPreferencesUtil.getPhone()), ticket);
                }
                break;

            case Draw.LOTTO5JR:
            case Draw.LOTTO5P5:
                if (ticket.isSubscribe()) {
                    walletPresenter.getLotto5jrSubscribe(new Phone(SharedPreferencesUtil.getPhone()), ticket.getNum(), ticket.getSubscribeRegion());
                } else {
                    walletPresenter.getLotto5jrTicket(new Phone(SharedPreferencesUtil.getPhone()), ticket);
                }
                break;
        }
    }

    private void createTicketsArray() {
        tickets = new ArrayList<>();
        for (Map.Entry<String, ArrayList<Ticket>> entry : allGamesTickets.entrySet()) {
            for (Ticket ticket : entry.getValue()) {
                if (ticket.isFl() && isCurrentTimeBigger(flDrawResponse.getLastStop())) {
                    ticket.setDraw(flDrawResponse.getNext());
                } else if (ticket.isNy() && isCurrentTimeBigger(nyDrawResponse.getLastStop())) {
                    ticket.setDraw(nyDrawResponse.getNext());
                } else if (ticket.isGa() && isCurrentTimeBigger(gaDrawResponse.getLastStop())) {
                    ticket.setDraw(gaDrawResponse.getNext());
                }
                tickets.add(ticket);
            }
        }
    }

    @Override
    public void getData(Object object) {
        TicketsPurchaseResponse ticketsPurchaseResponse;
        if (object instanceof TicketsPurchaseResponse) {
            ticketsPurchaseResponse = (TicketsPurchaseResponse) object;
            Log.d("LOGTAGSUBS", ticketsPurchaseResponse.getTicket() +
                    " state " + ticketsPurchaseResponse.getState());
            if (ticketsPurchaseResponse.getState() != 0) {
                Toast.makeText(getContext(), ticketsPurchaseResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
            clearTickets();
        } else if (object instanceof SubscribeResponse) {
            SubscribeResponse subscribeResponse = (SubscribeResponse) object;
            Log.d("LOGTAGSUBS", subscribeResponse.getTicket() + "  " + subscribeResponse.getState());
            if (subscribeResponse.getState() != 0) {
                Toast.makeText(getContext(), subscribeResponse.getSubscribeError(), Toast.LENGTH_SHORT).show();
            }
            clearTickets();
        }
    }

    private void clearTickets() {
        if (tickets == null || tickets.isEmpty()) {
            for (Map.Entry<String, ArrayList<Ticket>> entry : allGamesTickets.entrySet()) {
                for (Ticket t : tickets) {
                    entry.getValue().remove(t);
                    gamesTicketAdapter.removeTicket(t);
                }
            }

            allGamesTickets.clear();
            saveTicketsToMemory();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(MainActivity.MAIN_ACTIVITY, MainActivity.MY_TICKETS);
            startActivity(intent);

        }
    }

    private void getTicketRequest(Ticket ticket) {
        if ((ticket.getNum() == null || ticket.getNum().isEmpty()) && ticket.isCombo()) {
            ticket.setNum(TextUtil.getRandomNumberString(ticket.getName().equals(Draw.MARIAGE) ? 6 : Draw.getGameNumCount(ticket.getName())));
        }
        if ((Draw.BOLOTO.equals(ticket.getName().toLowerCase()) || Draw.LOTTO3.equals(ticket.getName().toLowerCase()) || Draw.MARIAGE.equals(ticket.getName().toLowerCase())) && ticket.isCombo()) {
            if (comboRequestCount == 0) {
                int step = ticket.getNum().length() % 2 == 0 ? 2 : 1;
                if (ticket.getName().equals(Draw.MARIAGE)) {
                    Log.d("LOGTAGG", ticket.getNum());
                }
                comboNumList = new ArrayList<>(Draw.MARIAGE.equals(ticket.getName()) ? TextUtil.getTwoPermutationsMaryajNew(TextUtil.getStringArray(ticket.getNum(), step)[0], TextUtil.getStringArray(ticket.getNum(), step)[1], TextUtil.getStringArray(ticket.getNum(), step)[2]) : TextUtil.getThreePermutations(TextUtil.getStringArray(ticket.getNum(), step)[0], TextUtil.getStringArray(ticket.getNum(), step)[1], TextUtil.getStringArray(ticket.getNum(), step)[2]));
            }

            tickets.remove(ticket);
            for (String num : comboNumList) {
                Ticket tckt = new Ticket(ticket.getName(), ticket.getDraw(), ticket.getNum(), ticket.getCost(), ticket.isCombo(), ticket.getSubscribeRegion(), ticket.getType(), ticket.isSubscribe());
                tckt.setNum(num);
                tckt.setCombo(false);
                tickets.add(tckt);
            }
            ticket = tickets.get(tickets.size() - 1);
        }

        getRequestByName(ticket);
        tickets.remove(ticket);

        if (tickets != null && !tickets.isEmpty()) {
            RxUtil.delayedMainThreadConsumer(1280, aLong -> getTicketRequest(tickets.get(tickets.size() - 1)));
        }

    }

    @OnClick({R.id.games_viewpager_arrow_left, R.id.games_viewpager_arrow_right})
    void onArrowClick(View view) {
        switch (view.getId()) {
            case R.id.games_viewpager_arrow_left:
                if (gamesViewpager.getCurrentItem() > 0) {
                    gamesViewpager.setCurrentItem(gamesViewpager.getCurrentItem() - 1, true);
                }
                break;

            case R.id.games_viewpager_arrow_right:
                if (gamesViewpager.getCurrentItem() < gamesItemAdapter.getCount() - 1) {
                    gamesViewpager.setCurrentItem(gamesViewpager.getCurrentItem() + 1, true);
                    break;
                }
        }
    }

    @OnClick({R.id.games_plus_button, R.id.games_qp})
    void onGamePlusClick(View view) {
        switch (view.getId()) {
            case R.id.games_plus_button:
                if (isNumFull() || isQPedited()) {
                    if (Draw.isBetNecessary(game) && !isBetInLimit()) {
                        return;
                    }

                    String firstNum = gamesFirstNumber.getText().toString();
                    String secondNum = gamesSecondNumber.getText().toString();
                    String thirdNum = gamesThirdNumber.getText().toString();

                    boolean isBolotoFieldsEquals = firstNum.equals(secondNum) && firstNum.equals(thirdNum);
                    if (game.equals(Draw.MARIAGE) && gamesComboRb.isChecked()) {
                        MaryajComboDialog maryajComboDialog = new MaryajComboDialog(getNum(), new MaryajComboDialog.OnComboButtonsClickListener() {
                            @Override
                            public void onCloseClick() {
                                gamesComboRb.setChecked(false);
                            }

                            @Override
                            public void onConfirmClick(String num) {
                                GamesTicketDialog gamesTicketDialog = new GamesTicketDialog(num, game, true, true, new GamesTicketDialog.OnConfirmButtonClickListener() {
                                    @Override
                                    public void onConfirmButtonClick() {
                                        addTicketToList();
                                    }

                                    @Override
                                    public void onCancel() {

                                    }
                                });
                                gamesTicketDialog.show(getFragmentManager(), GamesTicketDialog.class.getSimpleName());
                                maryajComboNum = num;
                            }
                        });
                        maryajComboDialog.show(getFragmentManager(), MaryajComboDialog.class.getSimpleName());
                    } else if ((((game.equals(Draw.BOLOTO) || game.equals(Draw.LOTTO3) || game.equals(Draw.ROYAL5)) && gamesComboRb.isChecked())) && !isBolotoFieldsEquals && !firstNum.equals(getString(R.string.qp))) {
                        if (getFragmentManager() != null) {
                            GamesTicketDialog gamesTicketDialog = new GamesTicketDialog(getNum(), game, true, new GamesTicketDialog.OnConfirmButtonClickListener() {
                                @Override
                                public void onConfirmButtonClick() {
                                    addTicketToList();
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                            gamesTicketDialog.show(getFragmentManager(), GamesTicketDialog.class.getSimpleName());

                        }
                    } else {
                        onDialogConfirmClick();
                    }

                } else {
                    if (getNumCount() != Draw.getGameNumCount(game)) {
                        gamesFirstNumber.setBackgroundResource(R.drawable.red_circle);
                        gamesSecondNumber.setBackgroundResource(R.drawable.red_circle);
                        gamesThirdNumber.setBackgroundResource(R.drawable.red_circle);
                        RxUtil.delayedConsumer(250, aLong -> {
                            for (EditText editText : numbersEditTexts) {
                                editText.setBackgroundResource(editText.length() == 0 ? R.drawable.empty_number : R.drawable.filled_number);
                                editText.getLayoutParams().height = editText.length() == 0 ? editTextSize : editTextSize + 10;
                                editText.getLayoutParams().width = editText.length() == 0 ? editTextSize : editTextSize + 10;
                            }
                        });
                    }
                    if (Draw.isBetNecessary(game) && gamesTicketBet.getText().toString().isEmpty()) {
                        gamesTicketBet.setBackgroundResource(R.drawable.red_stroke);
                        RxUtil.delayedConsumer(250, aLong -> gamesTicketBet.setBackgroundResource(R.drawable.ticket_bet_bg));
                    }
                    showMessage(getStringFromResource(R.string.fill_required_fields));
                }
                break;

            case R.id.games_qp:
                setQuickPickFields();
                break;
        }
    }

    private void onDialogConfirmClick() {
       /* if (gamesSubscribeRb.isChecked()) {
            GamesTicketDialog gamesTicketDialog = new GamesTicketDialog(getNum(), false, this::addTicketToList);
            gamesTicketDialog.show(getFragmentManager(), GamesTicketDialog.class.getSimpleName());
        } else */
        addTicketToList();
    }

    private void addTicketToList() {
        if (flDrawResponse == null || nyDrawResponse == null) {
            showMessage(getString(R.string.error));
            return;
        }
        try {
            if (editedTicket != null) {
                deleteTicket(editedTicket);
                editedTicket = null;
            }

            for (int gameType : getGameType()) {
                if (flDrawCheckbox.isChecked()) {
                    addTicket(game,
                            new Ticket(game, flDrawResponse.getDraw(), game.equals(Draw.MARIAGE) && gamesComboRb.isChecked() &&
                                    !gamesFirstNumber.getText().toString().equals(getString(R.string.qp)) ? maryajComboNum : getNum(),
                                    game.equals(Draw.BOLOTO) ?
                                            (BOLOTO_TICKET_COST) :
                                            game.equals(Draw.LOTTO5P5) ?
                                                    LOTTO5JR_TICKET_COST :
                                                    game.equals(Draw.ROYAL5) ?
                                                            (ROYAL5_TICKET_COST) :
                                                            Integer.parseInt(gamesTicketBet.getText().toString()),
                                    getTicketCost(), (((game.equals(Draw.BOLOTO) || game.equals(Draw.LOTTO3) || (game.equals(Draw.MARIAGE))) && gamesComboRb.isChecked())), Ticket.FL, gameType, false));
                }
                if (nyDrawCheckbox.isChecked()) {
                    addTicket(game,
                            new Ticket(game, nyDrawResponse.getDraw(), game.equals(Draw.MARIAGE) && gamesComboRb.isChecked() &&
                                    !gamesFirstNumber.getText().toString().equals(getString(R.string.qp)) ? maryajComboNum : getNum(),
                                    game.equals(Draw.BOLOTO) ?
                                            (BOLOTO_TICKET_COST) :
                                            game.equals(Draw.LOTTO5P5) ?
                                                    LOTTO5JR_TICKET_COST :
                                                    game.equals(Draw.ROYAL5) ?
                                                            (ROYAL5_TICKET_COST) :
                                                            Integer.parseInt(gamesTicketBet.getText().toString()),
                                    getTicketCost(), (((game.equals(Draw.BOLOTO) || game.equals(Draw.LOTTO3) || (game.equals(Draw.MARIAGE))) && gamesComboRb.isChecked())), Ticket.NY, gameType, false));
                }

                if (gaDrawCheckbox.isChecked()) {
                    addTicket(game,
                            new Ticket(game, gaDrawResponse.getDraw(), game.equals(Draw.MARIAGE) && gamesComboRb.isChecked() &&
                                    !gamesFirstNumber.getText().toString().equals(getString(R.string.qp)) ? maryajComboNum : getNum(),
                                    game.equals(Draw.BOLOTO) ?
                                            (BOLOTO_TICKET_COST) :
                                            game.equals(Draw.LOTTO5P5) ?
                                                    LOTTO5JR_TICKET_COST :
                                                    game.equals(Draw.ROYAL5) ?
                                                            (ROYAL5_TICKET_COST) :
                                                            Integer.parseInt(gamesTicketBet.getText().toString()),
                                    getTicketCost(), (((game.equals(Draw.BOLOTO) || game.equals(Draw.LOTTO3) || (game.equals(Draw.MARIAGE))) && gamesComboRb.isChecked())), Ticket.GA, gameType, false));
                }

            }
            setGamesTicketLayout(allGamesTickets);
            setTicketNumberFields("", game);
            setTicketBetField(-1);
            for (CheckBox checkBox : optionCheckBoxes) {
                checkBox.setChecked(false);
            }

            if (game.equals(Draw.LOTTO5) || game.equals(Draw.LOTTO4))
                gamesTicketOption1.setChecked(true);

            gamesComboRb.setChecked(false);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            showMessage(getString(R.string.error));
        }
    }

    @OnClick(R.id.games_play_buttons)
    void onGamePlayClick() {
        createTicketsArray();
        if (tickets.size() > 0) {
            showProgress();
            getTicketRequest(tickets.get(tickets.size() - 1));
        } else {
            if (gamesTicketAdapter != null)
                gamesTicketAdapter.removeAllTickets();
            showMessage(getStringFromResource(R.string.choose_ticket_first));
        }

    }

    @OnClick(R.id.games_rules)
    void onGamesRulesClick() {
        WebViewActivity.openUrl(getContext(), "http://lisa.s7.devpreviewr.com/game-rules/");
    }

    @OnClick(R.id.recent_numbers)
    void onRecentNumbersClick() {
        allGamesTickets.clear();
        FragmentUtil.replaceFragment(getFragmentManager(), new RecentNumbersFragment(), true);
    }


}

