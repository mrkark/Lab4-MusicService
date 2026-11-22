package by.ipprpo.music.metrics;

import by.ipprpo.music.service.PlaylistService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetricsServer {
    private static final Logger logger = LoggerFactory.getLogger(MetricsServer.class);

    private final int port;
    private final PlaylistService playlistService;
    private final MetricsManager metricsManager;
    private HttpServer server;

    public MetricsServer(int port, PlaylistService playlistService, MetricsManager metricsManager) {
        this.port = port;
        this.playlistService = playlistService;
        this.metricsManager = metricsManager;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/metrics", new MetricsHandler());
        server.createContext("/api/play", new PlayHandler());
        server.createContext("/api/simulate", new SimulateHandler());
        server.createContext("/", new RootHandler());

        server.setExecutor(null);
        server.start();
        logger.info("HTTP Metrics Server запущен на http://localhost:{}/metrics", port);
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            logger.info("HTTP Metrics Server остановлен");
        }
    }

    public int getPort() {
        return port;
    }

    private class MetricsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String scrapeData = metricsManager.scrape();
            byte[] responseBytes = scrapeData.getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders().set("Content-Type", "text/plain; version=0.0.4; charset=utf-8");
            exchange.sendResponseHeaders(200, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        }
    }

    private class PlayHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery());
            int playlistId = parseInteger(params.get("playlistId"), 1);
            int trackId = parseInteger(params.get("trackId"), 1);

            boolean success = playlistService.playTrack(playlistId, trackId);
            String message = success
                    ? "Успешное воспроизведение трека ID=" + trackId + " из плейлиста ID=" + playlistId
                    : "Ошибка воспроизведения: трек ID=" + trackId + " недоступен или отсутствует";

            String jsonResponse = "{\"success\":" + success + ",\"message\":\"" + message + "\"}";
            byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(success ? 200 : 400, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        }
    }

    private class SimulateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            logger.info("Запуск симуляции активности приложения для мониторинга...");

            // 5 успешных воспроизведений
            for (int i = 1; i <= 5; i++) {
                playlistService.playTrack(1, 1);
            }

            // 2 ошибки (недоступный трек и несуществующий трек)
            playlistService.playTrack(1, 3);
            playlistService.playTrack(1, 999);

            String jsonResponse = "{\"status\":\"ok\",\"simulated\":{\"success\":5,\"errors\":2}}";
            byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(200, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        }
    }

    private class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String html = "<!DOCTYPE html><html><head><meta charset='utf-8'>"
                    + "<title>Music Service Monitoring</title>"
                    + "<style>body{font-family:Arial,sans-serif;margin:40px;line-height:1.6;}"
                    + "a{color:#0275d8;text-decoration:none;}a:hover{text-decoration:underline;}"
                    + ".card{background:#f8f9fa;border:1px solid #ddd;padding:15px;margin-bottom:15px;"
                    + "border-radius:6px;}</style></head><body>"
                    + "<h1>Музыкальный сервис — Лабораторная работа №6</h1>"
                    + "<div class='card'><h3>Эндпоинты мониторинга:</h3>"
                    + "<ul><li><a href='/metrics'><b>/metrics</b> — Prometheus Scrape Endpoint</a></li>"
                    + "<li><a href='/api/play?playlistId=1&trackId=1'>"
                    + "/api/play?playlistId=1&trackId=1 — Успешное воспроизведение (Track 1)</a></li>"
                    + "<li><a href='/api/play?playlistId=1&trackId=3'>"
                    + "/api/play?playlistId=1&trackId=3 — Ошибка: заблокированный трек (Track 3)</a></li>"
                    + "<li><a href='/api/play?playlistId=1&trackId=999'>"
                    + "/api/play?playlistId=1&trackId=999 — Ошибка: несуществующий трек</a></li>"
                    + "<li><a href='/api/simulate'>"
                    + "/api/simulate — Симуляция трафика (5 успехов + 2 ошибки)</a></li></ul></div>"
                    + "<div class='card'><h3>Текущие показатели метрик:</h3>"
                    + "<p>Всего операций: <b>" + (long) metricsManager.getOperationsTotal() + "</b><br>"
                    + "Успешных операций: <b>" + (long) metricsManager.getOperationsSuccessTotal() + "</b><br>"
                    + "Ошибок: <b>" + (long) metricsManager.getOperationsErrorTotal() + "</b><br>"
                    + "Успешных воспроизведений: <b>" + (long) metricsManager.getPlaybackSuccessTotal() + "</b><br>"
                    + "Ошибок воспроизведения: <b>" + (long) metricsManager.getPlaybackErrorTotal() + "</b></p>"
                    + "</div></body></html>";

            byte[] responseBytes = html.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
            exchange.sendResponseHeaders(200, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        }
    }

    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return map;
        }
        for (String param : query.split("&")) {
            String[] pair = param.split("=", 2);
            if (pair.length == 2) {
                map.put(pair[0], pair[1]);
            }
        }
        return map;
    }

    private int parseInteger(String value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
