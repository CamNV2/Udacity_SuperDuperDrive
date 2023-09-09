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
    public List<Credentials> getListCredentialsByUserName (String userName){
        List<Credentials> credentials = credentialsMapper.getListByUserName(userName);
        for (int i = 0; i<credentials.size();i ++){
            credentials.get(i).setDecryptValue(encryptionService.decryptValue(credentials.get(i).getPassword(),credentials.get(i).getSalt()));
        }
        return credentials;
    }
    public void addCredentials(Credentials credentials) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String encryptedPassword = encryptionService.encryptValue(credentials.getPassword(), encodedSalt);
        credentialsMapper.insertCredential(new Credentials(null,credentials.getUrl(), credentials.getUsername(), encodedSalt, encryptedPassword, credentials.getUserId()));
    }
    public void updateCredentials(Credentials credentials) {
        Credentials credentialUd = credentialsMapper.getCredentialById(credentials.getCredentialId());
        credentials.setSalt(credentialUd.getSalt());
        String encryptedPassword = encryptionService.encryptValue(credentials.getPassword(), credentials.getSalt());
        String decryptValuePassword = encryptionService.decryptValue(encryptedPassword, credentials.getSalt());
        credentials.setPassword(encryptedPassword);
        credentials.setDecryptValue(decryptValuePassword);
        credentialsMapper.updateCredential(credentials);
    }
    public void deleteById (int credentialsId){
        credentialsMapper.deleteCredential(credentialsId);
    }

}