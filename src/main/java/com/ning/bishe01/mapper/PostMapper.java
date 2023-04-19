package com.ning.bishe01.mapper;


import com.ning.bishe01.entity.Post;
import com.ning.bishe01.entity.Post;

import java.util.List;

public interface PostMapper {

Post selectUidByPid(String pid);
        /**
         * 通过岗位id查询岗位信息
         * @param post
         * @return  post对象
         */
        Post selectByPid(Post post);


        Post selectPostByPid(Post post);




        /**
         * 查询post列表
         * @param post
         * @return
         */

        List<Post> selectPostList(Post post);

        /**
         * 增加岗位信息（只允许管理员添加）
         * @param post
         * @return   返回添加数量
         */
        Integer insertPost(Post post);

        /**
         * 删除岗位信息
         * @param pid
         * @return
         */
        Integer deletePost(String pid);


        /**
         * 修改岗位信息
         * @param post
         * @return
         */
        Integer updatePost(Post post);

Integer updatePostAdmin(Post post);
    }
