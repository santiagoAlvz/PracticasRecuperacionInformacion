public class Practica4 {
    private static EpisodeIndexer indexer;

    public static void main(String[] args) {
        if(args[0].equals("index")){
            if(args[1].equals("episodes")){
                indexer.createIndex(args[2]);
            }
        } else if (args[0].equals("append")){
            if(args[1].equals("episodes")){
                indexer.updateIndex(args[2]);
            }
        }
    }
}
