package ht.lisa.app.ui.wallet.winningnumbers;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import ht.lisa.app.R;
import ht.lisa.app.model.Draw;
import ht.lisa.app.model.FullSingleDraw;
import ht.lisa.app.model.Phone;
import ht.lisa.app.model.Ticket;
import ht.lisa.app.model.response.DrawResponse;
import ht.lisa.app.ui.datepicker.DatePickerDialog;
import ht.lisa.app.ui.datepicker.SpinnerDatePickerDialogBuilder;
import ht.lisa.app.ui.wallet.BaseWalletFragment;
import ht.lisa.app.ui.wallet.WalletActivity;
import ht.lisa.app.util.DateTimeUtil;
import ht.lisa.app.util.SharedPreferencesUtil;
import ht.lisa.app.util.TextUtil;

import static ht.lisa.app.model.Draw.BOLET;

public class WinningNumbersFragment extends BaseWalletFragment implements View.OnClickListener {
    public static final String SCREEN_NAME = "WinningNumbersScreen";

    private static final int NEW_YORK_CHOSEN = 0;
    private static final int FLORIDA_CHOSEN = 1;
    //private static final int INSTABOUL_CHOSEN = 2;
    private static final int GEORGIA_CHOSEN = 2;

    @BindView(R.id.day_draw_date)
    TextView dayDrawDate;
    @BindView(R.id.night_draw_date)
    TextView nightDrawDate;
    @BindView(R.id.day_number_1)
    TextView dayNumber1;
    @BindView(R.id.day_number_2)
    TextView dayNumber2;
    @BindView(R.id.day_number_3)
    TextView dayNumber3;
    @BindView(R.id.night_number_1)
    TextView nightNumber1;
    @BindView(R.id.night_number_2)
    TextView nightNumber2;
    @BindView(R.id.night_number_3)
    TextView nightNumber3;
    @BindView(R.id.evening_number_1)
    TextView eveningNumber1;
    @BindView(R.id.evening_number_2)
    TextView eveningNumber2;
    @BindView(R.id.evening_number_3)
    TextView eveningNumber3;
    @BindView(R.id.new_york_draw_text)
    TextView newYorkDrawText;
    @BindView(R.id.georgia_draw_text)
    TextView georgiaDrawText;
    @BindView(R.id.florida_draw_text)
    TextView floridaDrawText;
    @BindView(R.id.games_results_layout)
    LinearLayout gamesResultsLayout;
    @BindView(R.id.new_york_draw)
    CardView newYorkDraw;
    @BindView(R.id.georgia_draw)
    CardView georgiaDraw;
    @BindView(R.id.florida_draw)
    CardView floridaDraw;
    @BindView(R.id.instaboul_layout)
    View instaboulLayout;/*
    @BindView(R.id.instaboul)
    CardView instaboul;*/
    @BindView(R.id.game_result_text)
    TextView gameResultText;
    @BindView(R.id.default_draws_layout)
    View defaultDrawsLayout;/*
    @BindView(R.id.instaboul_text)
    TextView instaboulText;*/
    @BindView(R.id.games_results_grid_view)
    GridLayout gamesResultsGridLayout;
    @BindView(R.id.evening_draw_layout)
    View eveningDrawLayout;

    @BindView(R.id.evening_draw_date)
    TextView eveningDrawDate;

    @BindView(R.id.iconNight)
    ImageView iconNight;
    @BindView(R.id.iconEvening)
    ImageView iconEvening;

    private int chosenDraw = NEW_YORK_CHOSEN;
    private DrawResponse drawResponse;

    private final Calendar dateAndTime = Calendar.getInstance();


    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_winning_numbers, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showProgress();

        walletPresenter.getDraw(new Phone(SharedPreferencesUtil.getPhone()));

