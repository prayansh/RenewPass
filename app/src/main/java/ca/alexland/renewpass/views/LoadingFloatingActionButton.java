package ca.alexland.renewpass.views;

import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import ca.alexland.renewpass.R;
import ca.alexland.renewpass.utils.DrawableUtil;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Wrapper for Floating Action Button with progress bar and animation helper functions
 */
//TODO: implement onSaveInstanceState() and onRestoreInstanceState
public class LoadingFloatingActionButton extends FrameLayout {
    private FloatingActionButton fab;
    private MaterialProgressBar fabProgressBar;
    private CompleteFABView completeFABView;
    private Drawable completeIcon;
    private Drawable failureIcon;
    private Drawable fabIcon;

    public LoadingFloatingActionButton(Context context) {
        super(context);
        init(null, 0, 0);
    }

    public LoadingFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0, 0);
    }

    public LoadingFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public LoadingFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attributeSet, int defStyleAttr, int defStyleRes) {
        inflate();
        initAttributes(attributeSet, defStyleAttr, defStyleRes);
    }

    private void inflate() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_custom_fab, this);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabProgressBar = (MaterialProgressBar) findViewById(R.id.fabProgressBar);
    }

    private void initAttributes(AttributeSet attributeSet, int defStyleAttr, int defStyleRes) {
        if (attributeSet == null) {
            return;
        }
        TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.LoadingFab,
                defStyleAttr,
                defStyleRes);

        completeIcon = styledAttributes.getDrawable(R.styleable.LoadingFab_completeIcon);
        failureIcon = styledAttributes.getDrawable(R.styleable.LoadingFab_failureIcon);

        int progressColor = styledAttributes.getColor(
                R.styleable.LoadingFab_progressColor,
                ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        DrawableUtil.tint(fabProgressBar.getDrawable(), progressColor);

        fabIcon = styledAttributes.getDrawable(R.styleable.LoadingFab_fabIcon);
        if (fabIcon != null) {
            fab.setImageDrawable(fabIcon);
        }
    }

    @Nullable
    public Drawable getCompleteIconDrawable() {
        return completeIcon;
    }

    @Nullable
    public Drawable getFabIconDrawable() {
        return fabIcon;
    }

    @Override
    public void setOnClickListener(View.OnClickListener fabListener) {
        fab.setOnClickListener(fabListener);
    }

    public void startLoading() {
        fabProgressBar.setVisibility(View.VISIBLE);
    }

    public void finishSuccess() {
        initCompleteFabView(completeIcon, ContextCompat.getColor(getContext(), R.color.colorSuccess));
    }

    public void finishFailure() {
        initCompleteFabView(failureIcon, ContextCompat.getColor(getContext(), R.color.colorFailure));
        completeFABView.reset();
    }

    private void initCompleteFabView(Drawable icon, @ColorInt int backgroundColor) {
        fabProgressBar.setVisibility(View.INVISIBLE);
        if (completeFABView == null) {
            completeFABView = new CompleteFABView(getContext(), icon, backgroundColor);
            ViewCompat.setElevation(completeFABView, ViewCompat.getElevation(fab) + 1);

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER);
            final int fabMargin = getResources().getDimensionPixelSize(R.dimen.fab_margin);
            layoutParams.setMargins(fabMargin, fabMargin, fabMargin, fabMargin);
            addView(completeFABView, layoutParams);

            completeFABView.animateIn();
        } else {
            completeFABView.setIconDrawable(icon);
            completeFABView.setBackgroundColor(backgroundColor);
            completeFABView.animateIn();
        }
    }

    public Drawable getFailureIconDrawable() {
        return failureIcon;
    }
}
