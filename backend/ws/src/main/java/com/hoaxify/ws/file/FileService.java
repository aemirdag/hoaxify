package com.hoaxify.ws.file;

import com.hoaxify.ws.configuration.HoaxifyProperties;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileService {
    private final HoaxifyProperties hoaxifyProperties;
    private final Tika tika = new Tika();

    @Autowired
    public FileService(HoaxifyProperties hoaxifyProperties) {
        this.hoaxifyProperties = hoaxifyProperties;
    }

    public String saveBase64StringAsFile(String image) {
        String filename = UUID.randomUUID().toString();
        Path path = Paths.get(hoaxifyProperties.getStorage().getRoot(), hoaxifyProperties.getStorage().getProfile(),
                filename);

        try (OutputStream outputStream = new FileOutputStream(path.toFile())) {
            outputStream.write(decodeImage(image));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return filename;
    }

    public String detectType(String value) {
        return tika.detect(decodeImage(value));
    }

    public void deleteProfileImage(String image) {
        if (Objects.isNull(image)) {
            return;
        }

        Path path = getProfileImagePath(image);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] decodeImage(String encodedImage) {
        return Base64.getDecoder().decode(encodedImage.split(",")[1]);
    }

    private Path getProfileImagePath(String filename) {
        return Paths.get(hoaxifyProperties.getStorage().getRoot(), hoaxifyProperties.getStorage().getProfile(), filename);
    }
}
