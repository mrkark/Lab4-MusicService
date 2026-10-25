package by.ipprpo.music.service;

import by.ipprpo.music.entity.Playlist;
import by.ipprpo.music.entity.Track;
import by.ipprpo.music.entity.User;
import by.ipprpo.music.repository.IPlaylistRepository;

public class PlaylistService {
    private final IPlaylistRepository repository;

    public PlaylistService(IPlaylistRepository repository) {
        this.repository = repository;
    }

    public void createPlaylist(int playlistId, User owner, String playListName) {
        Playlist playlist = new Playlist(playlistId, playListName, owner);
        repository.save(playlist);
    }

    public boolean addTrackToPlaylist(int playlistId, Track track) {
        Playlist playlist = repository.findById(playlistId);
        if (playlist == null) {
            return false;
        }

        boolean added = playlist.addTrack(track);
        if (added) {
            repository.save(playlist);
        }
        return added;
    }

    public boolean removeTrackFromPlaylist(int playlistId, Track track) {
        Playlist playlist = repository.findById(playlistId);
        if (playlist == null) {
            return false;
        }

        boolean removed = playlist.removeTrack(track);
        if (removed) {
            repository.save(playlist);
        }
        return removed;
    }
}
