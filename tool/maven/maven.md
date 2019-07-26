
> mvn deploy:deploy-file -Dfile=jar包 -DgroupId=groupID -DartifactId=artifacid -Dversion=版本号 -Dpackaging=jar -Durl=http://ip:port/nexus/content/repositories/thirdparty/ -DrepositoryId=thirdparty


> mvn deploy:deploy-file -Dfile=wechatpay-apache-httpclient-0.1.4-SNAPSHOT.jar -DgroupId=com.github.wechatpay-apiv3 -DartifactId=artifacid -Dversion=0.1.4-SNAPSHOT -Dpackaging=jar -Durl=https://mvn.caimom.net/repository/maven-snapshots/ -DrepositoryId=snapshots