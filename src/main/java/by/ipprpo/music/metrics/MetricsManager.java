package by.ipprpo.music.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheusmetrics.PrometheusConfig;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import java.util.concurrent.TimeUnit;

public class MetricsManager {
    private static final MetricsManager INSTANCE = new MetricsManager();

    private final PrometheusMeterRegistry registry;
    private final Counter operationsTotal;
    private final Counter operationsSuccessTotal;
    private final Counter operationsErrorTotal;
    private final Counter playbackSuccessTotal;
    private final Counter playbackErrorTotal;
    private final Timer playbackTimer;

    public MetricsManager() {
        this.registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

        this.operationsTotal = Counter.builder("application_operations_total")
                .description("Total number of operations executed by the application")
                .register(registry);

        this.operationsSuccessTotal = Counter.builder("application_operations_success_total")
                .description("Total number of successfully executed operations")
                .register(registry);

        this.operationsErrorTotal = Counter.builder("application_operations_error_total")
                .description("Total number of failed operations")
                .register(registry);

        this.playbackSuccessTotal = Counter.builder("music_service_playback_success_total")
                .description("Total number of successful track playbacks")
                .register(registry);

        this.playbackErrorTotal = Counter.builder("music_service_playback_error_total")
                .description("Total number of failed playback attempts (e.g. unavailable track)")
                .register(registry);

        this.playbackTimer = Timer.builder("music_service_playback_duration_seconds")
                .description("Duration of track playback operations in seconds")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);
    }

    public static MetricsManager getInstance() {
        return INSTANCE;
    }

    public void recordOperationSuccess() {
        operationsTotal.increment();
        operationsSuccessTotal.increment();
    }

    public void recordOperationError() {
        operationsTotal.increment();
        operationsErrorTotal.increment();
    }

    public void recordPlaybackSuccess(long durationMillis) {
        operationsTotal.increment();
        operationsSuccessTotal.increment();
        playbackSuccessTotal.increment();
        playbackTimer.record(durationMillis, TimeUnit.MILLISECONDS);
    }

    public void recordPlaybackError() {
        operationsTotal.increment();
        operationsErrorTotal.increment();
        playbackErrorTotal.increment();
    }

    public String scrape() {
        return registry.scrape();
    }

    public PrometheusMeterRegistry getRegistry() {
        return registry;
    }

    public double getOperationsTotal() {
        return operationsTotal.count();
    }

    public double getOperationsSuccessTotal() {
        return operationsSuccessTotal.count();
    }

    public double getOperationsErrorTotal() {
        return operationsErrorTotal.count();
    }

    public double getPlaybackSuccessTotal() {
        return playbackSuccessTotal.count();
    }

    public double getPlaybackErrorTotal() {
        return playbackErrorTotal.count();
    }
}
