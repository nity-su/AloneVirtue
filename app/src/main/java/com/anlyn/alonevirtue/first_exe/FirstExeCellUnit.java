package com.anlyn.alonevirtue.first_exe;

public class FirstExeCellUnit {
    private String contents;
    private int drawable;

    public FirstExeCellUnit(String contents, int drawable) {
        this.contents = contents;
        this.drawable = drawable;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
}
