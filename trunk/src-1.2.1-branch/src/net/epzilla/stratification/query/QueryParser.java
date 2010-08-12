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
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Feb 24, 2010
 * Time: 1:46:14 PM
 * To change this template use File | Settings | File Templates.
 */
public interface QueryParser {

    /**
     * implementations are expected to parse the query and return a Query object with all desired information.
     *
     * @param query
     * @return
     * @throws InvalidSyntaxException if the query is syntactically wrong
     */
    public Query parseString(String query) throws InvalidSyntaxException;

}
