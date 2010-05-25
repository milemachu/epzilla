package net.epzilla.stratification.restruct;

import net.epzilla.stratification.query.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: May 24, 2010
 * Time: 10:01:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class Restructuring {

    LinkedList<Query> qList = new LinkedList<Query>();
    ArrayList<ArrayList<Integer>> map = null;
    boolean[] present = null;

    LinkedList<LinkedList<Integer>> strata = null;

    public void addQuery(Query q) {
        qList.add(q);
    }

    public void addQueries(List<Query> list) {
        for (Query q : list) {
            qList.add(q);
        }
    }

    public void buildGraph() {
        map = new ArrayList<ArrayList<Integer>>(qList.size());
        present = new boolean[qList.size()];
        strata = new LinkedList<LinkedList<Integer>>();
        Arrays.fill(present, true);




    }


}
