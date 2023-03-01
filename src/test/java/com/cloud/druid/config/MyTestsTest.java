package com.cloud.druid.config;import com.alibaba.druid.filter.config.ConfigTools;import org.junit.jupiter.api.Assertions;import org.junit.jupiter.api.Test;/** * @Author zhouTao * @Date 2023/2/22 */class MyTestsTest {    /**     * 加密     * @throws Exception     */    @Test    void encrypt() throws Exception {        String password = "root";        String[] keyPair = ConfigTools.genKeyPair(512);        String privateKey = keyPair[0];        String publicKey = keyPair[1];        System.out.println("私    钥： " + privateKey);        System.out.println("公    钥： " + publicKey);        String encryptPassword = ConfigTools.encrypt(privateKey, password);        System.out.println("密文密码： " + encryptPassword);        Assertions.assertEquals("root", ConfigTools.decrypt(publicKey, encryptPassword));    }    /**     * 解密     * @throws Exception     */    @Test    void decrypt() throws Exception {        //密文密码        String encrypted = "Nx9+e4kyRxMnbovvGNPM3AE3Gnaw6kOrTomVpz7N19QBpc7SGf3nprGI2a0O8MMhbc0IOSjKC4Fi3yn5bvtrQg==";        //公    钥        String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIBXY90xKUWgwZHNv/1gIThI/SeELm89itGDE+VRaIjxFuJmzLQvj9wI7+RvwyLJSr0bNzjdXN0gAe9guvc+n/ECAwEAAQ==";        String decrypted = ConfigTools.decrypt(publicKey, encrypted);        Assertions.assertEquals("root", decrypted);    }}//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme