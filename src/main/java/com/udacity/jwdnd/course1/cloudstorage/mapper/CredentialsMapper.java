package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credentials;
import lombok.Setter;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialsMapper {

    @Select("Select * from CREDENTIALS ")
    List<Credentials> getListByUserName(String userName);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    Credentials getCredentialById(int credentialId);

    @Insert("INSERT INTO CREDENTIALS (url, username, salt, password, userId) VALUES (#{url}, #{username}, #{salt}, #{password},#{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    void insertCredentials(Credentials credentials);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, salt = #{salt}, password= #{password} WHERE credentialId = #{credentialId}")
    void updateCredentials(Credentials credentials);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    void deleteCredentials(int credentialId);

}
