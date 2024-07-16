package ht.lisa.app.ui.wallet.games;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import ht.lisa.app.LisaApp;
import ht.lisa.app.R;
import ht.lisa.app.model.Draw;
import ht.lisa.app.util.SharedPreferencesUtil;

public class GameGuideItemFragment extends Fragment {

    public static final String GAME_GUIDE_POSITION = "gameGuidePosition";
    public static final String GAME_GUIDE_NAME = "gameGuideName";

    @BindView(R.id.game_layout)
    View gameLayout;
    @BindView(R.id.game_guide_main_game)
    ImageView gameGuideMainGame;
    @BindView(R.id.numbers_qp_image)
    ImageView numbersQpImage;
    @BindView(R.id.numbers_qp_top_title)
    TextView numbersQpTopTitle;
    @BindView(R.id.numbers_qp_top_description)
    TextView numbersQpTopDescription;
    @BindView(R.id.numbers_qp_bot_title)
    TextView numbersQpBotTitle;
    @BindView(R.id.numbers_qp_bot_description)
    TextView numbersQpBotDescription;
    @BindView(R.id.numbers_qp_layout)
    View numbersQpLayout;
    @BindView(R.id.numbers_qp_top_layout)
    View numbersQpTopLayout;
    @BindView(R.id.numbers_qp_bot_layout)
    View numbersQpBotLayout;
    @BindView(R.id.ticket_image)
    ImageView ticketImage;
    @BindView(R.id.subscribe_layout)
    View subscribeLayout;
    @BindView(R.id.subscribe_image)
    ImageView subscribeImage;
    @BindView(R.id.game_guide_games_description)
    TextView gamesDescription;
    @BindView(R.id.draw_type_layout)
    View drawTypeLayout;
    @BindView(R.id.draw_type_image)
    ImageView drawTypeImage;

