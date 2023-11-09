// run configuration : index episodes ../TestData/CapitulosUnidos

public class Practica4 {

    public static void main(String[] args) {
        if(args[0].equals("index")){
            if(args[1].equals("episodes")){
                EpisodeIndexer indexer = new EpisodeIndexer(true);

                indexer.index(args[2]);
            }
            else if (args[1].equals("scripts")){ 
            	ScriptIndexer indexer = new ScriptIndexer(true);

                indexer.index(args[2]);
            }
        } else if (args[0].equals("append")){
           if(args[1].equals("episodes")){
                EpisodeIndexer indexer = new EpisodeIndexer(false);

                indexer.index(args[2]);
           }
           else if (args[1].equals("script")){ 
           	ScriptIndexer indexer = new ScriptIndexer(false);

               indexer.index(args[2]);
           }
        }
    }
}
