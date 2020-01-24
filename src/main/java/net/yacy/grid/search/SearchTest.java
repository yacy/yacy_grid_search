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
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONArray;
import org.json.JSONObject;

import net.yacy.grid.http.ClientConnection;

/**
 * search test class
 * call i.e. with parameter localhost:8800 or localhost:8100
 */
public class SearchTest {


    private static Random random = new Random();

    private String address;
    private Set<String> wordcorpus;
    private int maxwords;
    private AtomicInteger threads;

    public SearchTest(String address, int maxwords) {
        this.address = address;
        this.wordcorpus = ConcurrentHashMap.newKeySet();
        this.maxwords = maxwords;
        this.threads = new AtomicInteger(0);
    }

    public String searchURL(String query) {
        return "http://" + this.address + "/yacy/grid/mcp/index/yacysearch.json?query=" + query;
    }

    public List<String> getTexts(String query) {
        ArrayList<String> list = new ArrayList<>();
        if (!"*".equals(query)) try {query = URLEncoder.encode(query, "UTF-8");} catch (UnsupportedEncodingException e) {}
        try {
            JSONObject json = ClientConnection.loadJSONObject(searchURL(query));
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

    public void storeWords(List<String> l) {
        for (String s: l) {
            String[] a = s.toLowerCase().replaceAll("[-–+.^:,\"'|()?@/]*°%","").split(" ");
            for (String b: a) if (b.length() > 1) this.wordcorpus.add(b);
        }
    }

    public List<String> queries(int count) {
        List<String> wc = new ArrayList<>();
        for (String s: this.wordcorpus) wc.add(s);
        List<String> l = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int words = random.nextInt(this.maxwords) + 1;
            StringBuilder s = new StringBuilder();
            for (int j = 0; j < words; j++) {
                s.append(wc.get(random.nextInt(wc.size()))).append(" ");
            }
            l.add(s.toString().trim());
        }
        return l;
    }

    public int threads() {
        return this.threads.get();
    }

    public class Request extends Thread {

        private String query;
        public Request(String query) {
            this.query = query;
        }

        public void run() {
            try {
                SearchTest.this.threads.incrementAndGet();
                storeWords(getTexts(this.query));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                SearchTest.this.threads.decrementAndGet();
            }
        }
    }

    public void runTest(String query) {
        new Request(query).start();
    }

    public static void main(String[] args) {
        String address = args[0]; // host:port
        String pausetimes = args.length > 1 ? args[1] : "500";
        int pausetime = Integer.parseInt(pausetimes);

        SearchTest st = new SearchTest(address, 3);
        st.storeWords(st.getTexts("*"));
        List<String> queries = new ArrayList<>();

        while (true) {
            queries = st.queries(10);
            for (String query: queries) {
                System.out.println("testing with '" + query + "', " + st.threads() + " +1 threads");
                st.runTest(query);
                try {
                    while (st.threads() > 100) Thread.sleep(1000);
                    Thread.sleep(random.nextInt(pausetime * 2));
                } catch (InterruptedException e) {}
            }
        }
    }

}
