package com.hosmos.linkind.services;

import com.hosmos.linkind.models.Tag;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tagService")
public interface TagService {

    void save(String tag);

    Tag get(int id);

    List<Long> saveTags(String[] tags);

    List<Tag> getTags();

    void delete(String name);

    void delete(Long id);

    void deleteTags(String[] tags);

    void saveTagsRelations(List<Long> tagsIds, long linkId);
}
