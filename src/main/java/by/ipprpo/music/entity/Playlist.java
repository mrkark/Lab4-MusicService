package by.ipprpo.music.entity;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private int id;
    private String playlistName;
    private User owner;
    private List<Track> trackList;

    public Playlist(int id, String playlistName, User owner) {
        this.id = id;
        this.playlistName = playlistName;
        this.owner = owner;
        this.trackList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public User getOwner() {
        return owner;
    }

    public List<Track> getTracks() {
        return new ArrayList<>(trackList);
    }

    public boolean addTrack(Track track) {
        if (track == null || trackList.contains(track)) {
            return false;
        }
        trackList.add(track);
        return true;
    }

    public boolean removeTrack(Track track) {
        if (track == null || !trackList.contains(track)) {
            return false;
        }
        return trackList.remove(track);
    }

    @Override
    public String toString() {
        return "Playlist{id=" + id + ", playlistName='" + playlistName + "', owner=" + owner + ", tracksCount=" + trackList.size() + "}";
    }
}
