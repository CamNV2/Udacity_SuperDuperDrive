package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.entity.Credentials;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialsService {
    private final CredentialsMapper credentialsMapper;
    private EncryptionService encryptionService;
    private final HashService hashService;
    public CredentialsService(CredentialsMapper credentialsMapper, EncryptionService encryptionService, HashService hashService) {
        this.credentialsMapper = credentialsMapper;
        this.encryptionService = encryptionService;
        this.hashService = hashService;
    }
    public List<Credentials> getListAll (){
        List<Credentials> credentials = credentialsMapper.getListAll();
        for (int i = 0; i<credentials.size();i ++){
            credentials.get(i).setDecryptValue(encryptionService.decryptValue(credentials.get(i).getPassword(),credentials.get(i).getSalt()));
        }
        return credentials;
    }
    public int addCredentials(Credentials credentials) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String encryptedPassword = encryptionService.encryptValue(credentials.getPassword(), encodedSalt);
        return credentialsMapper.insertCredentials(new Credentials(null,credentials.getUrl(), credentials.getUsername(), encodedSalt, encryptedPassword, credentials.getUserId()));
    }
    public int updateCredentials(Credentials credentials) {
        Credentials credentialUd = credentialsMapper.getCredentialById(credentials.getCredentialId());
        credentials.setSalt(credentialUd.getSalt());
        String encryptedPassword = encryptionService.encryptValue(credentials.getPassword(), credentials.getSalt());
        String decryptValuePassword = encryptionService.decryptValue(encryptedPassword, credentials.getSalt());
        credentials.setPassword(encryptedPassword);
        credentials.setDecryptValue(decryptValuePassword);
        return credentialsMapper.updateCredentials(credentials);
    }
    public int deleteById (int credentialsId){
        return credentialsMapper.deleteCredentials(credentialsId);
    }
    public List<Credentials> getCredentialByUserName (String userName){
        return credentialsMapper.getCredentialByUserName(userName);
    }
    public boolean checkExistUserName (String userName){
        if(getCredentialByUserName(userName) != null && getCredentialByUserName(userName).size() > 0) {
            return true;
        }
        return false;
    }
}