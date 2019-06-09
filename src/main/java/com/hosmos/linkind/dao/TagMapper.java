package com.hosmos.linkind.dao;

import com.hosmos.linkind.dao.dynamic.TagDynamicSql;
import com.hosmos.linkind.models.Tag;
import org.apache.ibatis.annotations.*;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

@Mapper
public interface TagMapper {

    @SelectKey(before = true, statement = "SELECT nextval('STAGS')", resultType = long.class, keyProperty = "id", keyColumn = "id")
    @Insert("INSERT INTO TAGS VALUES (#{id}, #{tag.name}, #{tag.owner})")
    void save(@Param("tag") Tag tag) throws DuplicateKeyException;

    @SelectProvider(type = TagDynamicSql.class, method = "selectExistingTags")
    List<Tag> selectExistingTags(@Param("tags") String tags, @Param("owner") long owner);

    @Select("SELECT * FROM TAGS WHERE ID = #{id}")
    Tag get(@Param("id") long id);

    @Delete("DELETE FROM TAGS WHERE ID = #{id}")
    int deleteTagById(@Param("id") long id);

    @Delete("DELETE FROM TAGS WHERE NAME = #{name} AND OWNER = #{owner}")
    int deleteTag(@Param("name") String tagName, @Param("owner") long owner);

    @DeleteProvider(type = TagDynamicSql.class, method = "deleteTags")
    void deleteTags(@Param("tags") String tags, @Param("owner") long owner);

    @Select("SELECT * FROM TAGS WHERE OWNER = #{owner}")
    List<Tag> getTags(long owner);

    @Insert("INSERT INTO LINKSTAGS VALUES (#{linkId}, #{tagId})")
    void saveTagRelation(@Param("tagId") long tagId, @Param("linkId") long linkId);
}
