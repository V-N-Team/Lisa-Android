package ht.lisa.app.ui.wallet.cashout;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import butterknife.OnClick;
import ht.lisa.app.R;
import ht.lisa.app.ui.main.MainActivity;
import ht.lisa.app.ui.userprofile.UserProfileMainFragment;
import ht.lisa.app.ui.wallet.BaseWalletFragment;
import ht.lisa.app.util.OnFragmentVisibleListener;

public class CashOutChooseFragment extends BaseWalletFragment {
    public static final String SCREEN_NAME = "ChooseCashOutOptionScreen";

    OnFragmentVisibleListener onFragmentVisibleListener;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cash_out_choose, parent, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            onFragmentVisibleListener = (OnFragmentVisibleListener) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (onFragmentVisibleListener != null) {
            onFragmentVisibleListener.onFragmentShowListener(CashOutChooseFragment.class.getSimpleName(), true);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (onFragmentVisibleListener != null) {
            onFragmentVisibleListener.onFragmentShowListener(CashOutChooseFragment.class.getSimpleName(), false);
            onFragmentVisibleListener = null;
        }
    }


    @OnClick({R.id.cash_out_choose_mon_cash, R.id.cash_out_choose_sogexpress})
    void onChooseCashOtButtonClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.cash_out_choose_mon_cash:
                bundle.putBoolean(CashOutBalanceFragment.IS_MON_CASH, true);
                break;
            case R.id.cash_out_choose_sogexpress:
                bundle.putBoolean(CashOutBalanceFragment.IS_MON_CASH, false);
                break;
        }
        showNextFragment(BaseWalletFragment.newInstance(CashOutBalanceFragment.class, bundle));
    }
}
