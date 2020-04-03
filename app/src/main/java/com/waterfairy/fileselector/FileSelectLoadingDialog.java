package com.waterfairy.fileselector;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/11/15 15:22
 * @info:
 */
public class FileSelectLoadingDialog extends Dialog implements DialogInterface.OnDismissListener, View.OnClickListener {
    private TextView mTextView;
    private boolean canDismiss = true;
    private boolean canBackDismiss;
    private ImageView mIVLoading;
    private RotateAnimation rotateAnimation;

    public FileSelectLoadingDialog(@NonNull Context context) {
        super(context, R.style.LoadingDialogTheme);
        setCancelable(false);
        initView();
        initAnim();
    }

    private void initAnim() {
        rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    protected void initView() {
        setContentView(LayoutInflater.from(getContext()).inflate(R.layout.file_select_loading_dialog, null));
        mIVLoading = findViewById(R.id.img_loading);
        mTextView = findViewById(R.id.tv_dialog);
        mTextView.setVisibility(View.GONE);
    }

    public void setText(String text) {
        if (TextUtils.isEmpty(text)) {
            mTextView.setVisibility(View.GONE);
        } else {
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText(text);
        }
    }

    public TextView getTextView() {
        return mTextView;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (dismissListener != null) {
            dismissListener.onDismiss(dialog);
        }
        mIVLoading.clearAnimation();
    }

    public void onBackClick() {
        if (canBackDismiss) {
            dismiss();
        }
    }

    private OnDismissListener dismissListener;
    private View.OnClickListener onClickListener;

    public void setOnDismissListener(OnDismissListener dismissListener) {
        super.setOnDismissListener(dismissListener);
        this.dismissListener = dismissListener;
    }

    public void setOnBgClickListener(View.OnClickListener listener) {
        onClickListener = listener;
    }

    public void setCanClickBGDismiss(boolean canDismiss) {
        this.canDismiss = canDismiss;
    }

    public void setCanBackClickDismiss(boolean canBackDismiss) {
        this.canBackDismiss = canBackDismiss;
    }

    @Override
    public void onClick(View v) {
        if (onClickListener != null) {
            onClickListener.onClick(v);
        }
        if (canDismiss) {
            dismiss();
        }
    }


    public void show() {
        mIVLoading.startAnimation(rotateAnimation);
        super.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackClick();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
