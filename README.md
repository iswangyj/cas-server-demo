# cas-server-demo
## 关于TGC相关配置
在application.properties中添加如下配置：  
cas.warningCookie.path=/  
cas.warningCookie.maxAge=-1  
cas.warningCookie.domain=  
cas.warningCookie.name=CASPRIVACY  
cas.warningCookie.secure=true  
cas.warningCookie.httpOnly=true  

cas.tgc.path=/  
cas.tgc.maxAge=-1  
cas.tgc.domain=  
cas.tgc.name=CASTGC  
cas.tgc.secure=true  
cas.tgc.httpOnly=true  
cas.tgc.rememberMeMaxAge=1209600  

cas.tgc.crypto.encryption.key=  
cas.tgc.crypto.signing.key=  
cas.tgc.crypto.enabled=true  

cas.ticket.tgt.rememberMe.enabled=true  
cas.ticket.tgt.rememberMe.timeToKillInSeconds=3600  

各项配置参数具体含义见https://ningyu1.github.io/site/post/54-cas-server/  


## 配置Redis Ticket Registry
首先，添加依赖如下:  
<dependency>  
    <groupId>org.apereo.cas</groupId>  
    <artifactId>cas-server-support-redis-ticket-registry</artifactId>  
    <version>${cas.version}</version>  
</dependency>  
其次：在application.properties中添加如下配置：  
cas.ticket.registry.redis.host=127.0.0.1  
cas.ticket.registry.redis.database=0  
cas.ticket.registry.redis.port=6379  
cas.ticket.registry.redis.password=  
cas.ticket.registry.redis.timeout=2000  
cas.ticket.registry.redis.useSsl=false  
cas.ticket.registry.redis.usePool=false  
......  
最后，注意实体类的序列化，如未序列化会造成票据无法存入redis