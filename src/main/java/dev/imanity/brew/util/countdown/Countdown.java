package dev.imanity.brew.util.countdown;

import io.fairyproject.state.Signal;
import io.fairyproject.state.State;
import io.fairyproject.state.StateHandler;
import io.fairyproject.state.StateMachine;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public interface Countdown {

    @NotNull
    static Countdown create() {
        return new CountdownImpl();
    }

    @NotNull
    static StateHandler createStatHandler(Countdown countdown, Signal signal) {
        return new StateHandler() {
            @Override
            public void onStart(@NotNull StateMachine stateMachine, @NotNull State state, @Nullable Signal signal) {
                countdown.start();
            }

            @Override
            public void onTick(@NotNull StateMachine stateMachine, @NotNull State state) {
                if (countdown.isStarted()) {
                    countdown.tick();

                    if (countdown.isEnded()) {
                        stateMachine.signal(signal);
                    }
                }
            }

            @Override
            public void onStop(@NotNull StateMachine stateMachine, @NotNull State state, @Nullable Signal signal) {
                countdown.reset();
            }
        };
    }

    @Contract("_ -> this")
    Countdown duration(long millis);

    @Contract("_, _ -> this")
    Countdown duration(long duration, @NotNull TimeUnit unit);

    @Contract("_ -> this")
    Countdown timeUnit(@NotNull TimeUnit timeUnit);

    @Contract("_ -> this")
    Countdown numbersToCount(long... numbers);

    @Contract("_ -> this")
    Countdown consumer(@NotNull Consumer<Long> consumer);

    boolean isStarted();

    boolean isEnded();

    void start();

    void start(long millis);

    void tick();

    void reset();

    long getRemainingMillis();
}
