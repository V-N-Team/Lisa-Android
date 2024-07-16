package ht.lisa.app.ui.wallet.winningnumbers;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import ht.lisa.app.LisaApp;
import ht.lisa.app.R;
import ht.lisa.app.model.Draw;
import ht.lisa.app.model.DrawDetailPay;
import ht.lisa.app.model.DrawDetailRate;
import ht.lisa.app.model.DrawPay;
import ht.lisa.app.model.DrawRate;
import ht.lisa.app.model.FullSingleDraw;
import ht.lisa.app.model.Phone;
import ht.lisa.app.model.response.DrawBolotoResponse;
import ht.lisa.app.model.response.DrawLotto3Response;
import ht.lisa.app.model.response.DrawLotto4Response;
import ht.lisa.app.model.response.DrawLotto5Response;
import ht.lisa.app.model.response.DrawLotto5RoyalResponse;
import ht.lisa.app.model.response.DrawLotto5jrResponse;
import ht.lisa.app.model.response.DrawMariageResponse;
import ht.lisa.app.ui.wallet.BaseWalletFragment;
import ht.lisa.app.util.DateTimeUtil;
import ht.lisa.app.util.SharedPreferencesUtil;
import ht.lisa.app.util.TextUtil;
import ht.lisa.app.util.ViewUtil;

public class GameResultDetailsFragment extends BaseWalletFragment {
    public static final String SCREEN_NAME = "WinningNumberDetailsScreen";

    public static final String DRAW_ITEM = "drawItem";
    public static final String IS_NY_KEY = "nyKey";
    public static final String REGION_KEY = "regionKey";
    public static final String DAY_DRAW_KEY = "dayDraw";
    public static final String NIGHT_DRAW_KEY = "nightDraw";

    @BindView(R.id.lotto_name)
    TextView lottoName;
    @BindView(R.id.day_draw_date)
    TextView dayDrawDate;
    @BindView(R.id.day_number_1)
    TextView dayNumber1;
    @BindView(R.id.day_number_2)
    TextView dayNumber2;
    @BindView(R.id.day_number_layout_1)
    LinearLayout dayNumberLayout1;
    @BindView(R.id.day_number_layout_2)
    LinearLayout dayNumberLayout2;
    @BindView(R.id.day_number_layout_3)
    LinearLayout dayNumberLayout3;
    @BindView(R.id.day_number_3)
    TextView dayNumber3;
    @BindView(R.id.night_draw_date)
    TextView nightDrawDate;
    @BindView(R.id.night_number_1)
    TextView nightNumber1;
    @BindView(R.id.night_number_2)
    TextView nightNumber2;
    @BindView(R.id.night_number_layout_1)
    LinearLayout nightNumberLayout1;
    @BindView(R.id.night_number_layout_2)
    LinearLayout nightNumberLayout2;
    @BindView(R.id.night_number_layout_3)
    LinearLayout nightNumberLayout3;
    @BindView(R.id.night_number_3)
    TextView nightNumber3;


    @BindView(R.id.evening_draw_date)
    TextView eveningDrawDate;
    @BindView(R.id.evening_number_1)
    TextView eveningNumber1;
    @BindView(R.id.evening_number_2)
    TextView eveningNumber2;
    @BindView(R.id.evening_number_layout_1)
    LinearLayout eveningNumberLayout1;
    @BindView(R.id.evening_number_layout_2)
    LinearLayout eveningNumberLayout2;
    @BindView(R.id.evening_number_3)
    TextView eveningNumber3;
    @BindView(R.id.evening_number_layout_3)
    LinearLayout eveningNumberLayout3;

    //boloto
    @BindView(R.id.item_boloto_details)
    ConstraintLayout itemBolotoDetails;

    //royal5
    @BindView(R.id.item_lotto_5_royal_details)
    ConstraintLayout itemLottoRoyal5Details;
    @BindView(R.id.day_value_lotto_5_royal_1)
    TextView dayValueLotto5Royal1;
    @BindView(R.id.day_value_lotto_5_royal_2)
    TextView dayValueLotto5Royal2;
    @BindView(R.id.day_value_lotto_5_royal_3)
    TextView dayValueLotto5Royal3;
    @BindView(R.id.day_value_lotto_5_royal_4)
    TextView dayValueLotto5Royal4;
    @BindView(R.id.day_value_lotto_5_royal_5)
    TextView dayValueLotto5Royal5;
    @BindView(R.id.night_value_lotto_5_royal_1)
    TextView nightValueLotto5Royal1;
    @BindView(R.id.night_value_lotto_5_royal_2)
    TextView nightValueLotto5Royal2;
    @BindView(R.id.night_value_lotto_5_royal_3)
    TextView nightValueLotto5Royal3;
    @BindView(R.id.night_value_lotto_5_royal_4)
    TextView nightValueLotto5Royal4;
    @BindView(R.id.night_value_lotto_5_royal_5)
    TextView nightValueLotto5Royal5;
    @BindView(R.id.evening_value_lotto_5_royal_1)
    TextView eveningValueLotto5Royal1;
    @BindView(R.id.evening_value_lotto_5_royal_2)
    TextView eveningValueLotto5Royal2;
    @BindView(R.id.evening_value_lotto_5_royal_3)
    TextView eveningValueLotto5Royal3;
    @BindView(R.id.evening_value_lotto_5_royal_4)
    TextView eveningValueLotto5Royal4;
    @BindView(R.id.evening_value_lotto_5_royal_5)
    TextView eveningValueLotto5Royal5;
    @BindView(R.id.evening_image_royal)
    ImageView eveningImageRoyal;
    @BindView(R.id.night_image_royal)
    ImageView nightImageRoyal;


    //bolet
    @BindView(R.id.item_bolet_details)
    ConstraintLayout itemBoletDetails;
    @BindView(R.id.day_bolet_1)
    TextView dayValueBolet1;
    @BindView(R.id.day_bolet_2)
    TextView dayValueBolet2;
    @BindView(R.id.day_bolet_3)
    TextView dayValueBolet3;
    @BindView(R.id.night_bolet_1)
    TextView nightValueBolet1;
    @BindView(R.id.night_bolet_2)
    TextView nightValueBolet2;
    @BindView(R.id.night_bolet_3)
    TextView nightValueBolet3;
    @BindView(R.id.evening_bolet_1)
    TextView eveningValueBolet1;
    @BindView(R.id.evening_bolet_2)
    TextView eveningValueBolet2;
    @BindView(R.id.evening_bolet_3)
    TextView eveningValueBolet3;
    @BindView(R.id.evening_image_bolet)
    ImageView eveningImageBolet;
    @BindView(R.id.night_image_bolet)
    ImageView nightImageBolet;

