package com.stys.platform.pages.markdown.plugins;

import org.markdown4j.Plugin;

import java.util.List;
import java.util.Map;

/**
 * Plugin to render wiki table markup
 * http://en.wikipedia.org/wiki/Help:Table
 */
public class WikitablePlugin extends Plugin {

    public WikitablePlugin(String idPlugin) {
        super(idPlugin);
    }

    @Override
    public void emit(StringBuilder out, List<String> lines, Map<String, String> params) {
        
        Table table = null;
        
        try {

            for (String line : lines) {

                if (line.startsWith("{|")) {
                    table = new Table();
                    continue;
                }

                if (line.startsWith("|}")) {
                    out.append(table.close());
                    return;
                }
                
                if (line.startsWith("|+")) {
                    table.caption(line.substring(2));
                    continue;
                }

                if (line.startsWith("|-")) {
                    table.row();
                    continue;
                }

                if (line.startsWith("|")) {
                    table.cell(line.substring(1));
                    continue;
                }
                
            }

        } catch (Throwable ex) {
            return;
        }
                
    }
    
    private static class Table {
        
        private StringBuilder html;
        private int state = 0;

        public Table() {
            html = new StringBuilder();
            html.append(String.format("<table class=\"table\">"));
        }
        
        public void caption(String content) {
            html.append(String.format("<caption class=\"table__caption\">%s</caption>", content));
        }

        public void row() {
            if (state == 1) html.append("</tr>");
            html.append(String.format("<tr class=\"table__row>\">"));
            state = 1;
        } 
        
        public void cell(String content) {
           html.append(String.format("<td>%s</td>", content));
        }
        
        public String close() {
            html.append("</tr>");
            html.append("</table>");
            return html.toString();
        }        
    }
    
}
