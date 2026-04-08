package com.basicarch.module.file.service;

import com.basicarch.base.exception.CustomException;
import com.basicarch.base.exception.SystemErrorCode;
import com.basicarch.base.utils.CollectionUtils;
import com.basicarch.base.utils.DateUtils;
import com.basicarch.base.utils.NetworkUtils;
import com.basicarch.module.file.entity.FileInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileStorageService {
    @Value("${file.store.path}")
    private String storePath;

    /**
     * @param mf 파일
     * @return
     * @throws IOException
     */
    public FileInfo upload(MultipartFile mf) {
        if (mf == null || mf.isEmpty()) {
            throw new IllegalArgumentException("파일이 존재하지 않습니다.");
        }

        String savePath = DateUtils.localDateToString(LocalDate.now(), "yyyyMM");
        File dir = new File(String.format("%s/%s", storePath, savePath));

        if (!dir.mkdirs()) {
            throw new IllegalArgumentException("디렉터리가 생성에 실패하였습니다.");
        }

        String oriName = mf.getOriginalFilename();
        String extension = extractSafeExtension(Objects.requireNonNull(oriName));
        String newName = String.format("%s.%s", UUID.randomUUID(), extension);

        try {
            FileCopyUtils.copy(mf.getInputStream(), new FileOutputStream(new File(dir, newName)));
            return FileInfo.builder()
                    .oriName(oriName)
                    .newName(newName)
                    .savePath(savePath)
                    .size(mf.getSize())
                    .type(mf.getContentType())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("파일 저장에 실패하였습니다.");
        }
    }

    /**
     * @param mfs 파일리스트
     * @return
     * @throws IOException
     */
    public List<FileInfo> upload(List<MultipartFile> mfs) {
        if (CollectionUtils.isEmpty(mfs)) {
            throw new IllegalArgumentException("파일 리스트가 비어있습니다.");
        }
        return mfs.stream()
                .map(this::upload)
                .collect(Collectors.toList());
    }


    /**
     * @param request
     * @param response
     * @param fileInfo
     */
    public void download(HttpServletRequest request, HttpServletResponse response, FileInfo fileInfo) {
        if (fileInfo == null) {
            throw new IllegalArgumentException("다운로드할 파일 정보가 없습니다.");
        }

        String filePath = String.format("%s/%s", storePath, fileInfo.getSavePath());
        File file = new File(filePath, fileInfo.getNewName());

        if (!file.exists()) {
            throw new IllegalArgumentException("파일이 존재하지 않습니다.");
        }

        try (InputStream is = new FileInputStream(file); OutputStream os = response.getOutputStream()) {

            response.setContentType(fileInfo.getType());
            response.setContentLength((int) file.length());

            this.setDisposition(fileInfo.getOriName(), NetworkUtils.getBrowser(request), response);

            FileCopyUtils.copy(is, os);

        } catch (IOException e) {
            throw new CustomException(SystemErrorCode.FILE_ERROR, "파일 다운로드 중 오류가 발생했습니다.");
        }
    }


    /**
     * 파일 읽기
     *
     * @param response
     * @param fileInfo
     * @return
     */
    public FileInfo readFile(HttpServletResponse response, FileInfo fileInfo) {
        if (fileInfo == null) {
            throw new IllegalArgumentException("읽을 파일 정보가 없습니다.");
        }

        String filePath = String.format("%s/%s", storePath, fileInfo.getSavePath());
        File file = new File(filePath, fileInfo.getNewName());

        if (!file.exists()) {
            throw new IllegalArgumentException("파일이 존재하지 않습니다.");
        }

        try (InputStream is = new FileInputStream(file); OutputStream os = response.getOutputStream()) {

            response.setContentType(fileInfo.getType());
            response.setContentLength((int) file.length());

            FileCopyUtils.copy(is, os);

        } catch (IOException e) {
            throw new CustomException(SystemErrorCode.FILE_ERROR, "파일 읽기 중 오류가 발생했습니다.");
        }

        return fileInfo;
    }

    /**
     * 파일삭제
     * @param fileInfo
     */
    public void delete(FileInfo fileInfo) {
        if (fileInfo == null) {
            throw new IllegalArgumentException("삭제할 파일 정보가 없습니다.");
        }

        String filePath = String.format("%s/%s", storePath, fileInfo.getSavePath());
        File file = new File(filePath, fileInfo.getNewName());

        if (file.exists() && file.delete()) {
            log.info("파일을 삭제했습니다: {}", file.getAbsolutePath());
        } else {
            log.warn("파일을 삭제할 수 없습니다: {}", file.getAbsolutePath());
        }
    }

    private void setDisposition(String fileName, String browser, HttpServletResponse response) {
        String dispositionPrefix = "attachment; filename=";
        String encodedFilename;

        try {
            if (browser != null && (browser.equalsIgnoreCase("MSIE")
                    || browser.equalsIgnoreCase("Trident")
                    || browser.equalsIgnoreCase("Edge"))) {
                encodedFilename = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            } else {
                encodedFilename = new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO_8859_1");
            }

            response.setHeader("Content-Disposition", dispositionPrefix + encodedFilename);
        } catch (UnsupportedEncodingException e) {
            throw new CustomException(SystemErrorCode.FILE_ERROR, "파일명 인코딩에 실패했습니다.");
        }
    }

    private String extractSafeExtension(String originalFilename) {
        List<String> unsafeExtList = List.of("jsp", "php");
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

        return unsafeExtList.stream()
                .filter(it -> it.equals(extension))
                .findFirst()
                .orElse("txt");
    }
}
