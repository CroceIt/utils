package com.joe.utils.cluster;

import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author joe
 * @version 2018.08.09 09:51
 */
public class ClusterManagerTest {
    String         pre  = ClusterManagerTest.class.getName() + "-%s";
    /**
     * redis host
     */
    String         host = "192.168.2.222";
    /**
     * redis port
     */
    int            port = 7001;
    String         text = "text";
    ClusterManager manager;
    ClusterManager check;

    @Test
    public void doLock() throws Exception {
        Lock lock = manager.getLock(String.format(pre, "Lock"));
        lock.lock();
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            try {
                Assert.assertFalse("分布式锁控制失败", lock.tryLock(10, TimeUnit.MILLISECONDS));
            } catch (Exception e) {
                Assert.assertNull(e.toString(), e);
            } finally {
                latch.countDown();
            }
        }).start();
        latch.await();
        lock.unlock();
    }

    @Test
    public void doMap() {
        Map<String, String> map = manager.getMap(String.format(pre, "Map"));
        map.put(text, text);
        Map<String, String> checkMap = check.getMap(String.format(pre, "Map"));
        Assert.assertTrue(checkMap != map);
        Assert.assertTrue(text.equals(checkMap.get(text)));
        map.clear();
    }

    @Test
    public void doBlockingDeque() throws Exception {
        BlockingDeque<String> deque = manager.getBlockingDeque(String.format(pre, "BlockingDeque"));
        BlockingDeque<String> checkDeque = manager
            .getBlockingDeque(String.format(pre, "BlockingDeque"));
        deque.put(text);

        CountDownLatch latch = new CountDownLatch(1);
        Thread thread = new Thread(() -> {
            try {
                Assert.assertEquals(checkDeque.take(), text);
                latch.countDown();
            } catch (InterruptedException e) {
                Assert.assertNull("从BlockingDeque获取数据失败", e);
            }
        });
        thread.setDaemon(true);
        thread.start();

        Assert.assertTrue(latch.await(3, TimeUnit.SECONDS));
    }

    @Test
    public void doPub() throws Exception {
        Topic<String> topic = manager.getTopic(String.format(pre, "PUB/SUB"));
        CountDownLatch latch = new CountDownLatch(1);
        topic.addListener(((channel, msg) -> {
            Assert.assertEquals(text, msg);
            latch.countDown();
        }));
        Topic<String> checkTopic = manager.getTopic(String.format(pre, "PUB/SUB"));
        checkTopic.publish(text);
        Assert.assertTrue(latch.await(3, TimeUnit.SECONDS));
    }

    @Before
    public void init() throws Exception {
        manager = ClusterManager.getInstance(host, port);
        check = ClusterManager.getInstance(host, port);
    }

    @After
    public void destroy() {
        manager.shutdown();
        check.shutdown();
    }
}
