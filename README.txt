
1) 关于编译, 项目根下运行
mvn clean install -Dmaven.test.skip=true

2) dao的自动生成, 在qyds-dao模块下
rm -rf target/main
mkdir -p target/main/java
mkdir -p target/main/resources
mvn mybatis-generator:generate -X

然后复制生成的mapper文件到
src/main/resources/mapper下,并更改mapper文件的BaseResult,避免重名;并声明@Repository

复制生成的JavaBean到
{pams-interface}模块的
src/main/java/包名/dto目录,并做实现序列化接口(implements Serializable)。

3)ERP服务代码生成

cd qyds-web-front
mkdir -p target/classes
wsimport -p net.dlyt.qyds.web.erp  -keep http://www.dealuna.com:8080/Service.asmx?wsdl  -B-XautoNameResolution -d target/classes -s src/main/java -verbose
cd qyds-service-api
wsimport -p net.dlyt.qyds.web.service.erp -keep http://www.dealuna.com:8080/Service.asmx?wsdl -B-XautoNameResolution -d target/classes -s src/main/java -verbose
cd qyds-service-api
wsimport -p net.dlyt.qyds.web.service.erp -keep http://dsweb.dealuna.cn:24444/Service.asmx?wsdl -B-XautoNameResolution -d target/classes -s src/main/java -verbose
cd qyds-service-api
wsimport -p net.dlyt.qyds.web.service.erp -keep http://dsweb.dealuna.cn:27676/Service.asmx?wsdl -B-XautoNameResolution -d target/classes -s src/main/java -verbose
