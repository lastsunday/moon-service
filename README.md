# Moon-Service
## 启动开发模式
运行 *com.github.lastsunday.moon.MainApplicationTest* 并添加**Environment variables**
```
MOON_APP_MODULE_CLIENT_LOGIN_CAPTCHA_CHECKING_ENABLE=false;MOON_SPRING_LIQUIBASE_CHANGE_LOG=classpath:db/changelog/db.changelog-dev-master.xml;MOON_APP_SWAGGER_ENABLE=true
```

## API文档

[Click here to access swagger](http://localhost:9211/moon/swagger-ui/index.html)

## ErrorCode说明

| 描述   | ErrorCode |
| :----- | :-------- |
| Common | 1xxxx     |
| Client | 2xxxx     |
| User   | 3xxxx     |
| Role   | 4xxxx     |
| Log    | 5xxxx     |