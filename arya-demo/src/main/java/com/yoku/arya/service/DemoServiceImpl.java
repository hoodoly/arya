package com.yoku.arya.service;

public class DemoServiceImpl implements DemoSrevice {

    @Override
    public Integer count(int number) {

        return number++;
    }
}
