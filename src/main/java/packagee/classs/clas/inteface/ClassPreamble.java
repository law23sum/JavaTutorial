package packagee.classs.clas.inteface;

import java.lang.annotation.Documented;

@Documented
public @interface ClassPreamble {
    String author();
    String date();
    int currentRevision();
    String lastModified();
    String lastModifiedBy();
    String[] reviewers();
}
