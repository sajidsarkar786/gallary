package com.myapp.gallary.controller;

import com.myapp.gallary.Entity.File;
import com.myapp.gallary.Entity.Folder;
import com.myapp.gallary.Entity.User;
import com.myapp.gallary.repository.FileRepository;
import com.myapp.gallary.repository.FolderRepository;
import com.myapp.gallary.repository.UserRepository;
import com.myapp.gallary.services.FileServices;
import com.myapp.gallary.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
public class FolderController {

    private static final Logger logger = LoggerFactory.getLogger(FolderController.class);

    @Autowired
    private FileServices fileServices;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    FileRepository fileRepository;

    @Autowired
    FolderRepository folderRepository;

    @GetMapping("/root")
    public String listUploadedFiles(Model model) throws Exception {
        User user = userService.getCurrentUser();
        Folder rootFolder = user.getRootFolder();
        model.addAttribute("currentFolder", rootFolder);
        model.addAttribute("childFolder",rootFolder.getChildFolder());
        model.addAttribute("fileList", rootFolder.getFileList());
        return "fileview";
    }

    @GetMapping("/folder/{id}")
    public String findPhotos(Model model, @PathVariable(name = "id") Long id) {
        Folder folder = folderRepository.getOne(Long.valueOf(id));
        model.addAttribute("currentFolder", folder);
        model.addAttribute("childFolder",folder.getChildFolder());
        model.addAttribute("fileList", folder.getFileList());
        return "fileview";
    }

    @PostMapping("/uploadFile/{id}")
    public String uploadFile(@RequestParam("file") MultipartFile file,@PathVariable(name = "id") Long currentFolderId, Model model) {
        String fileName = fileServices.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                                                            .path("/downloadFile/")
                                                            .path(fileName)
                                                            .toUriString();

        Folder currentFolder = folderRepository.getOne(currentFolderId);

        File uploadedFile = new File();
        uploadedFile.setName(fileName);
        uploadedFile.setFileUrl(fileDownloadUri);


        List<File> fileList = currentFolder.getFileList();
        fileList.add(uploadedFile);
        fileRepository.save(uploadedFile);
        folderRepository.save(currentFolder);
        model.addAttribute("currentFolder", currentFolder);
        model.addAttribute("childFolder",currentFolder.getChildFolder());
        model.addAttribute("fileList", currentFolder.getFileList());

        return "fileview";
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileServices.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                             .contentType(MediaType.parseMediaType(contentType))
                             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                             .body(resource);
    }

    @PostMapping(value = "/addFolder/{id}")
    public String addFolder(@PathVariable(name = "id") Long folderId, @RequestParam String folderName ,Model model){
        Folder folder = folderRepository.getOne(Long.valueOf(folderId));
        Folder newFolder = new Folder();
        newFolder.setName(folderName);
        newFolder.setParentFolder(folder);
        List<Folder> folderList = folder.getChildFolder();
        folderList.add(newFolder);
        folderRepository.save(newFolder);
        folderRepository.save(folder);
        model.addAttribute("currentFolder", folder);
        model.addAttribute("childFolder",folder.getChildFolder());
        model.addAttribute("fileList", folder.getFileList());
        return "fileview";
    }

}
