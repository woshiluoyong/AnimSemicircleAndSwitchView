package com.example.mytestapplication.source;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 实现表格行的样式
 */
public class TableRowView extends LinearLayout{

    public TableRowView(Context context,final int rowPosition,final TableRow tableRow) {
        super(context);
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setBackgroundColor(Color.WHITE);
        for(int i = 0; i < tableRow.getSize(); i++) {//逐个格单元添加到行
            final TableCell tableCell = tableRow.getCellValue(i);
            if(null == tableCell)continue;
            LayoutParams layoutParams = new LayoutParams(tableCell.width, tableCell.height);//按照格单元指定的大小设置空间
            layoutParams.setMargins(0, 2, 5, 2);//表格大小
            switch(tableCell.getType()){
                case TableCell.String://如果格单元是文本内容
                    TextView textCell = new TextView(context);
                    textCell.setSingleLine();
                    textCell.setGravity(Gravity.CENTER);
                    textCell.setTextColor(Color.BLACK);
                    textCell.setText(String.valueOf(tableCell.value));
                    textCell.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(null != tableRow.getOnClickListener())tableRow.getOnClickListener().onClick(rowPosition,tableCell.getIndex(),tableCell.value);
                        }
                    });
                    addView(textCell, layoutParams);
                    break;
                case TableCell.Image://如果格单元是图像内容
                    ImageView imgCell = new ImageView(context);
                    imgCell.setBackgroundColor(Color.WHITE);
                    imgCell.setScaleType(ImageView.ScaleType.FIT_XY);
                    if(-1 != (Integer)tableCell.value)imgCell.setImageResource((Integer)tableCell.value);
                    imgCell.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(null != tableRow.getOnClickListener())tableRow.getOnClickListener().onClick(rowPosition,tableCell.getIndex(),tableCell.value);
                        }
                    });
                    addView(imgCell, layoutParams);
                    break;
                case TableCell.CommCell://如果格单元是通用格对象
                    TextView textAryCell = new TextView(context);
                    textAryCell.setGravity(Gravity.CENTER);
                    textAryCell.setTextColor(Color.BLACK);
                    EntityTableCommCell commCell = (EntityTableCommCell)tableCell.value;
                    if(null != commCell){
                        textAryCell.setText(commCell.getShowStr());
                        textAryCell.setTextColor(commCell.getShowColorResId());
                    }//end of if
                    textAryCell.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(null != tableRow.getOnClickListener())tableRow.getOnClickListener().onClick(rowPosition,tableCell.getIndex(),tableCell.value);
                        }
                    });
                    addView(textAryCell, layoutParams);
                    break;
                case TableCell.Empty://如果格单元是占位
                    addView(new View(context), layoutParams);
                    break;
            }//end of switch
        }//end of for
    }
}
