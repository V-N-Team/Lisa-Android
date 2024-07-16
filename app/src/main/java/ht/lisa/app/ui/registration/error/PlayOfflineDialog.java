package ht.lisa.app.ui.registration.error;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ht.lisa.app.R;
import ht.lisa.app.util.ActionUtil;

public class PlayOfflineDialog extends DialogFragment {

    private Unbinder unbinder;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_play_offline, container);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        }
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            if (dialog.getWindow() == null) return;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @OnClick({R.id.offline_boloto, R.id.offline_bolet, R.id.offline_maryaj, R.id.offline_lotto3, R.id.offline_lotto4, R.id.offline_lotto5, R.id.offline_lotto5jr})
    void onOfflineLottoClick(View view){
        switch (view.getId()){
            case R.id.offline_boloto:
                ActionUtil.openPhoneNumber(getContext(), getString(R.string.boloto_offline));
                break;
            case R.id.offline_bolet:
                ActionUtil.openPhoneNumber(getContext(), getString(R.string.bolet_offline));
                break;
            case R.id.offline_maryaj:
                ActionUtil.openPhoneNumber(getContext(), getString(R.string.maryaj_offline));
                break;
            case R.id.offline_lotto3:
                ActionUtil.openPhoneNumber(getContext(), getString(R.string.lotto3_offline));
                break;
            case R.id.offline_lotto4:
                ActionUtil.openPhoneNumber(getContext(), getString(R.string.lotto4_offline));
                break;
            case R.id.offline_lotto5:
                ActionUtil.openPhoneNumber(getContext(), getString(R.string.lotto5_offline));
                break;
            case R.id.offline_lotto5jr:
                ActionUtil.openPhoneNumber(getContext(), getString(R.string.lotto5jr_offline));
                break;
        }

    }

}