    //lotto5jr
    @BindView(R.id.item_lottojr_details)
    ConstraintLayout item_lottojr_details;
    @BindView(R.id.day_value_lottojr_1)
    TextView dayValueLottojr1;
    @BindView(R.id.day_value_lottojr_2)
    TextView dayValueLottojr2;
    @BindView(R.id.day_value_lottojr_3)
    TextView dayValueLottojr3;
    @BindView(R.id.day_value_lottojr_4)
    TextView dayValueLottojr4;
    @BindView(R.id.evening_value_lottojr_1)
    TextView eveningValueLottojr1;
    @BindView(R.id.evening_value_lottojr_2)
    TextView eveningValueLottojr2;
    @BindView(R.id.evening_value_lottojr_3)
    TextView eveningValueLottojr3;
    @BindView(R.id.evening_value_lottojr_4)
    TextView eveningValueLottojr4;
    @BindView(R.id.night_value_lottojr_1)
    TextView nightValueLottojr1;
    @BindView(R.id.night_value_lottojr_2)
    TextView nightValueLottojr2;
    @BindView(R.id.night_value_lottojr_3)
    TextView nightValueLottojr3;
    @BindView(R.id.night_value_lottojr_4)
    TextView nightValueLottojr4;
    @BindView(R.id.evening_image_jr)
    ImageView eveningImageJr;
    @BindView(R.id.night_image_jr)
    ImageView nightImageJr;

    //mariage
    @BindView(R.id.item_mariage_details)
    ConstraintLayout itemMariageDetails;
    @BindView(R.id.day_mariage_1)
    TextView dayMariage1;
    @BindView(R.id.day_mariage_2)
    TextView dayMariage2;
    @BindView(R.id.day_mariage_3)
    TextView dayMariage3;
    @BindView(R.id.day_mariage_4)
    TextView dayMariage4;
    @BindView(R.id.day_mariage_5)
    TextView dayMariage5;
    @BindView(R.id.day_mariage_6)
    TextView dayMariage6;
    @BindView(R.id.night_mariage_1)
    TextView nightMariage1;
    @BindView(R.id.night_mariage_2)
    TextView nightMariage2;
    @BindView(R.id.night_mariage_3)
    TextView nightMariage3;
    @BindView(R.id.night_mariage_4)
    TextView nightMariage4;
    @BindView(R.id.night_mariage_5)
    TextView nightMariage5;
    @BindView(R.id.night_mariage_6)
    TextView nightMariage6;
    @BindView(R.id.evening_mariage_1)
    TextView eveningMariage1;
    @BindView(R.id.evening_mariage_2)
    TextView eveningMariage2;
    @BindView(R.id.evening_mariage_3)
    TextView eveningMariage3;
    @BindView(R.id.evening_mariage_4)
    TextView eveningMariage4;
    @BindView(R.id.evening_mariage_5)
    TextView eveningMariage5;
    @BindView(R.id.evening_mariage_6)
    TextView eveningMariage6;

    //lotto3
    @BindView(R.id.item_combo_details)
    ConstraintLayout itemComboDetails;
    @BindView(R.id.day_combo_1)
    TextView daycombo1;
    @BindView(R.id.day_combo_2)
    TextView daycombo2;
    @BindView(R.id.day_combo_3)
    TextView daycombo3;
    @BindView(R.id.day_combo_4)
    TextView daycombo4;
    @BindView(R.id.day_combo_5)
    TextView daycombo5;
    @BindView(R.id.day_combo_6)
    TextView daycombo6;
    @BindView(R.id.night_combo_1)
    TextView nightcombo1;
    @BindView(R.id.night_combo_2)
    TextView nightcombo2;
    @BindView(R.id.night_combo_3)
    TextView nightcombo3;
    @BindView(R.id.night_combo_4)
    TextView nightcombo4;
    @BindView(R.id.night_combo_5)
    TextView nightcombo5;
    @BindView(R.id.night_combo_6)
    TextView nightcombo6;
    @BindView(R.id.evening_combo_1)
    TextView eveningcombo1;
    @BindView(R.id.evening_combo_2)
    TextView eveningcombo2;
    @BindView(R.id.evening_combo_3)
    TextView eveningcombo3;
    @BindView(R.id.evening_combo_4)
    TextView eveningcombo4;
    @BindView(R.id.evening_combo_5)
    TextView eveningcombo5;
    @BindView(R.id.evening_combo_6)
    TextView eveningcombo6;

    //lotto4, lotto5
    @BindView(R.id.item_draw_options_details)
    ConstraintLayout itemDrawOptionsDetails;
    @BindView(R.id.day_draw_option_1)
    TextView dayDrawOption1;
    @BindView(R.id.day_draw_option_2)
    TextView dayDrawOption2;
    @BindView(R.id.day_draw_option_3)
    TextView dayDrawOption3;
    @BindView(R.id.evening_draw_option_1)
    TextView eveningDrawOption1;
    @BindView(R.id.evening_draw_option_2)
    TextView eveningDrawOption2;
    @BindView(R.id.evening_draw_option_3)
    TextView eveningDrawOption3;
    @BindView(R.id.night_draw_option_1)
    TextView nightDrawOption1;
    @BindView(R.id.night_draw_option_2)
    TextView nightDrawOption2;
    @BindView(R.id.night_draw_option_3)
    TextView nightDrawOption3;

    @BindView(R.id.night_values_group)
    Group nightValuesGroupLotto5jr;
    @BindView(R.id.evening_values_group)
    Group eveningValuesGroupLotto5jr;
    @BindView(R.id.night_values_group_royal5)
    Group nightValuesGroupLottoRoyal5;
    @BindView(R.id.evening_values_group_royal5)
    Group eveningValuesGroupLottoRoyal5;
    @BindView(R.id.evening_values_group_bolet)
    Group eveningValuesGroupBolet;

    @BindView(R.id.evening_image_draw_options)
    ImageView eveningImageDrawOptions;
    @BindView(R.id.night_image_draw_options)
    ImageView nightImageDrawOptions;
    @BindView(R.id.evening_image_maryage)
    ImageView eveningImageMaryage;
    @BindView(R.id.night_image_maryage)
    ImageView nightImageMaryage;
    @BindView(R.id.evening_image_home)
    ImageView eveningImageHome;
    @BindView(R.id.night_image_home)
    ImageView nightImageHome;
    @BindView(R.id.evening_image_combo)
    ImageView eveningImageCombo;
    @BindView(R.id.night_image_combo)
    ImageView nightImageCombo;

    @Nullable
    @BindView(R.id.day_value_lottojr_3_cross1)
    View cross1Lotto5Royal;
    @Nullable
    @BindView(R.id.day_value_lottojr_4_cross1)
    View cross2Lotto5Royal;
    @Nullable
    @BindView(R.id.night_value_lottojr_3_cross1)
    View cross3Lotto5Royal;
    @Nullable
    @BindView(R.id.night_value_lottojr_4_cross1)
    View cross4Lotto5Royal;

    private int dayDraw;
    private int eveningDraw;
    private int nightDraw;
    private String region;

    private FullSingleDraw fullSingleDraw;