    private int position;
    private String gameName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(GAME_GUIDE_POSITION);
            gameName = getArguments().getString(GAME_GUIDE_NAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_guide_item, container, false);
        ButterKnife.bind(this, view);
        if (position == 0) {
            saveGameGuideSeen();
        }
        initGameGuideObjects();
        return view;
    }

    private void saveGameGuideSeen() {
        Set<String> set = SharedPreferencesUtil.getGameGuideSet() == null ? new HashSet<>() : SharedPreferencesUtil.getGameGuideSet();
        set.add(gameName);
        SharedPreferencesUtil.setGameGuideSet(set);
    }

    private void initGameGuideObjects() {
        if (getContext() == null) return;
        //Game
        gameLayout.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        drawTypeLayout.setVisibility(position == 3 ? View.VISIBLE : View.GONE);
        drawTypeImage.setImageDrawable(AppCompatResources.getDrawable(getContext(), Draw.getDrawTypeImage(getActivity())));

        gamesDescription.setText(String.format("%s\n%s", getString(Draw.getGamesDescription(gameName)), getString(R.string.game_description)));
        gameGuideMainGame.setImageResource(Draw.getGameBgByName(gameName));

        ticketImage.setVisibility(position == 4 ? View.VISIBLE : View.GONE);

        if (position == 5 && (gameName.toLowerCase().equals(Draw.LOTTO3) || gameName.toLowerCase().equals(Draw.LOTTO5JR) || gameName.toLowerCase().equals(Draw.LOTTO5P5) || gameName.toLowerCase().equals(Draw.BOLOTO) || gameName.toLowerCase().equals(Draw.ROYAL5))) {
            subscribeImage.setImageDrawable(AppCompatResources.getDrawable(getContext(), getSubscribeTicketImage()));
            subscribeLayout.setVisibility(View.VISIBLE);
        } else {
            subscribeLayout.setVisibility(View.GONE);
        }


        //Nums & QP
        numbersQpLayout.setVisibility(position == 1 || position == 2 ? View.VISIBLE : View.GONE);
        numbersQpImage.setImageResource(Draw.getTicketHeaderByName(gameName, position, getActivity()));
        if (position == 1) {
            numbersQpTopLayout.setVisibility(View.VISIBLE);
            numbersQpTopTitle.setText(R.string.quick_pick);
            numbersQpTopDescription.setText(R.string.qp_description);
            numbersQpBotDescription.setText(Draw.getNumsAndBetDescription(gameName));
            numbersQpBotTitle.setText(gameName.toLowerCase().equals(Draw.LOTTO5JR) || gameName.toLowerCase().equals(Draw.BOLOTO)  || gameName.toLowerCase().equals(Draw.ROYAL5) ? getText(R.string.enter_numbers) : getText(R.string.enter_numbers_and_bet));
        } else if (position == 2) {
            numbersQpTopLayout.setVisibility(gameName.equals(Draw.LOTTO5JR) || gameName.toLowerCase().equals(Draw.LOTTO5P5) || gameName.equals(Draw.BOLET) || gameName.equals(Draw.ROYAL5) ? View.INVISIBLE : View.VISIBLE);
            if (gameName.equals(Draw.BOLOTO)) {
                numbersQpTopTitle.setText(R.string.combo6);
                numbersQpTopDescription.setText(getString(R.string.combo6_description));
            } else if (gameName.equals(Draw.MARIAGE) || gameName.equals(Draw.LOTTO3)) {
                numbersQpTopTitle.setText(R.string.combo_);
                numbersQpTopDescription.setText(getString(R.string.combo_description));
            } else {
                numbersQpTopTitle.setText(R.string.click_add);
                numbersQpTopDescription.setText(getString(R.string.add_description));
            }
            numbersQpTopLayout.setBackgroundResource(gameName.equals(Draw.LOTTO5) || gameName.equals(Draw.LOTTO4) ? R.drawable.bubble_right_top : R.drawable.bubble_top_right);
            numbersQpBotTitle.setText(gameName.equals(Draw.LOTTO5) || gameName.equals(Draw.LOTTO4) ? getText(R.string.choose_options) : getText(R.string.click_add));
            numbersQpBotDescription.setText(gameName.equals(Draw.LOTTO5) || gameName.equals(Draw.LOTTO4) ? getText(R.string.draw_option_description) : getText(R.string.add_description));
            numbersQpBotLayout.setBackgroundResource(gameName.equals(Draw.LOTTO5) || gameName.equals(Draw.LOTTO4) ? R.drawable.bubble_bottom_middle : R.drawable.bubble_bottom_right_end);
        } else if (position == 4) {
            ticketImage.setImageResource(getTicketImage());
        }
    }


    private int getSubscribeTicketImage() {
        int image = 0;
        switch (gameName.toLowerCase()) {
            case Draw.BOLOTO:
                image = R.drawable.boloto_subscribe;
                break;
            case Draw.LOTTO3:
                image = R.drawable.lotto3_subscribe;
                break;
            case Draw.LOTTO5JR:
            case Draw.LOTTO5P5:
            case Draw.ROYAL5:
                image = R.drawable.lotto5jr_subscribe;
                break;
        }
        return image;
    }

    private int getTicketImage() {
        int ticketImage;
        boolean isFr = LisaApp.getInstance().isFrench(getActivity());
        boolean isKr = LisaApp.getInstance().isKreyol(getActivity());
        switch (gameName.toLowerCase()) {
            case Draw.BOLOTO:
                ticketImage = isFr ? R.drawable.ticket_boloto_fr : isKr ? R.drawable.ticket_boloto_kr : R.drawable.ticket_boloto;
                break;

            case Draw.LOTTO5JR:
            case Draw.LOTTO5ROYAL:
            case Draw.LOTTO5P5:
                ticketImage = isFr ? R.drawable.ticket_lotto5jr_fr : isKr ? R.drawable.ticket_lotto5jr_kr : R.drawable.ticket_lotto5jr;
                break;

            case Draw.BOLET:
                ticketImage = isFr ? R.drawable.ticket_bolet_fr : isKr ? R.drawable.ticket_bolet_kr : R.drawable.ticket_bolet;
                break;

            case Draw.LOTTO3:
                ticketImage = isFr ? R.drawable.ticket_lotto3_fr : isKr ? R.drawable.ticket_lotto3_kr : R.drawable.ticket_lotto3;
                break;

            case Draw.LOTTO5:
                ticketImage = isFr ? R.drawable.ticket_lotto5_fr : isKr ? R.drawable.ticket_lotto5_kr : R.drawable.ticket_lotto5;
                break;

            case Draw.LOTTO4:
                ticketImage = isFr ? R.drawable.ticket_lotto4_fr : isKr ? R.drawable.ticket_lotto4_kr : R.drawable.ticket_lotto4;
                break;
            case Draw.MARIAGE:
                ticketImage = isFr ? R.drawable.ticket_maryaj_fr : isKr ? R.drawable.ticket_maryaj_kr : R.drawable.ticket_maryaj;
                break;

            default:
                ticketImage = 0;
        }
        return ticketImage;
    }

}
