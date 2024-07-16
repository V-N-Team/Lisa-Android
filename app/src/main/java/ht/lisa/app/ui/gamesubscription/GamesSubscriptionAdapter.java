package ht.lisa.app.ui.gamesubscription;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ht.lisa.app.R;
import ht.lisa.app.model.Ticket;

public class GamesSubscriptionAdapter extends RecyclerView.Adapter<GamesSubscriptionAdapter.ViewHolder> {

    private static final String BOLOTO = "boloto";
    private static final String LOTTO3 = "lotto3";
    private static final String LOTTO5jr = "lotto5jr";
    private static final String ROYAL5 = "royal5";
    private HashMap<String, ArrayList<Ticket>> listHashMap;
    private GamesSubscriptionTicketAdapter.OnItemClickListener onItemClickListener;

    public GamesSubscriptionAdapter(HashMap<String, ArrayList<Ticket>> listHashMap, GamesSubscriptionTicketAdapter.OnItemClickListener onItemClickListener) {
        this.listHashMap = listHashMap;
        this.onItemClickListener = onItemClickListener;

    }

    public void setListHashMap(HashMap<String, ArrayList<Ticket>> listHashMap) {
        for (Map.Entry<String, ArrayList<Ticket>> arrayList : listHashMap.entrySet()) {
            if (arrayList.getValue() == null || arrayList.getValue().size() == 0) {
                listHashMap.remove(arrayList.getKey());
            }
        }
        this.listHashMap = listHashMap;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GamesSubscriptionAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_subscription, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArrayList<String> keys = new ArrayList<>(listHashMap.keySet());
        switch (keys.get(position)) {
            case BOLOTO:
                holder.gameSubscriptionName.setText(R.string.boloto);
                break;
            case LOTTO3:
                holder.gameSubscriptionName.setText(R.string.lotto3);
                break;
            case LOTTO5jr:
                holder.gameSubscriptionName.setText(R.string.lotto5jr);
                break;
            case ROYAL5:
                holder.gameSubscriptionName.setText(R.string.lotto5_royal);
                break;
        }
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.recyclerView.setAdapter(new GamesSubscriptionTicketAdapter(listHashMap.get(keys.get(position)), onItemClickListener, keys.get(position)));

    }

    @Override
    public int getItemCount() {
        return listHashMap.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.game_subscription_recycler)
        RecyclerView recyclerView;
        @BindView(R.id.game_subscription_card)
        CardView gameSubscriptionCard;
        @BindView(R.id.game_subscription_name)
        TextView gameSubscriptionName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
