package dev.imanity.brew.util.countdown;

import io.fairyproject.state.StateHandler;
import io.fairyproject.state.StateMachine;
import io.fairyproject.state.trigger.Trigger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public interface Countdown {

    @NotNull
    static Countdown create() {
        return new CountdownImpl();
    }

    @NotNull
    static <S, T> StateHandler<S, T> createStatHandler(Countdown countdown, StateMachine<S, T> stateMachine, Trigger<T> trigger) {
        return StateHandler.<S, T>builder()
                .onStart(() -> countdown.start())
                .onTick(() -> {
                    if (countdown.isStarted()) {
                        countdown.tick();

                        if (countdown.isEnded()) {
                            stateMachine.fire(trigger);
                        }
                    }
                })
                .onStop(countdown::reset)
                .build();
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
