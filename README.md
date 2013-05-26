# Belajar Akses Database Menggunakan Java #

Ini adalah contoh kode program untuk mengakses database dengan Java.
Seperti kita ketahui, di Java ada banyak sekali metode untuk berinteraksi dengan database.
Dengan adanya repository ini, kita dapat membandingkan berbagai metode yang tersedia tersebut.

## Fitur Aplikasi ##

Agar contoh kode programnya representatif, kita mengambil studi kasus klasik aplikasi bisnis.
Yaitu tiga tabel database yang berfungsi sebagai:

1. Master Data atau Data Referensi
2. Header Transaksi
3. Detail Transaksi

Ketiga tabel ini akan direpresentasikan oleh class Java, yaitu:

1. Produk
2. Penjualan
3. PenjualanDetail

## Metode Akses Database ##

Beberapa metode yang dicontohkan di sini :

* [JDBC polos tanpa library apa-apa](http://docs.oracle.com/javase/tutorial/jdbc/)
* [JDBC dengan Spring Framework (Spring JDBC)](http://static.springsource.org/spring/docs/3.2.x/spring-framework-reference/html/jdbc.html)
* [Hibernate ORM](http://docs.jboss.org/hibernate/orm/4.2/manual/en-US/html/)
* [Spring Data JPA](http://www.springsource.org/spring-data/jpa)

Selain keempat metode di atas, masih banyak metode lain yang tidak kita bahas, diantaranya:

* [Java Persistence API (JPA)](http://docs.oracle.com/javaee/6/tutorial/doc/bnbpz.html)
* [Java Data Object (JDO)](http://www.datanucleus.org/products/datanucleus/jdo/guides/tutorial_rdbms.html)
* [Google DataStore](https://developers.google.com/appengine/docs/python/gettingstartedpython27/usingdatastore)
* dan sebagainya

Metode-metode tersebut tidak saya bahas karena beberapa alasan, yaitu :

1. Belum ada pengalaman menggunakannya
2. Belum ada waktu untuk eksplorasi

Jadi jika ada metode yang ingin Anda lihat dan belum tersedia, 
tidak perlu galau. 
Silahkan explore sendiri, dan submit [Pull Request](https://help.github.com/articles/creating-a-pull-request)
agar hasil eksplorasi Anda juga bisa bermanfaat bagi orang lain.

