package ht.lisa.app.ui.wallet.games;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ht.lisa.app.R;

public class MaryajComboDialog extends DialogFragment {


    @BindView(R.id.maryaj_combo_error)
    TextView maryajComboError;
    @BindView(R.id.third_combination_number)
    EditText thirdCombinationNumber;
    private Unbinder unbinder;
    private OnComboButtonsClickListener buttonsClickListener;
    private String num;

    public MaryajComboDialog(String num, OnComboButtonsClickListener buttonsClickListener) {
        this.buttonsClickListener = buttonsClickListener;
        this.num = num;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_maryaj_combo, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @OnClick(R.id.combo_confirm)
    void onConfirmClick() {
        if (thirdCombinationNumber.length() != 2) {
            maryajComboError.setVisibility(View.VISIBLE);
            return;
        }
        buttonsClickListener.onConfirmClick(num + thirdCombinationNumber.getText().toString());
        dismiss();
    }

    @OnClick(R.id.combo_close)
    void onCloseClick() {
        buttonsClickListener.onCloseClick();
        dismiss();
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            dialog.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;
            dialog.getWindow().setLayout((int) (width * 0.9), (int) (height * 0.4));
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public interface OnComboButtonsClickListener {
        void onCloseClick();

        void onConfirmClick(String num);
    }
}
