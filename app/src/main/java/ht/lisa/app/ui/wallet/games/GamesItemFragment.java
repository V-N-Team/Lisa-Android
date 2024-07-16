package ht.lisa.app.ui.wallet.games;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import ht.lisa.app.R;
import ht.lisa.app.model.Draw;

public class GamesItemFragment extends Fragment {

    public static final String GAMES_ITEM_NAME = "gamesItemLayout";

    @Nullable
    @BindView(R.id.game_list_boloto_amount)
    TextView gameListBolotoAmount;
    @Nullable
    @BindView(R.id.game_list_lotto5jr_amount)
    TextView gameListLotto5jrAmount;
    @Nullable
    @BindView(R.id.game_list_royal5_amount)
    TextView gameListLotto5RoyalAmount;

    private int layout;
    private String name;
    private String jackPot;
    private String progressiveJackPot;
    private String royal5Jackpot;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(GAMES_ITEM_NAME);
            layout = Draw.getLayoutByName(name == null ? "" : name);
            jackPot = getArguments().getString(GamesFragment.JACK_POT);
            progressiveJackPot = getArguments().getString(GamesFragment.PROGRESSIVE_JACK_POT);
            royal5Jackpot = getArguments().getString(GamesFragment.ROYAL5_JACK_POT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layout, container, false);
        ButterKnife.bind(this, view);
        if (Draw.BOLOTO.equals(name.toLowerCase()) && gameListBolotoAmount != null) {
            gameListBolotoAmount.setText(jackPot);
        }

        if (Draw.LOTTO5ROYAL.equals(name.toLowerCase()) && gameListLotto5RoyalAmount != null) {
            gameListLotto5RoyalAmount.setText(royal5Jackpot);
        }

        if (gameListLotto5jrAmount != null && (Draw.LOTTO5P5.equals(name.toLowerCase()) || Draw.LOTTO5JR_NEW.equals(name.toLowerCase()) || Draw.LOTTO5JR.equals(name.toLowerCase()))) {
            gameListLotto5jrAmount.setText(progressiveJackPot);
        }
        return view;
    }
}
