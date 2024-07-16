package ht.lisa.app.ui.base;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.DialogFragment;

public class BaseDialogFragment extends DialogFragment {

    ProgressBar progressBar;

    protected void addProgressBar(View view) {
        ConstraintLayout rootLayout = (ConstraintLayout) view;
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
}
