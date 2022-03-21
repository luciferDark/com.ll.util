package com.ll.internet.models.onso;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

@Root(name = "article")
public class ChapterItem {
    @Path("h2/a")
    @Attribute(name = "href",required = false)
    public String href_fix;

    public ChapterItem(String href_fix) {
        this.href_fix = href_fix;
    }

    public String getHref_fix() {
        return href_fix;
    }

    public void setHref_fix(String href_fix) {
        this.href_fix = href_fix;
    }

    @Override
    public String toString() {
        return "ChapterItem{" +
                "href_fix='" + href_fix + '\'' +
                '}';
    }

}
