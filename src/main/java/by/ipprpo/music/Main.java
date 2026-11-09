package by.ipprpo.music;

import by.ipprpo.music.entity.Playlist;
import by.ipprpo.music.entity.Track;
import by.ipprpo.music.entity.User;
import by.ipprpo.music.repository.IPlaylistRepository;
import by.ipprpo.music.repository.PlaylistRepository;
import by.ipprpo.music.service.PlaylistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("=== Запуск приложения: Музыкальный сервис (ЛР №5) ===");

        IPlaylistRepository repository = new PlaylistRepository();
        PlaylistService playlistService = new PlaylistService(repository);

        User user = new User(1, "Ivan");
        logger.info("Создан пользователь: {}", user);

        playlistService.createPlaylist(1, user, "Imagine Dragons - Best");

        Track track1 = new Track(1, "Believer", "Imagine Dragons");
        Track track2 = new Track(2, "Natural", "Imagine Dragons");
        Track duplicateTrack = new Track(3, "Believer", "Imagine Dragons");

        playlistService.addTrackToPlaylist(1, track1);
        playlistService.addTrackToPlaylist(1, track2);
        playlistService.addTrackToPlaylist(1, duplicateTrack);

        // Демонстрация WARN: добавление в несуществующий плейлист
        playlistService.addTrackToPlaylist(999, track1);

        // Демонстрация ERROR: передача null трека
        playlistService.addTrackToPlaylist(1, null);

        Playlist playlist = repository.findById(1);
        if (playlist != null) {
            logger.info("Итоговый плейлист: {}, количество треков: {}",
                    playlist.getPlaylistName(), playlist.getTracks().size());
            for (Track t : playlist.getTracks()) {
                logger.info("  - '{}' ({})", t.getTrackName(), t.getArtist());
            }
        }

        logger.info("=== Приложение успешно завершило работу ===");
    }
}
