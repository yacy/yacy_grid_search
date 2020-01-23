/**
 *  Search Test
 *  Copyright 23.01.2020 by Michael Peter Christen, @0rb1t3r
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import net.yacy.grid.http.ClientConnection;

/**
 * search test class
 * call i.e. with parameter localhost:8800 or localhost:8100
 */
public class SearchTest {

    private static Random random = new Random();

    public static String searchURL(String address, String query) {
        String url = "http://" + address + "/yacy/grid/mcp/index/yacysearch.json?query=" + query;
        return url;
    }

    public static List<String> getTexts(String address, String query) {
        ArrayList<String> list = new ArrayList<>();
        if (!"*".equals(query)) try {query = URLEncoder.encode(query, "UTF-8");} catch (UnsupportedEncodingException e) {}
        try {
            JSONObject json = ClientConnection.loadJSONObject(searchURL(address, "*"));
            //System.out.println(json.toString(2));
            JSONObject channel = json.getJSONArray("channels").getJSONObject(0);
            JSONArray items = channel.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                list.add(item.getString("title"));
                list.add(item.getString("description"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void words(Set<String> h, List<String> l) {
        for (String s: l) {
            String[] a = s.toLowerCase().replaceAll("[-–+.^:,\"'|]","").split(" ");
            for (String b: a) if (b.length() > 1) h.add(b);
        }
    }

    public static List<String> queries(Set<String> h, int count, int maxwords) {
        List<String> wordcorpus = new ArrayList<>();
        for (String s: h) wordcorpus.add(s);
        List<String> l = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int words = random.nextInt(maxwords) + 1;
            StringBuilder s = new StringBuilder();
            for (int j = 0; j < words; j++) {
                s.append(wordcorpus.get(random.nextInt(wordcorpus.size()))).append(" ");
            }
            l.add(s.toString().trim());
        }
        return l;
    }

    public static void main(String[] args) {
        String address = args[0]; // host:port
        String pausetimes = args.length > 1 ? args[1] : "500";
        long pausetime = Long.parseLong(pausetimes);
        List<String> l = getTexts(address, "*");
        Set<String> h = new HashSet<>();
        words(h, l);

        //for (String s: h) System.out.println(s);
        List<String> q = queries(h, 10, 3);
        for (String s: q) System.out.println(s);
    }

}
