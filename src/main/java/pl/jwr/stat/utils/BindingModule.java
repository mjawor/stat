package pl.jwr.stat.utils;

import com.google.inject.AbstractModule;
import io.vertx.rxjava.core.Context;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.eventbus.EventBus;


/**
 * Created by mjawor on 2017-04-21.
 */
public class BindingModule extends AbstractModule {

    private final Vertx vertx;
    private final Context context;

    public BindingModule(Vertx vertx) {
        this.vertx = vertx;
        this.context = vertx.getOrCreateContext();
    }

    @Override
    protected void configure() {
        bind(EventBus.class).toInstance(vertx.eventBus());
    }
}