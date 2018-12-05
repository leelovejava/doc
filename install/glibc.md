>> wget http://ftp.gnu.org/gnu/glibc/glibc-2.14.tar.gz
>> tar xf glibc-2.14.tar.gz
>> cd glibc-2.14
>> mkdir build
>> cd build
>> ../configure --prefix=/opt/glibc-2.14 --disable-profile --enable-add-ons --with-headers=/usr/include --with-binutils=/usr/bin
>> make
>> make install

>> strings /lib64/libc.so.6 | grep GLIBC

卸载
>> ./configure --prefix=/home/hadoop/app/glibc && make install





./configure --prefix=/home/hadoop/app/glibc-2.14 --disable-profile --enable-add-ons --with-headers=/usr/include --with-binutils=/usr/bin