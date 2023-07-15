package io.fairyproject.brew.util.task;

public interface IntervalTask {

    static IntervalTask create(Runnable runnable, int ticks) {
        return new IntervalTaskImpl(runnable, ticks);
    }

    void update();

}
