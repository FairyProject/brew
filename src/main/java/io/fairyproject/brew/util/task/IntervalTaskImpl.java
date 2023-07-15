package io.fairyproject.brew.util.task;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IntervalTaskImpl implements IntervalTask {
    private final Runnable runnable;
    private final int interval;

    private int ticks;

    @Override
    public void update() {
        if (ticks++ >= interval) {
            ticks = 0;
            runnable.run();
        }
    }
}
