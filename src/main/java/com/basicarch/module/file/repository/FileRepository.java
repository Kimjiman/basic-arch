package com.basicarch.module.file.repository;

import com.basicarch.module.file.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileInfo, Long> {
    List<FileInfo> findAllByIdIn(List<Long> ids);
    List<FileInfo> findByRefPathAndRefId(String refPath, Long refId);
    void deleteByRefPathAndRefId(String refPath, Long refId);
}
