package com.yoku.arya.service;

/**
 * @author HODO
 */
public class DemoServiceImpl implements DemoService {

    @Override
    public Integer count(int number) {

        return ++number;
    }
}
