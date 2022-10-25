package grabsky.configuration.tests.util;

import java.io.File;
import java.net.URL;

public class TestUtil {

    public static File getFileFromClassPath(final String name) {
        final URL url = TestUtil.class.getClassLoader().getResource(name);
        return (url != null) ? new File(url.getPath()) : null;
    }

}
