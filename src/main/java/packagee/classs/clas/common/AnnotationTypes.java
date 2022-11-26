package packagee.classs.clas.common;

import packagee.classs.clas.inteface.ClassPreamble;
import packagee.classs.clas.inteface.Schedules;

import java.lang.annotation.Repeatable;

public class AnnotationTypes {
    @ClassPreamble(author = "John Doe", date = "3/17/2002", currentRevision = 6, lastModified = "4/12/2004", lastModifiedBy = "Jane Doe",
            // Note array notation
            reviewers = {"Alice", "Bob", "Cindy"})

    // use a deprecated method and tell
    // compiler not to generate a warning
    @SuppressWarnings("unchecked")
    void useDeprecatedMethod() {
        // deprecation warning
        // - suppressed
        deprecatedMethod();
    }

    // Javadoc comment follow
    /**
     * @deprecated explanation of why it was deprecated
     */
    @Deprecated
    static void deprecatedMethod() {
    }

    @Repeatable(value = Schedules.class)
    public @interface Schedule {
        String dayOfMonth() default "first";
        String dayOfWeek() default "Mon";
        int hour() default 12;
    }

    private AnnotationTypes objectOne;
}
