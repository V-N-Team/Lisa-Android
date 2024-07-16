package ht.lisa.app.ui.wallet.reloadmoney;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;

import ht.lisa.app.ui.wallet.BaseWalletFragment;
import ht.lisa.app.util.PaymentsInfo;

public class ReloadMoneyWebViewFragment extends BaseWalletFragment {
    private WebView webView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        webView = new WebView(getContext());
        webView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        initPayment();
        return webView;
    }

    private void initPayment() {
        webView.loadData(PaymentsInfo.getPaymentHtml(5000), PaymentsInfo.MIME_TYPE, PaymentsInfo.ENCODING);
    }
}
