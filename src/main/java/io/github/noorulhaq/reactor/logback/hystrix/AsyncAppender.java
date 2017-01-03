package io.github.noorulhaq.reactor.logback.hystrix;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.netflix.hystrix.*;
import com.netflix.hystrix.exception.HystrixRuntimeException;

/**
 * Created by noor on 1/2/17.
 */
public class AsyncAppender extends reactor.logback.AsyncAppender {


    private AsyncAppender.FallbackAction fallbackAction = new AsyncAppender.DefaultFallbackAction();
    private int circuitBreakerSleepWindow = 2000;
    private int circuitBreakerErrorThresholdPercentage = 25;
    private int circuitBreakerRequestVolumeThreshold = 5;
    private int loggingTimeout = 1000;

    @Override
    protected void loggingEventDequeued(ILoggingEvent evt) {
        try {
            new HystrixLoggingEventCommand(evt, this).execute();
        }catch (HystrixRuntimeException ex){
            if(ex.getFailureType() == HystrixRuntimeException.FailureType.SHORTCIRCUIT) // Call fallback only when circuit is open
                fallbackAction.onFallback(evt);
        }
    }

    protected void logEvent(ILoggingEvent evt){
        super.loggingEventDequeued(evt);
    }

    class HystrixLoggingEventCommand extends HystrixCommand<Void> {

        AsyncAppender asyncAppender;
        ILoggingEvent loggingEvent;

        protected HystrixLoggingEventCommand(ILoggingEvent loggingEvent, AsyncAppender asyncAppender) {
            super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("reactorLoggingCB")).andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                    .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
                    .withCircuitBreakerRequestVolumeThreshold(circuitBreakerRequestVolumeThreshold)
                    .withCircuitBreakerErrorThresholdPercentage(circuitBreakerErrorThresholdPercentage)
                    .withCircuitBreakerSleepWindowInMilliseconds(circuitBreakerSleepWindow)
                    .withExecutionTimeoutInMilliseconds(loggingTimeout)
                    .withFallbackEnabled(false)));
            this.asyncAppender = asyncAppender;
            this.loggingEvent = loggingEvent;
        }

        @Override
        protected Void run() throws Exception {
            asyncAppender.logEvent(loggingEvent);
            return null;
        }
    }


    public interface FallbackAction{
        public void onFallback(ILoggingEvent evt);
    }

    static class DefaultFallbackAction implements FallbackAction {

        @Override
        public void onFallback(ILoggingEvent evt) {
            System.out.println(evt);
        }
    }


    public FallbackAction getFallbackAction() {
        return fallbackAction;
    }

    public void setFallbackAction(FallbackAction fallbackAction) {
        this.fallbackAction = fallbackAction;
    }

    public int getCircuitBreakerSleepWindow() {
        return circuitBreakerSleepWindow;
    }

    public void setCircuitBreakerSleepWindow(int circuitBreakerSleepWindow) {
        this.circuitBreakerSleepWindow = circuitBreakerSleepWindow;
    }

    public int getCircuitBreakerErrorThresholdPercentage() {
        return circuitBreakerErrorThresholdPercentage;
    }

    public void setCircuitBreakerErrorThresholdPercentage(int circuitBreakerErrorThresholdPercentage) {
        this.circuitBreakerErrorThresholdPercentage = circuitBreakerErrorThresholdPercentage;
    }

    public int getCircuitBreakerRequestVolumeThreshold() {
        return circuitBreakerRequestVolumeThreshold;
    }

    public void setCircuitBreakerRequestVolumeThreshold(int circuitBreakerRequestVolumeThreshold) {
        this.circuitBreakerRequestVolumeThreshold = circuitBreakerRequestVolumeThreshold;
    }

    public int getLoggingTimeout() {
        return loggingTimeout;
    }

    public void setLoggingTimeout(int loggingTimeout) {
        this.loggingTimeout = loggingTimeout;
    }
}