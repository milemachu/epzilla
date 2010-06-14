package net.epzilla.stratification.query;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: May 12, 2010
 * Time: 10:56:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class test {

    public static void main(String[] args) throws InvalidSyntaxException {
        QueryParser p = new BasicQueryParser();
        p.parseString("SELECT avg(StockTrades.price), min(StockPrices.price) RETAIN 10 EVENTS OUTPUT StkTrades.avgprice, StkTrades.minprice;");

        


    }



}
