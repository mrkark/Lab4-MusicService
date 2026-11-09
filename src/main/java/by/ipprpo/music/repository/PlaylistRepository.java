package by.ipprpo.music.repository;

import by.ipprpo.music.entity.Playlist;
import java.util.ArrayList;
import java.util.List;

public class PlaylistRepository implements IPlaylistRepository {
    private final List<Playlist> storage = new ArrayList<>();

    @Override
    public void save(Playlist playlist) {
        if (playlist == null) {
            return;
        }
        storage.removeIf(p -> p.getId() == playlist.getId());
        storage.add(playlist);
    }

    @Override
    public Playlist findById(int id) {
        return storage.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Playlist[] getPlaylist() {
        return storage.toArray(new Playlist[0]);
    }

    @Override
    public List<Playlist> getAllPlaylists() {
        return new ArrayList<>(storage);
    }
}
