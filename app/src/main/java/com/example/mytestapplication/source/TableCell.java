package com.example.mytestapplication.source;

/**
 * 实现表格的格单元
 */
public class TableCell {
    public static final int String = 0,Image = 1,CommCell = 2,Empty = 3;
    private int index;
    public Object value;
    public int width,height;
    private int type;

    public TableCell(int index, Object value, int width, int height, int type) {
        this.index = index;
        this.value = value;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public int getType() {
        return type;
    }

    public interface OnTableClickListener{
        void onClick(int row,int col,Object value);
    }
}
