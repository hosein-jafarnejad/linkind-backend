package com.hosmos.linkind.dao;

import com.hosmos.linkind.models.Link;
import com.hosmos.linkind.models.Visit;
import org.apache.ibatis.annotations.*;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

@Mapper
public interface LinkMapper {

    @Select("SELECT nextval('SLINKS')")
    long getId();

    /**
     * Save new link
     *
     * @param link
     */
    @Insert(value = {"INSERT INTO LINKS(ID, URL, OWNER, SHORT_URL) VALUES(#{link.id}, #{link.url}, #{link.owner}, #{link.short_url})"})
//    @Options(keyProperty = "id", flushCache = Options.FlushCachePolicy.TRUE)
//    @SelectKey(statement = "SELECT nextval('SLINKS')", before = true, keyProperty = "id", resultType = Long.class)

    void save(@Param("link") Link link) throws DuplicateKeyException;

    /**
     * returns information about link
     *
     * @param id link id
     */
    @Select("SELECT * FROM LINKS WHERE ID = #{id}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "url", column = "url"),
            @Result(property = "owner", column = "owner"),
            @Result(property = "short_url", column = "short_url"),
            @Result(property = "creation_date", column = "creation_date"),
            @Result(property = "tags", javaType = String[].class, column = "id", many = @Many(select = "getTags"))
    })
    Link getWithId(@Param("id") int id);

    /**
     * returns list of tags name for the link
     *
     * @param id link id
     */
    @Select("SELECT NAME FROM TAGS WHERE ID IN (SELECT TAG_ID FROM LINKSTAGS WHERE LINK_ID = #{id})")
    List<String> getTags(@Param("id") int id);

    /**
     * returns information about link using generated short url
     *
     * @param shortUrl
     */
    @Select("SELECT ID,URL FROM LINKS WHERE SHORT_URL = #{short_url}")
    Link getWithShortUrl(@Param("short_url") String shortUrl);

    /**
     * delete the existing link
     *
     * @param id
     */
    @Delete("DELETE FROM LINKS WHERE ID = #{id}")
    int delete(@Param("id") int id);

    /**
     * returns list of links owned by logged in user
     *
     * @param limit  max rows to fetch
     * @param offset start position to fetch
     */
    @Select("SELECT" +
            " ID," +
            " URL," +
            " SHORT_URL," +
            " CREATION_DATE" +
            " FROM LINKS ORDER BY CREATION_DATE LIMIT #{limit} OFFSET #{offset}")
    List<Link> getLinks(@Param("limit") int limit, @Param("offset") int offset);

    /**
     * save new visit record for link
     *
     * @param visit
     */
    @Insert("INSERT INTO VISITS(IP, LINK_ID, BROWSER_NAME, BROWSER_VERSION, OS) VALUES(#{visit.ip}, #{visit.link_id}, #{visit.browser_name}, #{visit.browser_version}, #{visit.os})")
    void saveVisit(@Param("visit") Visit visit);
}
