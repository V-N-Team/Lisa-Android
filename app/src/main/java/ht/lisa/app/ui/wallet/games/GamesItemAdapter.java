package ht.lisa.app.ui.wallet.games;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class GamesItemAdapter extends FragmentStatePagerAdapter {

    public static int LOOPS_COUNT = 1000;
    private ArrayList<String> drawNames;
    private String jackPot;
    private String progressiveJackPot;
    private String royal5JackPot;

    public GamesItemAdapter(FragmentManager fragmentManager, ArrayList<String> drawNames, String jackPot, String progressiveJackPot, String royal5Jackpot) {
        super(fragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.drawNames = drawNames;
        this.jackPot = jackPot;
        this.royal5JackPot = royal5Jackpot;
        this.progressiveJackPot = progressiveJackPot;
    }

    public String getGameName(int position) {
        return drawNames.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (drawNames != null && drawNames.size() > 0) {
            position = position % drawNames.size(); // use modulo for infinite cycling
            Bundle bundle = new Bundle();
            bundle.putString(GamesItemFragment.GAMES_ITEM_NAME, drawNames.get(position));
            bundle.putString(GamesFragment.JACK_POT, jackPot);
            bundle.putString(GamesFragment.PROGRESSIVE_JACK_POT, progressiveJackPot);
            bundle.putString(GamesFragment.ROYAL5_JACK_POT, royal5JackPot);
            GamesItemFragment gamesItemFragment = new GamesItemFragment();
            gamesItemFragment.setArguments(bundle);
            return gamesItemFragment;
        } else {
            return new GamesItemFragment();
        }

    }

    @Override
    public int getCount() {
        if (drawNames != null && drawNames.size() > 0) {
            return drawNames.size() * LOOPS_COUNT; // simulate infinite by big number of products
        } else {
            return 1;
        }
    }
}
