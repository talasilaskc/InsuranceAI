package com.project.InsureAI.service;

import com.project.InsureAI.exception.BusinessException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

@Service
public class LocalFileStorageService {

    private final Path rootPath = Paths.get("uploads/claims");

    public LocalFileStorageService() {
        try {
            Files.createDirectories(rootPath);
        } catch (IOException e) {
            throw new BusinessException("Could not initialize storage folder");
        }
    }

    public String storeFile(Long claimId, MultipartFile file) {

        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null) {
            throw new BusinessException("Invalid file name");
        }

        // sanitize filename
        originalFileName = originalFileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

        String uniqueName =
                claimId + "_" + System.currentTimeMillis() + "_" + originalFileName;

        try {
            Path targetLocation = rootPath.resolve(uniqueName);
            Files.copy(file.getInputStream(), targetLocation,
                    StandardCopyOption.REPLACE_EXISTING);
            return uniqueName;
        } catch (IOException e) {
            throw new BusinessException("Failed to store file");
        }
    }

    public Resource loadFile(String storedFileName) {

        try {
            Path filePath = rootPath.resolve(storedFileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new BusinessException("File not found");
            }

            return resource;
        } catch (MalformedURLException e) {
            throw new BusinessException("Invalid file path");
        }
    }
}