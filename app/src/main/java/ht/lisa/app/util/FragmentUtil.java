package ht.lisa.app.util;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ht.lisa.app.R;

public class FragmentUtil {
    private static void replaceFragment(FragmentManager manager, Fragment fragment,
                                        boolean addToBackStack, boolean useLeftRightAnimations, int container) {
        if (manager != null) {
            FragmentTransaction fTrans;
            fTrans = manager.beginTransaction();
            if (!useLeftRightAnimations) {
                fTrans.setTransition(FragmentTransaction.TRANSIT_NONE);
            }
            if (addToBackStack) {
                fTrans.addToBackStack(null);
            } else {
                manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            fTrans.replace(container, fragment, fragment.getClass().getSimpleName());
            fTrans.commitAllowingStateLoss();
        }
    }

    public static void replaceFragment(FragmentManager manager, Fragment fragment, boolean addToBackStack) {
        replaceFragment(manager, fragment, addToBackStack, false, R.id.container);
    }

    public static void addFragment(FragmentManager manager, Fragment fragment, boolean addToBackStack) {
        addFragment(manager, fragment, addToBackStack, false, R.id.container);
    }

    private static void addFragment(FragmentManager manager, Fragment fragment,
                                    boolean addToBackStack, boolean useLeftRightAnimations, int container) {
        FragmentTransaction fTrans;
        fTrans = manager.beginTransaction();
        if (!useLeftRightAnimations) {
            fTrans.setTransition(FragmentTransaction.TRANSIT_NONE);
        }
        if (addToBackStack) {
            fTrans.addToBackStack(null);
        } else {
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        fTrans.add(container, fragment, fragment.getClass().getSimpleName());
        fTrans.commitAllowingStateLoss();
    }

    public static boolean isFragmentUIActive(Fragment fragment) {
        return fragment.isAdded() && !fragment.isDetached() && !fragment.isRemoving();
    }


}
