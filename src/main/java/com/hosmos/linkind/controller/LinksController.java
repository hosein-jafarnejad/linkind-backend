package com.hosmos.linkind.controller;

import com.hosmos.linkind.models.Link;
import com.hosmos.linkind.services.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/links")
public class LinksController {

    private LinkService linkService;

    @Autowired
    public void setLinkService(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping
    public ResponseEntity saveLink (@RequestParam("address") String address) {
        linkService.save(address);

        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping("/{id}")
    public void deleteLink (@PathVariable(name = "id") int id) {
        linkService.delete(id);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Link getLink (@PathVariable(name = "id") int id) {
        Link link = linkService.get(id);
        System.out.println("------------------------------------ LinksController " + link.getUrl());

        return link;
    }

    @GetMapping(value = "/address/{shortUrl}")
    public String getLinkAddress(@PathVariable(name = "shortUrl") String shortUrl) {
        return linkService.get(shortUrl);
    }

    @GetMapping(value = {"/{page}/{rowsPerPage}", "/"})
    public List<Link> getLinks (@PathVariable(name = "page") Optional<Integer> page, @PathVariable(name = "rowsPerPage") Optional<Integer> rowsPerPage) {
        if (page.isPresent() && rowsPerPage.isPresent()) {
            return linkService.getLinks(page.get(), rowsPerPage.get());
        } else {
            return linkService.getLinks(1, 10);
        }
    }

}
