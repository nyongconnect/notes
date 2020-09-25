package com.devthrust.mynotes;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;

/**
 * TODO: document your custom view class.
 */
public class ModuleStatusView extends View {

    private static final int INVALID_INDEX = -1;
    private boolean[] mModuleStatus;
    private float mOutlineWidth;
    private float mShapeSize;
    private float mSpacing;
    private Rect[] mModuleRectangle;
    private int mOutlineColor;
    private Paint mPaintOutline;
    private int mFillColor;
    private Paint mPaintFill;
    private float mRadius;
    private int mMaxHorizontalModule;


    public boolean[] getModuleStatus() {
        return mModuleStatus;
    }

    public void setModuleStatus(boolean[] moduleStatus) {
        mModuleStatus = moduleStatus;
    }

    public ModuleStatusView(Context context) {
        super(context);
        init(null, 0);
    }

    public ModuleStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ModuleStatusView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes

        if (isInEditMode()){
            setUpEditModeValues();
        }
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ModuleStatusView, defStyle, 0);
        mFillColor = getContext().getResources().getColor(R.color.pluralsight_orange);

        a.recycle();
        mOutlineWidth = 6f;
        mShapeSize = 144f;
        mSpacing = 30f;
        mRadius = (mShapeSize - mOutlineWidth) / 2;



        mPaintOutline = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintOutline.setStyle(Paint.Style.STROKE);
        mPaintOutline.setStrokeWidth(mOutlineWidth);
        mPaintOutline.setColor(mOutlineColor);


        mPaintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintFill.setStyle(Paint.Style.FILL);
        mPaintFill.setColor(mFillColor);


    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        int availableWidth = specWidth - getPaddingLeft() - getPaddingRight();
        int horizontalModuleThatCanFit = (int) (availableWidth / (mShapeSize + mSpacing));
        mMaxHorizontalModule = Math.min(horizontalModuleThatCanFit, mModuleStatus.length);


        int desiredWidth = getPaddingLeft() + getPaddingRight() + (int) ((mMaxHorizontalModule * (mShapeSize + mSpacing) - mSpacing));

        int rows = ((mModuleStatus.length - 1) / mMaxHorizontalModule) + 1;
        int desiredHeight  = getPaddingTop() + getPaddingBottom() + (rows * (int) (mShapeSize + mSpacing)) - (int) mSpacing;
        int width = resolveSizeAndState(desiredWidth,widthMeasureSpec,0);
        int height = resolveSizeAndState(desiredHeight,heightMeasureSpec,0);
        setMeasuredDimension(width,height);
    }

    private void setUpEditModeValues() {
        boolean [] exampleModuleValues = new boolean[7];

        for (int i = 0; i < 4 ; i++ ){
            exampleModuleValues[i] = true;}
        setModuleStatus(exampleModuleValues);

    }

    private void setUpModuleRectangle(int width) {
        mModuleRectangle = new Rect[mModuleStatus.length];

        int availableWidth = width - getPaddingLeft() - getPaddingRight();
        int horizontalModuleThatCanFit = (int) (availableWidth / (mShapeSize + mSpacing));
        int maxHorizontalModule = Math.min(horizontalModuleThatCanFit, mModuleRectangle.length);

        for (int moduleIndex = 0; moduleIndex < mModuleRectangle.length; moduleIndex++){
            int row = moduleIndex / mMaxHorizontalModule;
            int column = moduleIndex % mMaxHorizontalModule;

            int x = getPaddingLeft () + (int) (column * (mShapeSize + mSpacing));
            int y = getPaddingTop () + (int) (row * (mShapeSize + mSpacing));
            mModuleRectangle[moduleIndex] = new Rect(x, y, x + (int) mShapeSize, y + (int) mShapeSize);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        setUpModuleRectangle(w);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                int moduleIndex = findItemAtPoint(event.getX(), event.getY());
                onModuleSelected(moduleIndex);
                return true;
            case MotionEvent.ACTION_UP:

                return true;
        }
        return super.onTouchEvent(event);
    }

    private void onModuleSelected(int moduleIndex) {
        if (moduleIndex == INVALID_INDEX){
            return;
        }
        mModuleStatus[moduleIndex] = !mModuleStatus[moduleIndex];
        invalidate();
    }

    private int findItemAtPoint(float x, float y) {
        int moduleIndex = INVALID_INDEX;
        for (int i = 0; i < mModuleRectangle.length; i++){
            if (mModuleRectangle[i].contains((int) x, (int) y)){
                moduleIndex = i;
                break;
            }
        }

        return moduleIndex;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int moduleIndex = 0; moduleIndex < mModuleRectangle.length; moduleIndex++){
            float x = mModuleRectangle[moduleIndex].centerX();
            float y  = mModuleRectangle[moduleIndex].centerY();

            if (mModuleStatus[moduleIndex] == true)
                canvas.drawCircle(x, y, mRadius, mPaintFill);
            canvas.drawCircle(x, y, mRadius, mPaintOutline);
        }


    }



}






