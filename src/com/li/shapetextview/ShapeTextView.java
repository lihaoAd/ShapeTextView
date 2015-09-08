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
package com.li.shapetextview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.StateSet;
import android.widget.TextView;

/**
 * 
 * @author lihao
 * @date 2015-9-8
 * */

public class ShapeTextView extends TextView {
	private GradientDrawable mBackground;
	private StateListDrawable mStateDrawable;

	private int mStrokeWidth;
	private int mStrokeColor;

	private float mTopLeftRadius;
	private float mTopRightRadius;
	private float mBottomLeftRadius;
	private float mBottomRightRadius;

	/** 边框是否是虚线 */
	private boolean mDash;
	private float mDashWidth;
	private float mDashGap;

	private int mColorNormal;
	private int mColorPressed;
	private int mColorDisabled;

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

		initAttributes(context, attributeSet);
		initStateDrawable();
		setBackgroundCompat(mStateDrawable);
	}

	private void initStateDrawable() {
		if (mBackground == null) {
			mBackground = createDrawable(mColorNormal, mStrokeColor);
		}

		GradientDrawable drawableDisabled = createDrawable(mColorDisabled,
				mStrokeColor);
		GradientDrawable drawableFocused = createDrawable(mColorPressed,
				mStrokeColor);
		GradientDrawable drawablePressed = createDrawable(mColorPressed,
				mStrokeColor);
		mStateDrawable = new StateListDrawable();

		mStateDrawable.addState(new int[] { android.R.attr.state_pressed },
				drawablePressed);
		mStateDrawable.addState(new int[] { android.R.attr.state_focused },
				drawableFocused);
		mStateDrawable.addState(new int[] { -android.R.attr.state_enabled },
				drawableDisabled);
		mStateDrawable.addState(StateSet.WILD_CARD, mBackground);
	}

	private GradientDrawable createDrawable(int color, int strokeColor) {
		GradientDrawable drawable = new GradientDrawable();
		drawable.setColor(color);
		drawable.setCornerRadii(new float[] { mTopLeftRadius, mTopLeftRadius,
				mTopRightRadius, mTopRightRadius, mBottomLeftRadius,
				mBottomLeftRadius, mBottomRightRadius, mBottomRightRadius });
		if (!mDash) {
			drawable.setStroke(mStrokeWidth, strokeColor);
		} else {
			drawable.setStroke(mStrokeWidth, strokeColor, mDashWidth, mDashGap);
		}
		return drawable;
	}

	private void initAttributes(Context context, AttributeSet attributeSet) {
		TypedArray a = getTypedArray(context, attributeSet,
				R.styleable.ShapeTextView);
		if (a == null)
			return;
		try {
			int n = a.getIndexCount();
			for (int i = 0; i < n; i++) {
				int attr = a.getIndex(i);

				switch (attr) {
				case R.styleable.ShapeTextView_cornerRadius:
					mTopLeftRadius = mTopRightRadius = mBottomLeftRadius = mBottomRightRadius = a
							.getDimension(
									R.styleable.ShapeTextView_cornerRadius, 0);
					break;
				case R.styleable.ShapeTextView_topLeftRadius:
					mTopLeftRadius = a.getDimension(
							R.styleable.ShapeTextView_topLeftRadius, 0);
					break;

				case R.styleable.ShapeTextView_topRightRadius:
					mTopRightRadius = a.getDimension(
							R.styleable.ShapeTextView_topRightRadius, 0);
					break;

				case R.styleable.ShapeTextView_bottomLeftRadius:
					mBottomLeftRadius = a.getDimension(
							R.styleable.ShapeTextView_bottomLeftRadius, 0);
					break;
				case R.styleable.ShapeTextView_bottomRightRadius:
					mBottomRightRadius = a.getDimension(
							R.styleable.ShapeTextView_bottomRightRadius, 0);
					break;
				case R.styleable.ShapeTextView_stroke:
					mStrokeWidth = (int) a.getDimension(
							R.styleable.ShapeTextView_stroke, 0);
					break;

				case R.styleable.ShapeTextView_strokeColor:
					mStrokeColor = a.getColor(
							R.styleable.ShapeTextView_strokeColor, 0);
					break;

				case R.styleable.ShapeTextView_normalColor:
					mColorNormal = a.getColor(
							R.styleable.ShapeTextView_normalColor, 0);
					break;

				case R.styleable.ShapeTextView_pressedColor:
					mColorPressed = a.getColor(
							R.styleable.ShapeTextView_pressedColor, 0);
					break;

				case R.styleable.ShapeTextView_disabledColor:
					mColorDisabled = a.getColor(
							R.styleable.ShapeTextView_disabledColor, 0);
					break;

				case R.styleable.ShapeTextView_isdash:
					mDash = a.getBoolean(R.styleable.ShapeTextView_isdash,
							false);
					break;
				case R.styleable.ShapeTextView_dashWidth:
					mDashWidth = a.getDimension(
							R.styleable.ShapeTextView_dashWidth, 0);
					break;

				case R.styleable.ShapeTextView_dashGap:
					mDashGap = a.getDimension(
							R.styleable.ShapeTextView_dashGap, 0);
					break;

				}
			}

		} finally {
			if (a != null)
				a.recycle();
		}
	}

	protected TypedArray getTypedArray(Context context,
			AttributeSet attributeSet, int[] attr) {
		return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
	}

	protected int getColor(int id) {
		return getResources().getColor(id);
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
