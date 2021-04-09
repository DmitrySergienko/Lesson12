package ru.geekbrins.Lesson12HT_Test2;

import java.util.Arrays;
import java.util.concurrent.*;

class Main implements Runnable{

    static final int SIZE = 10_000_000;
    static final int HALF = SIZE / 2;

    @Override
    public void run() {

    }
    public static void main(String[] args)  {
        float[] a1 = new float[5_000_000];
        float[] a2 = new float[5_000_000];
//заполняю массив
        float[] arr = new float[SIZE];
        Arrays.fill(arr,1);
//разбиваю на две части
        //long a= System.currentTimeMillis();
        System.arraycopy(arr,0,a1,0,HALF);
        System.arraycopy(arr,HALF,a2,0,HALF);

        CallableArray callableOne = new CallableArray(a1);
        CallableArray callableOne1 = new CallableArray(a2);
        ExecutorService executorService1 = Executors.newSingleThreadExecutor();
        executorService1.execute(new FirstMethod());
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<float[]> future1 = executorService.submit(callableOne);
        Future<float[]> future2 = executorService.submit(callableOne1);
        executorService.shutdown();

//склеиваю массив
        try {
            System.arraycopy(future1.get(),0,arr,0,HALF);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {
            System.arraycopy(future2.get(),0,arr,0,HALF);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}

//class Callable
class CallableArray implements Callable<float[]>{
    float[] firstArray;

    public CallableArray(float[] array) {
        this.firstArray = array;
    }
    @Override
    public float[] call() throws Exception {
        return workProcess();
    }
    public float[] workProcess() {
        long a= System.currentTimeMillis();
        for (int i = 0; i < firstArray.length; i++) {
            firstArray[i] = (float)(firstArray[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        System.out.println("Второй метод разными потоками - время исполнения : " + Thread.currentThread().getName() + " " + (System.currentTimeMillis() - a)+ " ms");

        return firstArray;
    }
}

class FirstMethod implements Runnable{
    static final int HALF = 5_000_000;
    @Override
    public void run() {
          float[] arr = new float[HALF];
          Arrays.fill(arr,1);
          long a = System.currentTimeMillis();
          for (int i = 0; i < HALF; i++) {
              arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
          }
          System.currentTimeMillis();
          System.out.println("Первый метод один поток - время исполнения : " + Thread.currentThread().getName() + " " + (System.currentTimeMillis() - a)+ " ms");
    }
}