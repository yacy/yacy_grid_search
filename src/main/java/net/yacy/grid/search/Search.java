/**
 *  Search Client
 *  Copyright 05.11.2018 by Michael Peter Christen, @0rb1t3r
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program in the file lgpl21.txt
 *  If not, see <http://www.gnu.org/licenses/>.
 */

package net.yacy.grid.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.Servlet;

import org.apache.log4j.Level;

import net.yacy.grid.YaCyServices;
import net.yacy.grid.mcp.Data;
import net.yacy.grid.mcp.MCP;
import net.yacy.grid.mcp.Service;
import net.yacy.grid.tools.GitTool;

/**
 * The Search Client main class
 * 
 * You can search at the following API:
 * http://localhost:8800/yacy/grid/mcp/index/yacysearch.json?query=*
 * http://localhost:8800/yacy/grid/mcp/index/gsasearch.xml?q=*
 * 
 * performance debugging:
 * http://localhost:8800/yacy/grid/mcp/info/threaddump.txt
 * http://localhost:8800/yacy/grid/mcp/info/threaddump.txt?count=100 *
 */
public class Search {

    private final static YaCyServices SEARCH_SERVICE = YaCyServices.aggregation; // check with http://localhost:8800/yacy/grid/mcp/status.json
    private final static String DATA_PATH = "data";
 
    public static void main(String[] args) {
        // initialize environment variables
        List<Class<? extends Servlet>> services = new ArrayList<>();
        services.addAll(Arrays.asList(MCP.MCP_SERVICES)); // the search services are in the MCP services embedded
        Service.initEnvironment(SEARCH_SERVICE, services, DATA_PATH, false);
        Data.logger.getLoggerRepository().setThreshold(Level.INFO);

        // start server
        Data.logger.info("Search.main started Search");
        Data.logger.info(new GitTool().toString());
        Service.runService(null);
    }

}
