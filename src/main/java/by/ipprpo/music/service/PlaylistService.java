package by.ipprpo.music.service;

import by.ipprpo.music.entity.Playlist;
import by.ipprpo.music.entity.Track;
import by.ipprpo.music.entity.User;
import by.ipprpo.music.repository.IPlaylistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlaylistService {
    private static final Logger logger = LoggerFactory.getLogger(PlaylistService.class);
    private final IPlaylistRepository repository;

    public PlaylistService(IPlaylistRepository repository) {
        this.repository = repository;
    }

    public void createPlaylist(int playlistId, User owner, String playListName) {
        logger.info("Создание плейлиста: ID={}, название='{}', владелец={}",
                playlistId, playListName, (owner != null ? owner.getName() : "null"));
        Playlist playlist = new Playlist(playlistId, playListName, owner);
        repository.save(playlist);
        logger.info("Плейлист успешно создан и сохранен: ID={}", playlistId);
    }

    public boolean addTrackToPlaylist(int playlistId, Track track) {
        logger.info("Запрос на добавление трека в плейлист ID={}", playlistId);

        if (track == null) {
            logger.error("Критическая ошибка: передан null трек для плейлиста ID={}", playlistId);
            return false;
        }

        Playlist playlist = repository.findById(playlistId);
        if (playlist == null) {
            logger.warn("Предупреждение: плейлист с ID={} не найден (трек '{}' не может быть добавлен)",
                    playlistId, track.getTitle());
            return false;
        }

        boolean added = playlist.addTrack(track);
        if (!added) {
            logger.warn("Предупреждение: трек '{}' уже присутствует в плейлисте ID={} (отклонено как дубликат)",
                    track.getTitle(), playlistId);
            return false;
        }

        repository.save(playlist);
        logger.info("Успех: трек '{}' (исполнитель: '{}') добавлен в плейлист ID={}, текущее количество треков: {}",
                track.getTitle(), track.getArtist(), playlistId, playlist.getTracks().size());
        return true;
    }

    public boolean removeTrackFromPlaylist(int playlistId, Track track) {
        logger.info("Запрос на удаление трека из плейлиста ID={}", playlistId);

        if (track == null) {
            logger.error("Критическая ошибка: передан null трек при удалении из плейлиста ID={}", playlistId);
            return false;
        }

        Playlist playlist = repository.findById(playlistId);
        if (playlist == null) {
            logger.warn("Предупреждение: плейлист с ID={} не найден для удаления трека", playlistId);
            return false;
        }

        boolean removed = playlist.removeTrack(track);
        if (!removed) {
            logger.warn("Предупреждение: трек '{}' отсутствует в плейлисте ID={}, удаление невозможно",
                    track.getTitle(), playlistId);
            return false;
        }

        repository.save(playlist);
        logger.info("Успех: трек '{}' удален из плейлиста ID={}, осталось треков: {}",
                track.getTitle(), playlistId, playlist.getTracks().size());
        return true;
    }
}
