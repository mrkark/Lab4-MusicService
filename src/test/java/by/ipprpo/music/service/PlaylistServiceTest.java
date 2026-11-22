package by.ipprpo.music.service;

import by.ipprpo.music.entity.Track;
import by.ipprpo.music.entity.User;
import by.ipprpo.music.metrics.MetricsManager;
import by.ipprpo.music.repository.IPlaylistRepository;
import by.ipprpo.music.repository.PlaylistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Основные тесты PlaylistService")
public class PlaylistServiceTest {
    private PlaylistService playlistService;
    private IPlaylistRepository repository;
    private MetricsManager metricsManager;

    @BeforeEach
    void setUp() {
        repository = new PlaylistRepository();
        metricsManager = new MetricsManager();
        playlistService = new PlaylistService(repository, metricsManager);
    }

    @Test
    @DisplayName("Тест 1: Успешный сценарий - добавление двух треков в плейлист")
    void SuccesfullTest() {
        System.out.println("--------------------------------------------------");
        System.out.println("[ТЕСТ 1] Запуск: SuccesfullTest (Успешный сценарий)");
        User testUser = new User(1, "Ivan");
        Track testTrack1 = new Track(1, "Believer", "Imagine Dragons");
        Track testTrack2 = new Track(2, "Natural", "Imagine Dragons");

        playlistService.createPlaylist(1, testUser, "Imagine Dragons");
        boolean r1 = playlistService.addTrackToPlaylist(1, testTrack1);
        boolean r2 = playlistService.addTrackToPlaylist(1, testTrack2);

        assertTrue(r1, "Первый трек должен успешно добавиться");
        assertTrue(r2, "Второй трек должен успешно добавиться");
        assertEquals(2, repository.findById(1).getTracks().size(), "В плейлисте должно быть ровно 2 трека");
        System.out.println("[ТЕСТ 1] Результат: успешно добавлены 2 трека в плейлист 1.");
    }

    @Test
    @DisplayName("Тест 2: Ошибочный сценарий - добавление трека в несуществующий плейлист")
    void UnsuccesfullTest() {
        System.out.println("--------------------------------------------------");
        System.out.println("[ТЕСТ 2] Запуск: UnsuccesfullTest (Ошибочный сценарий)");
        User testUser = new User(1, "Ivan");
        Track testTrack1 = new Track(1, "Believer", "Imagine Dragons");
        Track testTrack2 = new Track(2, "Believer", "Imagine Dragons");

        playlistService.createPlaylist(1, testUser, "Imagine Dragons");
        boolean r1 = playlistService.addTrackToPlaylist(2, testTrack1);
        boolean r2 = playlistService.addTrackToPlaylist(2, testTrack2);

        assertFalse(r1, "Добавление в несуществующий плейлист 2 должно возвращать false");
        assertFalse(r2, "Добавление в несуществующий плейлист 2 должно возвращать false");
        assertNull(repository.findById(2), "Плейлист 2 не должен существовать");
        assertEquals(0, repository.findById(1).getTracks().size(), "В плейлисте 1 не должно быть добавленных треков");
        System.out.println("[ТЕСТ 2] Результат: добавление в несуществующий плейлист корректно отклонено.");
    }

    @Test
    @DisplayName("Тест 3: Индивидуальное задание - запрет добавления одной композиции дважды")
    void Individual_Task_Test() {
        System.out.println("--------------------------------------------------");
        System.out.println("[ТЕСТ 3] Запуск: Individual_Task_Test (Индивидуальное задание ЛР №1)");
        User testUser = new User(1, "Ivan");
        Track testTrack1 = new Track(1, "Believer", "Imagine Dragons");

        playlistService.createPlaylist(1, testUser, "Imagine Dragons");
        boolean first = playlistService.addTrackToPlaylist(1, testTrack1);
        boolean second = playlistService.addTrackToPlaylist(1, testTrack1);

        assertTrue(first, "Первое добавление трека должно быть успешным");
        assertFalse(second, "Повторное добавление того же трека должно быть отклонено");
        assertEquals(1, repository.findById(1).getTracks().size(), "В плейлисте должен остаться только 1 трек");
        System.out.println("[ТЕСТ 3] Результат: дубликат трека успешно обнаружен и отклонён.");
        System.out.println("--------------------------------------------------");
    }

