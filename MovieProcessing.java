import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MovieProcessing {

    private static String getString(String line) {
        int colon = line.indexOf(":");
        return line.substring(colon + 1).trim();
    }

    private static List<String> getList(String line) {
        return Stream.of(getString(line).split(", "))
                .collect(Collectors.toList());
      /* Alternative without streams:
      ArrayList<String> result = new ArrayList<>();
      for (String s : getString(line).split(", "))
      {
         result.add(s);
      }
      return result;
      */
    }

    public static List<Movie> readMovies(String filename) throws IOException {
        List<Movie> movies = new ArrayList<>();
        try (Scanner in = new Scanner(new File(filename))) {
            while (in.hasNextLine()) {
                String nameLine = in.nextLine();
                String yearLine = in.nextLine();
                String directorsLine = in.nextLine();
                String producersLine = in.nextLine();
                String actorsLine = in.nextLine();
                movies.add(new Movie(getString(nameLine),
                        Integer.parseInt(getString(yearLine)),
                        getList(directorsLine), getList(producersLine),
                        getList(actorsLine)));
            }
        }
        return movies;
    }


    public static void main(String[] args) throws IOException {

        //3 Read Movies.txt into a List<Movie> Collection
        List<Movie> movieCollection = readMovies("movies.txt");



        //4 Print the count
        System.out.println(movieCollection.stream().count());


        //System.out.println(allMovies);

        //5 All movies that start with "Gone"
        long countGone = movieCollection.stream().filter(w->w.getTitle().startsWith("Gone")).count();
        System.out.println(countGone);


        //6 Sorted all movies that start with Gone
        List<Movie> sortGone = movieCollection.stream().filter(w->w.getTitle().startsWith("Gone")).sorted((x,y) -> x.getYear()-y.getYear()).collect(Collectors.toList());
        //System.out.println(sortGone.toString());


        //7 Alien movies converted to Star Beast Movies
        List<Movie> alienReplaced = movieCollection.stream()
                .filter(w->w.getTitle().contains("Alien"))
                .map(m-> new Movie(m.getTitle().replace("Alien","Star Beast"),m.getYear(),m.getDirectors(),m.getProducers(),m.getActors()))
                .collect(Collectors.toList());

        //System.out.println(alienReplaced.toString());

        //8 Convert Michael Caine to Maurice MickleWhite

        List<Movie> caineReplaced = movieCollection.stream()
                .filter(w->w.getActors().contains("Michael Caine"))
                .map(m-> new Movie(m.getTitle(),m.getYear(),m.getDirectors(),m.getProducers(),m.getActors().stream().map(x -> x.replace("Michael Caine","Maurice MickleWhite")).collect(Collectors.toList())))
                .collect(Collectors.toList());

        //System.out.println(caineReplaced.toString());


        //9 Movies Directed by Quentin Tarantino

        Map<String,List<Movie>> directorMap =
                movieCollection.stream()
                //filter(w->w.getTitle().startsWith("Quentin Tarantino"))
                .collect(Collectors.groupingBy(w->w.getDirectors().get(0)));

        System.out.println(directorMap.get("Quentin Tarantino"));
    }
}