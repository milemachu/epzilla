package net.epzilla.stratification.query;

/**
 * Created by IntelliJ IDEA.
 * User: rajeev
 * Date: Dec 2, 2009
 * Time: 8:32:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class LanguageDefinitions {

    public static String[] keywords = {"select ", "count ", "findOcurrences ", " output ", " retain ", " as ", " where "};
    public static String[] functions = {"avg", "sum", "total", "max", "min"};// to identify operations directly in a query.
    public static String[] functionTokens = {"avg[\\s]*\\(", "sum[\\s]*\\(", "total[\\s]*\\(", "max[\\s]*\\(", "min[\\s]*\\("};
    public static String[] units = {" events ", " minutes "};
    public static String[] operators = {"\\+", "-", "\\*", "/", ","};
}
