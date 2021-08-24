package ru.otus.helpers;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public final class FileSystemHelper {

    private FileSystemHelper() {
    }

    public static String localFileNameOrResourceNameToFullPath(String fileOrResourceName) {
        String path = null;
        File file = new File(String.format("./%s", fileOrResourceName));
        if (file.exists()) {
            // path = URLDecoder.decode(file.toURI().getPath(), StandardCharsets.UTF_8);
            try {
                path = java.net.URLDecoder.decode(file.toURI().getPath(), StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                // not going to happen - value came from JDK's own StandardCharsets
            }
        }

        if (path == null) {
            System.out.println("Local file not found, looking into resources");
            path = Optional.ofNullable(FileSystemHelper.class.getClassLoader().getResource(fileOrResourceName))
                    .orElseThrow(() -> new RuntimeException(String.format("File \"%s\" not found", fileOrResourceName))).toExternalForm();

        }
        return path;
    }
}
