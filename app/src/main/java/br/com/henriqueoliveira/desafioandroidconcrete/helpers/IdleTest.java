package br.com.henriqueoliveira.desafioandroidconcrete.helpers;


import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.test.espresso.IdlingResource;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

public final class IdleTest implements IdlingResource {

    @CheckResult
    @NonNull
    @SuppressWarnings("ConstantConditions") // Extra guards as a library.
    public static IdleTest create(@NonNull String name, @NonNull OkHttpClient client) {
        if (name == null) throw new NullPointerException("name == null");
        if (client == null) throw new NullPointerException("client == null");
        return new IdleTest(name, client.dispatcher());
    }

    private final String name;
    private final Dispatcher dispatcher;
    volatile ResourceCallback callback;

    private IdleTest(String name, Dispatcher dispatcher) {
        this.name = name;
        this.dispatcher = dispatcher;
        dispatcher.setIdleCallback(new Runnable() {
            @Override public void run() {
                ResourceCallback callback = IdleTest.this.callback;
                if (callback != null) {
                    callback.onTransitionToIdle();
                }
            }
        });
    }

    @Override public String getName() {
        return name;
    }

    @Override public boolean isIdleNow() {
        boolean idle = (dispatcher.runningCallsCount() == 0);
        if (idle && callback != null) callback.onTransitionToIdle();
        return idle;
    }

    @Override public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }
}
