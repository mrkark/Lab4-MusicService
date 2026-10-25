package by.ipprpo.music.service;

import by.ipprpo.music.entity.Playlist;
import by.ipprpo.music.entity.Track;
import by.ipprpo.music.entity.User;
import by.ipprpo.music.repository.IPlaylistRepository;
import by.ipprpo.music.repository.PlaylistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AdditionalPlaylistServiceTest {
    private PlaylistService playlistService;
    private IPlaylistRepository repository;

    @BeforeEach
    void setUp() {
        repository = new PlaylistRepository();
        playlistService = new PlaylistService(repository);
    }

    @Test
    void testDuplicateTrackWithDifferentInstances() {
        User user = new User(10, "Anna");
        playlistService.createPlaylist(10, user, "Rock Hits");

        Track trackA = new Track(101, "Thunder", "Imagine Dragons");
        Track trackB = new Track(102, "Thunder", "Imagine Dragons");
        boolean addedA = playlistService.addTrackToPlaylist(10, trackA);
        boolean addedB = playlistService.addTrackToPlaylist(10, trackB);

        assertTrue(addedA, "Первый экземпляр трека должен успешно добавиться");
        assertFalse(addedB, "Дублирующий трек должен быть отклонен логикой сервиса");
        assertEquals(1, repository.findById(10).getTracks().size(), "В плейлисте должен остаться ровно 1 трек");
    }

    @Test
    void testAddMultipleUniqueTracks() {
        User user = new User(20, "Alex");
        playlistService.createPlaylist(20, user, "My Mix");

        Track track1 = new Track(201, "Radioactive", "Imagine Dragons");
        Track track2 = new Track(202, "Demons", "Imagine Dragons");
        Track track3 = new Track(203, "Bones", "Imagine Dragons");

        assertTrue(playlistService.addTrackToPlaylist(20, track1));
        assertTrue(playlistService.addTrackToPlaylist(20, track2));
        assertTrue(playlistService.addTrackToPlaylist(20, track3));

        Playlist playlist = repository.findById(20);
        assertNotNull(playlist, "Плейлист должен быть найден в репозитории");
        assertEquals(3, playlist.getTracks().size(), "В плейлисте должно быть 3 уникальных трека");
        assertEquals("My Mix", playlist.getPlaylistName());
        assertEquals(user, playlist.getOwner());
    }

    @Test
    void testFindNonExistentPlaylist() {
        System.out.println("Запуск: поиск несуществующего плейлиста...");
        Playlist notFound = repository.findById(999);
        assertNull(notFound, "Поиск по несуществующему идентификатору возвращает null");
    }
}
