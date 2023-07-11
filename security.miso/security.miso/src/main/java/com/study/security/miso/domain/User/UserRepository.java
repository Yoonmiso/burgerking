package com.study.security.miso.domain.User;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository {
	public int save(User user) throws Exception;
	public User findUSerByUserName(String userName) throws Exception;
	public User findOauth2USerByUserName(String oauth2_id) throws Exception;
}