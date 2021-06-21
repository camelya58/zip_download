package com.github.camelya58.zip_download.util;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class ZipUtil {

    public static String TEXT = "Some text in file.";

    public static ByteArrayOutputStream createZip() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ZipOutputStream zip = new ZipOutputStream(baos);
            ZipEntry entry = new ZipEntry("file.txt");
            zip.putNextEntry(entry);
            zip.write(TEXT.getBytes());
            zip.closeEntry();
            zip.close();
        } catch (Exception e) {
            log.error("error = {}, message = {}", e, e.getMessage());
        }
        return baos;
    }
}
