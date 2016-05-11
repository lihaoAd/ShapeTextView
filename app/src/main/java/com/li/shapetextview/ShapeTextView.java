package com.li.shapetextview;

/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.StateSet;
import android.view.Gravity;
import android.widget.CheckedTextView;

import app.shapetextview.R;


/**
 * @author lihao
 * @date 2016-5-11
 */

public class ShapeTextView extends CheckedTextView {

    private StateListDrawable mStateDrawable;
    private ColorStateList mColorList;
    private int mStrokeWidth;
    private int mStrokeColor;

    private float mTopLeftRadius;
    private float mTopRightRadius;
    private float mBottomLeftRadius;
    private float mBottomRightRadius;

    /**
     * 边框是否是虚线
     */
    private boolean mDash;
    private float mDashWidth;
    private float mDashGap;

    //背景色
    private int mColorNormal;
    private int mColorPressed;
    private int mColorDisabled;
    private int mColorChecked;


    private int text_checked_color  =0;
    private int text_pressed_color  =0;
    private int text_focused_color  =0;
    private int text_unable_color   =0;
    private int text_window_focused_color=0;
    private int text_normal_color   =0;

    public void setTextColor(int checkedColor,int pressedColor,int focusedColor ,int unableColor,int windowFocusedColor,int normalColor){
        text_checked_color = checkedColor;
        text_pressed_color = pressedColor;
        text_focused_color = focusedColor;
        text_unable_color = unableColor;
        text_window_focused_color = windowFocusedColor;
        text_normal_color = normalColor;
        createColorStateList(text_normal_color,text_checked_color,text_pressed_color,text_focused_color,text_unable_color);
    }


    public ShapeTextView(Context context) {
        super(context);
        init(context, null);
    }

