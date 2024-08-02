package io.fairyproject.brew.util.task;

public class IntervalTaskImpl implements IntervalTask {
    private final Runnable runnable;
    private final int interval;

    private int ticks;

    public IntervalTaskImpl(Runnable runnable, int interval) {
        this.runnable = runnable;
        this.interval = interval;
        this.ticks = interval;
    }

    @Override
    public void reset() {
        ticks = interval;
    }

    @Override
    public void update() {
        if (ticks++ >= interval) {
            ticks = 0;
            runnable.run();
        }
    }
}
