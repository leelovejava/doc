# mysql安装

wget http://dev.mysql.com/get/mysql-community-release-el7-5.noarch.rpm 

rpm -ivh mysql-community-release-el7-5.noarch.rpm

sudo yum -y install mysql mysql-server mysql-devel

service mysqld start

mysql -u root -p 输入密码，默认为空(root)

show databases;