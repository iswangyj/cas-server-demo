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

## 其他注意问题
### 登出页面casLogoutView.html
根据cas client的url，修改“<a href="http://127.0.0.1:8080">返回登录页面</a>”中的相对应地url即可
### 关于静态资源加载失败问题
建议查看war包内/target/cas/WEB-INF/statica/themes/meng/images下的图片资源是否可用。若资源不可用，可尝试采用手动替换该路径下的图片的方法。
