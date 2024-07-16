package ht.lisa.app.ui.base;

import android.os.Build;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import ht.lisa.app.ui.registration.ResetPinCodeDialog;
import ht.lisa.app.util.DateTimeUtil;
import ht.lisa.app.util.OnDismissDialogInterface;
import ht.lisa.app.util.SharedPreferencesUtil;

public class BaseFragment extends Fragment implements OnDismissDialogInterface {

    private static final int TIME_TO_RELOGIN = 300000;
    ProgressBar progressBar;
    private CountDownTimer countDownTimer;

    protected void addProgressBar(View view) {
        ConstraintLayout rootLayout;
        if (view instanceof ConstraintLayout) {
            rootLayout = (ConstraintLayout) view;
        } else {
            rootLayout = (ConstraintLayout) ((ViewGroup) view).getChildAt(0);
        }
        progressBar = new ProgressBar(getActivity(), null);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomToBottom = ConstraintSet.PARENT_ID;
        params.endToEnd = ConstraintSet.PARENT_ID;
        params.startToStart = ConstraintSet.PARENT_ID;
        params.topToTop = ConstraintSet.PARENT_ID;
        progressBar.setLayoutParams(params);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setElevation(10);
        }
        progressBar.setVisibility(View.GONE);
        rootLayout.addView(progressBar);
    }

    public void showProgress() {
        if (progressBar == null) return;
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        if (progressBar == null) return;
        progressBar.setVisibility(View.GONE);
    }

    public void cancelCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void initCountdown() {
        cancelCountdown();
        countDownTimer = new CountDownTimer(TIME_TO_RELOGIN, DateTimeUtil.SECOND) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                ResetPinCodeDialog dialog = new ResetPinCodeDialog();
                dialog.setOnDismissDialogInterface(BaseFragment.this);
                dialog.setCancelable(false);
                dialog.show(getFragmentManager(), ResetPinCodeDialog.class.getSimpleName());
            }
        }.start();
    }


    @Override
    public void onStop() {
        super.onStop();
        cancelCountdown();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (SharedPreferencesUtil.isAuthorized()) {
            initCountdown();
        }
    }

    @Override
    public void onDismissed() {
        initCountdown();
    }
}
