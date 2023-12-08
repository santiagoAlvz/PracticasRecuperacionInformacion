// run configuration : index episodes ../TestData/CapitulosUnidos

/*
 * Practica 6. Index simpsons data stored in csv files
 * Santiago Álvarez Valdivia
 * Ioannis Efthymiou
 */
package practica6;

public class Practica6 {

    public static void main(String[] args) {
    	boolean create = false;
    	
    	if(args.length < 3) {
    		System.out.println("Error. Missing arguments\nUsage: Practica4 OPERATION DATAKIND DATADIRECTORY, where OPERATION = index | append and DATAKIND = episodes | scripts");
    		return;
    	}
    	
    	if(args[0].equals("index")){
    		create = true;
    	} else if (args[0].equals("append")){
    		create = false;
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
