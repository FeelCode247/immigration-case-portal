package com.caseportal.util;

import jakarta.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    public static File getOrCreateUploadDir(ServletContext context) {
        String configured = context.getInitParameter("uploadDir");
        String base = System.getProperty("catalina.base", System.getProperty("java.io.tmpdir"));
        String path = configured != null ? configured.replace("${catalina.base}", base) : base + File.separator + "case-portal-uploads";
        File dir = new File(path);
        if (!dir.exists()) dir.mkdirs();
        return dir;
    }

    public static File saveStreamToFile(InputStream in, File targetDir, String fileName) throws IOException {
        targetDir.mkdirs();
        File out = uniqueFile(targetDir, sanitize(fileName));
        try (FileOutputStream fos = new FileOutputStream(out)) {
            in.transferTo(fos);
        }
        return out;
    }
    
 // add to com.caseportal.util.FileUploadUtil
    public static File uniquify(File file) {
        String name = file.getName();
        String parent = file.getParent();
        String baseName = name;
        String extension = "";

        int dotIndex = name.lastIndexOf('.');
        if (dotIndex > 0) {
            baseName = name.substring(0, dotIndex);
            extension = name.substring(dotIndex);
        }

        File candidate = file;
        int count = 1;
        while (candidate.exists()) {
            String newName = baseName + "_" + count + extension;
            candidate = new File(parent, newName);
            count++;
        }
        return candidate;
    }

    
    private static String sanitize(String name) {
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private static File uniqueFile(File dir, String baseName) throws IOException {
        File f = new File(dir, baseName);
        String name = baseName;
        String ext = "";
        int dot = baseName.lastIndexOf('.');
        if (dot > 0) {
            name = baseName.substring(0, dot);
            ext = baseName.substring(dot);
        }
        int i = 1;
        while (f.exists()) {
            f = new File(dir, name + "_" + i + ext);
            i++;
        }
        return f;
    }
}
