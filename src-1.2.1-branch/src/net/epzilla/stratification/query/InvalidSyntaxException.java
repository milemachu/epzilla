package net.epzilla.stratification.query;

/**
 *thrown when a query with invalid syntax is added.
 */
public class InvalidSyntaxException extends Exception {

    public String toString() {
        return "InvalidSyntaxException: CEP query is invalid.";    //To change body of overridden methods use File | Settings | File Templates.
    }
}
