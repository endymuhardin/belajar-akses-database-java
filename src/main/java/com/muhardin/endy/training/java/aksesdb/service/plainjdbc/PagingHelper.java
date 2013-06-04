package com.muhardin.endy.training.java.aksesdb.service.plainjdbc;

public class PagingHelper {
    public static Integer halamanJadiStart(Integer halaman,
            Integer baris){
        if (halaman < 1) {
            return 0;
        }
        return (halaman - 1) * baris;
    }
}