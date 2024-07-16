package ht.lisa.app.ui.registration.error;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ht.lisa.app.R;

public class OfflineLottoAdapter extends RecyclerView.Adapter<OfflineLottoAdapter.ViewHolder> {
    private ArrayList<OfflineLotto> offlineLottos;

    public OfflineLottoAdapter(ArrayList<OfflineLotto> offlineLottos) {
        this.offlineLottos = offlineLottos;
    }

    @NonNull
    @Override
    public OfflineLottoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offline_lotto, parent, false);
        return new OfflineLottoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfflineLottoAdapter.ViewHolder holder, int position) {
        OfflineLotto offlineLotto = offlineLottos.get(position);
        holder.lottoLogo.setImageBitmap(offlineLotto.getLogo());
        holder.lottoName.setText(offlineLotto.getName());
        holder.lottoNumber.setText(offlineLotto.getNumbers());

    }

    @Override
    public int getItemCount() {
        return offlineLottos.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lotto_name)
        TextView lottoName;
        @BindView(R.id.lotto_logo)
        ImageView lottoLogo;
        @BindView(R.id.number)
        TextView lottoNumber;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}