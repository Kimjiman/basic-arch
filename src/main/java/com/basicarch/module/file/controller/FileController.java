package com.basicarch.module.file.controller;

import com.basicarch.module.file.entity.FileInfo;
import com.basicarch.module.file.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Controller
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping
    @ResponseBody
    public List<FileInfo> get(@RequestParam(value = "ids[]") List<Long> ids) {
        return fileService.getList(ids);
    }

    @PostMapping(value = "/upload/{refPath}/{refId}")
    @ResponseBody
    public List<FileInfo> upload(List<MultipartFile> files,
                                 @PathVariable String refPath,
                                 @PathVariable Long refId) {
        if (files == null) return new ArrayList<>();

        List<CompletableFuture<FileInfo>> futureList = files.stream()
                .map(it -> CompletableFuture
                        .supplyAsync(() -> fileService.upload(it, refPath, refId))
                        .exceptionally(e -> null)
                )
                .toList();

        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();

        return futureList.stream()
                .map(it -> it.join())
                .filter(it -> it != null)
                .toList();
    }

    @GetMapping(value = "/download/{id}")
    public void download(@PathVariable("id") String strId, HttpServletRequest request, HttpServletResponse response) {
        try {
            FileInfo fileInfo = fileService.download(request, response, Long.valueOf(strId));
            log.info("download fileInfo: {}", fileInfo);
        } catch (Exception e) {
            log.error("파일 아이디 오류: {}", strId);
        }
    }

    @GetMapping(value = "/{id}")
    public void readFile(@PathVariable("id") String strId, HttpServletResponse response) {
        try {
            FileInfo fileInfo = fileService.readFile(response, Long.valueOf(strId));
            log.info("readFile fileInfo: {}", fileInfo);
        } catch (Exception e) {
            log.error("파일 아이디 오류: {}", strId);
        }
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public void delete(@PathVariable("id") String strId) {
        try {
            fileService.delete(Long.valueOf(strId));
        } catch (Exception e) {
            log.error("파일 아이디 오류: {}", strId);
        }
    }

    @DeleteMapping(value = "/ref/{refPath}/{refId}")
    @ResponseBody
    public void deleteByRef(@PathVariable("refPath") String refPath, @PathVariable("refId") Long refId) {
        try {
            fileService.deleteByRef(refPath, refId);
        } catch (Exception e) {
            log.error("ref error: refPath:{}, refId:{}", refPath, refId);
        }
    }
}

