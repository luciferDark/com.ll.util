package com.ll.internet.models.onso;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

@Root(name = "html")
public class ChapterBody {
    @Element(name = "body",required = false)
    @Path(value = "body")
    public org.jsoup.nodes.Element chapterBody;
    @Element(name = "head",required = false)
    public org.jsoup.nodes.Element head;
}