    @Test
    @DisplayName("Тест 4 (Вариант 11, ЛР №4): Удаление трека из плейлиста")
    void testRemoveTrackFromPlaylist_Variant11() {
        System.out.println("--------------------------------------------------");
        System.out.println("[ТЕСТ 4] Запуск: testRemoveTrackFromPlaylist_Variant11 (Инд. задание Вариант 11, ЛР №4)");
        User testUser = new User(1, "Ivan");
        Track track1 = new Track(1, "Believer", "Imagine Dragons");
        Track track2 = new Track(2, "Natural", "Imagine Dragons");

        playlistService.createPlaylist(1, testUser, "Rock Playlist");
        playlistService.addTrackToPlaylist(1, track1);
        playlistService.addTrackToPlaylist(1, track2);
        assertEquals(2, repository.findById(1).getTracks().size(), "В плейлисте должно быть 2 трека перед удалением");

        boolean removed = playlistService.removeTrackFromPlaylist(1, track1);
        assertTrue(removed, "Трек track1 должен быть успешно удален из плейлиста");
        assertEquals(1, repository.findById(1).getTracks().size(), "В плейлисте должен остаться ровно 1 трек");
        assertFalse(repository.findById(1).getTracks().contains(track1), "Плейлист больше не должен содержать удаленный трек");
        assertTrue(repository.findById(1).getTracks().contains(track2), "Второй трек должен остаться в плейлисте");

        // Попытка удалить несуществующий трек
        boolean removeAgain = playlistService.removeTrackFromPlaylist(1, track1);
        assertFalse(removeAgain, "Повторное удаление того же трека должно возвращать false");

        System.out.println("[ТЕСТ 4] Результат: операция удаления трека из плейлиста успешно протестирована.");
        System.out.println("--------------------------------------------------");
    }

    @Test
    @DisplayName("Тест 5 (ЛР №5, Вариант 11): Проверка Logging (INFO, WARN, ERROR)")
    void testLoggingScenarios_Variant11_Lab5() {
        System.out.println("--------------------------------------------------");
        System.out.println("[ТЕСТ 5] Запуск: testLoggingScenarios_Variant11_Lab5 (Журналирование ЛР №5)");
        User testUser = new User(10, "Olga");
        Track track = new Track(101, "Thunder", "Imagine Dragons");

        // 1. INFO сценарий: создание и успешное добавление
        playlistService.createPlaylist(10, testUser, "Pop Rock");
        boolean added = playlistService.addTrackToPlaylist(10, track);
        assertTrue(added, "Трек должен успешно добавиться (лог INFO)");

        // 2. WARN сценарий: повторное добавление дубликата
        boolean duplicateAdded = playlistService.addTrackToPlaylist(10, track);
        assertFalse(duplicateAdded, "Дубликат не должен добавиться (лог WARN)");

        // 3. WARN сценарий: добавление в несуществующий плейлист
        boolean nonExistentAdded = playlistService.addTrackToPlaylist(999, track);
        assertFalse(nonExistentAdded, "Добавление в несуществующий плейлист должно вернуть false (лог WARN)");

        // 4. ERROR сценарий: добавление null трека
        boolean nullAdded = playlistService.addTrackToPlaylist(10, null);
        assertFalse(nullAdded, "Добавление null трека должно вернуть false (лог ERROR)");

        System.out.println("[ТЕСТ 5] Результат: все сценарии Logging (INFO, WARN, ERROR) успешно выполнены.");
        System.out.println("--------------------------------------------------");
    }

