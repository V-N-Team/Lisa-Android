package ht.lisa.app.ui.wallet.games;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.matthewtamlin.sliding_intro_screen_library.indicators.DotIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ht.lisa.app.R;
import ht.lisa.app.model.Draw;

public class GameGuideDialog extends DialogFragment {

    @BindView(R.id.game_guide_button_next)
    Button gameGuideButtonNext;
    @BindView(R.id.game_guide_view_pager)
    ViewPager2 gameGuideViewPager;
    @BindView(R.id.game_guide_tabs)
    DotIndicator gameGuideTabs;

    private Unbinder unbinder;
    private String gameName;
    private GameGuideAdapter gameGuideAdapter;

    public GameGuideDialog(String gameName) {
        this.gameName = gameName;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_games_guide, container);
        setCancelable(false);
        unbinder = ButterKnife.bind(this, view);
        initPager();
        return view;
    }

    private void initPager() {
        gameGuideAdapter = new GameGuideAdapter(this, gameName);
        gameGuideViewPager.setAdapter(gameGuideAdapter);
        gameGuideTabs.setNumberOfItems(Draw.getGameGuidePageCount(gameName));
        gameGuideTabs.setSelectedItem(0, true);
        gameGuideAdapter.notifyDataSetChanged();
        gameGuideViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                gameGuideTabs.setSelectedItem(position, true);
                gameGuideButtonNext.setText(position == Draw.getGameGuidePageCount(gameName) - 1 ? getString(R.string.finish) : getString(R.string.next));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null && getActivity() != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



    @OnClick({R.id.game_guide_button_skip, R.id.game_guide_button_next})
    void onGamesTicketConfirm(View view) {
        switch (view.getId()) {
            case R.id.game_guide_button_skip:
                dismiss();
                break;

            case R.id.game_guide_button_next:
                if (getString(R.string.finish).equals(gameGuideButtonNext.getText().toString())) {
                    dismiss();
                } else {
                    gameGuideViewPager.setCurrentItem(gameGuideViewPager.getCurrentItem() + 1);
                }

                break;
        }
    }
}
