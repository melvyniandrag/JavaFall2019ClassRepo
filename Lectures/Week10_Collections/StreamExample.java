import java.util.List;
import java.util.ArrayList;

public class StreamExample{
    public static void main(String[] args){
        List<Song> songs = new ArrayList<Song>();
        songs.add(new Song("Yellow Submarine", "Rock"));
        songs.add(new Song("Ode to Joy", "Classical"));
        songs.stream().forEach(
                e -> System.out.println(
                    "Song " + e.getName() + " is a " + e.getGenre() + " song."
                )
            );
    }
}

class Song{
    private String _n;
    private String _g;
    public Song(String n, String genre){
        _n = n;
        _g = genre;
    }
    public String getName(){
        return _n;
    }
    public String getGenre(){
        return _g;
    }
}
