package ht.lisa.app.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import ht.lisa.app.R;
import ht.lisa.app.model.Message;
import ht.lisa.app.ui.wallet.BaseWalletFragment;
import ht.lisa.app.ui.wallet.MainWalletFragment;
import ht.lisa.app.util.SharedPreferencesUtil;

public class WebViewFragment extends BaseWalletFragment {

    public static final String MESSAGE = "message";
    private static final String LISA_HT = "lisa.ht";
    @BindView(R.id.web_view)
    WebView webView;
    private Message message;

    public static WebViewFragment newInstance(Message message) {
        WebViewFragment webViewFragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MESSAGE, message);
        webViewFragment.setArguments(bundle);
        return webViewFragment;
    }

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web_view, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null)
            webView.restoreState(savedInstanceState);
        else {
            if (getArguments() != null) {
                message = (Message) getArguments().getSerializable(MESSAGE);
                if (message != null) {
                    initPayment(message.getUrl());
                }
            }
            webView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    if (url.contains(LISA_HT)) {
                        SharedPreferencesUtil.setLastMsgId(message.getMsgId());
                        showNextFragment(BaseWalletFragment.newInstance(MainWalletFragment.class, null));
                    }
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        webView.saveState(bundle);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initPayment(String webViewUrl) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(webViewUrl);
    }
}
