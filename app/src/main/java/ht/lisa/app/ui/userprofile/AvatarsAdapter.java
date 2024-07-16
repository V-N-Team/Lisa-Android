package ht.lisa.app.ui.userprofile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ht.lisa.app.R;
import ht.lisa.app.model.Avatar;

public class AvatarsAdapter extends RecyclerView.Adapter<AvatarsAdapter.ViewHolder> {

    private OnItemClickListener onItemClickListener;
    private List<Avatar> avatars;

    public AvatarsAdapter(OnItemClickListener onItemClickListener, List<Avatar> avatars) {
        this.onItemClickListener = onItemClickListener;
        this.avatars = avatars;
    }

    public void setAvatars(List<Avatar> avatars) {
        this.avatars = avatars;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_avatar, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.itemView.setOnClickListener(view -> onItemClickListener.onItemClick(position));
        viewHolder.checked.setVisibility(avatars.get(position).isChecked() ? View.VISIBLE : View.GONE);
        Glide.with(viewHolder.itemView.getContext())
                .load(avatars.get(position).getUrl())
                .circleCrop()
                .apply(RequestOptions.circleCropTransform())
                .error(R.drawable.profile_logo_bg)
                .into(viewHolder.avatar);
    }

    @Override
    public int getItemCount() {
        return avatars != null ? avatars.size() : 0;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.avatar)
        ImageView avatar;
        @BindView(R.id.checked)
        ImageView checked;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
