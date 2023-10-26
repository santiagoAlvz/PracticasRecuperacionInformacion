public class Practica4 {
    private static EpisodeIndexer indexer;

    public static void main(String[] args) {
        if(args[0].equals("create")){
            if(args[1].equals("episodeIndex")){
                indexer.createIndex(args[2]);
            }
        } else if (args[0].equals("update")){
            if(args[1].equals("episodeIndex")){
                indexer.updateIndex(args[2]);
            }
        }
    }
}
