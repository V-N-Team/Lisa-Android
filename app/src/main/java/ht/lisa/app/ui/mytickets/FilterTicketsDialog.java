package ht.lisa.app.ui.mytickets;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ht.lisa.app.R;

public class FilterTicketsDialog extends DialogFragment {

    private static final String BOLOTO_KEY = "bolotoKey";
    private static final String BORLET_KEY = "borletKey";
    private static final String MARIAGE_KEY = "mariageKey";
    private static final String LOTTO3_KEY = "lotto3Key";
    private static final String LOTTO4_KEY = "lotto4Key";
    private static final String LOTTO5_KEY = "lotto5Key";
    private static final String LOTTO55G_KEY = "lotto55gKey";
    private static final String LOTTO5ROYAL_KEY = "lotto5RoyalKey";
    private static final String NOT_ACCEPTED_KEY = "notAcceptedKey";
    private static final String WIN_KEY = "winKey";
    private static final String PENDING_KEY = "pendingKey";
    private static final String CLOSED_KEY = "closedKey";

    @BindView(R.id.filter_by_boloto)
    CheckBox filterByBolotoChb;
    @BindView(R.id.filter_by_borlet)
    CheckBox filterByBorletChb;
    @BindView(R.id.filter_by_maryage)
    CheckBox filterByMariageChb;
    @BindView(R.id.filter_by_lotto3)
    CheckBox filterByLotto3Chb;
    @BindView(R.id.filter_by_lotto4)
    CheckBox filterByLotto4Chb;
    @BindView(R.id.filter_by_lotto5)
    CheckBox filterByLotto5Chb;
    @BindView(R.id.filter_by_lotto5_5g)
    CheckBox filterByLotto55gChb;
    @BindView(R.id.filter_by_lotto5_royal)
    CheckBox filterByLotto5RoyalChb;
    @BindView(R.id.filter_by_not_accepted)
    CheckBox filterByNotAcceptedChb;
    @BindView(R.id.filter_by_pending)
    CheckBox filterByPendingChb;
    @BindView(R.id.filter_by_win)
    CheckBox filterByWinChb;
    @BindView(R.id.filter_by_closed)
    CheckBox filterByClosedChb;


    private Unbinder unbinder;
    private OnFilterListener onFilterListener;


    public static FilterTicketsDialog newInstance(boolean filterByBoloto, boolean filterByBorlet, boolean filterByMariage,
                                                  boolean filterByLotto3, boolean filterByLotto4, boolean filterByLotto5,
                                                  boolean filterByLotto55g,  boolean filterByLotto5Royal,boolean filterByWin, boolean filterByNotAccepted,
                                                  boolean filterByClosed, boolean filterByPending) {
        FilterTicketsDialog dialog = new FilterTicketsDialog();
        Bundle args = new Bundle();
        args.putBoolean(BOLOTO_KEY, filterByBoloto);
        args.putBoolean(BORLET_KEY, filterByBorlet);
        args.putBoolean(MARIAGE_KEY, filterByMariage);
        args.putBoolean(LOTTO3_KEY, filterByLotto3);
        args.putBoolean(LOTTO4_KEY, filterByLotto4);
        args.putBoolean(LOTTO5_KEY, filterByLotto5);
        args.putBoolean(LOTTO55G_KEY, filterByLotto55g);
        args.putBoolean(LOTTO5ROYAL_KEY, filterByLotto5Royal);
        args.putBoolean(WIN_KEY, filterByWin);
        args.putBoolean(PENDING_KEY, filterByPending);
        args.putBoolean(NOT_ACCEPTED_KEY, filterByNotAccepted);
        args.putBoolean(CLOSED_KEY, filterByClosed);
        dialog.setArguments(args);
        return dialog;
    }

    public void setOnFilterListener(OnFilterListener onFilterListener) {
        this.onFilterListener = onFilterListener;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_filter_tickets, container);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            Bundle args = getArguments();
            initCheckboxes(args.getBoolean(BOLOTO_KEY),
                    args.getBoolean(BORLET_KEY),
                    args.getBoolean(MARIAGE_KEY),
                    args.getBoolean(LOTTO3_KEY),
                    args.getBoolean(LOTTO4_KEY),
                    args.getBoolean(LOTTO5_KEY),
                    args.getBoolean(LOTTO55G_KEY),
                    args.getBoolean(LOTTO5ROYAL_KEY),
                    args.getBoolean(WIN_KEY),
                    args.getBoolean(NOT_ACCEPTED_KEY),
                    args.getBoolean(CLOSED_KEY),
                    args.getBoolean(PENDING_KEY));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            dialog.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            dialog.getWindow().setLayout((int) (width * 0.9), ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.close)
    void onCloseClick(){
        dismiss();
    }

    @OnClick(R.id.filter_apply_filters)
    void onApplyFiltersClick() {
        onFilterListener.onApply(filterByBolotoChb.isChecked(), filterByBorletChb.isChecked(), filterByMariageChb.isChecked(),
                filterByLotto3Chb.isChecked(), filterByLotto4Chb.isChecked(), filterByLotto5Chb.isChecked(),
                filterByLotto55gChb.isChecked(), filterByLotto5RoyalChb.isChecked(), filterByWinChb.isChecked(), filterByNotAcceptedChb.isChecked(),
                filterByClosedChb.isChecked(), filterByPendingChb.isChecked());
        dismiss();
    }

    public void initCheckboxes(boolean filterByBoloto, boolean filterByBorlet, boolean filterByMariage,
                               boolean filterByLotto3, boolean filterByLotto4, boolean filterByLotto5,
                               boolean filterByLotto55g, boolean filterByLotto5Royal, boolean filterByWin, boolean filterByNotAccepted,
                               boolean filterByClosed, boolean filterByPending) {
        if (unbinder != null) {
            filterByBolotoChb.setChecked(filterByBoloto);
            filterByBorletChb.setChecked(filterByBorlet);
            filterByMariageChb.setChecked(filterByMariage);
            filterByLotto3Chb.setChecked(filterByLotto3);
            filterByLotto4Chb.setChecked(filterByLotto4);
            filterByLotto5Chb.setChecked(filterByLotto5);
            filterByLotto55gChb.setChecked(filterByLotto55g);
            filterByLotto5RoyalChb.setChecked(filterByLotto5Royal);
            filterByClosedChb.setChecked(filterByClosed);
            filterByPendingChb.setChecked(filterByPending);
            filterByWinChb.setChecked(filterByWin);
            filterByNotAcceptedChb.setChecked(filterByNotAccepted);
        }
    }

    public interface OnFilterListener {
        void onApply(boolean filterByBoloto, boolean filterByBorlet, boolean filterByMariage,
                     boolean filterByLotto3, boolean filterByLotto4, boolean filterByLotto5,
                     boolean filterByLotto55g, boolean filterByLotto5Royal, boolean filterByWin, boolean filterByNotAccepted,
                     boolean filterByClosed, boolean filterByPending);
    }

}
