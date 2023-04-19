package com.ning.bishe01.service;

import com.ning.bishe01.entity.Post;

import java.util.List;

public interface IPostService {
    /**
     * 通过岗位id查询岗位信息
     * @param post
     * @return post对象
     */
    Post findByPid(Post post);
   String findUidByPid(String pid);

    Post findPostByPid(Post post);

    List<Post> findPostList(Post post);
    /**
     * 增加岗位信息（只允许管理员添加）
     * @param post
     * @return 插入数据数量 true表示成功；false表示未成功
     */
    boolean addPost(Post post);
    boolean addPost_en(Post post);
    /**
     * 删除岗位
     * @param pid
     * @return
     */
    boolean deletePost(String pid);

    /**
     * 修改岗位
     * @param post
     * @return
     */
    boolean updatePost(Post post);


}
