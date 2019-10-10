package com.anthonyzero.server.handler;

import com.anthonyzero.domain.City;
import com.anthonyzero.repository.CityDao;
import com.anthonyzero.server.ChannelRepository;
import com.anthonyzero.service.CityService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private CityService cityService;
    @Autowired
    private CityDao cityDao;
    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(1, 5, 20, TimeUnit.SECONDS, new ArrayBlockingQueue<>(50));

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String stringMessage = (String) msg;
        //: split
        String[] splitMessage = stringMessage.split(":");

        if (splitMessage.length != 2 ) {
            ctx.channel().writeAndFlush(stringMessage + "\n\r");
            return;
        }


        City city = new City();
        city.setName("重庆");
        city.setCountryCode("465200");
        city.setDistrict("xxxxxxxxx");
        city.setPopulation(31000000);
        Boolean success = cityService.saveCity(city);
        System.out.println("success是否为空？" + (success == null)); //异步 结果直接返回null

        /*EXECUTOR.submit(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            City city = new City();
            city.setName("重庆");
            city.setCountryCode("42150");
            city.setDistrict("xx");
            city.setPopulation(500000);
            cityDao.save(city);
            System.out.println(Thread.currentThread().getName());
        });*/

        System.out.println(Thread.currentThread().getName());
        // send other channel
        if (channelRepository.get(splitMessage[0]) != null ) {
            channelRepository.get(splitMessage[0]).writeAndFlush( splitMessage[1] + "\n\r");
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Assert.notNull(this.channelRepository, "[Assertion failed] - ChannelRepository is required; it must not be null");

        ctx.fireChannelActive();
        log.info(ctx.channel().remoteAddress() + "");

        String channelKey = ctx.channel().remoteAddress().toString().split(":")[1];
        channelRepository.put(channelKey, ctx.channel());

        ctx.writeAndFlush("Your channel key is " + channelKey + "\r\n");

        log.info("Binded Channel Count is {}", this.channelRepository.size());

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage(), cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx){
        Assert.notNull(this.channelRepository, "[Assertion failed] - ChannelRepository is required; it must not be null");

        String channelKey = ctx.channel().remoteAddress().toString().split(":")[1];
        this.channelRepository.remove(channelKey);

        log.info("Binded Channel Count is " + this.channelRepository.size());

    }
}
