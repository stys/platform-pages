package com.stys.platform.pages.markdown;

import org.markdown4j.Plugin;

import java.util.List;

public interface MarkdownPluginsProvider {

    public List<Plugin> plugins();

}
