package ht.lisa.app.ui.wallet.games;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import java.util.ArrayList;

import ht.lisa.app.R;
import ht.lisa.app.util.TextUtil;

public class GamesItemTicketAdapter extends BaseAdapter {

    ArrayList<String> comboNumList;
    LayoutInflater inflater;
    private Context context;

    public GamesItemTicketAdapter(Context context, ArrayList<String> comboNumList) {
        this.comboNumList = comboNumList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return comboNumList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_games_combo, parent, false);
            TextView firstNum = convertView.findViewById(R.id.lotto_ball_1_number);
            TextView secondNum = convertView.findViewById(R.id.lotto_ball_2_number);
            TextView thirdNum = convertView.findViewById(R.id.lotto_ball_3_number);
            firstNum.setBackground(AppCompatResources.getDrawable(context, R.drawable.ic_yellow_lotto_ball));
            secondNum.setBackground(AppCompatResources.getDrawable(context, R.drawable.ic_yellow_lotto_ball));
            thirdNum.setBackground(AppCompatResources.getDrawable(context, R.drawable.ic_yellow_lotto_ball));
            if (comboNumList.size() == 1) {
                ArrayList<String> numsList = new ArrayList<>(TextUtil.getSeparateTicketNums(comboNumList.get(0)));
                firstNum.setText(numsList.get(0));
                if (numsList.size() > 1) {
                    secondNum.setText(numsList.get(1));
                    if (numsList.size() > 2) {
                        thirdNum.setText(numsList.get(2));
                    }
                }
                secondNum.setVisibility(numsList.size() > 1 ? View.VISIBLE : View.GONE);
                thirdNum.setVisibility(numsList.size() > 2 ? View.VISIBLE : View.GONE);
            } else {
                int step = comboNumList.get(position).length() % 2 == 0 ? 2 : 1;
                firstNum.setText(comboNumList.get(position).substring(0, step));
                secondNum.setText(comboNumList.get(position).substring(step, step + step));
                thirdNum.setText(comboNumList.get(position).substring(step + step));
                thirdNum.setVisibility(comboNumList.get(position).length() == 4 ? View.GONE : View.VISIBLE);
            }
        }
        return convertView;
    }
}
