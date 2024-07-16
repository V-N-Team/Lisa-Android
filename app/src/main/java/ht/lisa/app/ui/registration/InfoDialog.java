package ht.lisa.app.ui.registration;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ht.lisa.app.R;

public class InfoDialog extends DialogFragment {

    public static final String INFO_DIALOG_TITLE = "infoDialogTitle";
    public static final String INFO_DIALOG_MESSAGE = "infoDialogMessage";

    @BindView(R.id.dialog_info_title)
    TextView dialogInfoTitle;
    @BindView(R.id.dialog_info_message)
    TextView dialogInfoMessage;

    private Unbinder unbinder;

    public static InfoDialog newInstance(String title, String message) {
        InfoDialog infoDialog = new InfoDialog();
        Bundle bundle = new Bundle();
        bundle.putString(INFO_DIALOG_TITLE, title);
        bundle.putString(INFO_DIALOG_MESSAGE, message);
        infoDialog.setArguments(bundle);
        return infoDialog;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_info, container);
        unbinder = ButterKnife.bind(this, view);
        setCancelable(false);
        if (getArguments() != null) {
            dialogInfoTitle.setText(getArguments().getString(INFO_DIALOG_TITLE) == null ? "" : getArguments().getString(INFO_DIALOG_TITLE));
            dialogInfoMessage.setText(getArguments().getString(INFO_DIALOG_MESSAGE) == null ? "" : getArguments().getString(INFO_DIALOG_MESSAGE));
        }
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            if (getActivity() == null) return;
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int displayWidth = (int) (displayMetrics.widthPixels * 0.9);
            if (dialog.getWindow() == null) return;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(displayWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setGravity(Gravity.CENTER);
        }
        return dialog;
    }


    @OnClick({R.id.dialog_info_close_button, R.id.dialog_info_ok_button})
    void onInfoDialogClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_info_close_button:
            case R.id.dialog_info_ok_button:
                if (getDialog() == null) return;
                getDialog().dismiss();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
