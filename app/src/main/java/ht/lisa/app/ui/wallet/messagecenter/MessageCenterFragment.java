package ht.lisa.app.ui.wallet.messagecenter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import butterknife.BindView;
import ht.lisa.app.LisaApp;
import ht.lisa.app.R;
import ht.lisa.app.model.Message;
import ht.lisa.app.model.Phone;
import ht.lisa.app.model.response.MessageResponse;
import ht.lisa.app.ui.main.MainActivity;
import ht.lisa.app.ui.wallet.BaseWalletFragment;
import ht.lisa.app.util.OnFragmentVisibleListener;
import ht.lisa.app.util.SharedPreferencesUtil;

public class MessageCenterFragment extends BaseWalletFragment implements MessageCenterAdapter.OnMessageClickListener {
    public static final String SCREEN_NAME = "NotificationsScreen";

    @BindView(R.id.message_center_recyclerview)
    RecyclerView messageCenterRecyclerview;
    @BindView(R.id.no_messages_layout)
    View noMessagesLayout;

    private MessageCenterAdapter messageCenterAdapter;
    private ArrayList<Message> messages;
    private OnFragmentVisibleListener onFragmentVisibleListener;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message_center, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            messages = new ArrayList<>();
            setupRecyclerview();
            walletPresenter.getMessageList(new Phone(SharedPreferencesUtil.getPhone()), 100);
        } catch (Throwable t) {
            t.printStackTrace();
            onBackArrowClick();
        }
    }

    private void setupRecyclerview() {
        messageCenterRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        messageCenterAdapter = new MessageCenterAdapter(this);
        messageCenterRecyclerview.setAdapter(messageCenterAdapter);
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
            onFragmentVisibleListener.onFragmentShowListener(MessageCenterFragment.class.getSimpleName(), true);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (onFragmentVisibleListener != null) {
            onFragmentVisibleListener.onFragmentShowListener(MessageCenterFragment.class.getSimpleName(), false);
            onFragmentVisibleListener = null;
        }
    }

    @Override
    public void getData(Object object) {
        if (object instanceof MessageResponse) {
            MessageResponse response = (MessageResponse) object;
            messages.clear();
            messages.addAll(response.getDataset());

            if (SharedPreferencesUtil.getDeletedMessages() != null) {
                Iterator<Message> iter = messages.iterator();
                while (iter.hasNext()) {
                    try {
                        if (SharedPreferencesUtil.getDeletedMessages().contains(iter.next().getMsgId())) {
                            iter.remove();
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }

            Collections.sort(messages, (lhs, rhs) -> Integer.compare(rhs.getMsgTime(), lhs.getMsgTime()));

            messageCenterAdapter.addMessages(messages);

            if (messages != null && messages.size() != 0) {
                noMessagesLayout.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onItemClick(Message message) {
        Set<String> readMessages = new HashSet<>();
        if (SharedPreferencesUtil.getReadMessages() != null) {
            readMessages = SharedPreferencesUtil.getReadMessages();
        }
        readMessages.add(message.getMsgId());
        SharedPreferencesUtil.setReadMessages(readMessages);
        getChildFragmentManager();
        new MessageDialog(message).show(getChildFragmentManager(), MessageDialog.class.getSimpleName());
        messageCenterAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteClick(String messageId) {
        if (getActivity() == null) return;
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.want_remove_message)
                .setNegativeButton(getStringFromResource(R.string.no), (dialogInterface, i) ->
                        dialogInterface.dismiss())
                .setPositiveButton(getStringFromResource(R.string.yes), (dialogInterface, i) -> {
                    for (Message message : messages) {
                        if (message.getMsgId().equals(messageId)) {
                            messages.remove(message);
                            Set<String> deletedMessages = new HashSet<>();
                            if (SharedPreferencesUtil.getReadMessages() != null) {
                                deletedMessages = SharedPreferencesUtil.getReadMessages();
                            }
                            deletedMessages.add(message.getMsgId());
                            SharedPreferencesUtil.setDeletedMessages(deletedMessages);
                            break;
                        }
                    }
                    messageCenterAdapter.addMessages(messages);
                    dialogInterface.dismiss();
                })
                .show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColorFromResource(R.color.grey));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColorFromResource(R.color.grey));
    }
}
