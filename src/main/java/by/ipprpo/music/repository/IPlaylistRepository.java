package by.ipprpo.music.repository;

import by.ipprpo.music.entity.Playlist;
import java.util.List;

public interface IPlaylistRepository {
    void save(Playlist playlist);
    Playlist findById(int id);
    Playlist[] getPlaylist();
    List<Playlist> getAllPlaylists();
}
