package ht.lisa.app.ui.wallet.poplocaton;

import com.google.android.material.tabs.TabLayout;

public abstract class OnTabSelectedListener implements TabLayout.OnTabSelectedListener {
    private int selectedOrder = 0;

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        selectedOrder = tab.getPosition();
        initMap();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public abstract void initMap();

    public int getSelectedOrder() {
        return selectedOrder;
    }

}
