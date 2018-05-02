import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

public class WebCrawler {
    private static void crawling(String address, int level, int levelLimit, ArrayList<String> links, PrintWriter fileWriter) throws IOException {
        links.add(address);
        StringBuilder output = new StringBuilder();
        output.append(new String(new char[level]).replace("\0", " "));
        output.append("Level: ").append(level).append(" address: ").append(address);
        System.out.println(output);
        fileWriter.println(output);
        URL url = new URL(address);
        try (BufferedReader urlContentStream = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String inputLine;
            while ((inputLine = urlContentStream.readLine()) != null) {
                Pattern htmlPagePattern = Pattern.compile("((http|ftp|https)://)[a-zA-Z0-9._-]+[a-zA-Z-._~!$&'),(*+;=:@%/]+(\\.html)");
                Matcher matcher = htmlPagePattern.matcher(inputLine);
                while (matcher.find()) {
                    if (!sitesResearch(matcher.group(), links) && level < levelLimit) {
                        crawling(matcher.group(), level + 1, levelLimit, links, fileWriter);
                    }
                }
            }
        }
    }

    private static boolean sitesResearch(String link, ArrayList<String> links) {
        boolean check = false;
        for (String url : links)
            if (link.equals(url)) {
                check = true;
                break;
            }
        return check;
    }

    public static void main(String[] args) throws IOException {
        final String url = "http://csharp.net-informations.com/collection/csharp-array.htm";
        final int levelLimit = 10;
        ArrayList<String> urls = new ArrayList<>();
        try (
                PrintWriter writer = new PrintWriter("crawled-urls.txt", "UTF-8")) {
            crawling(url, 0, levelLimit, urls, writer);
        }
    }
}