package com.example.model;

public class Filter {
    private Tags tag;
    private boolean isSelected;

    public Filter(Tags tag, boolean isSelected) {
        this.tag = tag;
        this.isSelected = isSelected;
    }

    public Tags getTag() {
        return tag;
    }

    public void setTag(Tags tag) {
        this.tag = tag;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
