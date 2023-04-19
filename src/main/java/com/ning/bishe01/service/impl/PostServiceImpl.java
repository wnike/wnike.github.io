package com.ning.bishe01.service.impl;


import com.ning.bishe01.entity.Post;
import com.ning.bishe01.mapper.PostMapper;
import com.ning.bishe01.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;


@Service
public class PostServiceImpl implements IPostService {

    @Autowired
    private PostMapper postMapper;

    //查询——岗位——通过pid
    @Override
    public Post findByPid(Post post) {
        Post result = postMapper.selectByPid(post);
        //未查询到返回空
        if (result == null) {
            return null;
        }
        return result;
    }

    //查询uid——岗位——通过pid
    @Override
    public String findUidByPid(String pid) {
        Post result = postMapper.selectUidByPid(pid);
        //未查询到返回空
        if (result == null) {
            return null;
        }
        return result.getUid();
    }

    @Override
    public Post findPostByPid(Post post) {
        Post result = postMapper.selectPostByPid(post);
        //未查询到返回空
        if (result == null) {
            return null;
        }
        return result;
    }




    //查询——岗位列表
    @Override
    public List<Post> findPostList(Post post) {
        return postMapper.selectPostList(post);
    }

    //添加——岗位
    @Override
    public boolean addPost(Post post) {
        if(post.getHireNumber()==null){
            post.setHireNumber(0);
        }
        Post result = postMapper.selectByPid(post);
        //如果结果为空（即未注册）可以插入
        try { if (result == null) {
            //管理员默认设置岗位申请通过
            post.setApplyPost(1);
            Integer row = postMapper.insertPost(post);
            // 判断受影响的行数是否不为1
            if (row != 1) {
                // 是：插入数据时出现某种错误
                return false;
            }
        } else {
            return false;
        }}catch (Exception e){
            return false;
        }
        return true;
    }
    //用工单位——申请——岗位
    @Override
    public boolean addPost_en(Post post) {
        if(post.getHireNumber()==null){
            post.setHireNumber(0);
        }
        Post result = postMapper.selectByPid(post);
        //如果结果为空（即未注册）可以插入
      try {
          if (result == null) {
              //用工单位默认设置岗位申请不通过
              post.setApplyPost(0);
              Integer row = postMapper.insertPost(post);
              // 判断受影响的行数是否不为1
              if (row != 1) {
                  // 是：插入数据时出现某种错误
                  return false;
              }
          } else {
              return false;
          }
      }catch (Exception e){
          return false;
      }
        return true;
    }

    //删除岗位
    @Override
    public boolean deletePost(String pid) {
        Integer row = postMapper.deletePost(pid);
        if (row != 1) {
            return false;
        }
        return true;
    }

    //修改岗位信息
    @Override
    public boolean updatePost(Post post) {
        Integer row = postMapper.updatePost(post);
        if(row!=1){
            return false;
        }
        return true;
    }


}
