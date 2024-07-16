package ht.lisa.app.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;

import java.lang.reflect.Field;
import java.util.ArrayList;

import ht.lisa.app.R;

public class ViewUtil {

    public static <T> ArrayList<T> getViews(ViewGroup viewGroup, Class<T> viewType) {
        return new ArrayList<T>(getViewsFromLayout(viewGroup, viewType));
    }

    private static <T> ArrayList<T> getViewsFromLayout(ViewGroup viewGroup, Class<T> viewType) {
        return getViewsFromLayout(new ArrayList<>(), viewGroup, viewType);
    }

    private static <T> ArrayList<T> getViewsFromLayout(ArrayList<T> views, ViewGroup viewGroup, Class<T> viewType) {

        final int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                getViewsFromLayout(views, (ViewGroup) view, viewType);
            } else if (viewType.isInstance(view)) {
                T targetView = (T) viewGroup.getChildAt(i);
                views.add(targetView);
            }
        }
        return views;
    }

    public static void setErrorFields(ArrayList<ImageView> imageViewArray, boolean isCorrect) {
        for (ImageView imageView : imageViewArray) {
            imageView.setImageResource(isCorrect ? R.drawable.ic_pin_empty : R.drawable.ic_pin_error);
        }
    }

    public static void changeGuidelinePercent(Guideline guideline, float percent) {
        ConstraintLayout.LayoutParams paramsGuideLine = (ConstraintLayout.LayoutParams) guideline.getLayoutParams();
        paramsGuideLine.guidePercent = percent;
        guideline.setLayoutParams(paramsGuideLine);
    }

    public static void changeViewConstraintTop(ConstraintLayout constraintLayout, int view, int topView) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(view, ConstraintSet.TOP, topView, ConstraintSet.BOTTOM, 0);
        constraintSet.applyTo(constraintLayout);
    }
}
