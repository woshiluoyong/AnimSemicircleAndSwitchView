package com.example.mytestapplication.source;

/**
 * 实现表格的行
 */
public class TableRow {
    private Object paramObj;
    private TableCell[] cell;
    private TableCell.OnTableClickListener onClickListener;

    public TableRow(TableCell[] cell) {
        this.cell = cell;
    }

    public TableRow(Object paramObj,TableCell[] cell) {
        this.paramObj = paramObj;
        this.cell = cell;
    }

    public Object getParamObj() {
        return paramObj;
    }

    public int getSize() {
        return cell.length;
    }

    public TableCell getCellValue(int index) {
        if(index >= cell.length)return null;
        return cell[index];
    }

    public TableCell.OnTableClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(TableCell.OnTableClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}