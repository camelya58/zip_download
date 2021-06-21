package com.github.camelya58.zip_download.controller;

import com.github.camelya58.zip_download.util.ZipUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ZipController {

    @GetMapping(value = "/getZip", produces = {"application/x-zip-compressed"})
    public ResponseEntity<byte[]> getZip() {
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type")
                .header("Content-Disposition", "attachment; filename=attachment.zip")
                .contentType(new MediaType("application", "x-zip-compressed"))
                .body(ZipUtil.createZip().toByteArray());
    }
}
