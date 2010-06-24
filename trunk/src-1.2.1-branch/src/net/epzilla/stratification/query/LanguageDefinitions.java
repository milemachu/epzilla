package net.epzilla.stratification.query;

/**
 * The common keywords in query language.
 */
public class LanguageDefinitions {

    public static String[] keywords = {"SELECT ", "COUNT ", "FINDOCCURRENCES ", " OUTPUT ", " RETAIN ", " OUTPUT AS ", " WHERE "};
    public static String[] functions = {"avg", "sum", "total", "max", "min"};// to identify operations directly in a query.
    public static String[] functionTokens = {"avg[\\s]*\\(", "sum[\\s]*\\(", "total[\\s]*\\(", "max[\\s]*\\(", "min[\\s]*\\("};
    public static String[] units = {" EVENTS ", " MINUTES "};
    public static String[] operators = {"\\+", "-", "\\*", "/", ","};
}
