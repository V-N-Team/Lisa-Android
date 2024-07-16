package ht.lisa.app.ui.registration;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ht.lisa.app.R;
import ht.lisa.app.util.ActionUtil;

public class ForgotPinDialog extends DialogFragment {

    @BindView(R.id.forgot_pin_call_center_value)
    TextView forgotPinCallCenterValue;
    @BindView(R.id.forgot_pin_whats_app_value)
    TextView forgotPinWhatsAppValue;
    @BindView(R.id.forgot_pin_facebook_value)
    TextView forgotPinFacebookValue;
    @BindView(R.id.forgot_pin_email_value)
    TextView forgotPinEmailValue;
    private Unbinder unbinder;

    public static ForgotPinDialog newInstance() {
        return new ForgotPinDialog();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_forgot_pin, container);
        unbinder = ButterKnife.bind(this, v);
        setCancelable(false);
        return v;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    @OnClick({R.id.forgot_pin_call_center, R.id.forgot_pin_whats_app, R.id.forgot_pin_facebook, R.id.forgot_pin_email})
    void onForgotPinClick(View view) {
        switch (view.getId()) {
            case R.id.forgot_pin_call_center:
                ActionUtil.openPhoneNumber(getContext(), forgotPinCallCenterValue.getText().toString());
                break;
            case R.id.forgot_pin_whats_app:
                PackageManager packageManager = getActivity().getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);
                try {
                    String url = "https://api.whatsapp.com/send?phone=" + forgotPinWhatsAppValue.getText().toString();
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        startActivity(i);
                    } else {
                        final String appPackageName = "com.whatsapp"; // getPackageName() from Context or SlideshowActivity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.forgot_pin_facebook:
                ActionUtil.openLisaFacebookMessenger(getContext());
                break;
            case R.id.forgot_pin_email:
                try {
                    Intent intent4 = new Intent(Intent.ACTION_SENDTO);
                    intent4.setData(Uri.parse("mailto:"));
                    intent4.putExtra(Intent.EXTRA_EMAIL, new String[]{forgotPinEmailValue.getText().toString()});
                    startActivity(intent4);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There are no email client installed on your device.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @OnClick(R.id.forgot_pin_close)
    void onForgotPinClose() {
        if (getDialog() == null) return;
        getDialog().dismiss();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
