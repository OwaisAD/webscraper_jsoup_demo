
package webscraper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Tester {

    public static List<TagCounter> runSequential() {

        List<TagCounter> urls = new ArrayList<>();

        urls.add(new TagCounter("https://www.fck.dk"));
        urls.add(new TagCounter("https://www.google.com"));
        urls.add(new TagCounter("https://elgiganten.dk"));
        urls.add(new TagCounter("https://cphbusiness.dk"));
        urls.add(new TagCounter("https://owais.dk"));

        for (TagCounter tc : urls) {
            tc.doWork();
        }
        return urls;
    }

    public static List<TagCounter> runParallel() throws ExecutionException, InterruptedException {

        List<TagCounter> urls = new ArrayList<>();
        // TODO:
        urls.add(new TagCounter("https://www.fck.dk"));
        urls.add(new TagCounter("https://www.google.com"));
        urls.add(new TagCounter("https://elgiganten.dk"));
        urls.add(new TagCounter("https://cphbusiness.dk"));
        urls.add(new TagCounter("https://owais.dk"));

        // CREATE THREAD POOL WITH EXECUTOR SERVICE
        ExecutorService executorService = Executors.newFixedThreadPool(8);

        // CREATE A LIST OF FUTURE TagCounter
        List<Future<TagCounter>> futureList = new ArrayList<>();

        // ITERATE OVER URL LIST AND ADD FUTURE FOR EACH TagCounter
        for(TagCounter tc : urls) {
            Future<TagCounter> future = executorService.submit(() -> {
                tc.doWork();
                return tc;
            });
            futureList.add(future);
        }

        for (Future<TagCounter> future : futureList) {
            future.get();
        }

        return urls;
    }

    public static void main(String[] args) throws Exception {
        long timeSequential;
        long start = System.nanoTime();

        List<TagCounter> fetchedData = new Tester().runSequential();
        long end = System.nanoTime();
        timeSequential = end - start;
        System.out.println("Time Sequential: " + ((timeSequential) / 1_000_000) + " ms.");

        for (TagCounter tc : fetchedData) {
            System.out.println("Title: " + tc.getTitle());
            System.out.println("Div's: " + tc.getDivCount());
            System.out.println("Body's: " + tc.getBodyCount());
            System.out.println("----------------------------------");
        }

    }
}