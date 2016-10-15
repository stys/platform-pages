package com.stys.platform.pages.markdown;

import com.stys.platform.pages.markdown.plugins.WikitablePlugin;
import com.stys.platform.pages.markdown.plugins.YoutubePlugin;
import org.markdown4j.Plugin;

import java.util.ArrayList;
import java.util.List;

public class DefaultMarkdownPluginsProvider implements MarkdownPluginsProvider {

    private List<Plugin> plugins;

    public DefaultMarkdownPluginsProvider() {
        plugins = new ArrayList<>();
        plugins.add(new YoutubePlugin("youtube"));
        plugins.add(new WikitablePlugin("wikitable"));
    }

    @Override
    public List<Plugin> plugins() {
        return this.plugins;
    }

}
