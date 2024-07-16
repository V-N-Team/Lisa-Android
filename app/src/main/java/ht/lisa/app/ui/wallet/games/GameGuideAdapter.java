package ht.lisa.app.ui.wallet.games;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ht.lisa.app.model.Draw;

public class GameGuideAdapter extends FragmentStateAdapter {

    private String gameName;

    public GameGuideAdapter(Fragment fragment, String gameName) {
        super(fragment);
        this.gameName = gameName;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(GameGuideItemFragment.GAME_GUIDE_POSITION, position);
        bundle.putString(GameGuideItemFragment.GAME_GUIDE_NAME, gameName);
        GameGuideItemFragment gameGuideItemFragment = new GameGuideItemFragment();
        gameGuideItemFragment.setArguments(bundle);
        return gameGuideItemFragment;
    }

    @Override
    public int getItemCount() {
        return Draw.getGameGuidePageCount(gameName);
    }
}
