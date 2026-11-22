package by.ipprpo.music;

import by.ipprpo.music.entity.Track;
import by.ipprpo.music.entity.User;
import by.ipprpo.music.metrics.MetricsManager;
import by.ipprpo.music.metrics.MetricsServer;
import by.ipprpo.music.repository.IPlaylistRepository;
import by.ipprpo.music.repository.PlaylistRepository;
import by.ipprpo.music.service.PlaylistService;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final int DEFAULT_PORT = 8081;

    public static void main(String[] args) {
        logger.info("=== Запуск приложения: Музыкальный сервис (ЛР №6) ===");

        IPlaylistRepository repository = new PlaylistRepository();
        MetricsManager metricsManager = MetricsManager.getInstance();
        PlaylistService playlistService = new PlaylistService(repository, metricsManager);

        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {
                // keep default port
            }
        }

        MetricsServer metricsServer = new MetricsServer(port, playlistService, metricsManager);
        try {
            metricsServer.start();
        } catch (IOException e) {
            logger.error("Не удалось запустить HTTP сервер метрик: {}", e.getMessage());
            return;
        }

        // Подготовка данных предметной области
        User user = new User(1, "Ivan");
        logger.info("Создан пользователь: {}", user);
        playlistService.createPlaylist(1, user, "Imagine Dragons - Best");

        Track track1 = new Track(1, "Believer", "Imagine Dragons", true);
        Track track2 = new Track(2, "Natural", "Imagine Dragons", true);
        // Трек 3 помечен как недоступный (заблокирован/удален правообладателем)
        Track trackUnavailable = new Track(3, "Radioactive", "Imagine Dragons", false);

        playlistService.addTrackToPlaylist(1, track1);
        playlistService.addTrackToPlaylist(1, track2);
        playlistService.addTrackToPlaylist(1, trackUnavailable);

        // Демонстрация успешных сценариев воспроизведения
        logger.info("--- Демонстрация успешного воспроизведения (SUCCESS) ---");
        playlistService.playTrack(1, 1);
        playlistService.playTrack(1, 2);

        // Демонстрация проблемных сценариев (ERROR / UNAVAILABLE)
        logger.info("--- Демонстрация воспроизведения недоступного трека (ERROR) ---");
        playlistService.playTrack(1, 3);

        logger.info("--- Демонстрация воспроизведения несуществующего трека (ERROR) ---");
        playlistService.playTrack(1, 999);

        logger.info("=================================================================");
        logger.info("Приложение готово к сбору метрик Prometheus!");
        logger.info("Endpoint метрик: http://localhost:{}/metrics", port);
        logger.info("Веб-интерфейс:   http://localhost:{}/", port);
        logger.info("Показатели: Total={}, Success={}, Errors={}",
                (long) metricsManager.getOperationsTotal(),
                (long) metricsManager.getOperationsSuccessTotal(),
                (long) metricsManager.getOperationsErrorTotal());
        logger.info("Воспроизведение: Успешно={}, Ошибки (недоступные)={}",
                (long) metricsManager.getPlaybackSuccessTotal(),
                (long) metricsManager.getPlaybackErrorTotal());
        logger.info("=================================================================");

        boolean exitImmediately = false;
        for (String arg : args) {
            if ("--exit".equals(arg) || "--test".equals(arg)) {
                exitImmediately = true;
                break;
            }
        }

        if (exitImmediately) {
            metricsServer.stop();
            logger.info("Флаг немедленного завершения обнаружен. Остановка.");
            return;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(metricsServer::stop));

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            metricsServer.stop();
        }
    }
}
