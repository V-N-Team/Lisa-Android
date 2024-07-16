package ht.lisa.app.ui.userprofile;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ht.lisa.app.R;
import ht.lisa.app.model.Avatar;
import ht.lisa.app.util.DateTimeUtil;
import ht.lisa.app.util.RxUtil;

public class ChooseAvatarDialog extends DialogFragment {

    @BindView(R.id.choose_avatar_recycler)
    RecyclerView chooseAvatarRecycler;
    private ArrayList<Avatar> avatars;
    private Unbinder unbinder;
    private AvatarsAdapter avatarsAdapter;
    private OnChooseAvatarListener onChooseAvatarListener;
    private Avatar chosenAvatar;

    public ChooseAvatarDialog(ArrayList<Avatar> avatars) {
        this.avatars = avatars;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_choose_avatar, container);
        unbinder = ButterKnife.bind(this, v);
        avatarsAdapter = new AvatarsAdapter(this::setChosenAvatar, avatars);
        chooseAvatarRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
        chooseAvatarRecycler.setAdapter(avatarsAdapter);
        setCancelable(false);
        return v;
    }

    private void setChosenAvatar(int position) {
        chosenAvatar = avatars.get(position);
        for (int i = 0; i < avatars.size(); i++) {
            Avatar a = avatars.get(i);
            a.setAvatarChecked(position == i);
            avatars.set(i, a);
        }
        avatarsAdapter.setAvatars(avatars);
        if (chosenAvatar != null) {
            onChooseAvatarListener.onAvatarChoose(chosenAvatar.getId());
        }
        RxUtil.delayedConsumer(DateTimeUtil.SECOND / 4, aLong -> dismiss());
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int displayWidth = (int) (displayMetrics.widthPixels * 0.9);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(displayWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setGravity(Gravity.CENTER);
        }
        return dialog;
    }

    public void setOnChooseAvatarListener(OnChooseAvatarListener onChooseAvatarListener) {
        this.onChooseAvatarListener = onChooseAvatarListener;
    }

    @OnClick(R.id.choose_avatar_close)
    void onChooseAvatarClick(View view) {
        if (getDialog() == null) return;
        getDialog().dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface OnChooseAvatarListener {
        void onAvatarChoose(int id);
    }
}
