/***************************************************************************************************
Copyright 2009 - 2010 Harshana Eranga Martin, Dishan Metihakwala, Rajeev Sampath, Chathura Randika

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
****************************************************************************************************/
package org.epzilla.util;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: May 1, 2010
 * Time: 9:11:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class RoundRobinList<E> {

    private LinkedList<E> list = new LinkedList<E>();
    private int size = 0;
    private int current = 0;

    public synchronized void add(E item) {
        list.add(item);
        size++;
    }

    public int size() {
        return this.size;
    }

    public synchronized void clear() {
        list.clear();
        size = 0;
        current = 0;
    }

    public synchronized boolean remove(E item) {
        boolean success = list.remove(item);
        if (success) {
            size--;
        }
        return success;
    }

    public synchronized boolean contains(E item) {
        return this.list.contains(item);
    }

    public synchronized E next() {
        if (size == 0) {
            return null;
        }
        if (current > (size - 1)) {
            current = 0;
        }
        return this.list.get(current++);
    }

    public synchronized String toString() {
        return list.toString();
    }

}