    public ShapeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ShapeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        setGravity(Gravity.CENTER);
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        initAttributes(context, attributeSet);
        initStateDrawable();
        if (mStateDrawable != null) setBackgroundCompat(mStateDrawable);
        createColorStateList(text_normal_color,text_checked_color,text_pressed_color,text_focused_color,text_unable_color);
    }

    /***
     * 背景
     */
    private void initStateDrawable() {
        if(mColorNormal ==0&&mColorChecked==0&&mColorPressed==0&&mColorDisabled==0)return;
        GradientDrawable normalBackground = createDrawable(mColorNormal, mStrokeColor);
        GradientDrawable drawableDisabled = createDrawable(mColorDisabled, mStrokeColor);
        GradientDrawable drawableFocused = createDrawable(mColorPressed, mStrokeColor);
        GradientDrawable drawablePressed = createDrawable(mColorPressed, mStrokeColor);
        GradientDrawable drawableChecked = createDrawable(mColorChecked, mStrokeColor);
        mStateDrawable = new StateListDrawable();
        mStateDrawable.addState(new int[]{android.R.attr.state_pressed}, drawablePressed);
        mStateDrawable.addState(new int[]{android.R.attr.state_checked}, drawableChecked);
        mStateDrawable.addState(new int[]{android.R.attr.state_focused}, drawableFocused);
        mStateDrawable.addState(new int[]{-android.R.attr.state_enabled}, drawableDisabled);
        mStateDrawable.addState(StateSet.WILD_CARD, normalBackground);
    }

    private GradientDrawable createDrawable(int color, int strokeColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadii(new float[]{
                mTopLeftRadius, mTopLeftRadius,
                mTopRightRadius, mTopRightRadius,
                mBottomRightRadius, mBottomRightRadius,
                mBottomLeftRadius, mBottomLeftRadius});
        if (mDash) {
            drawable.setStroke(mStrokeWidth, strokeColor, mDashWidth, mDashGap);
        } else {
            drawable.setStroke(mStrokeWidth, strokeColor);
        }
        return drawable;
    }


    /**
     * 对TextView设置不同状态时其文字颜色。
     */
    private void createColorStateList(int normal, int checked, int pressed, int focused, int unable) {
        if(text_checked_color!=0||text_focused_color!=0||text_normal_color!=0||text_pressed_color!=0||text_unable_color!=0
                ||text_window_focused_color!=0){
            int[] colors = new int[]{checked, focused, pressed, pressed, focused, focused, checked, normal, unable, normal};
            int[][] states = new int[10][];
            states[0] = new int[]{android.R.attr.state_checked, android.R.attr.state_window_focused};
            states[1] = new int[]{-android.R.attr.state_checked, -android.R.attr.state_window_focused};
            states[2] = new int[]{android.R.attr.state_checked, android.R.attr.state_pressed};
            states[3] = new int[]{-android.R.attr.state_checked, android.R.attr.state_pressed};
            states[4] = new int[]{android.R.attr.state_checked, android.R.attr.state_focused};
            states[5] = new int[]{-android.R.attr.state_checked, android.R.attr.state_focused};
            states[6] = new int[]{android.R.attr.state_checked};
            states[7] = new int[]{-android.R.attr.state_checked};
            states[8] = new int[]{-android.R.attr.state_enabled};
            states[9] = new int[]{};
            mColorList = new ColorStateList(states, colors);
        }
        if (mColorList != null) {
            setTextColor(mColorList);
        }
    }


    private void initAttributes(Context context, AttributeSet attributeSet) {
        TypedArray a = getTypedArray(context, attributeSet, R.styleable.ShapeTextView);
        if (a == null) return;
        try {
            int n = a.getIndexCount();
            for (int i = 0; i < n; i++) {
                int attr = a.getIndex(i);

                switch (attr) {
                    case R.styleable.ShapeTextView_st_cornerRadius:
                        mTopLeftRadius = mTopRightRadius = mBottomLeftRadius = mBottomRightRadius = a
                                .getDimension(
                                        R.styleable.ShapeTextView_st_cornerRadius, 0);
                        break;
                    case R.styleable.ShapeTextView_st_topLeftRadius:
                        mTopLeftRadius = a.getDimension(
                                R.styleable.ShapeTextView_st_topLeftRadius, 0);
                        break;

                    case R.styleable.ShapeTextView_st_topRightRadius:
                        mTopRightRadius = a.getDimension(
                                R.styleable.ShapeTextView_st_topRightRadius, 0);
                        break;

                    case R.styleable.ShapeTextView_st_bottomLeftRadius:
                        mBottomLeftRadius = a.getDimension(
                                R.styleable.ShapeTextView_st_bottomLeftRadius, 0);
                        break;
                    case R.styleable.ShapeTextView_st_bottomRightRadius:
                        mBottomRightRadius = a.getDimension(
                                R.styleable.ShapeTextView_st_bottomRightRadius, 0);
                        break;
                    case R.styleable.ShapeTextView_st_stroke:
                        mStrokeWidth = (int) a.getDimension(
                                R.styleable.ShapeTextView_st_stroke, 0);
                        break;

                    case R.styleable.ShapeTextView_st_strokeColor:
                        mStrokeColor = a.getColor(
                                R.styleable.ShapeTextView_st_strokeColor, 0);
                        break;

                    case R.styleable.ShapeTextView_st_normalColor:
                        mColorNormal = a.getColor(
                                R.styleable.ShapeTextView_st_normalColor, 0);
                        break;

                    case R.styleable.ShapeTextView_st_pressedColor:
                        mColorPressed = a.getColor(
                                R.styleable.ShapeTextView_st_pressedColor, 0);
                        break;
                    case R.styleable.ShapeTextView_st_checkedColor:
                        mColorChecked = a.getColor(
                                R.styleable.ShapeTextView_st_checkedColor, 0);
                        break;

                    case R.styleable.ShapeTextView_st_disabledColor:
                        mColorDisabled = a.getColor(
                                R.styleable.ShapeTextView_st_disabledColor, 0);
                        break;

                    case R.styleable.ShapeTextView_st_isdash:
                        mDash = a.getBoolean(R.styleable.ShapeTextView_st_isdash, false);
                        break;
                    case R.styleable.ShapeTextView_st_dashWidth:
                        mDashWidth = a.getDimension(
                                R.styleable.ShapeTextView_st_dashWidth, 0);
                        break;

                    case R.styleable.ShapeTextView_st_dashGap:
                        mDashGap = a.getDimension(
                                R.styleable.ShapeTextView_st_dashGap, 0);
                        break;

                    case R.styleable.ShapeTextView_st_text_checked_color:
                        text_checked_color = a.getColor(R.styleable.ShapeTextView_st_text_checked_color, 0);
                        break;

                    case R.styleable.ShapeTextView_st_text_pressed_color:
                        text_pressed_color = a.getColor(R.styleable.ShapeTextView_st_text_pressed_color, 0);
                        break;

                    case R.styleable.ShapeTextView_st_text_focused_color:
                        text_focused_color = a.getColor(R.styleable.ShapeTextView_st_text_focused_color, 0);
                        break;
                    case R.styleable.ShapeTextView_st_text_unable_color:
                        text_unable_color = a.getColor(R.styleable.ShapeTextView_st_text_unable_color, 0);
                        break;

                    case R.styleable.ShapeTextView_st_text_window_focused_color:
                        text_window_focused_color = a.getColor(R.styleable.ShapeTextView_st_text_window_focused_color, 0);
                        break;
                    case R.styleable.ShapeTextView_st_text_normal_color:
                        text_normal_color = a.getColor(R.styleable.ShapeTextView_st_text_normal_color, 0);
                        break;
                }
            }

        } finally {
            if (a != null) a.recycle();
        }
    }

    protected TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    @SuppressLint("NewApi")
    public void setBackgroundCompat(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }

}
