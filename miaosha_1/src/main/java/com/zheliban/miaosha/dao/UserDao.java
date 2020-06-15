package com.zheliban.miaosha.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

import com.zheliban.miaosha.domain.User;

@Mapper
public interface UserDao {
	@Select("select * from user where id = #{id}")
	public User getById(@Param("id")int id);
}
