package com.hosmos.linkind.controller;

import com.hosmos.linkind.models.IpDetail;
import com.hosmos.linkind.models.Link;
import com.hosmos.linkind.services.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity saveLink(@RequestBody Link link) {
        linkService.save(link);

        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping("/{id}")
    public void deleteLink(@PathVariable(name = "id") int id) {
        linkService.delete(id);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Link getLink(@PathVariable(name = "id") int id) {
        return linkService.get(id);

    }

    @GetMapping(value = "/address/{shortUrl}")
    public String getLinkAddress(@PathVariable(name = "shortUrl") String shortUrl) {
        return linkService.get(shortUrl);
    }

    @GetMapping(value = {"/{page}/{rowsPerPage}", "/"})
    public List<Link> getLinks(@PathVariable(name = "page") Optional<Integer> page, @PathVariable(name = "rowsPerPage") Optional<Integer> rowsPerPage) {
        if (page.isPresent() && rowsPerPage.isPresent()) {
            return linkService.getLinks(page.get(), rowsPerPage.get());
        } else {
            return linkService.getLinks(1, 10);
        }
    }

    @GetMapping("/visit/{shortUrl}")
    public String visit(@PathVariable(name = "shortUrl") String shortUrl, HttpServletRequest request) {
        return linkService.visit(shortUrl, request.getRemoteAddr(), request.getHeader("User-Agent"));
    }


    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        System.out.println(restTemplate.getForObject("http://ip-api.com/json/24.48.0.1", IpDetail.class).toString());
    }
}
