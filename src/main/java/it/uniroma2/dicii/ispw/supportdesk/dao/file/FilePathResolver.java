package it.uniroma2.dicii.ispw.supportdesk.dao.file;

import java.io.File;

final class FilePathResolver {

    private static final String DATA_DIR_PROP = "supportdesk.data.dir";
    private static final String DEFAULT_DIR   = "data";

    private FilePathResolver() {}

    static String resolve(String filename) {
        String dir = System.getProperty(DATA_DIR_PROP, DEFAULT_DIR);
        File dataDir = new File(dir);
        if (!dataDir.exists()) dataDir.mkdirs();
        return dir + File.separator + filename;
    }
}
