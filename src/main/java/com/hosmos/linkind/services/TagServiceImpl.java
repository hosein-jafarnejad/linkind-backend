package com.hosmos.linkind.services;

import com.hosmos.linkind.dao.TagMapper;
import com.hosmos.linkind.models.Tag;
import com.hosmos.linkind.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
@Component
public class TagServiceImpl implements TagService{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private TagMapper tagMapper;

    @Autowired
    public void setTagMapper(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @Override
    @Transactional
    public void save(String tag) {
        try {
            // TODO replace with spring security context id
            tagMapper.save(new Tag(tag, 1));
        } catch (DuplicateKeyException e) {
            if (e.getCause().getMessage().contains("U_TAG")) {
                logger.trace(String.format("DuplicateKeyException: u_tag. [tag_name: %s]", tag));
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            } else if (e.getCause().getMessage().contains("F_TAG_OWNER")) {
                // TODO replace with spring security context id
                logger.error(String.format("DataIntegrityViolationException: f_tag_owner. [owner_id: %s]", 1));
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Tag get(int id) {
        return tagMapper.get(id);
    }

    @Override
    @Transactional
    public List<Long> saveTags(String[] tags) {
        // TODO replace with spring security context id
        List<Tag> existingTags = tagMapper.selectExistingTags(StringUtils.arrayToInAcceptableString(tags), 1);

        logger.trace(String.format("Count of exist tags is %d", existingTags.size()));

        List<Long> newTagsId = new ArrayList<>();

        for (Tag tag: existingTags) {
            newTagsId.add(tag.getId());
        }

        // There is no new tags to insert
        if (existingTags.size() == tags.length) {
            return newTagsId;
        }

        for (String tag: tags) {
            boolean newTag = true;

            for (Tag extTag: existingTags) {
                if (tag.equals(extTag.getName())) {
                    newTag = false;
                }
            }

            if (newTag) {
                try {
                    // TODO replace with spring security context id
                    Tag t = new Tag(tag, 1);
                    tagMapper.save(t);
                    newTagsId.add(t.getId());
                } catch (Exception e) {
                    logger.error(String.format("Exception while insert %s", tag));
                }
            }
        }

        return newTagsId;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tag> getTags() {
        // TODO replace with spring security context id
        return tagMapper.getTags(1);
    }

    @Override
    @Transactional
    public void delete(String name) {
        // TODO replace with spring security context id
        if (tagMapper.deleteTag(name, 1) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void delete(Long id) {
        if (tagMapper.deleteTagById(id) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void deleteTags(String[] tags) {
        // TODO replace with spring security context id
        tagMapper.deleteTags(StringUtils.arrayToInAcceptableString(tags), 1);
    }

    @Override
    @Transactional
    public void saveTagsRelations(List<Long> tagsIds, long linkId) {
        System.out.println(tagsIds);
        System.out.println(linkId);
        for (Long tagId: tagsIds) {
            tagMapper.saveTagRelation(tagId, linkId);
        }
    }
}
