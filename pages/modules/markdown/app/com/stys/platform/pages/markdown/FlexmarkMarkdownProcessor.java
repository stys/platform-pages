package com.stys.platform.pages.markdown;

import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;

public class FlexmarkMarkdownProcessor implements MarkdownProcessor {
    private Parser parser;
    private HtmlRenderer renderer;

    public FlexmarkMarkdownProcessor() {
        MutableDataSet options = new MutableDataSet();
        parser = Parser.builder(options).build();
        renderer = HtmlRenderer.builder(options).build();
    }

    @Override
    public String render(String source) {
        Node document = parser.parse(source);
        return renderer.render(document);
    }
}
