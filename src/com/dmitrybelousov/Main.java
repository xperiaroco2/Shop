package com.dmitrybelousov;

import javenue.csv.Csv;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final int PURCHASE_GOODS_COUNT = 150;
    private static final double DAY_START = 8.00;
    private static final double DAY_END = 21.00;
    private static final String[] DAYS_OF_THE_WEEK = {"Понедельник","Вторник","Среда","Четверг","Пятница","Суббота","Воскресенье"};

    private static String dayOfTheWeek;
    private static double money;
    private static double spentMoney;
    private static  ArrayList<Product> productsList = new ArrayList<>();

    //Цикл одного дня
    private static void dayLoop() {

        double time;
        double markup;

        for(time = DAY_START; time < DAY_END+1; time++){

            System.out.println("\nВремя: " + time + "\n");


            if (time >= 18.00 && time <= 20.00) markup = 0.08;
        else if (dayOfTheWeek.equals("Суббота") || dayOfTheWeek.equals("Воскресенье")) markup = 0.15;
        else markup = 0.1;

        int customersCount = 1 + (int) (Math.random() * 10);

            for (int i = 0; i < customersCount; i++) {

                int productID = (int) (Math.random() * (productsList.size()));
                int productsCount = (int) (Math.random() * 10);
                double price = productsList.get(productID).getPrice();

                if(productsCount == 0) continue;
                if (productsList.get(productID).getInStock() < productsCount) continue;

                if (productsCount > 2) {

                    //Чистая прибыль
                    money += Math.round((((price * 2) * markup) + ((price * (productsCount - 2)) * 0.07)) * 100.0) / 100.0;

                    System.out.println("Продано: " + productsList.get(productID).getName()
                            + " \n2 шт. по цене " + Math.round((price +(price * markup)) * 100.0) / 100.0 + " за шт. и "
                            + (productsCount - 2) + " шт. по цене "
                            + Math.round((price +(price * 0.07)) * 100.0) / 100.0 + " за шт.");
                    if(markup == 0.1) System.out.println("Правило наценки: Стандартная за 2шт. и 7% на остальные");
                    if(markup == 0.15) System.out.println("Правило наценки: Наценка в выходные дни за 2шт. и 7% на остальные");
                    if(markup == 0.08) System.out.println("Правило наценки: Наценка 18:00 - 20:00 за 2шт. и 7% на остальные");
                    System.out.println("---------------------------------------------------------------");
                }
                else {

                    //Чистая прибыль
                    money += Math.round(((price * productsCount) * markup) * 100.0) / 100.0;
                    System.out.println("Продано: " + productsList.get(productID).getName()
                            + " " + productsCount + " шт. по цене "
                            + Math.round((price +(price * markup)) * 100.0) / 100.0 + " за шт.");
                    if(markup == 0.1) System.out.println("Правило наценки: Стандартная");
                    if(markup == 0.15) System.out.println("Правило наценки: Наценка в выходные дни");
                    if(markup == 0.08) System.out.println("Правило наценки: Наценка 18:00 - 20:00");
                    System.out.println("---------------------------------------------------------------");
                }

                productsList.get(productID).setSoldCount(
                        productsList.get(productID).getSoldCount() + productsCount);

                productsList.get(productID).setInStock(
                        productsList.get(productID).getInStock() - productsCount);

            }
        }
    }

    //Цикл 30 дней
    private static void monthLoop(){
        int mult = 0;
        for(int i = 0; i < 30; i++){

            if(i % 7 == 0 && i != 0) {
                mult++;
            }

            dayOfTheWeek = DAYS_OF_THE_WEEK[i-(7*mult)];
            System.out.println("["+dayOfTheWeek+"]\n");

            dayLoop();
            purchase();

        }
    }

    //Метод дозакупки товаров
    private static void purchase(){
        for (Product l: productsList) {
            if(l.getInStock() < 10){

                //Так как в переменную money я добавляю только чистую прибыль от продажи,
                //то при закупки товаров я не вычитываю деньги за их стоимость
                l.setInStock(l.getInStock() + PURCHASE_GOODS_COUNT);

                l.setPurchaseCount(
                        l.getPurchaseCount() + 1);

                spentMoney += Math.round(PURCHASE_GOODS_COUNT * l.getPrice() * 100.0) / 100.0;
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {

        //Чтение из CSV файла товаров
        Csv.Reader reader = new Csv.Reader(new FileReader("src\\com\\dmitrybelousov\\csv\\assortment"))
                .delimiter(',').ignoreComments(true);

        while (true) {

            List<String> list = reader.readLine();

            if (list == null) break;

            productsList.add(new Product(list.get(0), Double.parseDouble(list.get(1)), list.get(2)
                    , Double.parseDouble(list.get(3)), list.get(4), Integer.parseInt(list.get(5))));

        }

        //Запуск работы программы
        monthLoop();

        //Перезапись CSV файла товаров
        Csv.Writer writer = new Csv.Writer("src\\com\\dmitrybelousov\\csv\\assortment").delimiter(',');
        writer.comment("Store assortment");
        for (Product l : productsList) {
            writer.value(l.getName()).value(String.valueOf(l.getPrice())).value(l.getClassification())
                    .value(String.valueOf(l.getSize())).value(l.getComposition()).value(String.valueOf(l.getInStock()))
                    .newLine();
        }
        writer.close();


        //Запись отчета в файл
        try(FileWriter writer2 = new FileWriter("src\\com\\dmitrybelousov\\report\\report.txt", false))
        {

            writer2.write("Проданно:\n");
            for (Product l : productsList) {
                writer2.write(l.getName() + " - " + l.getSoldCount() + " шт.\n");
            }
            writer2.write("Закупленно:\n");
            for (Product l : productsList) {
                writer2.write(l.getName() + " - " + l.getPurchaseCount() * PURCHASE_GOODS_COUNT + " шт.\n");
            }
            writer2.write("Чистая прибыть магазина: " + Math.round(money * 100.0) / 100.0 + "\n");
            writer2.write("Затраченные средства на дозакупку товара: " + spentMoney);
            writer2.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
