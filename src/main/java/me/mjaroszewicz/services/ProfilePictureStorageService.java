package me.mjaroszewicz.services;


import me.mjaroszewicz.config.ProfilePictureStorageProps;
import me.mjaroszewicz.exceptions.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

@Service
public class ProfilePictureStorageService {

    private final Path root;

    private final static Logger log = LoggerFactory.getLogger(ProfilePictureStorageService.class);

    @Autowired
    public ProfilePictureStorageService(ProfilePictureStorageProps properties){
        this.root = Paths.get(properties.getLocation());
    }

    public void storeProfilePic(MultipartFile file, String username) throws StorageException{
        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        try{
            if(file.isEmpty())
                throw new StorageException("File is empty: " + filename);

            //relative path check
            if(filename.contains(".."))
                throw new StorageException("Cannot storeProfilePic file with relative path: " + filename);

            Files.copy(file.getInputStream(), this.root.resolve(username), StandardCopyOption.REPLACE_EXISTING);

        }catch(IOException | StorageException ex){
            throw new StorageException("Failed to storeProfilePic: " + filename);
        }
    }

    public Path load(String filename){
        return root.resolve(filename);
    }

    public Resource loadAsResource(String filename) throws StorageException {

        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable())
                return resource;
            else
                throw new StorageException("Could not read file: " + filename);

        } catch (MalformedURLException mux) {
            throw new StorageException("Could not read file: " + filename);
        }
    }

    public void deleteAll(){
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @PostConstruct
    private void init() throws StorageException{
        try{
            Files.createDirectories(root);
        }catch(FileAlreadyExistsException faex){
            log.debug("Picture storage directory already exists. ");
        }catch(IOException ioex){
            throw new StorageException("Could not initialize storage. ");
        }
    }

}
