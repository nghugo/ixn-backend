package com.hng.ixn.content;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/content")
@AllArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @GetMapping("")
    public ResponseEntity<List<Content>> getAllContent() {
        List<Content> allContent = contentService.getAllContent();
        return new ResponseEntity<>(allContent, HttpStatus.OK);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<Content>> getLatestContent() {
        List<Content> latestContent = contentService.getLatestContent();
        return new ResponseEntity<>(latestContent, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")  // only admins can post content
    @PostMapping("")
    public ResponseEntity<List<Content>> createContent(@RequestBody List<ContentDTO> contentDTOs) {
        List<Content> createdContent = contentService.saveMultipleContent(contentDTOs);
        return new ResponseEntity<>(createdContent, HttpStatus.CREATED);
    }

}
