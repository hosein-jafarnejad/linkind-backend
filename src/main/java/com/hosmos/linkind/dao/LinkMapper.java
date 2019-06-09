package com.hosmos.linkind.dao;

import com.hosmos.linkind.models.Link;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

public interface LinkMapper {
    @Insert("INSERT INTO " +
            "LINKS(ID, URL, OWNER, SHORT_URL, CREATION_DATE) " +
            "VALUES(nextval('SLINKS'), #{link.url}, #{link.owner}, #{link.short_url}, #{link.creation_date})")
    void save(@Param("link") Link link) throws DuplicateKeyException;

    @Select("SELECT * FROM LINKS WHERE ID = #{id}")
    Link getWithId(@Param("id") int id);

    @Select("SELECT URL FROM LINKS WHERE SHORT_URL = #{short_url}")
    String getWithShortUrl(@Param("short_url") String shortUrl);

    @Delete("DELETE FROM LINKS WHERE ID = #{id}")
    int delete(@Param("id") int id);

    @Select("SELECT" +
            " ID," +
            " URL," +
            " SHORT_URL," +
            " CREATION_DATE"+
            " FROM LINKS ORDER BY CREATION_DATE LIMIT #{limit} OFFSET #{offset}")
    List<Link> getLinks(@Param("limit") int limit, @Param("offset") int offset);
}
