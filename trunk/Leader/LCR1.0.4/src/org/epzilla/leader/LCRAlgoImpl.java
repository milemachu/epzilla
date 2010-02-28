package org.epzilla.leader;

/**
 * This class contains the LCR leader election algorithm implementation.
 * All the necessary LCR specific methods are here.
 * @author Administrator
 *
 */
public class LCRAlgoImpl {
	
	// Private constructor prevents instantiation from other classes
	   private LCRAlgoImpl() {}
	 
	   /**
	    * SingletonHolder is loaded on the first execution of Singleton.getInstance() 
	    * or the first access to SingletonHolder.INSTANCE, not before.
	    */
	   private static class LCRAlgoImplHolder { 
	     private static final LCRAlgoImpl INSTANCE = new LCRAlgoImpl();
	   }
	 
	   public static LCRAlgoImpl getInstance() {
	     return LCRAlgoImplHolder.INSTANCE;
	   }
	   
	   //This is the LCR logic
	   public void runAlgorithm(String []strArray){
		   
		   if(strArray!=null){
//			   if(strArray)
		   }
	   }

}
