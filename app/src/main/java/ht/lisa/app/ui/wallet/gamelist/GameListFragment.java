package ht.lisa.app.ui.wallet.gamelist;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import butterknife.BindView;
import butterknife.OnClick;
import ht.lisa.app.LisaApp;
import ht.lisa.app.R;
import ht.lisa.app.model.Phone;
import ht.lisa.app.model.response.BolotoDrawResponse;
import ht.lisa.app.model.response.BolotoJackpotResponse;
import ht.lisa.app.model.response.DrawResponse;
import ht.lisa.app.model.response.JackpotResponse;
import ht.lisa.app.model.response.Lotto5jrJackpotResponse;
import ht.lisa.app.model.response.RegionDrawResponse;
import ht.lisa.app.ui.main.MainActivity;
import ht.lisa.app.ui.wallet.BaseWalletFragment;
import ht.lisa.app.ui.wallet.WalletActivity;
import ht.lisa.app.ui.wallet.games.GamesFragment;
import ht.lisa.app.util.DateTimeUtil;
import ht.lisa.app.util.OnFragmentVisibleListener;
import ht.lisa.app.util.RxUtil;
import ht.lisa.app.util.SharedPreferencesUtil;
import ht.lisa.app.util.TextUtil;
import io.reactivex.functions.Consumer;

import static ht.lisa.app.model.response.DrawResponse.SECOND_DRAW;

public class GameListFragment extends BaseWalletFragment {

    public static final String SCREEN_NAME = "HomeScreen";

    @BindView(R.id.game_list_next_draw)
    TextView gameListNextDraw;
    @BindView(R.id.game_list_next_draw_icon)
    ImageView gameListNextDrawIcon;
    @BindView(R.id.game_list_hours_amount)
    TextView gameListHoursAmount;
    @BindView(R.id.game_list_minutes_amount)
    TextView gameListMinutesAmount;
    @BindView(R.id.game_list_seconds_amount)
    TextView gameListSecondsAmount;
    @BindView(R.id.game_list_boloto_amount)
    TextView gameListBolotoAmount;
    @BindView(R.id.game_list_royal5_amount)
    TextView gameListRoyal5Amount;
    @BindView(R.id.game_list_lotto5jr_amount)
    TextView gameListLotto5jrAmount;
    @BindView(R.id.item_game_boloto)
    View itemGameBoloto;
    @BindView(R.id.item_game_bolet)
    View itemGameBolet;
    @BindView(R.id.item_game_mariaj)
    View itemGameMariaj;
    @BindView(R.id.item_game_lotto3)
    View itemGameLotto3;
    @BindView(R.id.item_game_lotto4)
    View itemGameLotto4;
    @BindView(R.id.item_game_lotto5)
    View itemGameLotto5;
    @BindView(R.id.item_game_lotto5jr)
    View itemGameLotto5Jr;

    private OnFragmentVisibleListener onFragmentVisibleListener;
    private CountDownTimer countDownTimer;
    private String jackPot;
    private String progressiveJackPot;
    private String royalJackpot;
    private DrawResponse drawResponse;

    private RegionDrawResponse flDrawResponse;
    private RegionDrawResponse nyDrawResponse;
    private RegionDrawResponse gaDrawResponse;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_list, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showProgress();
        RxUtil.delayedConsumer(600, aLong -> walletPresenter.getDraw(new Phone(SharedPreferencesUtil.getPhone())));
        setButtonsClickable(false);
        LisaApp.getInstance().initGetUserListRequest();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            onFragmentVisibleListener = (OnFragmentVisibleListener) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (onFragmentVisibleListener != null) {
            onFragmentVisibleListener.onFragmentShowListener(GameListFragment.class.getSimpleName(), true);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (onFragmentVisibleListener != null) {
            onFragmentVisibleListener.onFragmentShowListener(GameListFragment.class.getSimpleName(), false);
            onFragmentVisibleListener = null;
        }
    }

