package me.dzikimlecz.touchserver.model.container;

import java.util.Collection;
import java.util.List;

public abstract class Container<E> {

    private final int index;
    private final int numberOfPages;

    public abstract List<E> getElements();

    public int getSize() {
        return getElements().size();
    }

    protected Container(int index, int numberOfPages) {
        this.index = index;
        this.numberOfPages = numberOfPages;
    }

    public int getIndex() {
        return index;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }
}