        floridaDraw.setOnClickListener(this);
        newYorkDraw.setOnClickListener(this);
        georgiaDraw.setOnClickListener(this);
        //    instaboul.setOnClickListener(this);
    }


    @Override
    public void getData(Object object) {
        if (object instanceof DrawResponse) {
            DrawResponse response = (DrawResponse) object;
            if (response.getState() == 0) {
                drawResponse = response;
                setGamesResultsLayout();
            }
        }
    }

    private boolean isNy() {
        return chosenDraw == NEW_YORK_CHOSEN;
    }

    private boolean isFl() {
        return chosenDraw == FLORIDA_CHOSEN;
    }

    private boolean isGr() {
        return chosenDraw == GEORGIA_CHOSEN;
    }


    private void setGamesResultsLayout() {
        if (drawResponse == null) return;
        if (drawResponse.getDrawList() == null) return;

        gamesResultsLayout.removeAllViews();

        if (drawResponse.getDrawList().size() < 4) {
            floridaDraw.setVisibility(View.GONE);
            chosenDraw = NEW_YORK_CHOSEN;
            gameResultText.setText(R.string.games_results_ny);
            changeUiByRegion();
        } else {
            floridaDraw.setVisibility(View.VISIBLE);
        }

        DrawResponse formattedResponse = new DrawResponse();
        formattedResponse.setDrawList(new ArrayList<>());

        for (Draw draw : drawResponse.getDrawList()) {
            if (isNy() && draw.getRegion().equals(Ticket.NY)) {
                formattedResponse.getDrawList().add(draw);
            } else if (isFl() && draw.getRegion().equals(Ticket.FL)) {
                formattedResponse.getDrawList().add(draw);
            } else if (isGr() && draw.getRegion().equals(Ticket.GA)) {
                formattedResponse.getDrawList().add(draw);
            }
        }

        int dayDraw = formattedResponse.getDayDraw(getActivity());
        int eveningDraw = formattedResponse.getEveningDraw(getActivity());
        int nightDraw = formattedResponse.getNightDraw(getActivity());

        dayDrawDate.setText(DateTimeUtil.getWinningNumbersDateFormat(formattedResponse.getDrawList().get(dayDraw).getDraw(), getActivity()));
        nightDrawDate.setText(DateTimeUtil.getWinningNumbersDateFormat(formattedResponse.getDrawList().get(nightDraw).getDraw(), getActivity()));

        dayNumber1.setText(formattedResponse.getDrawArray(dayDraw)[0]);
        dayNumber2.setText(formattedResponse.getDrawArray(dayDraw)[1]);
        dayNumber3.setText(formattedResponse.getDrawArray(dayDraw)[2]);
        nightNumber1.setText(formattedResponse.getDrawArray(nightDraw)[0]);
        nightNumber2.setText(formattedResponse.getDrawArray(nightDraw)[1]);
        nightNumber3.setText(formattedResponse.getDrawArray(nightDraw)[2]);
        if (chosenDraw == GEORGIA_CHOSEN) {
            eveningDrawLayout.setVisibility(View.VISIBLE);
            eveningNumber1.setText(formattedResponse.getDrawArray(eveningDraw)[0]);
            eveningNumber2.setText(formattedResponse.getDrawArray(eveningDraw)[1]);
            eveningNumber3.setText(formattedResponse.getDrawArray(eveningDraw)[2]);

            eveningDrawDate.setVisibility(View.VISIBLE);
            eveningDrawDate.setText(DateTimeUtil.getWinningNumbersDateFormat(formattedResponse.getDrawList().get(eveningDraw).getDraw(), getActivity()));
            iconNight.setImageResource(R.drawable.ic_evening_draw);
        } else {
            eveningDrawLayout.setVisibility(View.GONE);
            eveningDrawDate.setVisibility(View.GONE);

            iconNight.setImageResource(R.drawable.ic_moon);
        }


        ArrayList<FullSingleDraw> fullSingleDraws = formattedResponse.getFullSingleDrawList(getContext());
        for (int i = 0; i < fullSingleDraws.size(); i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_game_result, gamesResultsLayout, false);
            FullSingleDraw fullSingleDraw = fullSingleDraws.get(i);
            TextView lottoName = ((TextView) (view.findViewById(R.id.lotto_name)));
            lottoName.setText(fullSingleDraw.getName() == null ? "" : (fullSingleDraw.getName().equals(Draw.MARIAGE) ? getText(R.string.maryage) : fullSingleDraw.getName().equals(Draw.LOTTO5JR_NEW) || fullSingleDraw.getName().equals(Draw.LOTTO5JR) ? getText(R.string.lotto5_5g) : fullSingleDraw.getName()));
            lottoName.setText(TextUtil.capitalize(lottoName.getText()));

            if (fullSingleDraw.getName().equals(Draw.MARIAGE) || fullSingleDraw.getName().equals(Draw.LOTTO4)) {
                view.findViewById(R.id.day_number_layout_1).setVisibility(View.GONE);
                view.findViewById(R.id.evening_number_layout_1).setVisibility(View.GONE);
                view.findViewById(R.id.night_number_layout_1).setVisibility(View.GONE);
            }


            ((TextView) (view.findViewById(R.id.day_draw_date))).setText(DateTimeUtil.getWinningNumbersDateFormat(fullSingleDraw.getDayDate(), getActivity()));
            ((TextView) (view.findViewById(R.id.day_number_1))).setText(fullSingleDraw.getDayDraw() == null ? "" : getBallText(fullSingleDraw.getDayDrawArray()[0]));
            ((TextView) (view.findViewById(R.id.day_number_2))).setText(fullSingleDraw.getDayDraw() == null || fullSingleDraw.getDayDrawArray().length < 2 ? "" : getBallText(fullSingleDraw.getDayDrawArray()[1]));
            view.findViewById(R.id.day_number_layout_2).setVisibility(fullSingleDraw.getDayDrawArray().length > 1 ? View.VISIBLE : View.GONE);
            ((TextView) (view.findViewById(R.id.day_number_3))).setText(fullSingleDraw.getDayDraw() == null || fullSingleDraw.getDayDrawArray().length < 3 ? "" : getBallText(fullSingleDraw.getDayDrawArray()[2]));
            view.findViewById(R.id.day_number_layout_3).setVisibility(fullSingleDraw.getDayDrawArray().length > 2 ? View.VISIBLE : View.GONE);
            ((TextView) (view.findViewById(R.id.night_draw_date))).setText(fullSingleDraw.getName() == null ? "" : DateTimeUtil.getWinningNumbersDateFormat(fullSingleDraw.getNightDate(), getActivity()));
            ((TextView) (view.findViewById(R.id.night_number_1))).setText(fullSingleDraw.getNightDraw() == null ? "" : getBallText(fullSingleDraw.getNightDrawArray()[0]));
            ((TextView) (view.findViewById(R.id.night_number_2))).setText(fullSingleDraw.getNightDraw() == null || fullSingleDraw.getDayDrawArray().length < 2 ? "" : getBallText(fullSingleDraw.getNightDrawArray()[1]));
            view.findViewById(R.id.night_number_layout_2).setVisibility(fullSingleDraw.getNightDrawArray().length > 1 ? View.VISIBLE : View.GONE);
            ((TextView) (view.findViewById(R.id.night_number_3))).setText(fullSingleDraw.getNightDraw() == null || fullSingleDraw.getDayDrawArray().length < 3 ? "" : getBallText(fullSingleDraw.getNightDrawArray()[2]));
            view.findViewById(R.id.night_number_layout_3).setVisibility(fullSingleDraw.getNightDrawArray().length > 2 ? View.VISIBLE : View.GONE);
            //view.findViewById(R.id.draw_detail).setVisibility(fullSingleDraw.getName().equals(BOLET) ? View.GONE : View.VISIBLE);

            if (chosenDraw == GEORGIA_CHOSEN) {
                ((TextView) (view.findViewById(R.id.evening_draw_date))).setText(DateTimeUtil.getWinningNumbersDateFormat(fullSingleDraw.getEveningDate(), getActivity()));
                ((TextView) (view.findViewById(R.id.evening_number_1))).setText(fullSingleDraw.getEveningDraw() == null ? "" : getBallText(fullSingleDraw.getEveningDrawArray()[0]));
                ((TextView) (view.findViewById(R.id.evening_number_2))).setText(fullSingleDraw.getEveningDraw() == null || fullSingleDraw.getEveningDrawArray().length < 2 ? "" : getBallText(fullSingleDraw.getEveningDrawArray()[1]));
                view.findViewById(R.id.evening_number_layout_2).setVisibility(fullSingleDraw.getEveningDrawArray().length > 1 ? View.VISIBLE : View.GONE);
                ((TextView) (view.findViewById(R.id.evening_number_3))).setText(fullSingleDraw.getEveningDraw() == null || fullSingleDraw.getEveningDrawArray().length < 3 ? "" : getBallText(fullSingleDraw.getEveningDrawArray()[2]));
                view.findViewById(R.id.evening_number_layout_3).setVisibility(fullSingleDraw.getEveningDrawArray().length > 2 ? View.VISIBLE : View.GONE);

                ((ImageView)view.findViewById(R.id.evening_image)).setImageResource(R.drawable.ic_moon);
                ((ImageView)view.findViewById(R.id.night_image)).setImageResource(R.drawable.ic_evening_draw);
                ((ImageView)view.findViewById(R.id.night_image)).setColorFilter(ContextCompat.getColor(getContext(), R.color.eveningColor));
            } else {
                view.findViewById(R.id.eveningLayout).setVisibility(View.GONE);
                view.findViewById(R.id.evening_draw_date).setVisibility(View.GONE);
            }

            view.findViewById(R.id.draw_detail).setOnClickListener(view1 -> {
                Intent intent = new Intent(getActivity(), WalletActivity.class);
                intent.putExtra(WalletActivity.WALLET_FRAGMENT, WalletActivity.WalletPage.GAME_RESULT_DETAILS);
                intent.putExtra(GameResultDetailsFragment.DRAW_ITEM, fullSingleDraw);
                intent.putExtra(GameResultDetailsFragment.REGION_KEY, getChosenDrawString());
                intent.putExtra(GameResultDetailsFragment.DAY_DRAW_KEY, dayDraw);
                intent.putExtra(GameResultDetailsFragment.NIGHT_DRAW_KEY, nightDraw);
                startActivity(intent);
            });
            gamesResultsLayout.addView(view);
        }
    }

    private String getChosenDrawString() {
        if (chosenDraw == NEW_YORK_CHOSEN) {
            return Ticket.NY;
        } else if (chosenDraw == FLORIDA_CHOSEN) {
            return Ticket.FL;
        } else return Ticket.GA;
    }

    private void onFloridaNotProvidedYet() {
        dayDrawDate.setText(DateTimeUtil.getWinningNumbersDateFormat(drawResponse.getDrawList().get(drawResponse.getDayDraw(getActivity())).getDraw(), getActivity()));
        nightDrawDate.setText(DateTimeUtil.getWinningNumbersDateFormat(drawResponse.getDrawList().get(drawResponse.getNightDraw(getActivity())).getDraw(), getActivity()));

        dayNumber1.setText("");
        dayNumber2.setText("");
        dayNumber3.setText("");
        nightNumber1.setText("");
        nightNumber2.setText("");
        nightNumber3.setText("");
    }

    @OnClick(R.id.winning_numbers_past_draw)
    void onPastDrawClick() {
        if (getActivity() == null) return;
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        DatePickerDialog datePickerDialog = new SpinnerDatePickerDialogBuilder()
                .context(getContext())
                .callback((view, year, monthOfYear, dayOfMonth) -> {
                    dateAndTime.set(year, monthOfYear, dayOfMonth, 0, 0);
                    walletPresenter.getDrawByDate(new Phone(SharedPreferencesUtil.getPhone()), DateTimeUtil.getDobFormat(dateAndTime.getTimeInMillis()));
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

    private void testInstaboulItems() {
        gamesResultsGridLayout.removeAllViews();
        gamesResultsGridLayout.setColumnCount(3);
        for (int i = 0; i < 10; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_instaboul, gamesResultsGridLayout, false);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (gamesResultsGridLayout.getWidth() / 3.05), ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
            gamesResultsGridLayout.addView(view, i);
        }
    }

    private String getBallText(String ballTextShort) {
        return ballTextShort;
    }

    private void changeUiByRegion() {
        /*instaboulLayout.setVisibility(chosenDraw == INSTABOUL_CHOSEN ? View.VISIBLE : View.GONE);
        defaultDrawsLayout.setVisibility(chosenDraw != INSTABOUL_CHOSEN ? View.VISIBLE : View.GONE);*/
/// instaboulText.setTextColor(ContextCompat.getColor(getContext(), chosenDraw == INSTABOUL_CHOSEN ? R.color.lottoTextColor : R.color.unselectedDrawTextColor));

        newYorkDrawText.setTextColor(ContextCompat.getColor(getContext(), isNy() ? R.color.lottoTextColor : R.color.unselectedDrawTextColor));
        newYorkDraw.setCardBackgroundColor(ContextCompat.getColor(getContext(), isNy() ? R.color.white : R.color.unselectedDrawCardColor));

        georgiaDrawText.setTextColor(ContextCompat.getColor(getContext(), isGr() ? R.color.lottoTextColor : R.color.unselectedDrawTextColor));
        georgiaDraw.setCardBackgroundColor(ContextCompat.getColor(getContext(), isGr() ? R.color.white : R.color.unselectedDrawCardColor));

        floridaDrawText.setTextColor(ContextCompat.getColor(getContext(), isFl() ? R.color.lottoTextColor : R.color.unselectedDrawTextColor));
        floridaDraw.setCardBackgroundColor(ContextCompat.getColor(getContext(), isFl() ? R.color.white : R.color.unselectedDrawCardColor));
    }

    @Override
    public void onClick(View v) {
        gamesResultsLayout.removeAllViews();
        if (v.getId() == newYorkDraw.getId()) {
            chosenDraw = NEW_YORK_CHOSEN;
            gameResultText.setText(R.string.games_results_ny);
            setGamesResultsLayout();
        } else if (v.getId() == floridaDraw.getId()) {
            chosenDraw = FLORIDA_CHOSEN;
            gameResultText.setText(R.string.games_results_fl);
            setGamesResultsLayout();
        } /*else if (v.getId() == instaboul.getId()) {
            chosenDraw = INSTABOUL_CHOSEN;
            gameResultText.setText(R.string.draw_results);
            testInstaboulItems();
        }*/
         else if (v.getId() == georgiaDraw.getId()) {
            chosenDraw = GEORGIA_CHOSEN;
            gameResultText.setText(R.string.games_results_ga);
            setGamesResultsLayout();
            //testInstaboulItems();
        }
        changeUiByRegion();
        //    instaboul.setCardBackgroundColor(ContextCompat.getColor(getContext(), chosenDraw == INSTABOUL_CHOSEN ? R.color.white : R.color.unselectedDrawCardColor));
    }
}
