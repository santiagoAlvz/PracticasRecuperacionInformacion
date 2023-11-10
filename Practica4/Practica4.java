// run configuration : index episodes ../TestData/CapitulosUnidos

public class Practica4 {

    public static void main(String[] args) {
    	Boolean create = false;
    	
    	if(args[0].equals("index")){
    		create = false;
    	} else if (args[0].equals("append")){
    		create = true;
    	} else {
        	System.out.println("Error. Invalid Operation");
        }
    		
        
        if(args[1].equals("episodes")){
            EpisodeIndexer indexer = new EpisodeIndexer(create);

            indexer.index(args[2]);
        } else if (args[1].equals("scripts")){ 
        	ScriptIndexer indexer = new ScriptIndexer(create);

            indexer.index(args[2]);
        } else {
        	System.out.println("Invalid object to index. Objects available: episodes and scripts");
        }
    }
}
