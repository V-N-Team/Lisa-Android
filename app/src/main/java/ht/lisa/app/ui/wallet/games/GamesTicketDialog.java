package ht.lisa.app.ui.wallet.games;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ht.lisa.app.R;
import ht.lisa.app.util.TextUtil;

public class GamesTicketDialog extends DialogFragment {

    @BindView(R.id.games_ticket_grid)
    GridView gamesTicketGrid;
    @BindView(R.id.games_ticket_title)
    TextView gamesTicketTitle;
    @BindView(R.id.games_ticket_text)
    TextView gamesTicketText;

    private Unbinder unbinder;
    ArrayList<String> comboNumList;
    private String num;
    private boolean isCombo;
    private String gameName;
    private boolean isMaryaj;
    private OnConfirmButtonClickListener onConfirmButtonClickListener;

    public GamesTicketDialog(String num, String gameName, boolean isCombo, OnConfirmButtonClickListener onConfirmButtonClickListener) {
        this.num = num;
        this.isCombo = isCombo;
        this.gameName = gameName;
        this.onConfirmButtonClickListener = onConfirmButtonClickListener;
    }

    public GamesTicketDialog(String num, String gameName, boolean isCombo, boolean isMaryaj, OnConfirmButtonClickListener onConfirmButtonClickListener) {
        this.num = num;
        this.isMaryaj = isMaryaj;
        this.isCombo = isCombo;
        this.gameName = gameName;
        this.onConfirmButtonClickListener = onConfirmButtonClickListener;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_games_ticket, container);
        unbinder = ButterKnife.bind(this, view);
        gamesTicketTitle.setText(isCombo ? getString(R.string.combo_) : getString(R.string.games_subscriptions_));
        int step = num.length() % 2 == 0 ? 2 : 1;
        comboNumList = new ArrayList<>();
        if (isCombo) {
            if (isMaryaj) {
                comboNumList.addAll(TextUtil.getTwoPermutationsMaryajNew(TextUtil.getStringArray(num, step)[0], TextUtil.getStringArray(num, step)[1], TextUtil.getStringArray(num, step)[2]));
            } else {
                comboNumList.addAll(num.length() == 4 ? TextUtil.getTwoPermutationsMaryaj(TextUtil.getStringArray(num, step)[0], TextUtil.getStringArray(num, step)[1], TextUtil.getStringArray(num, step)[2]) : TextUtil.getThreePermutations(TextUtil.getStringArray(num, step)[0], TextUtil.getStringArray(num, step)[1], TextUtil.getStringArray(num, step)[2]));
            }
        } else {
            comboNumList.add(num);
            gamesTicketText.setText(TextUtil.capitalize(gameName));
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gamesTicketGrid.setNumColumns(comboNumList.size() == 1 ? 1 : 2);
        gamesTicketGrid.setAdapter(new GamesItemTicketAdapter(getContext(), comboNumList));
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null && getActivity() != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.games_ticket_confirm, R.id.games_ticket_close})
    void onGamesTicketConfirm(View view) {
        if (view.getId() == R.id.games_ticket_confirm && onConfirmButtonClickListener != null) {
            onConfirmButtonClickListener.onConfirmButtonClick();
            dismiss();
        } else if (getDialog() != null) onCancel(getDialog());

    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        dismiss();
        onConfirmButtonClickListener.onCancel();
    }

    public interface OnConfirmButtonClickListener {
        void onConfirmButtonClick();

        void onCancel();
    }
}
