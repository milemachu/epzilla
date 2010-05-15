package org.epzilla.accumulator.client.parser;



public class Util {
    public static String[] copyContent(String[] source, int[] placesToCopy) {
        String[] target = new String[placesToCopy.length];
        int i = 0;
        for (int place : placesToCopy) {
            target[i] = source[place];
            i++;
        }
        return target;
    }


    public static void print(int[] x) {
        for (int i : x) {
            System.out.print(i + ", ");
        }
        System.out.println("");
    }

    public static void print(Object[] x) {
        for (Object i : x) {
            System.out.print(i.toString() + ", ");
        }
        System.out.println("");
    }

    public static int[] getMapping(String[] src, String[] target) {
        int[] mapping = new int[src.length + 1];
        mapping[title] = indexOf("Title", target);
        int i = 1;
        for (String s : src) {
            mapping[i] = indexOf(s, target);
            i++;
        }
        return mapping;
    }


    public static int[] getMapping(String[] src, String[] target, boolean addTitle) {

          int[] mapping = new int[addTitle? src.length + 1: src.length];
        if (addTitle) {
          mapping[title] = indexOf("Title", target);
        }
          int i = addTitle? 1: 0;
          for (String s : src) {
              mapping[i] = indexOf(s, target);
              i++;
          }
          return mapping;
      }


    static int title = 0;

    public static int indexOf(String item, String[] items) {
        int i = 0;
        for (String x : items) {
            if (item.equals(x)) {
                return i;
            }
            i++;
        }
        return -1;
    }

}
