package by.ipprpo.music.entity;

import java.util.Objects;

public class Track {
    private int id;
    private String trackName;
    private String artist;

    public Track(int id, String trackName, String artist) {
        this.id = id;
        this.trackName = trackName;
        this.artist = artist;
    }

    public int getId() {
        return id;
    }

    public String getTrackName() {
        return trackName;
    }

    public String getTitle() {
        return trackName;
    }

    public String getArtist() {
        return artist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Track track = (Track) o;
        return id == track.id
                || (Objects.equals(trackName, track.trackName) && Objects.equals(artist, track.artist));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trackName, artist);
    }

    @Override
    public String toString() {
        return "Track{id=" + id + ", trackName='" + trackName + "', artist='" + artist + "'}";
    }
}
