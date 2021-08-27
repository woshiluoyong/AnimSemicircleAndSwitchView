package com.example.mytestapplication.source;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mytestapplication.R;
import com.example.mytestapplication.source.beans.TaglayoutBean;

import java.util.ArrayList;
import java.util.List;


public class LineBreakLayout extends ViewGroup {
    private final static String TAG = "LineBreakLayout";
    private Context context;
    private List<String> lables;
    private List<String> lableSelected = new ArrayList<>();
    private List<TaglayoutBean> taglayoutBeans;
    private List<TaglayoutBean> selectTaglayoutBeans = new ArrayList<>();

    private List<TextView> childTextViews = new ArrayList<>();
    private boolean isSingleCheck = true;

    //自定义属性
    private int LEFT_RIGHT_SPACE; //dip
    private int ROW_SPACE;

    private int selectTextColor, selectBgColor, normalTextColor, normalBgColor, noneCheckTextColor, noneCheckBgColor;

    public LineBreakLayout(Context context) {
        this(context, null);
    }

    public LineBreakLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineBreakLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        // ROW_SPACE=20   LEFT_RIGHT_SPACE=40
        Log.v(TAG, "ROW_SPACE=" + ROW_SPACE + "   LEFT_RIGHT_SPACE=" + LEFT_RIGHT_SPACE);
    }

    public void setIsSingleCheck(boolean isSingleCheck) {
        this.isSingleCheck = isSingleCheck;
    }

    public void setColor(int selectTextColor, int selectBgColor, int normalTextColor, int normalBgColor, int noneCheckTextColor, int noneCheckBgColor) {
        this.selectTextColor = selectTextColor;
        this.selectBgColor = selectBgColor;
        this.normalTextColor = normalTextColor;
        this.normalBgColor = normalBgColor;
        this.noneCheckTextColor = noneCheckTextColor;
        this.noneCheckBgColor = noneCheckBgColor;
    }

    /**
     * 添加标签
     *
     * @param lables 标签集合
     * @param add    是否追加
     */
    public void setLables(List<String> lables, boolean add) {
        if (this.lables == null) {
            this.lables = new ArrayList<>();
        }
        if (add) {
            this.lables.addAll(lables);
        } else {
            this.lables.clear();
            this.lables = lables;
        }
        if (lables != null && lables.size() > 0) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            for (final String lable : lables) {
                //获取标签布局
                final TextView tv = new TextView(context);
                tv.setText(lable);
                //设置选中效果
                if (lableSelected.contains(lable)) {
                    //选中
                    tv.setSelected(true);
                    tv.setTextColor(Color.parseColor("#FF8B00"));
                } else {
                    //未选中
                    tv.setSelected(false);
                    tv.setTextColor(Color.parseColor("#FF8B00"));
                }

                //点击标签后，重置选中效果
                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv.setSelected(tv.isSelected() ? false : true);
                        if (tv.isSelected()) {
                            tv.setTextColor(Color.parseColor("#FF8B00"));
                            lableSelected.add(lable);
                        } else {
                            tv.setTextColor(Color.parseColor("#FF8B00"));
                            lableSelected.remove(lable);
                        }
                    }
                });
                addView(tv);
            }
        }
    }

    /**
     * 添加标签
     *
     * @param tagbeans 标签集合
     * @param add      是否追加
     */
    public void setTagLables(List<TaglayoutBean> tagbeans, boolean add, final boolean isClickable) {
        if (this.taglayoutBeans == null) {
            this.taglayoutBeans = new ArrayList<>();
        }
        if (add) {
            this.taglayoutBeans.addAll(tagbeans);
        } else {
            this.taglayoutBeans.clear();
            this.taglayoutBeans = tagbeans;
        }
        if (taglayoutBeans != null && taglayoutBeans.size() > 0) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            for (int i = 0; i < taglayoutBeans.size(); i++) {
                final TaglayoutBean taglayoutBean = taglayoutBeans.get(i);
                //获取标签布局
                final TextView tv = new TextView(context);
                tv.setText(taglayoutBean.getText());
                //if(i >= selectTaglayoutBeans.size())continue;
                if(selectTaglayoutBeans.size() <=0)continue;
                //设置选中效果
                if (selectTaglayoutBeans.contains(taglayoutBean) && selectTaglayoutBeans.get(i).isChecked()) {
                    //选中
                    tv.setSelected(true);
                    tv.setTextColor(getResources().getColor(selectTextColor));
                    tv.setBackgroundResource(selectBgColor);
                } else {
                    //未选中
                    tv.setSelected(false);
                    tv.setTextColor(getResources().getColor(normalTextColor));
                    tv.setBackgroundResource(normalBgColor);
                }
                if (!taglayoutBean.isClickable() && isClickable) {
                    tv.setClickable(false);
                    tv.setBackgroundResource(noneCheckBgColor);
                    tv.setTextColor(getResources().getColor(noneCheckTextColor));
                } else {
                    tv.setTag(i);
                    //点击标签后，重置选中效果
                    tv.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int index = (int) tv.getTag();
                            if (mTagLayoutOnItemClickListener != null) {
                                mTagLayoutOnItemClickListener.setOnItemClickListener(index);
                            }
                            tv.setSelected(tv.isSelected() ? false : true);
                            if (tv.isSelected()) {
                                taglayoutBeans.get(index).setChecked(true);
                            } else {
                                taglayoutBeans.get(index).setChecked(false);
                            }
                            changeChildState(index,isClickable);
                        }
                    });
                }
                //将标签添加到容器中
                addView(tv);
                childTextViews.add(tv);
            }
        }
    }

    private void changeChildState(int selectIndex,boolean isClickable) {
        for (int i = 0; i < taglayoutBeans.size(); i++) {
            if(!isClickable){
                if(i == selectIndex && isSingleCheck){
                    taglayoutBeans.get(i).setChecked(true);
                }else {
                    taglayoutBeans.get(i).setChecked(false);
                }
            }else {
                if (i != selectIndex && isSingleCheck) {
                    taglayoutBeans.get(i).setChecked(false);
                }
            }
            //if(i >= selectTaglayoutBeans.size())continue;
            if (taglayoutBeans.get(i).isChecked()) {
                childTextViews.get(i).setTextColor(getResources().getColor(selectTextColor));
                childTextViews.get(i).setBackgroundResource(selectBgColor);
                childTextViews.get(i).setSelected(true);
            } else {
                if(!isClickable){
                    childTextViews.get(i).setTextColor(getResources().getColor(normalTextColor));
                    childTextViews.get(i).setBackgroundResource(normalBgColor);
                    childTextViews.get(i).setSelected(false);
                }
                if (taglayoutBeans.get(i).isClickable()) {
                    childTextViews.get(i).setTextColor(getResources().getColor(normalTextColor));
                    childTextViews.get(i).setBackgroundResource(normalBgColor);
                    childTextViews.get(i).setSelected(false);
                }
            }
        }
    }

    private TagLayoutOnItemClickListener mTagLayoutOnItemClickListener;

    public void setOnItemClickListener(TagLayoutOnItemClickListener tagLayoutOnItemClickListener) {
        mTagLayoutOnItemClickListener = tagLayoutOnItemClickListener;
    }

    /**
     * 获取选中标签
     */
    public List<String> getSelectedLables() {
        return lableSelected;
    }

    public void addSelectTagBean(TaglayoutBean taglayoutBean) {
        selectTaglayoutBeans.add(taglayoutBean);
    }

    public List<TaglayoutBean> getSelectTaglayoutBeans(){
        return selectTaglayoutBeans;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //为所有的标签childView计算宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //获取高的模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //建议的高度
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //布局的宽度采用建议宽度（match_parent或者size），如果设置wrap_content也是match_parent的效果
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height;
        if (heightMode == MeasureSpec.EXACTLY) {
            //如果高度模式为EXACTLY（match_perent或者size），则使用建议高度
            height = heightSize;
        } else {
            //其他情况下（AT_MOST、UNSPECIFIED）需要计算计算高度
            int childCount = getChildCount();
            if (childCount <= 0) {
                height = 0;   //没有标签时，高度为0
            } else {
                int row = 1;  // 标签行数
                int widthSpace = width;// 当前行右侧剩余的宽度
                for (int i = 0; i < childCount; i++) {
                    View view = getChildAt(i);
                    //获取标签宽度
                    int childW = view.getMeasuredWidth();
                    Log.v(TAG, "标签宽度:" + childW + " 行数：" + row + "  剩余宽度：" + widthSpace);
                    if (widthSpace >= childW) {
                        //如果剩余的宽度大于此标签的宽度，那就将此标签放到本行
                        widthSpace -= childW;
                    } else {
                        row++;    //增加一行
                        //如果剩余的宽度不能摆放此标签，那就将此标签放入一行
                        widthSpace = width - childW;
                    }
                    //减去标签左右间距
                    widthSpace -= LEFT_RIGHT_SPACE;
                }
                //由于每个标签的高度是相同的，所以直接获取第一个标签的高度即可
                int childH = getChildAt(0).getMeasuredHeight();
                //最终布局的高度=标签高度*行数+行距*(行数-1)
                height = (childH * row) + ROW_SPACE * (row - 1);

                Log.v(TAG, "总高度:" + height + " 行数：" + row + "  标签高度：" + childH);
            }
        }
        //设置测量宽度和测量高度
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int row = 0;
        int right = 0;   // 标签相对于布局的右侧位置
        int botom;       // 标签相对于布局的底部位置
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            int childW = childView.getMeasuredWidth();
            int childH = childView.getMeasuredHeight();
            //右侧位置=本行已经占有的位置+当前标签的宽度
            right += childW;
            //底部位置=已经摆放的行数*（标签高度+行距）+当前标签高度
            botom = row * (childH + ROW_SPACE) + childH;
            // 如果右侧位置已经超出布局右边缘，跳到下一行
            // if it can't drawing on a same line , skip to next line
            if (right > (r - LEFT_RIGHT_SPACE)) {
                row++;
                right = childW;
                botom = row * (childH + ROW_SPACE) + childH;
            }
            Log.d(TAG, "left = " + (right - childW) + " top = " + (botom - childH) +" right = " + right + " botom = " + botom);
            childView.layout(right - childW, botom - childH, right, botom);
            right += LEFT_RIGHT_SPACE;
        }
    }
}