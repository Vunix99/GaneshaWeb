package com.ganesha.diur;

public abstract class RiwayatItem {
    protected static final int TYPE_HEADER = 0;
    protected static final int TYPE_ITEM = 1;

    public abstract boolean isHeader();
    public abstract int getItemType();
}
