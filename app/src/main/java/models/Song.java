package models;

/**
 * Created by Ran on 4/27/2016.
 */
public class Song {

    private String id;
    private String name;
    private String title;
    private String album;
    private String artist;
    private String path;
    private int duration;

    public Song(String id, String name, String title, String album, String artist, String path, int duration) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.path = path;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}