    private void startCountDownTimer(long startTime) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        long timer = startTime - System.currentTimeMillis();
        countDownTimer = new CountDownTimer(timer, DateTimeUtil.SECOND) {
            public void onTick(long millisUntilFinished) {
                if (getContext() != null) {
                    gameListHoursAmount.setText(DateTimeUtil.getFormattedTime(millisUntilFinished, DateTimeUtil.HR));
                    gameListMinutesAmount.setText(DateTimeUtil.getFormattedTime(millisUntilFinished, DateTimeUtil.MIN));
                    gameListSecondsAmount.setText(DateTimeUtil.getFormattedTime(millisUntilFinished, DateTimeUtil.SEC));
                }
            }

            public void onFinish() {
                if (getContext() != null) {
                    gameListHoursAmount.setText(getStringFromResource(R.string.zero));
                    gameListMinutesAmount.setText(getStringFromResource(R.string.zero));
                    gameListSecondsAmount.setText(getStringFromResource(R.string.zero));
                    walletPresenter.getDraw(new Phone(SharedPreferencesUtil.getPhone()));
                }
            }
        }.start();
    }

    private void setButtonsClickable(boolean clickable) {
        itemGameBolet.setClickable(clickable);
        itemGameBoloto.setClickable(clickable);
        itemGameMariaj.setClickable(clickable);
        itemGameLotto3.setClickable(clickable);
        itemGameLotto4.setClickable(clickable);
        itemGameLotto5.setClickable(clickable);
        itemGameLotto5Jr.setClickable(clickable);
    }

    @Override
    public void getData(Object object) {
        if (object instanceof DrawResponse) {
            drawResponse = (DrawResponse) object;
            if (drawResponse.getState() == 0) {
                RxUtil.delayedConsumer(300, aLong -> walletPresenter.getBolotoDraw(new Phone(SharedPreferencesUtil.getPhone()), drawResponse.getDrawList().get(SECOND_DRAW).getDraw()));
                RxUtil.delayedConsumer(800, aLong -> walletPresenter.getBolotoJackpot());
                RxUtil.delayedConsumer(1200, aLong -> walletPresenter.getLotto5jrJackpot());
                RxUtil.delayedConsumer(1500, aLong -> walletPresenter.getLotto5RoyalJackpot());
            } else
                Toast.makeText(getContext(), drawResponse.getState() + " " + drawResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof BolotoDrawResponse) {
            BolotoDrawResponse bolotoDrawResponse = (BolotoDrawResponse) object;
            if (bolotoDrawResponse.getState() == 0) {
                flDrawResponse = bolotoDrawResponse.getFlDraw();
                nyDrawResponse = bolotoDrawResponse.getNyDraw();
                gaDrawResponse = bolotoDrawResponse.getGaDraw();
                long drawSec;
                long drawDate;
                drawSec = nyDrawResponse.getDrawLeft();
                drawDate = nyDrawResponse.getDraw();
                if (flDrawResponse.getDrawLeft() < drawSec) {
                    drawSec = flDrawResponse.getDrawLeft();
                    drawDate = flDrawResponse.getDraw();
                }
                if (gaDrawResponse.getDrawLeft() < drawSec) {
                    drawSec = gaDrawResponse.getDrawLeft();
                    drawDate = gaDrawResponse.getDraw();
                }

                /*if (flDrawResponse.getDrawLeft() < 0) {
                    drawSec = nyDrawResponse.getDrawLeft();
                    drawDate = nyDrawResponse.getDraw();
                } else if (nyDrawResponse.getDrawLeft() < 0) {
                    drawSec = flDrawResponse.getDrawLeft();
                    drawDate = flDrawResponse.getDraw();
                } else {
                    drawSec = Math.min(flDrawResponse.getDrawLeft(), nyDrawResponse.getDrawLeft());
                    drawDate = Math.min(flDrawResponse.getDraw(), nyDrawResponse.getDraw());
                }*/
                drawSec = System.currentTimeMillis() + drawSec * DateTimeUtil.SECOND;
                flDrawResponse.setLastLeft(System.currentTimeMillis() + flDrawResponse.getDrawLeft() * DateTimeUtil.SECOND);
                flDrawResponse.setLastStop(System.currentTimeMillis() + flDrawResponse.getStopLeft() * DateTimeUtil.SECOND);
                nyDrawResponse.setLastLeft(System.currentTimeMillis() + nyDrawResponse.getDrawLeft() * DateTimeUtil.SECOND);
                nyDrawResponse.setLastStop(System.currentTimeMillis() + nyDrawResponse.getStopLeft() * DateTimeUtil.SECOND);
                gaDrawResponse.setLastLeft(System.currentTimeMillis() + gaDrawResponse.getDrawLeft() * DateTimeUtil.SECOND);
                gaDrawResponse.setLastStop(System.currentTimeMillis() + gaDrawResponse.getStopLeft() * DateTimeUtil.SECOND);


                //drawSec = (System.currentTimeMillis() + (bolotoDrawResponse.getDrawLeft() * DateTimeUtil.SECOND)) - 58000;

                gameListNextDrawIcon.setImageResource(DateTimeUtil.isDayDraw(drawDate, getActivity()) ? R.drawable.ic_sun_white :
                        DateTimeUtil.isEveningDraw(drawDate, getActivity()) ? R.drawable.ic_evening_draw :
                                R.drawable.ic_white_moon);
                gameListNextDraw.setText(DateTimeUtil.getGameListDateFormat(drawDate, getActivity()));
                long finalDrawSec = drawSec;
                RxUtil.delayedConsumer(200, new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        startCountDownTimer(finalDrawSec);
                    }
                });
            } else {
                walletPresenter.getBolotoDraw(new Phone(SharedPreferencesUtil.getPhone()), drawResponse.getDrawList().get(SECOND_DRAW).getDraw());
                Toast.makeText(getContext(), bolotoDrawResponse.getBaseErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (object instanceof BolotoJackpotResponse) {
            BolotoJackpotResponse response = (BolotoJackpotResponse) object;
            if (response.getJackpot() == 0 || response.getState() == 5) {
                walletPresenter.getBolotoJackpot();
            }
            jackPot = String.format("%s", TextUtil.getDecimalStringJackpot(response.getJackpot()).replaceAll("[^0-9.G]", ","));
            LisaApp.getInstance().setBolotoJackpot(TextUtil.getDecimalStringJackpot(response.getJackpot()).replaceAll("[^0-9.G]", ","));
            gameListBolotoAmount.setText(jackPot);

        } else if (object instanceof Lotto5jrJackpotResponse) {
            Lotto5jrJackpotResponse response = (Lotto5jrJackpotResponse) object;
            if (response.getJackpot() == 0 || response.getState() == 5) {
                walletPresenter.getLotto5jrJackpot();
            } else {
                setButtonsClickable(true);
            }
            progressiveJackPot = String.format("%sG", TextUtil.getDecimalStringJackpot(response.getJackpot()).replaceAll("[^0-9.G]", ","));
            gameListLotto5jrAmount.post(() -> gameListLotto5jrAmount.setText(progressiveJackPot));

        } else if (object instanceof JackpotResponse) {
            JackpotResponse response = (JackpotResponse) object;
            if (response.getJackpot() == 0 || response.getState() == 5) {
                walletPresenter.getLotto5RoyalJackpot();
            }
            royalJackpot = String.format("%sG", TextUtil.getDecimalStringJackpot(response.getJackpot()).replaceAll("[^0-9.G]", ","));
            gameListRoyal5Amount.setText(royalJackpot);

        }
    }

    @OnClick({R.id.item_game_boloto, R.id.item_game_lotto5jr, R.id.item_game_bolet, R.id.item_game_mariaj,
            R.id.item_game_lotto3, R.id.item_game_lotto4, R.id.item_game_lotto5, R.id.item_game_lotto5_royal})
    void onGameClick(View view) {
        Intent gamesIntent = new Intent(getActivity(), WalletActivity.class);
        gamesIntent.putExtra(WalletActivity.WALLET_FRAGMENT, WalletActivity.WalletPage.GAMES);
        gamesIntent.putExtra(GamesFragment.GAME, view.getTag().toString());
        gamesIntent.putExtra(GamesFragment.FL_DRAW, flDrawResponse);
        gamesIntent.putExtra(GamesFragment.NY_DRAW, nyDrawResponse);
        gamesIntent.putExtra(GamesFragment.GA_DRAW, gaDrawResponse);
        gamesIntent.putExtra(GamesFragment.JACK_POT, jackPot);
        gamesIntent.putExtra(GamesFragment.PROGRESSIVE_JACK_POT, progressiveJackPot);
        gamesIntent.putExtra(GamesFragment.ROYAL5_JACK_POT, royalJackpot);
        startActivity(gamesIntent);
    }
}