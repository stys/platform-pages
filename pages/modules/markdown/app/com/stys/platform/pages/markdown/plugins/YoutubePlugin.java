package com.stys.platform.pages.markdown.plugins;

import org.markdown4j.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Markdown4j plugin to automatically generate Youtube embeds. 
 * Usage:
 * %%% youtube width=420 height=315
 * https://www.youtube.com/watch?v=PuM34db3Jn4
 * https://youtu.be/PuM34db3Jn4
 * %%%
 */
public class YoutubePlugin extends Plugin {
    
    private static Pattern watchPattern = Pattern.compile("youtube.com/watch\\?v=([A-Za-z0-9]+)");
    private static Pattern shortenedPattern = Pattern.compile("youtu.be/([A-Za-z0-9]+)");
    private static Pattern embedPattern = Pattern.compile("youtube.com/embed/([A-Za-z0-9]+)");
    
    public YoutubePlugin(String id) {
        super(id);
    }

    @Override
    public void emit(StringBuilder out, List<String> lines, Map<String, String> params) {

        // Get snippet parameters
        int width = 420;
        String w = params.get("width");
        if (null != w) {
            width = Integer.parseInt(w);
        }
        int height = 315;
        String h = params.get("height");
        if (null != h) {
            height = Integer.parseInt(h);
        }
        
        // Patterns 
        List<Pattern> patterns = Arrays.asList(watchPattern, shortenedPattern, embedPattern);
        
        // Generate embed code
        for (String line : lines) {
            for (Pattern pattern : patterns) {
                Matcher m = pattern.matcher(line);
                if (m.find()) {
                    String src = m.group(1);
                    String embed = template(width, height, src);
                    out.append(embed);
                    break;
                }
            }
        }
        
    }
    
    public String template(int width, int height, String src) {
        return String.format(
            "<iframe width =\"%d\" height = \"%d\" src = \"https://www.youtube.com/embed/%s?rel=0\" frameborder = \"0\" allowfullscreen></iframe>",
            width, height, src
        );
    }
    
}