    public static GameResultDetailsFragment newInstance(FullSingleDraw fullSingleDraw, String region, int dayDraw, int nightDraw) {
        GameResultDetailsFragment gameResultDetailsFragment = new GameResultDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DRAW_ITEM, fullSingleDraw);
        bundle.putInt(DAY_DRAW_KEY, dayDraw);
        bundle.putString(REGION_KEY, region);
        bundle.putInt(NIGHT_DRAW_KEY, nightDraw);
        gameResultDetailsFragment.setArguments(bundle);
        return gameResultDetailsFragment;
    }

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_result_details, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            fullSingleDraw = (FullSingleDraw) getArguments().getSerializable(DRAW_ITEM);
            dayDraw = getArguments().getInt(DAY_DRAW_KEY);
            nightDraw = getArguments().getInt(NIGHT_DRAW_KEY);
            region = getArguments().getString(REGION_KEY)/* ? Ticket.NY : Ticket.FL*/;
            setGamesResultsLayout();
            if (fullSingleDraw != null) {
                getDrawDetails(fullSingleDraw.getName());
            }
        }
    }


    private void setGamesResultsLayout() {
        lottoName.setText(TextUtil.capitalize(fullSingleDraw.getName() == null ? "" : fullSingleDraw.getName().equals(Draw.MARIAGE) ? getString(R.string.maryage) : fullSingleDraw.getName().equals(Draw.LOTTO5JR_NEW) ? getString(R.string.lotto5_5g) : fullSingleDraw.getName()));
        dayDrawDate.setText(DateTimeUtil.getWinningNumbersDateFormat(fullSingleDraw.getDayDate(), getActivity()));
        dayNumber1.setText(fullSingleDraw.getDayDraw() == null ? "" : fullSingleDraw.getDayDrawArray()[0]);
        dayNumber2.setText(fullSingleDraw.getDayDraw() == null || fullSingleDraw.getDayDrawArray().length < 2 ? "" : fullSingleDraw.getDayDrawArray()[1]);
        dayNumberLayout3.setVisibility(fullSingleDraw.getDayDrawArray().length > 1 ? View.VISIBLE : View.GONE);
        dayNumber3.setText(fullSingleDraw.getDayDraw() == null || fullSingleDraw.getDayDrawArray().length < 3 ? "" : fullSingleDraw.getDayDrawArray()[2]);
        dayNumberLayout3.setVisibility(fullSingleDraw.getDayDrawArray().length > 2 ? View.VISIBLE : View.GONE);
        nightDrawDate.setText(fullSingleDraw.getName() == null ? "" : DateTimeUtil.getWinningNumbersDateFormat(fullSingleDraw.getNightDate(), getActivity()));
        nightNumber1.setText(fullSingleDraw.getNightDraw() == null ? "" : fullSingleDraw.getNightDrawArray()[0]);
        nightNumber2.setText(fullSingleDraw.getNightDraw() == null || fullSingleDraw.getNightDrawArray().length < 2 ? "" : fullSingleDraw.getNightDrawArray()[1]);
        nightNumberLayout2.setVisibility(fullSingleDraw.getNightDrawArray().length > 1 ? View.VISIBLE : View.GONE);
        nightNumber3.setText(fullSingleDraw.getNightDraw() == null || fullSingleDraw.getNightDrawArray().length < 3 ? "" : fullSingleDraw.getNightDrawArray()[2]);
        nightNumberLayout3.setVisibility(fullSingleDraw.getNightDrawArray().length > 2 ? View.VISIBLE : View.GONE);

        eveningImageHome.setVisibility(isGeorgia() ? View.VISIBLE : View.GONE);
        eveningDrawDate.setVisibility(isGeorgia() ? View.VISIBLE : View.GONE);
        eveningDrawDate.setText(DateTimeUtil.getWinningNumbersDateFormat(fullSingleDraw.getEveningDate(), getActivity()));
        eveningNumber1.setText(fullSingleDraw.getEveningDraw() == null ? "" : fullSingleDraw.getEveningDrawArray()[0]);
        eveningNumberLayout1.setVisibility(isGeorgia() ? View.VISIBLE : View.GONE);
        eveningNumber2.setText(fullSingleDraw.getEveningDraw() == null || fullSingleDraw.getEveningDrawArray().length < 2 ? "" : fullSingleDraw.getEveningDrawArray()[1]);
        eveningNumberLayout2.setVisibility(isGeorgia() ? View.VISIBLE : View.GONE);
        eveningNumber3.setText(fullSingleDraw.getEveningDraw() == null || fullSingleDraw.getEveningDrawArray().length < 3 ? "" : fullSingleDraw.getEveningDrawArray()[2]);
        eveningNumberLayout3.setVisibility(isGeorgia() ? View.VISIBLE : View.GONE);

        if (isGeorgia()) {
            eveningImageHome.setImageResource(R.drawable.ic_moon);
            nightImageHome.setImageResource(R.drawable.ic_evening_draw);
            nightImageHome.setColorFilter(ContextCompat.getColor(getContext(), R.color.eveningColor));
        }

    }

    @Override
    public void getData(Object object) {
        if (object instanceof DrawBolotoResponse) {
            Log.d("bolotoTAG", object.toString());
            DrawBolotoResponse response = (DrawBolotoResponse) object;
            if (response.getState() == 0) {
                walletPresenter.getBolotoJackpot();
                itemBolotoDetails.setVisibility(View.VISIBLE);

                ArrayList<DrawDetailPay> drawDetailPays = getFormattedDrawDetailPays(response.getDrawPays());

                dayDraw = drawDetailPays.get(0).isDayDraw() ? 0 : 1;
                eveningDraw = drawDetailPays.get(0).isEveningDraw() ? 1 : 2;
                nightDraw = eveningDraw == 1 ? 2 : 1;

                ArrayList<DrawPay> drawPays = drawDetailPays.get(0).isDayDraw() ?
                        drawDetailPays.get(0).getDrawPays() :
                        drawDetailPays.get(1).getDrawPays();
                List<Long> pays = DrawDetailPay.getPayArray(drawPays);
                ArrayList<TextView> textViews = ViewUtil.getViews(itemBolotoDetails, TextView.class);
                int pay = 0;
                for (int i = 0; i < textViews.size(); i++) {
                    if (getString(R.string._value).equals(textViews.get(i).getTag())) {
                        textViews.get(i).setText(String.format("%sG", TextUtil.getDecimalString(pays.get(pay++))).replaceAll("[^0-9.G]", ","));
                    }

                    if (textViews.get(i).getId() == R.id.value_boloto_1) {
                        textViews.get(i).setText(String.format("%s.00G", LisaApp.getInstance().getBolotoJackpot()));
                    }
                }
            }
        } else if (object instanceof DrawLotto5RoyalResponse) {
            DrawLotto5RoyalResponse response = (DrawLotto5RoyalResponse) object;
            if (response.getState() == 0) {


                /*ArrayList<DrawDetailPay> drawDetailPays = getFormattedDrawDetailPays(response.getDrawPays());

                itemLottoRoyal5Details.setVisibility(View.VISIBLE);

                if (drawDetailPays.size() == 1) {
                    ArrayList<DrawPay> drawPays = drawDetailPays.get(0).getDrawPays();
                    setLotto5RoyalValues(drawPays, dayDraw);
                    nightValuesGroupLottoRoyal5.setVisibility(View.GONE);
                    return;
                }


                dayDraw = drawDetailPays.get(0).isDayDraw() ? 0 : 1;
                eveningDraw = drawDetailPays.get(0).isEveningDraw() ? 1 : 2;
                nightDraw = eveningDraw == 1 ? 2 : 1;

                ArrayList<DrawPay> drawDetailPaysDay = drawDetailPays.get(0).isDayDraw() ?
                        drawDetailPays.get(0).getDrawPays() :
                        drawDetailPays.get(1).getDrawPays();

                ArrayList<DrawPay> drawDetailPaysNight = drawDetailPays.get(0).isDayDraw() ?
                        drawDetailPays.get(1).getDrawPays() :
                        drawDetailPays.get(0).getDrawPays();

                setLotto5RoyalValues(drawDetailPaysDay, dayDraw);
                setLotto5RoyalValues(drawDetailPaysNight, nightDraw);*/
                setLotto5RoyalValues();
            }
        } else if (object instanceof DrawLotto5jrResponse) {
            DrawLotto5jrResponse response = (DrawLotto5jrResponse) object;
            if (response.getState() == 0) {


                /*ArrayList<DrawDetailPay> drawDetailPays = getFormattedDrawDetailPays(response.getDrawPays());

                item_lottojr_details.setVisibility(View.VISIBLE);

                if (drawDetailPays.size() == 1) {
                    ArrayList<DrawPay> drawPays = drawDetailPays.get(0).getDrawPays();
                    setLotto5jrValues(drawPays, dayDraw);
                    nightValuesGroupLotto5jr.setVisibility(View.GONE);
                    return;
                }


                dayDraw = drawDetailPays.get(0).isDayDraw() ? 0 : 1;
                eveningDraw = drawDetailPays.get(0).isEveningDraw() ? 1 : 2;
                nightDraw = eveningDraw == 1 ? 2 : 1;

                ArrayList<DrawPay> drawDetailPaysDay = drawDetailPays.get(0).isDayDraw() ?
                        drawDetailPays.get(0).getDrawPays() :
                        drawDetailPays.get(1).getDrawPays();

                ArrayList<DrawPay> drawDetailPaysNight = drawDetailPays.get(0).isDayDraw() ?
                        drawDetailPays.get(1).getDrawPays() :
                        drawDetailPays.get(0).getDrawPays();

                setLotto5jrValues(drawDetailPaysDay, dayDraw);
                setLotto5jrValues(drawDetailPaysNight, nightDraw);*/
                setLotto5jrValues();
            }
        } else if (object instanceof DrawMariageResponse) {
            DrawMariageResponse response = (DrawMariageResponse) object;
            if (response.getState() == 0) {
                /*ArrayList<DrawDetailRate> drawDetailRates = getFormattedDrawDetailRates(response.getDrawRates());
                itemMariageDetails.setVisibility(View.VISIBLE);

                dayDraw = drawDetailRates.get(0).isDayDraw() ? 0 : 1;
                eveningDraw = drawDetailRates.get(0).isEveningDraw() ? 1 : 2;
                nightDraw = eveningDraw == 1 ? 2 : 1;

                if (drawDetailRates.size() == 1) {
                    ArrayList<DrawRate> drawRates = drawDetailRates.get(0).getDrawRates();
                    setMariageValues(drawRates, dayDraw);
                    nightImage.setVisibility(View.GONE);
                    return;
                }

                ArrayList<DrawRate> drawDetailRatesDay = drawDetailRates.get(dayDraw).getDrawRates();
                ArrayList<DrawRate> drawDetailRatesEvening = drawDetailRates.get(eveningDraw).getDrawRates();
                ArrayList<DrawRate> drawDetailRatesNight = drawDetailRates.get(nightDraw).getDrawRates();*/

                setMariageValues();
                /*setMariageValues(drawDetailRatesDay, dayDraw);
                setMariageValues(drawDetailRatesEvening, eveningDraw);
                setMariageValues(drawDetailRatesNight, nightDraw);*/
            }
        } else if (object instanceof DrawLotto3Response) {
            DrawLotto3Response response = (DrawLotto3Response) object;
            if (response.getState() == 0) {

                /*ArrayList<DrawDetailRate> drawDetailRates = getFormattedDrawDetailRates(response.getDrawRates());

                itemComboDetails.setVisibility(View.VISIBLE);

                dayDraw = drawDetailRates.get(0).isDayDraw() ? 0 : 1;
                eveningDraw = drawDetailRates.get(0).isEveningDraw() ? 1 : 2;
                nightDraw = eveningDraw == 1 ? 2 : 1;


                if (drawDetailRates.size() == 1) {
                    ArrayList<DrawRate> drawRates = drawDetailRates.get(0).getDrawRates();
                    setLotto3Values(drawRates, dayDraw);
                    nightImage.setVisibility(View.GONE);
                    return;
                }
                ArrayList<DrawRate> drawDetailRatesDay = drawDetailRates.get(0).isDayDraw() ?
                        drawDetailRates.get(0).getDrawRates() :
                        drawDetailRates.get(1).getDrawRates();

                ArrayList<DrawRate> drawDetailRatesNight = drawDetailRates.get(0).isDayDraw() ?
                        drawDetailRates.get(1).getDrawRates() :
                        drawDetailRates.get(0).getDrawRates();

                setLotto3Values(drawDetailRatesDay, dayDraw);
                setLotto3Values(drawDetailRatesNight, nightDraw);*/
                setLotto3Values();
            }
        } else if (object instanceof DrawLotto4Response) {
            DrawLotto4Response response = (DrawLotto4Response) object;
            if (response.getState() == 0) {
                /*ArrayList<DrawDetailRate> drawDetailRates = getFormattedDrawDetailRates(response.getDrawRates());

                itemDrawOptionsDetails.setVisibility(View.VISIBLE);

                dayDraw = drawDetailRates.get(0).isDayDraw() ? 0 : 1;
                eveningDraw = drawDetailRates.get(0).isEveningDraw() ? 1 : 2;
                nightDraw = eveningDraw == 1 ? 2 : 1;

                if (drawDetailRates.size() == 1) {
                    ArrayList<DrawRate> drawRates = drawDetailRates.get(0).getDrawRates();
                    setDrawOptions(drawRates, dayDraw);
                    nightImage.setVisibility(View.GONE);
                    return;
                }

                ArrayList<DrawRate> drawDetailRatesDay = drawDetailRates.get(0).isDayDraw() ?
                        drawDetailRates.get(0).getDrawRates() :
                        drawDetailRates.get(1).getDrawRates();

                ArrayList<DrawRate> drawDetailRatesNight = drawDetailRates.get(0).isDayDraw() ?
                        drawDetailRates.get(1).getDrawRates() :
                        drawDetailRates.get(0).getDrawRates();

                setDrawOptions(drawDetailRatesDay, dayDraw);
                setDrawOptions(drawDetailRatesNight, nightDraw);*/
                setDrawOptions();
            }
        } else if (object instanceof DrawLotto5Response) {
            DrawLotto5Response response = (DrawLotto5Response) object;
            if (response.getState() == 0) {
                /*ArrayList<DrawDetailRate> drawDetailRates = getFormattedDrawDetailRates(response.getDrawRates());

                itemDrawOptionsDetails.setVisibility(View.VISIBLE);

                dayDraw = drawDetailRates.get(0).isDayDraw() ? 0 : 1;
                eveningDraw = drawDetailRates.get(0).isEveningDraw() ? 1 : 2;
                nightDraw = eveningDraw == 1 ? 2 : 1;

                if (drawDetailRates.size() == 1) {
                    ArrayList<DrawRate> drawRates = drawDetailRates.get(0).getDrawRates();
                    setDrawOptions(drawRates, dayDraw);
                    nightImage.setVisibility(View.GONE);
                    return;
                }


                ArrayList<DrawRate> drawDetailRatesDay = drawDetailRates.get(0).isDayDraw() ?
                        drawDetailRates.get(0).getDrawRates() :
                        drawDetailRates.get(1).getDrawRates();

                ArrayList<DrawRate> drawDetailRatesNight = drawDetailRates.get(0).isDayDraw() ?
                        drawDetailRates.get(1).getDrawRates() :
                        drawDetailRates.get(0).getDrawRates();

                setDrawOptions(drawDetailRatesDay, dayDraw);
                setDrawOptions(drawDetailRatesNight, nightDraw);*/
                setDrawOptions();
            }
        } /*else if (object instanceof BolotoJackpotResponse) {
            BolotoJackpotResponse response = (BolotoJackpotResponse) object;
            if (response.getJackpot() == 0 || response.getState() == 5) {
                walletPresenter.getBolotoJackpot();
            }
            Log.d("LOGTAGBJ", String.valueOf(response.getJackpot()));
            ArrayList<TextView> textViews = ViewUtil.getViews(itemBolotoDetails, TextView.class);
            for (int i = 0; i < textViews.size(); i++) {
                if (textViews.get(i).getId() == R.id.value_boloto_1) {
                    textViews.get(i).setText(response.getJackpot());
                }
            }
        }*/
    }

    private boolean isGeorgia() {
        return region.equals("GA");
    }

    private ArrayList<DrawDetailPay> getFormattedDrawDetailPays(ArrayList<DrawDetailPay> drawDetailPays) {
        /*ArrayList<DrawDetailPay> formattedDrawDetailPays = new ArrayList<>();
        for (DrawDetailPay drawDetailPay : drawDetailPays) {
            if ((region.equals(Ticket.NY) && drawDetailPay.getRegion().equals(Ticket.NY))
                    || (region.equals(Ticket.FL) && drawDetailPay.getRegion().equals(Ticket.FL)))
                formattedDrawDetailPays.add(drawDetailPay);
        }*/
        ArrayList<DrawDetailPay> formattedDrawDetailPays = new ArrayList<>(drawDetailPays);
        ArrayList<DrawDetailPay> removeRates = new ArrayList<>();
        /*for (DrawDetailPay removeRate: drawDetailPays) {
            if (removeRate.getRegion().equals("GA")) {
                removeRates.add(removeRate);
            }
        }*/
        formattedDrawDetailPays.removeAll(removeRates);
        return formattedDrawDetailPays;
    }

    private ArrayList<DrawDetailRate> getFormattedDrawDetailRates(ArrayList<DrawDetailRate> drawDetailRates) {
        /*ArrayList<DrawDetailRate> formattedDrawDetailRates = new ArrayList<>();
        for (DrawDetailRate drawDetailRate : drawDetailRates) {
            if ((region.equals(Ticket.NY) && drawDetailRate.getRegion().equals(Ticket.NY))
                    || (region.equals(Ticket.FL) && drawDetailRate.getRegion().equals(Ticket.FL)))
                formattedDrawDetailRates.add(drawDetailRate);
        }*/
        ArrayList<DrawDetailRate> formattedDrawDetailRates = new ArrayList<>(drawDetailRates);
        ArrayList<DrawDetailRate> removeRates = new ArrayList<>();
        /*for (DrawDetailRate removeRate: drawDetailRates) {
            if (!removeRate.getRegion().equals(region)) {
                removeRates.add(removeRate);
            }
        }*/
        formattedDrawDetailRates.removeAll(removeRates);
        return formattedDrawDetailRates;
    }

    private String formatDrawOption(int number, String option) {
        return String.format(getString(R.string.draw_option), String.valueOf(number), option);
    }

    private void setDrawOptions() {
        itemDrawOptionsDetails.setVisibility(View.VISIBLE);
        dayDrawOption1.setText(formatDrawOption(1, fullSingleDraw.getNumberAtPosMaryaj(0, FullSingleDraw.DRAW_DAY)));
        dayDrawOption2.setText(formatDrawOption(2, fullSingleDraw.getNumberAtPosMaryaj(1, FullSingleDraw.DRAW_DAY).substring(1)));
        dayDrawOption3.setText(formatDrawOption(3, fullSingleDraw.getNumberAtPosMaryaj(2, FullSingleDraw.DRAW_DAY).substring(1)));
        if (isGeorgia()) {
            eveningImageDrawOptions.setVisibility(View.VISIBLE);
            eveningDrawOption1.setText(formatDrawOption(1, fullSingleDraw.getNumberAtPosMaryaj(0, FullSingleDraw.DRAW_EVENING)));
            eveningDrawOption2.setText(formatDrawOption(2, fullSingleDraw.getNumberAtPosMaryaj(1, FullSingleDraw.DRAW_EVENING).substring(1)));
            eveningDrawOption3.setText(formatDrawOption(3, fullSingleDraw.getNumberAtPosMaryaj(2, FullSingleDraw.DRAW_EVENING).substring(1)));
            eveningImageDrawOptions.setImageResource(R.drawable.ic_moon);
            nightImageDrawOptions.setImageResource(R.drawable.ic_evening_draw);
            nightImageDrawOptions.setColorFilter(ContextCompat.getColor(getContext(), R.color.eveningColor));
        } else {
            eveningImageDrawOptions.setVisibility(View.GONE);
            eveningDrawOption1.setVisibility(View.GONE);
            eveningDrawOption2.setVisibility(View.GONE);
            eveningDrawOption3.setVisibility(View.GONE);

        }
        nightDrawOption1.setText(formatDrawOption(1, fullSingleDraw.getNumberAtPosMaryaj(0, FullSingleDraw.DRAW_NIGHT)));
        nightDrawOption2.setText(formatDrawOption(2, fullSingleDraw.getNumberAtPosMaryaj(1, FullSingleDraw.DRAW_NIGHT).substring(1)));
        nightDrawOption3.setText(formatDrawOption(3, fullSingleDraw.getNumberAtPosMaryaj(2, FullSingleDraw.DRAW_NIGHT).substring(1)));
        /*

        dayDrawOption1.setText(String.format(getString(R.string.draw_option), getString(R.string.one), drawRates.get(0).getNum()));
        dayDrawOption2.setText(String.format(getString(R.string.draw_option), getString(R.string.two), drawRates.get(1).getNum()));
        dayDrawOption3.setText(String.format(getString(R.string.draw_option), getString(R.string.three), drawRates.get(2).getNum()));
        eveningDrawOption1.setText(String.format(getString(R.string.draw_option), getString(R.string.one), drawRates.get(0).getNum()));
        eveningDrawOption2.setText(String.format(getString(R.string.draw_option), getString(R.string.two), drawRates.get(1).getNum()));
        eveningDrawOption3.setText(String.format(getString(R.string.draw_option), getString(R.string.three), drawRates.get(2).getNum()));
        nightDrawOption1.setText(String.format(getString(R.string.draw_option), getString(R.string.one), drawRates.get(0).getNum()));
        nightDrawOption2.setText(String.format(getString(R.string.draw_option), getString(R.string.two), drawRates.get(1).getNum()));
        nightDrawOption3.setText(String.format(getString(R.string.draw_option), getString(R.string.three), drawRates.get(2).getNum()));*/
    }

    private void setLotto5jrValues() {
        item_lottojr_details.setVisibility(View.VISIBLE);

        dayValueLottojr1.setText(fullSingleDraw.getNumberAtPosLotto5Jr(0, FullSingleDraw.DRAW_DAY));
        dayValueLottojr2.setText(fullSingleDraw.getNumberAtPosLotto5Jr(1, FullSingleDraw.DRAW_DAY));
        dayValueLottojr3.setText(fullSingleDraw.getNumberAtPosLotto5Jr(2, FullSingleDraw.DRAW_DAY));
        dayValueLottojr4.setText(fullSingleDraw.getNumberAtPosLotto5Jr(3, FullSingleDraw.DRAW_DAY));

        if (isGeorgia()) {
            eveningValueLottojr1.setText(fullSingleDraw.getNumberAtPosLotto5Jr(0, FullSingleDraw.DRAW_EVENING));
            eveningValueLottojr2.setText(fullSingleDraw.getNumberAtPosLotto5Jr(1, FullSingleDraw.DRAW_EVENING));
            eveningValueLottojr3.setText(fullSingleDraw.getNumberAtPosLotto5Jr(2, FullSingleDraw.DRAW_EVENING));
            eveningValueLottojr4.setText(fullSingleDraw.getNumberAtPosLotto5Jr(3, FullSingleDraw.DRAW_EVENING));
            eveningImageJr.setImageResource(R.drawable.ic_moon);
            nightImageJr.setImageResource(R.drawable.ic_evening_draw);
            nightImageJr.setColorFilter(ContextCompat.getColor(getContext(), R.color.eveningColor));
        } else {
            eveningValuesGroupLotto5jr.setVisibility(View.GONE);
        }
        nightValueLottojr1.setText(fullSingleDraw.getNumberAtPosLotto5Jr(0, FullSingleDraw.DRAW_NIGHT));
        nightValueLottojr2.setText(fullSingleDraw.getNumberAtPosLotto5Jr(1, FullSingleDraw.DRAW_NIGHT));
        nightValueLottojr3.setText(fullSingleDraw.getNumberAtPosLotto5Jr(2, FullSingleDraw.DRAW_NIGHT));
        nightValueLottojr4.setText(fullSingleDraw.getNumberAtPosLotto5Jr(3, FullSingleDraw.DRAW_NIGHT));
    }

    private void setLotto5RoyalValues() {
        itemLottoRoyal5Details.setVisibility(View.VISIBLE);

        dayValueLotto5Royal1.setText(fullSingleDraw.getNumberAtPosLotto5Royal(0, FullSingleDraw.DRAW_DAY));
        dayValueLotto5Royal2.setText(fullSingleDraw.getNumberAtPosLotto5Royal(1, FullSingleDraw.DRAW_DAY));
        dayValueLotto5Royal3.setText(fullSingleDraw.getNumberAtPosLotto5Royal(2, FullSingleDraw.DRAW_DAY));
        dayValueLotto5Royal4.setText(fullSingleDraw.getNumberAtPosLotto5Royal(3, FullSingleDraw.DRAW_DAY));
        dayValueLotto5Royal5.setText(fullSingleDraw.getNumberAtPosLotto5Royal(4, FullSingleDraw.DRAW_DAY));

        if (isGeorgia()) {
            eveningValueLotto5Royal1.setText(fullSingleDraw.getNumberAtPosLotto5Royal(0, FullSingleDraw.DRAW_EVENING));
            eveningValueLotto5Royal2.setText(fullSingleDraw.getNumberAtPosLotto5Royal(1, FullSingleDraw.DRAW_EVENING));
            eveningValueLotto5Royal3.setText(fullSingleDraw.getNumberAtPosLotto5Royal(2, FullSingleDraw.DRAW_EVENING));
            eveningValueLotto5Royal4.setText(fullSingleDraw.getNumberAtPosLotto5Royal(3, FullSingleDraw.DRAW_EVENING));
            eveningValueLotto5Royal5.setText(fullSingleDraw.getNumberAtPosLotto5Royal(4, FullSingleDraw.DRAW_EVENING));
            eveningImageRoyal.setImageResource(R.drawable.ic_moon);
            nightImageRoyal.setImageResource(R.drawable.ic_evening_draw);
            nightImageRoyal.setColorFilter(ContextCompat.getColor(getContext(), R.color.eveningColor));
        } else {
            eveningValuesGroupLottoRoyal5.setVisibility(View.GONE);
        }
        nightValueLotto5Royal1.setText(fullSingleDraw.getNumberAtPosLotto5Royal(0, FullSingleDraw.DRAW_NIGHT));
        nightValueLotto5Royal2.setText(fullSingleDraw.getNumberAtPosLotto5Royal(1, FullSingleDraw.DRAW_NIGHT));
        nightValueLotto5Royal4.setText(fullSingleDraw.getNumberAtPosLotto5Royal(3, FullSingleDraw.DRAW_NIGHT));
        nightValueLotto5Royal3.setText(fullSingleDraw.getNumberAtPosLotto5Royal(2, FullSingleDraw.DRAW_NIGHT));
        nightValueLotto5Royal5.setText(fullSingleDraw.getNumberAtPosLotto5Royal(4, FullSingleDraw.DRAW_NIGHT));
    }

    private void setBoletValues() {
        itemBoletDetails.setVisibility(View.VISIBLE);

        dayValueBolet1.setText(fullSingleDraw.getNumberAtPosBolet(0, FullSingleDraw.DRAW_DAY));
        dayValueBolet2.setText(fullSingleDraw.getNumberAtPosBolet(1, FullSingleDraw.DRAW_DAY));
        dayValueBolet3.setText(fullSingleDraw.getNumberAtPosBolet(2, FullSingleDraw.DRAW_DAY));

        if (isGeorgia()) {
            eveningValueBolet1.setText(fullSingleDraw.getNumberAtPosBolet(0, FullSingleDraw.DRAW_EVENING));
            eveningValueBolet2.setText(fullSingleDraw.getNumberAtPosBolet(1, FullSingleDraw.DRAW_EVENING));
            eveningValueBolet3.setText(fullSingleDraw.getNumberAtPosBolet(2, FullSingleDraw.DRAW_EVENING));
            eveningImageBolet.setImageResource(R.drawable.ic_moon);
            nightImageBolet.setImageResource(R.drawable.ic_evening_draw);
            nightImageBolet.setColorFilter(ContextCompat.getColor(getContext(), R.color.eveningColor));
        } else {
            eveningValuesGroupBolet.setVisibility(View.GONE);
        }
        nightValueBolet1.setText(fullSingleDraw.getNumberAtPosBolet(0, FullSingleDraw.DRAW_NIGHT));
        nightValueBolet2.setText(fullSingleDraw.getNumberAtPosBolet(1, FullSingleDraw.DRAW_NIGHT));
        nightValueBolet3.setText(fullSingleDraw.getNumberAtPosBolet(2, FullSingleDraw.DRAW_NIGHT));
    }

    private void setLotto5jrValues(ArrayList<DrawPay> drawPays, int draw) {
        if (draw == dayDraw) {
            dayValueLottojr1.setText(drawPays.get(0).getNum());
            dayValueLottojr2.setText(drawPays.get(1).getNum().replaceAll("X", "").replaceAll("-", ""));
            dayValueLottojr3.setText(drawPays.get(2).getNum().replaceAll("X", "").replaceAll("-", ""));
            dayValueLottojr4.setText(drawPays.get(3).getNum().replaceAll("X", "").replaceAll("-", ""));
        } else {
            nightValueLottojr1.setText(drawPays.get(0).getNum());
            nightValueLottojr2.setText(drawPays.get(1).getNum().replaceAll("X", "").replaceAll("-", ""));
            nightValueLottojr3.setText(drawPays.get(2).getNum().replaceAll("X", "").replaceAll("-", ""));
            nightValueLottojr4.setText(drawPays.get(3).getNum().replaceAll("X", "").replaceAll("-", ""));
        }
    }

    private void setLotto5RoyalValues(ArrayList<DrawPay> drawPays, int draw) {
        if (draw == dayDraw) {
            dayValueLotto5Royal1.setText(drawPays.get(0).getNum());
            dayValueLotto5Royal2.setText(drawPays.get(1).getNum().replaceAll("X", ""));
            dayValueLotto5Royal3.setText(drawPays.get(2).getNum().replaceAll("X", ""));
            dayValueLotto5Royal4.setText(drawPays.get(3).getNum().replaceAll("X", ""));
            dayValueLotto5Royal5.setText(drawPays.get(4).getNum().replaceAll("X", ""));
        } else {
            nightValueLotto5Royal1.setText(drawPays.get(0).getNum());
            nightValueLotto5Royal2.setText(drawPays.get(1).getNum().replaceAll("X", ""));
            nightValueLotto5Royal3.setText(drawPays.get(2).getNum().replaceAll("X", ""));
            nightValueLotto5Royal4.setText(drawPays.get(3).getNum().replaceAll("X", ""));
            nightValueLotto5Royal5.setText(drawPays.get(4).getNum().replaceAll("X", ""));
        }
    }

    @SuppressLint("NewApi")
    private void setLotto3Values(ArrayList<DrawRate> drawRates, int draw) {
        List<String> strings;
        strings = TextUtil.getThreePermutations(drawRates.get(0).getNum().split("-")[0], drawRates.get(0).getNum().split("-")[1], drawRates.get(0).getNum().split("-")[2]);
        if (draw == dayDraw) {
            daycombo1.setText(String.format(getString(R.string.combo), strings.get(0)));
            if (strings.size() > 1) {
                daycombo2.setText(String.format(getString(R.string.combo), strings.get(1)));
                daycombo3.setText(String.format(getString(R.string.combo), strings.get(2)));
                if (strings.size() > 3) {
                    daycombo4.setText(String.format(getString(R.string.combo), strings.get(3)));
                    daycombo5.setText(String.format(getString(R.string.combo), strings.get(4)));
                    daycombo6.setText(String.format(getString(R.string.combo), strings.get(5)));
                }
            }
        } else {
            nightcombo1.setText(String.format(getString(R.string.combo), strings.get(0)));
            if (strings.size() > 1) {
                nightcombo2.setText(String.format(getString(R.string.combo), strings.get(1)));
                nightcombo3.setText(String.format(getString(R.string.combo), strings.get(2)));
                if (strings.size() > 3) {
                    nightcombo4.setText(String.format(getString(R.string.combo), strings.get(3)));
                    nightcombo5.setText(String.format(getString(R.string.combo), strings.get(4)));
                    nightcombo6.setText(String.format(getString(R.string.combo), strings.get(5)));
                }
            }
        }
    }

    @SuppressLint("NewApi")
    private void setLotto3Values() {
        itemComboDetails.setVisibility(View.VISIBLE);

        List<String> strings = fullSingleDraw.getNumberDrawMariyaj(FullSingleDraw.DRAW_DAY);
        strings = TextUtil.getThreePermutations(strings.get(0), strings.get(1), strings.get(2));
        //strings = TextUtil.getThreePermutations(drawRates.get(0).getNum().split("-")[0], drawRates.get(0).getNum().split("-")[1], drawRates.get(0).getNum().split("-")[2]);
        daycombo1.setText(String.format(getString(R.string.combo), strings.get(0)));
        daycombo2.setText(String.format(getString(R.string.combo), strings.get(1)));
        daycombo3.setText(String.format(getString(R.string.combo), strings.get(2)));

        if (isGeorgia()) {
            strings = fullSingleDraw.getNumberDrawMariyaj(FullSingleDraw.DRAW_EVENING);
            strings = TextUtil.getThreePermutations(strings.get(0), strings.get(1), strings.get(2));
            eveningcombo1.setText(String.format(getString(R.string.combo), strings.get(0)));
            eveningcombo2.setText(String.format(getString(R.string.combo), strings.get(1)));
            eveningcombo3.setText(String.format(getString(R.string.combo), strings.get(2)));


            eveningImageCombo.setImageResource(R.drawable.ic_moon);
            nightImageCombo.setImageResource(R.drawable.ic_evening_draw);
            nightImageCombo.setColorFilter(ContextCompat.getColor(getContext(), R.color.eveningColor));
        } else {
            eveningImageCombo.setVisibility(View.GONE);
            eveningcombo1.setVisibility(View.GONE);
            eveningcombo2.setVisibility(View.GONE);
            eveningcombo3.setVisibility(View.GONE);
            eveningcombo4.setVisibility(View.GONE);
            eveningcombo5.setVisibility(View.GONE);
            eveningcombo6.setVisibility(View.GONE);
        }

        strings = fullSingleDraw.getNumberDrawMariyaj(FullSingleDraw.DRAW_NIGHT);
        strings = TextUtil.getThreePermutations(strings.get(0), strings.get(1), strings.get(2));
        nightcombo1.setText(String.format(getString(R.string.combo), strings.get(0)));
        nightcombo2.setText(String.format(getString(R.string.combo), strings.get(1)));
        nightcombo3.setText(String.format(getString(R.string.combo), strings.get(2)));

    }

    private void setMariageValues() {
        itemMariageDetails.setVisibility(View.VISIBLE);

        dayMariage1.setText(fullSingleDraw.getNumberAtPosMaryaj(0, FullSingleDraw.DRAW_DAY));
        dayMariage2.setText(fullSingleDraw.getNumberAtPosMaryaj(1, FullSingleDraw.DRAW_DAY));
        dayMariage3.setText(fullSingleDraw.getNumberAtPosMaryaj(2, FullSingleDraw.DRAW_DAY));
        dayMariage4.setText(fullSingleDraw.getNumberAtPosMaryaj(3, FullSingleDraw.DRAW_DAY));
        dayMariage5.setText(fullSingleDraw.getNumberAtPosMaryaj(4, FullSingleDraw.DRAW_DAY));
        dayMariage6.setText(fullSingleDraw.getNumberAtPosMaryaj(5, FullSingleDraw.DRAW_DAY));
        //dayMariage2.setText(fullSingleDraw.getNumberAtPos(1, FullSingleDraw.DRAW_DAY));
        //dayMariage4.setText(fullSingleDraw.getNumberAtPos(3, FullSingleDraw.DRAW_DAY));
        /*dayMariage5.setText(fullSingleDraw.getNumberAtPos(4, FullSingleDraw.DRAW_DAY));
        dayMariage6.setText(fullSingleDraw.getNumberAtPos(5, FullSingleDraw.DRAW_DAY));*/

        if (isGeorgia()) {
            eveningImageMaryage.setVisibility(View.VISIBLE);
            eveningMariage1.setText(fullSingleDraw.getNumberAtPosMaryaj(0, FullSingleDraw.DRAW_EVENING));
            eveningMariage2.setText(fullSingleDraw.getNumberAtPosMaryaj(1, FullSingleDraw.DRAW_EVENING));
            eveningMariage3.setText(fullSingleDraw.getNumberAtPosMaryaj(2, FullSingleDraw.DRAW_EVENING));
            eveningMariage4.setText(fullSingleDraw.getNumberAtPosMaryaj(3, FullSingleDraw.DRAW_EVENING));
            eveningMariage5.setText(fullSingleDraw.getNumberAtPosMaryaj(4, FullSingleDraw.DRAW_EVENING));
            eveningMariage6.setText(fullSingleDraw.getNumberAtPosMaryaj(5, FullSingleDraw.DRAW_EVENING));
            eveningImageMaryage.setImageResource(R.drawable.ic_moon);
            nightImageMaryage.setImageResource(R.drawable.ic_evening_draw);
            nightImageMaryage.setColorFilter(ContextCompat.getColor(getContext(), R.color.eveningColor));
        } else {
            eveningImageMaryage.setVisibility(View.GONE);
            eveningMariage1.setVisibility(View.GONE);
            eveningMariage2.setVisibility(View.GONE);
            eveningMariage3.setVisibility(View.GONE);
            eveningMariage4.setVisibility(View.GONE);
            eveningMariage5.setVisibility(View.GONE);
            eveningMariage6.setVisibility(View.GONE);
        }

        nightMariage1.setText(fullSingleDraw.getNumberAtPosMaryaj(0, FullSingleDraw.DRAW_NIGHT));
        nightMariage2.setText(fullSingleDraw.getNumberAtPosMaryaj(1, FullSingleDraw.DRAW_NIGHT));
        nightMariage3.setText(fullSingleDraw.getNumberAtPosMaryaj(2, FullSingleDraw.DRAW_NIGHT));
        nightMariage4.setText(fullSingleDraw.getNumberAtPosMaryaj(3, FullSingleDraw.DRAW_NIGHT));
        nightMariage5.setText(fullSingleDraw.getNumberAtPosMaryaj(4, FullSingleDraw.DRAW_NIGHT));
        nightMariage6.setText(fullSingleDraw.getNumberAtPosMaryaj(5, FullSingleDraw.DRAW_NIGHT));
    }

    private void setMariageValues(ArrayList<DrawRate> drawRates, int draw) {
        if (draw == dayDraw) {
            dayMariage1.setText(drawRates.get(0).getNum());
            dayMariage2.setText(drawRates.get(1).getNum());
            dayMariage3.setText(drawRates.get(2).getNum());
            dayMariage4.setText(drawRates.get(3).getNum());
            dayMariage5.setText(drawRates.get(4).getNum());
            dayMariage6.setText(drawRates.get(5).getNum());
        } else if (draw == eveningDraw) {
            eveningMariage1.setText(drawRates.get(0).getNum());
            eveningMariage2.setText(drawRates.get(1).getNum());
            eveningMariage3.setText(drawRates.get(2).getNum());
            eveningMariage4.setText(drawRates.get(3).getNum());
            eveningMariage5.setText(drawRates.get(4).getNum());
            eveningMariage6.setText(drawRates.get(5).getNum());
        } else if (draw == nightDraw) {
            nightMariage1.setText(drawRates.get(0).getNum());
            nightMariage2.setText(drawRates.get(1).getNum());
            nightMariage3.setText(drawRates.get(2).getNum());
            nightMariage4.setText(drawRates.get(3).getNum());
            nightMariage5.setText(drawRates.get(4).getNum());
            nightMariage6.setText(drawRates.get(5).getNum());
        }
    }

    private void getDrawDetails(String name) {
        switch (name) {
            case Draw.BOLOTO:
                walletPresenter.getBolotoDrawDetail(new Phone(SharedPreferencesUtil.getPhone()), DateTimeUtil.getDobFormat((long) fullSingleDraw.getDayDate() * DateTimeUtil.SECOND));
                break;
            case Draw.BOLET:
                setBoletValues();
                break;

            case Draw.MARIAGE:
                dayNumberLayout1.setVisibility(View.GONE);
                eveningNumberLayout1.setVisibility(View.GONE);
                nightNumberLayout1.setVisibility(View.GONE);
                walletPresenter.getMariageDrawDetail(new Phone(SharedPreferencesUtil.getPhone()), DateTimeUtil.getDobFormat((long) fullSingleDraw.getDayDate() * DateTimeUtil.SECOND));
                break;

            case Draw.LOTTO3:
                walletPresenter.getLotto3DrawDetail(new Phone(SharedPreferencesUtil.getPhone()), DateTimeUtil.getDobFormat((long) fullSingleDraw.getDayDate() * DateTimeUtil.SECOND));
                break;

            case Draw.LOTTO4:
                dayNumberLayout1.setVisibility(View.GONE);
                eveningNumberLayout1.setVisibility(View.GONE);
                nightNumberLayout1.setVisibility(View.GONE);
                walletPresenter.getLotto4DrawDetail(new Phone(SharedPreferencesUtil.getPhone()), DateTimeUtil.getDobFormat((long) fullSingleDraw.getDayDate() * DateTimeUtil.SECOND));
                break;

            case Draw.LOTTO5:
                walletPresenter.getLotto5DrawDetail(new Phone(SharedPreferencesUtil.getPhone()), DateTimeUtil.getDobFormat((long) fullSingleDraw.getDayDate() * DateTimeUtil.SECOND));
                break;

            case Draw.LOTTO5JR_NEW:
                dayNumberLayout3.setVisibility(View.GONE);
                eveningNumberLayout3.setVisibility(View.GONE);
                nightNumberLayout3.setVisibility(View.GONE);
                walletPresenter.getLotto5jrDrawDetail(new Phone(SharedPreferencesUtil.getPhone()), DateTimeUtil.getDobFormat((long) fullSingleDraw.getDayDate() * DateTimeUtil.SECOND));
                break;
            case Draw.ROYAL5:
                dayNumberLayout3.setVisibility(View.GONE);
                eveningNumberLayout3.setVisibility(View.GONE);
                nightNumberLayout3.setVisibility(View.GONE);
                walletPresenter.getLotto5RoyalDrawDetail(new Phone(SharedPreferencesUtil.getPhone()), DateTimeUtil.getDobFormat((long) fullSingleDraw.getDayDate() * DateTimeUtil.SECOND));
                break;
        }
    }
}