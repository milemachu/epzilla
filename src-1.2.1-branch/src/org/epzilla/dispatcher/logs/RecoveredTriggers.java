package org.epzilla.dispatcher.logs;

import org.epzilla.util.Logger;

import java.util.List;
import java.util.StringTokenizer;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: chathura
 * Date: Mar 25, 2010
 * Time: 10:22:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class RecoveredTriggers {
    private static ArrayList<String> listToReturn = new ArrayList<String>();

    public static void triggerList(List<String> array){
        for(int i=0;i<array.size();i++){
            StringTokenizer st = new StringTokenizer(array.get(i));
            String s1= st.nextToken();
            listToReturn.add(st.nextToken());
        }
        printArray(listToReturn);
    }
    public static void printArray(List<String> array) {
        for(int i=0; i<array.size();i++){
            Logger.log(array.get(i));
        }
    }
    
}
