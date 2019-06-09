package com.hosmos.linkind.controller;

import com.hosmos.linkind.models.Tag;
import com.hosmos.linkind.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagsController {

    private TagService tagService;

    @Autowired
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public void save(@RequestParam("name") String tag) {
        tagService.save(tag);
    }

    @GetMapping
    public Tag get(@RequestParam("id") int id) {
        return tagService.get(id);
    }

    @PostMapping("/stream")
    public void saveTags(@RequestBody String[] tags) {
        tagService.saveTags(tags);
    }

    @GetMapping("/stream")
    public List<Tag> getTags() {
        return tagService.getTags();
    }

    @DeleteMapping
    public void delete(@RequestParam("name") String name) {
        tagService.delete(name);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") long id) {
        tagService.delete(id);
    }

    @DeleteMapping("/stream")
    public void deleteTags(@RequestBody String[] tags) {
        tagService.deleteTags(tags);
    }
}
