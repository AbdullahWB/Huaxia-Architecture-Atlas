package com.huaxia.atlas.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class ImageStorageService {

    private static final Set<String> ALLOWED_EXT = Set.of("jpg", "jpeg", "png", "webp");

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    /**
     * Saves an image to the upload directory and returns the public URL path (e.g.,
     * /uploads/<file>).
     */
    public String saveCoverImage(MultipartFile file) throws IOException {
        return saveImage(file);
    }

    public String saveAvatarImage(MultipartFile file) throws IOException {
        return saveImage(file);
    }

    private String saveImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String original = file.getOriginalFilename();
        String cleanName = StringUtils.cleanPath(original == null ? "" : original);
        String ext = getExtension(cleanName);

        if (ext.isEmpty() || !ALLOWED_EXT.contains(ext)) {
            throw new IOException("Unsupported image type. Allowed: " + ALLOWED_EXT);
        }

        // Optional: basic content-type check (not fully secure alone)
        String contentType = file.getContentType();
        if (contentType != null && !contentType.toLowerCase(Locale.ROOT).startsWith("image/")) {
            throw new IOException("Invalid content type: " + contentType);
        }

        Path dir = Path.of(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(dir);

        String filename = UUID.randomUUID() + "." + ext;
        Path target = dir.resolve(filename);

        // Prevent path traversal
        if (!target.normalize().startsWith(dir)) {
            throw new IOException("Invalid upload path.");
        }

        try (var in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }

        return "/uploads/" + filename;
    }

    private String getExtension(String filename) {
        if (filename == null)
            return "";
        int dot = filename.lastIndexOf('.');
        if (dot < 0 || dot == filename.length() - 1)
            return "";
        return filename.substring(dot + 1).toLowerCase(Locale.ROOT);
    }
}
