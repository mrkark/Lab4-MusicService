package by.ipprpo.music.service;

import by.ipprpo.music.entity.Track;
import by.ipprpo.music.entity.User;
import by.ipprpo.music.repository.IPlaylistRepository;
import by.ipprpo.music.repository.PlaylistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Основные тесты PlaylistService (из ЛР №1)")
public class PlaylistServiceTest {
    private PlaylistService playlistService;
    private IPlaylistRepository repository;

    @BeforeEach
    void setUp() {
        repository = new PlaylistRepository();
        playlistService = new PlaylistService(repository);
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
}
