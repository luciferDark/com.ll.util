package com.ll.internet.models.onso;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "body")
public class ChapterList {
    @ElementList(required = false,inline = true,entry = "article")
    @Path("section/div/div/article")
    public List<ChapterItem> list ;

}
