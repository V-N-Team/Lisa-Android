package ht.lisa.app.ui.userprofile;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.OnClick;
import ht.lisa.app.R;
import ht.lisa.app.ui.wallet.BaseWalletFragment;
import ht.lisa.app.util.ActionUtil;

public class ContactUsFragment extends BaseWalletFragment {
    public static final String SCREEN_NAME = "ContactUsScreen";

    @BindView(R.id.contact_us_call_center)
    LinearLayout contactUsCallCenter;
    @BindView(R.id.contact_us_whats_app)
    LinearLayout contactUsWhatsApp;
    @BindView(R.id.contact_us_facebook)
    LinearLayout contactUsFacebook;
    @BindView(R.id.contact_us_email)
    LinearLayout contactUsEmail;
    @BindView(R.id.contact_us_browser)
    LinearLayout contactUsBrowser;
    @BindView(R.id.contact_us_call_center_value)
    TextView contactUsCallCenterValue;
    @BindView(R.id.contact_us_whats_app_value)
    TextView contactUsWhatsAppValue;
    @BindView(R.id.contact_us_facebook_value)
    TextView contactUsFacebookValue;
    @BindView(R.id.contact_us_email_value)
    TextView contactUsEmailValue;
    @BindView(R.id.contact_us_browser_value)
    TextView contactUsBrowserValue;


    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_us, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @OnClick({R.id.contact_us_call_center, R.id.contact_us_whats_app, R.id.contact_us_facebook, R.id.contact_us_email, R.id.contact_us_browser})
    void onContactUsClick(View view) {
        switch (view.getId()) {
            case R.id.contact_us_call_center:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contactUsCallCenterValue.getText().toString()));
                startActivity(intent);
                break;

            case R.id.contact_us_whats_app:
                PackageManager packageManager = getActivity().getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);
                try {
                    String url = "https://api.whatsapp.com/send?phone=" + contactUsWhatsAppValue.getText().toString();
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        startActivity(i);
                    } else {
                        final String appPackageName = "com.whatsapp";
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

            case R.id.contact_us_facebook:

                ActionUtil.openLisaFacebookMessenger(getContext());
                break;

            case R.id.contact_us_email:
                try {
                    Intent intent4 = new Intent(Intent.ACTION_SENDTO);
                    intent4.setData(Uri.parse("mailto:"));
                    intent4.putExtra(Intent.EXTRA_EMAIL, new String[]{contactUsEmailValue.getText().toString()});
                    startActivity(intent4);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There are no email client installed on your device.", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.contact_us_browser:
                try {
                    String url = contactUsBrowserValue.getText().toString();
                    if (!url.contains("http://www.")) {
                        url = "http://www." + url;
                    }
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                    browserIntent.setData(Uri.parse(url));
                    startActivity(browserIntent);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error to open page.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
