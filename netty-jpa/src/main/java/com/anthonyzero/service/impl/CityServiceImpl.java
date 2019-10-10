package com.anthonyzero.service.impl;

import com.anthonyzero.domain.City;
import com.anthonyzero.repository.CityDao;
import com.anthonyzero.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityDao cityDao;

    @Async
    public Boolean saveCity(City city) { //基本类型要用包装类型
        System.out.println("进入业务方法：当前线程名：" + Thread.currentThread().getName());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cityDao.save(city);
        return true;
    }
}
