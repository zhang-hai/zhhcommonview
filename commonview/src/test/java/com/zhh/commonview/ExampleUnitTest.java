package com.zhh.commonview;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test_hash_code(){
        String str = "abcdefg";
        String str2 = "abc";
        System.out.println(str+"---的hashcode--->"+str.hashCode());
        System.out.println(str+"---的hashcode--->"+System.identityHashCode(str));
        System.out.println(str2+"---的hashcode--->"+System.identityHashCode(str2));
    }

    private AtomicInteger value = new AtomicInteger();
    private ThreadLocal<SimpleDateFormat> format = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    /**
     * 多线程
     * @throws InterruptedException
     */
    @Test
    public void testMultiThread() throws InterruptedException{
        ExecutorService service = Executors.newCachedThreadPool();
        final CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0;i < 10;i++){
            service.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("CountDownLatch ------> before");
                    value.incrementAndGet();
                    latch.countDown();
                    System.out.println("CountDownLatch ------> after");
                }
            });
        }
        latch.await();
        service.shutdown();
        System.out.println("value ------> " + value.get());

        //使用CyclicBarrier来实现循环计数屏障
        ExecutorService service2 = Executors.newCachedThreadPool();
        final CyclicBarrier barrier = new CyclicBarrier(10);
        for (int i = 0;i < 10;i++){
            service2.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("CyclicBarrier ------> before");
                    try {
                        barrier.await();
                    }catch (InterruptedException | BrokenBarrierException e){

                    }
                    System.out.println("CyclicBarrier ------> after");
                }
            });
        }
        service2.shutdown();
        System.out.println("value ------> " + value.get());
    }


    /**
     * 不可变 保证线程安全
     */
    @Test
    public void testFinalParam(){
        Map<String,Integer> map = new HashMap<>();
        Map<String,Integer> unmodifyMap = Collections.unmodifiableMap(map);
        map.put("s1",10);
        map.put("s2",20);
        System.out.println("map.equals(unmodifyMap)------> " + map.equals(unmodifyMap));

        System.out.println("map ------> " + map.toString());
    }
}