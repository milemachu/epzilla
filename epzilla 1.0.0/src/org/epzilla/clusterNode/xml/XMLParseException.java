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
package org.epzilla.clusterNode.xml;

/**
 * An net.epzilla.clusterNode.XMLParseException is thrown when an error occures while parsing an XML
 * string.
 * <p/>
 * $Revision: 1.1.1.1 $<BR>
 * $Date: 2004/06/24 10:37:31 $<P>
 *
 * @author Marc De Scheemaecker
 * @version $Name:  $, $Revision: 1.1.1.1 $
 */
public class XMLParseException
        extends RuntimeException {

    /**
     * Indicates that no line number has been associated with this exception.
     */
    public static final int NO_LINE = -1;


    /**
     * The line number in the source code where the error occurred, or
     * <code>NO_LINE</code> if the line number is unknown.
     * <p/>
     * <dl><dt><b>Invariants:</b></dt><dd>
     * <ul><li><code>lineNr &gt 0 || lineNr == NO_LINE</code>
     * </ul></dd></dl>
     */
    private int lineNr;


    /**
     * Creates an exception.
     *
     * @param name    The name of the element where the error is located.
     * @param message A message describing what went wrong.
     *                <p/>
     *                </dl><dl><dt><b>Preconditions:</b></dt><dd>
     *                <ul><li><code>message != null</code>
     *                </ul></dd></dl>
     *                <p/>
     *                <dl><dt><b>Postconditions:</b></dt><dd>
     *                <ul><li>getLineNr() => NO_LINE
     *                </ul></dd></dl><dl>
     */
    public XMLParseException(String name,
                             String message) {
        super("XML Parse Exception during parsing of "
                + ((name == null) ? "the XML definition"
                : ("a " + name + " element"))
                + ": " + message);
        this.lineNr = XMLParseException.NO_LINE;
    }


    /**
     * Creates an exception.
     *
     * @param name    The name of the element where the error is located.
     * @param lineNr  The number of the line in the input.
     * @param message A message describing what went wrong.
     *                <p/>
     *                </dl><dl><dt><b>Preconditions:</b></dt><dd>
     *                <ul><li><code>message != null</code>
     *                <li><code>lineNr &gt; 0</code>
     *                </ul></dd></dl>
     *                <p/>
     *                <dl><dt><b>Postconditions:</b></dt><dd>
     *                <ul><li>getLineNr() => lineNr
     *                </ul></dd></dl><dl>
     */
    public XMLParseException(String name,
                             int lineNr,
                             String message) {
        super("XML Parse Exception during parsing of "
                + ((name == null) ? "the XML definition"
                : ("a " + name + " element"))
                + " at line " + lineNr + ": " + message);
        this.lineNr = lineNr;
    }


    /**
     * Where the error occurred, or <code>NO_LINE</code> if the line number is
     * unknown.
     */
    public int getLineNr() {
        return this.lineNr;
    }

}
