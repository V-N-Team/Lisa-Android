package ht.lisa.app.ui.gamesubscription;

import android.content.Context;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import ht.lisa.app.R;
import ht.lisa.app.model.Draw;
import ht.lisa.app.model.Phone;
import ht.lisa.app.model.Ticket;
import ht.lisa.app.model.response.BaseResponse;
import ht.lisa.app.model.response.BolotoSubscribeListResponse;
import ht.lisa.app.model.response.Lotto3SubscribeListResponse;
import ht.lisa.app.model.response.Lotto5jrSubscribeListResponse;
import ht.lisa.app.model.response.Royal5SubscribeListResponse;
import ht.lisa.app.ui.main.MainActivity;
import ht.lisa.app.ui.mytickets.MyTicketsFragment;
import ht.lisa.app.ui.wallet.BaseWalletFragment;
import ht.lisa.app.ui.wallet.gamelist.GameListFragment;
import ht.lisa.app.util.FragmentUtil;
import ht.lisa.app.util.OnFragmentVisibleListener;
import ht.lisa.app.util.SharedPreferencesUtil;
import ht.lisa.app.util.TextUtil;

import static ht.lisa.app.model.Draw.LOTTO5P5;

public class GamesSubscriptionFragment extends BaseWalletFragment implements GamesSubscriptionTicketAdapter.OnItemClickListener {
    public static final String SCREEN_NAME = "GamesSubscriptionsScreen";

    private static final String BOLOTO = "boloto";
    private static final String LOTTO3 = "lotto3";
    private static final String LOTTO5jr = "lotto5jr";
    private static final String ROYAL5 = "royal5";

    @BindView(R.id.games_subscription_recycler)
    RecyclerView gamesSubscriptionRecycler;
    @BindView(R.id.no_subscriptions_layout)
    View noSubscriptionsLayout;

    private GamesSubscriptionAdapter adapter;
    private OnFragmentVisibleListener onFragmentVisibleListener;
    private HashMap<String, ArrayList<Ticket>> subscribeListHashMap;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_games_subscription, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSubscribeList();
        walletPresenter.getBolotoSubscribeList(new Phone(SharedPreferencesUtil.getPhone()));
        subscribeListHashMap = new HashMap<>();
    }

    @Override
    public void getData(Object object) {
        if (object instanceof BolotoSubscribeListResponse) {
            BolotoSubscribeListResponse response = (BolotoSubscribeListResponse) object;
            subscribeListHashMap.put(BOLOTO, response.getDataset());
            walletPresenter.getLotto3SubscribeList(new Phone(SharedPreferencesUtil.getPhone()));
        } else if (object instanceof Lotto3SubscribeListResponse) {
            Lotto3SubscribeListResponse response = (Lotto3SubscribeListResponse) object;
            subscribeListHashMap.put(LOTTO3, response.getDataset());
            walletPresenter.getLotto5jrSubscribeList(new Phone(SharedPreferencesUtil.getPhone()));
        } else if (object instanceof Lotto5jrSubscribeListResponse) {
            Lotto5jrSubscribeListResponse response = (Lotto5jrSubscribeListResponse) object;
            subscribeListHashMap.put(LOTTO5jr, response.getDataset());
            walletPresenter.getRoyal5SubscribeList(new Phone(SharedPreferencesUtil.getPhone()));
        }else if (object instanceof Royal5SubscribeListResponse) {
            Royal5SubscribeListResponse response = (Royal5SubscribeListResponse) object;
            subscribeListHashMap.put(ROYAL5, response.getDataset());
        } else if (object instanceof BaseResponse) {
            BaseResponse response = (BaseResponse) object;
            Log.d("BASERESPTAG", String.valueOf(response.getState()));
        }
        adapter.setListHashMap(subscribeListHashMap);
        if (subscribeListHashMap != null && subscribeListHashMap.size() != 0) {
            noSubscriptionsLayout.setVisibility(View.GONE);
        }
    }

    private void initSubscribeList() {
        adapter = new GamesSubscriptionAdapter(new HashMap<>(), this);
        gamesSubscriptionRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        gamesSubscriptionRecycler.setAdapter(adapter);
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
            onFragmentVisibleListener.onFragmentShowListener(MyTicketsFragment.class.getSimpleName(), true);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (onFragmentVisibleListener != null) {
            onFragmentVisibleListener.onFragmentShowListener(MyTicketsFragment.class.getSimpleName(), false);
            onFragmentVisibleListener = null;
        }
    }

    @OnClick(R.id.select_game)
    void onSelectGameClick() {
        FragmentUtil.replaceFragment(getFragmentManager(), new GameListFragment(), true);
    }

    @Override
    public void onItemClick(Ticket ticket) {
        switch (ticket.getName()) {
            case Draw.BOLOTO:
                walletPresenter.unsubscribeBoloto(new Phone(SharedPreferencesUtil.getPhone()), ticket.getNum().replaceAll("-", ""), ticket.getRegion());
                break;
            case Draw.LOTTO3:
                walletPresenter.unsubscribeLotto3(new Phone(SharedPreferencesUtil.getPhone()), ticket.getNum().replaceAll("-", ""), ticket.getRegion());
                break;
            case Draw.LOTTO5JR:
            case Draw.LOTTO5JR_NEW:
            case LOTTO5P5:
                walletPresenter.unsubscribeLotto5jr(new Phone(SharedPreferencesUtil.getPhone()), ticket.getNum().replaceAll("-", ""), ticket.getRegion());
                break;
            case Draw.ROYAL5:
                walletPresenter.unsubscribeRoyal5(new Phone(SharedPreferencesUtil.getPhone()), ticket.getNum().replaceAll("-", ""),
                        ticket.getRegion());
                break;
        }

    }
}
