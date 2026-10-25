package by.ipprpo.music;

import by.ipprpo.music.entity.Playlist;
import by.ipprpo.music.entity.Track;
import by.ipprpo.music.entity.User;
import by.ipprpo.music.repository.IPlaylistRepository;
import by.ipprpo.music.repository.PlaylistRepository;
import by.ipprpo.music.service.PlaylistService;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Музыкальный сервис (ЛР №2 Maven) ===");

        IPlaylistRepository repository = new PlaylistRepository();
        PlaylistService playlistService = new PlaylistService(repository);

        User user = new User(1, "Ivan");
        System.out.println("Создан пользователь: " + user);

        playlistService.createPlaylist(1, user, "Imagine Dragons - Best");
        System.out.println("Создан плейлист: 'Imagine Dragons - Best' (id=1)");

        Track track1 = new Track(1, "Believer", "Imagine Dragons");
        Track track2 = new Track(2, "Natural", "Imagine Dragons");
        Track duplicateTrack = new Track(3, "Believer", "Imagine Dragons");

        boolean added1 = playlistService.addTrackToPlaylist(1, track1);
        System.out.println("Добавление трека 1 (" + track1.getTrackName() + "): " + (added1 ? "Успешно" : "Отклонено"));

        boolean added2 = playlistService.addTrackToPlaylist(1, track2);
        System.out.println("Добавление трека 2 (" + track2.getTrackName() + "): " + (added2 ? "Успешно" : "Отклонено"));

        boolean addedDuplicate = playlistService.addTrackToPlaylist(1, duplicateTrack);
        System.out.println("Повторное добавление дубликата (" + duplicateTrack.getTrackName() + "): "
                + (addedDuplicate ? "Успешно" : "Отклонено (Индивидуальное задание выполнено)"));

        Playlist playlist = repository.findById(1);
        System.out.println("Итоговый плейлист: " + playlist);
        System.out.println("Количество треков: " + playlist.getTracks().size());
        for (Track t : playlist.getTracks()) {
            System.out.println("  - " + t.getTrackName() + " (" + t.getArtist() + ")");
        }
        System.out.println("=== Программа успешно завершена ===");
    }
}
