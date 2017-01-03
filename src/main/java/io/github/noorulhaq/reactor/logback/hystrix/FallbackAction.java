package io.github.noorulhaq.reactor.logback.hystrix;

import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * Created by noor on 1/3/17.
 */
public interface FallbackAction {

    public void onFallback(ILoggingEvent evt);

}
