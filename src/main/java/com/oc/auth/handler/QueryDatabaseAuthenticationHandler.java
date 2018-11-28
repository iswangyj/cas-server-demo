package com.oc.auth.handler;

import com.oc.entity.User;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apereo.cas.authentication.HandlerResult;
import org.apereo.cas.authentication.MessageDescriptor;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.UsernamePasswordCredential;
import org.apereo.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.FailedLoginException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author wyj
 * @date 2018/11/22
 */
public class QueryDatabaseAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {
    public QueryDatabaseAuthenticationHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    @Override
    protected HandlerResult authenticateUsernamePasswordInternal(UsernamePasswordCredential usernamePasswordCredential, String s) throws GeneralSecurityException, PreventedException {
        String account = usernamePasswordCredential.getUsername();
        String password = usernamePasswordCredential.getPassword();

        // JDBC模板依赖于连接池来获得数据的连接，所以必须先要构造连接池
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://rm-m5ec16sm22j7393s3ro.mysql.rds.aliyuncs.com:3306/spp_dev2");
        dataSource.setUsername("wyj");
        dataSource.setPassword("wyj");

        // 创建JDBC模板
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);

        String sql = "SELECT * FROM spp_user WHERE account = ?";

        User info = (User) jdbcTemplate.queryForObject(sql, new Object[]{account}, new BeanPropertyRowMapper(User.class));
        String salt = info.getSalt();
        String encryptPwd = sha256Encrypt(salt, password);

        if (info == null) {
            throw new AccountException("Sorry, username not found!");
        }

        if (!encryptPwd.equals(info.getPassword())) {
            throw new FailedLoginException("Sorry, password not correct!");
        } else {

            // 可自定义返回给客户端的多个属性信息
            HashMap<String, Object> returnInfo = new HashMap<String, Object>();
            returnInfo.put(account, info);

            final List<MessageDescriptor> list = new ArrayList<MessageDescriptor>();

            return createHandlerResult(usernamePasswordCredential,
                    this.principalFactory.createPrincipal(account, returnInfo), list);
        }
    }

    public String sha256Encrypt(String salt, String password) {
        String plain = salt + " " + password;
        MessageDigest messageDigest;
        try {
            messageDigest = DigestUtils.getSha256Digest();
            byte[] byteFinal = messageDigest.digest(plain.getBytes("utf-8"));

            return Hex.encodeHexString(byteFinal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
