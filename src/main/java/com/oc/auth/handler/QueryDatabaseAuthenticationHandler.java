package com.oc.auth.handler;

import com.oc.entity.DatabaseProperties;
import com.oc.entity.User;
import com.oc.enums.LoginTypeEnum;
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

    private DatabaseProperties databaseProperties;

    public QueryDatabaseAuthenticationHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order, DatabaseProperties databaseProperties) {
        super(name, servicesManager, principalFactory, order);
        this.databaseProperties = databaseProperties;
    }

    @Override
    protected HandlerResult authenticateUsernamePasswordInternal(UsernamePasswordCredential usernamePasswordCredential, String s) throws GeneralSecurityException, PreventedException {
        String account = usernamePasswordCredential.getUsername();
        String password = usernamePasswordCredential.getPassword();

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(databaseProperties.getDriver());
        dataSource.setUrl(databaseProperties.getUrl());
        dataSource.setUsername(databaseProperties.getUsername());
        dataSource.setPassword(databaseProperties.getPassword());

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);

        String sql = "SELECT * FROM spp_user WHERE account = ?";
        User user = (User) jdbcTemplate.queryForObject(sql, new Object[]{account}, new BeanPropertyRowMapper(User.class));
        String salt = user.getSalt();
        String encryptPwd = sha256Encrypt(salt, password);

        if (user == null) {
            throw new AccountException("Sorry, username not found!");
        }

        if (!encryptPwd.equals(user.getPassword())) {
            throw new FailedLoginException("Sorry, password not correct!");
        } else {
            // 可自定义返回给客户端的多个属性信息
            HashMap<String, Object> principleAttributes = new HashMap<String, Object>();
            principleAttributes.put("name", user.getAccount());
            principleAttributes.put("accountId", user.getAccountId());
            principleAttributes.put("realName", user.getName());
            principleAttributes.put("loginType", LoginTypeEnum.SIMPLE.getMsg());

            final List<MessageDescriptor> list = new ArrayList<MessageDescriptor>();

            return createHandlerResult(usernamePasswordCredential,
                    this.principalFactory.createPrincipal(account, principleAttributes), list);
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
