package ht.lisa.app.ui.wallet.games;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import ht.lisa.app.R;
import ht.lisa.app.model.Phone;
import ht.lisa.app.model.Ticket;
import ht.lisa.app.model.response.TicketsResponse;
import ht.lisa.app.ui.wallet.BaseWalletFragment;
import ht.lisa.app.util.SharedPreferencesUtil;

public class RecentNumbersFragment extends BaseWalletFragment {
    public static final String SCREEN_NAME = "RecentNumbersScreen";

    @BindView(R.id.tickets_layout)
    LinearLayout ticketsLayout;
    private HashMap<String, ArrayList<Ticket>> ticketsHashMap;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recent_numbers, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ticketsHashMap = new HashMap<>();
        walletPresenter.getRecentNumbers(new Phone(SharedPreferencesUtil.getPhone()));
    }

    @Override
    public void getData(Object object) {
        if (object instanceof TicketsResponse) {
            TicketsResponse response = (TicketsResponse) object;
            ArrayList<Ticket> tickets = response.getDataset();
            for (Ticket ticket : tickets) {
                addTicket(ticket.getName(), ticket);
            }

            ArrayList<String> keys = new ArrayList<>(ticketsHashMap.keySet());
            for (String key : keys) {
                RecentNumbersAdapter adapter = new RecentNumbersAdapter(ticketsHashMap.get(key), getContext());
                RecyclerView gamesTicketRecyclerView = new RecyclerView(getContext());
                gamesTicketRecyclerView.setAdapter(adapter);
                gamesTicketRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                gamesTicketRecyclerView.setHasFixedSize(true);
                ticketsLayout.addView(gamesTicketRecyclerView);
            }
        }
    }

    private synchronized void addTicket(String mapKey, Ticket ticket) {
        ArrayList<Ticket> itemsList = ticketsHashMap.get(mapKey);
        if (itemsList == null) {
            itemsList = new ArrayList<>();
            itemsList.add(ticket);
            ticketsHashMap.put(mapKey, itemsList);
        } else {
            if (!itemsList.contains(ticket)) itemsList.add(ticket);
        }
    }
}