    @Test
    @DisplayName("Тест 6 (ЛР №6, Вариант 11): Успешное воспроизведение трека и фиксация метрик")
    void testPlayTrack_Success_Variant11_Lab6() {
        System.out.println("--------------------------------------------------");
        System.out.println("[ТЕСТ 6] Запуск: testPlayTrack_Success_Variant11_Lab6 (Воспроизведение трека)");
        User testUser = new User(1, "Ivan");
        Track track = new Track(1, "Believer", "Imagine Dragons", true);

        playlistService.createPlaylist(1, testUser, "Rock");
        playlistService.addTrackToPlaylist(1, track);

        double prevSuccess = metricsManager.getPlaybackSuccessTotal();
        double prevTotal = metricsManager.getOperationsTotal();

        boolean played = playlistService.playTrack(1, 1);
        assertTrue(played, "Воспроизведение доступного трека должно пройти успешно");

        assertEquals(prevSuccess + 1.0, metricsManager.getPlaybackSuccessTotal(), 0.001,
                "Метрика music_service_playback_success_total должна увеличиться на 1");
        assertEquals(prevTotal + 1.0, metricsManager.getOperationsTotal(), 0.001,
                "Метрика application_operations_total должна увеличиться на 1");
        System.out.println("[ТЕСТ 6] Результат: успешное воспроизведение зафиксировано в метриках.");
        System.out.println("--------------------------------------------------");
    }

    @Test
    @DisplayName("Тест 7 (ЛР №6, Вариант 11): Попытка воспроизведения недоступного трека")
    void testPlayTrack_Unavailable_Variant11_Lab6() {
        System.out.println("--------------------------------------------------");
        System.out.println("[ТЕСТ 7] Запуск: testPlayTrack_Unavailable_Variant11_Lab6 (Недоступный трек)");
        User testUser = new User(1, "Ivan");
        Track unavailableTrack = new Track(2, "Banned Song", "Artist", false);

        playlistService.createPlaylist(1, testUser, "Playlist");
        playlistService.addTrackToPlaylist(1, unavailableTrack);

        double prevErrors = metricsManager.getPlaybackErrorTotal();
        double prevOpErrors = metricsManager.getOperationsErrorTotal();

        boolean played = playlistService.playTrack(1, 2);
        assertFalse(played, "Воспроизведение недоступного трека должно вернуть false");

        assertEquals(prevErrors + 1.0, metricsManager.getPlaybackErrorTotal(), 0.001,
                "Метрика music_service_playback_error_total должна увеличиться на 1");
        assertEquals(prevOpErrors + 1.0, metricsManager.getOperationsErrorTotal(), 0.001,
                "Метрика application_operations_error_total должна увеличиться на 1");

        // Проверка несуществующего трека
        boolean nonExistent = playlistService.playTrack(1, 9999);
        assertFalse(nonExistent, "Воспроизведение несуществующего трека должно вернуть false");
        assertEquals(prevErrors + 2.0, metricsManager.getPlaybackErrorTotal(), 0.001,
                "Метрика music_service_playback_error_total должна увеличиться на 2");

        System.out.println("[ТЕСТ 7] Результат: попытка воспроизведения недоступного трека зафиксирована в ошибках.");
        System.out.println("--------------------------------------------------");
    }

    @Test
    @DisplayName("Тест 8 (ЛР №6): Проверка формата выгрузки метрик для Prometheus Scrape")
    void testPrometheusScrapeOutput_Lab6() {
        System.out.println("--------------------------------------------------");
        System.out.println("[ТЕСТ 8] Запуск: testPrometheusScrapeOutput_Lab6 (Формат Scrape Prometheus)");
        User testUser = new User(1, "Ivan");
        Track track = new Track(1, "Believer", "Imagine Dragons", true);

        playlistService.createPlaylist(1, testUser, "Test");
        playlistService.addTrackToPlaylist(1, track);
        playlistService.playTrack(1, 1);
        playlistService.playTrack(1, 999);

        String scrape = metricsManager.scrape();
        assertNotNull(scrape, "Scrape не должен быть null");
        assertTrue(scrape.contains("application_operations_total"),
                "Scrape должен содержать application_operations_total");
        assertTrue(scrape.contains("application_operations_success_total"),
                "Scrape должен содержать application_operations_success_total");
        assertTrue(scrape.contains("application_operations_error_total"),
                "Scrape должен содержать application_operations_error_total");
        assertTrue(scrape.contains("music_service_playback_success_total"),
                "Scrape должен содержать music_service_playback_success_total");
        assertTrue(scrape.contains("music_service_playback_error_total"),
                "Scrape должен содержать music_service_playback_error_total");

        System.out.println("[ТЕСТ 8] Результат: Scrape-вывод корректно сформирован для Prometheus.");
        System.out.println("--------------------------------------------------");
    }
}
