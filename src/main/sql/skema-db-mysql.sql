create table m_produk (
    id int primary key auto_increment,
    kode varchar(20) not null,
    nama varchar(255) not null,
    harga decimal(19,2) not null
) engine=InnoDB ;

create table t_penjualan (
    id int primary key auto_increment,
    waktu_transaksi datetime not null
) engine=InnoDB AUTO_INCREMENT=100;

create table t_penjualan_detail (
    id int primary key auto_increment,
    id_penjualan int not null,
    id_produk int not null,
    harga decimal(19,2) not null,
    jumlah int not null, 
    foreign key(id_penjualan) references t_penjualan(id) on delete cascade,
    foreign key(id_produk) references m_produk(id) on delete restrict
) engine=InnoDB AUTO_INCREMENT=100;

insert into m_produk (id,kode,nama,harga) values 
(1, 'K-001', 'Keyboard USB', 150000),
(2, 'M-001', 'Mouse USB', 50000),
(3, 'L-001', 'Laptop', 10000000);

insert into t_penjualan (id,waktu_transaksi) values 
(1,'2013-01-01 20:30:30'),
(2,'2013-01-02 15:15:15'),
(3,'2013-02-02 09:09:09');

insert into t_penjualan_detail (id,id_penjualan, id_produk, harga, jumlah) values 
(1,1,1,150000,2),
(2,1,2,50000,5),
(3,2,1,150000,3),
(4,2,2,50000,3),
(5,3,3,10000000,1);

