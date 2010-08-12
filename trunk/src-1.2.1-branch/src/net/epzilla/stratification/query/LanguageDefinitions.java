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
