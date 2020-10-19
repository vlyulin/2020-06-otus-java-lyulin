﻿<?xml version="1.0" encoding="UTF-8"?>
<module type="JAVA_MODULE" version="4" />

# Курс "Разработчик Java" в OTUS

# Содержание:
* [Студент](#Студент)
* [Модуль hw01-gradle](#Модуль-hw01-gradle)
* [Модуль hw02-DIY-ArrayList](#Модуль_hw02-DIY-ArrayList)
* [Модуль hw03-Reflection](#Модуль_hw023-Reflection)
* [Модуль hw04-GCComparisons](#Модуль_hw04-GCComparisons)
* [Модуль hw05-AOP](#Модуль_hw05-AOP)
* [Модуль hw06-ATM](#Модуль_hw06-ATM)
* [Модуль hw07-MessageHandler](#Модуль_hw07-MessageHandler)
* [Модуль hw08-JsonObjectWriter](#Модуль_hw08-JsonObjectWriter)
* [Модуль hw09-Jdbc](#Модуль_hw09-Jdbc)
* [Модуль hw09-Hibernate](#Модуль_hw10-Hibernate)
* [Модуль hw11-MyCache](#Модуль_hw11-MyCache)

# Студент
ФИО слушателя: Люлин Вадим Евгеньевич
Название курса: Разработчик Java
Группа: 2020-06

## Модуль hw01-gradle<a name="Модуль-hw01-gradle"></a>
Проект gradle с модульной структурой

Домашнее задание
Проект gradle с модульной структурой
Цель: нучиться создавать проект Gradle (Maven), подготовиться к выполнению домашних заданий.
1) Создайте аккаунт на github.com (если еще нет)
2) Создайте репозиторий для домашних работ
3) Сделайте checkout репозитория на свой компьютер
4) Создайте локальный бранч hw01-gradle
5) Создать проект gradle
6) В проект добавьте последнюю версию зависимости
<groupId>com.google.guava</groupId>
<artifactId>guava</artifactId>
7) Создайте модуль hw01-gradle
8) В модуле сделайте класс HelloOtus
9) В этом классе сделайте вызов какого-нибудь метода из guava
10) Создайте "толстый-jar"
11) Убедитесь, что "толстый-jar" запускается.
12) Сделайте pull-request в gitHub
13) Ссылку на PR отправьте на проверку (личный кабинет, чат с преподавателем).

При желании, можно сделать maven-проект и далее на курсе работать с maven-ом.
Для Maven инструкция аналогичная (просто в тексте замените Gradle на Maven). 

## Модуль hw02-DIY-ArrayList<a name="Модуль_hw02-DIY-ArrayList"></a>
Домашнее задание DIY ArrayList

Цель: изучить как устроена стандартная коллекция ArrayList. Попрактиковаться в создании своей коллекции.
Написать свою реализацию ArrayList на основе массива.
class DIYarrayList<T> implements List<T>{...}

Проверить, что на ней работают методы из java.util.Collections:
Collections.addAll(Collection<? super T> c, T... elements)
Collections.static <T> void copy(List<? super T> dest, List<? extends T> src)
Collections.static <T> void sort(List<T> list, Comparator<? super T> c)

## Модуль hw03-Reflection<a name="Модуль_hw023-Reflection"></a>
Домашнее задание
Свой тестовый фреймворк
Цель: научиться работать с reflection и аннотациями, понять принцип работы фреймворка junit.
Написать свой тестовый фреймворк.

Поддержать свои аннотации @Test, @Before, @After.

Запускать вызовом статического метода с именем класса с тестами.

Т.е. надо сделать:
1) создать три аннотации - @Test, @Before, @After.
2) Создать класс-тест, в котором будут методы, отмеченные аннотациями.
3) Создать "запускалку теста". На вход она должна получать имя класса с тестами, в котором следует найти и запустить методы отмеченные аннотациями и пункта 1.
4) Алгоритм запуска должен быть следующий::
метод(ы) Before
текущий метод Test
метод(ы) After
для каждой такой "тройки" надо создать СВОЙ объект класса-теста.
5) Исключение в одном тесте не должно прерывать весь процесс тестирования.
6) На основании возникших во время тестирования исключений вывести статистику выполнения тестов (сколько прошло успешно, сколько упало, сколько было всего) 

## Модуль hw04-GCComparisons<a name="Модуль_hw04-GCComparisons"></a>
Сравнение разных сборщиков мусора
Цель: на примере простого приложения понять какое влияние оказывают сборщики мусора
Написать приложение, которое следит за сборками мусора и пишет в лог количество сборок каждого типа
(young, old) и время которое ушло на сборки в минуту.

Добиться OutOfMemory в этом приложении через медленное подтекание по памяти
(например добавлять элементы в List и удалять только половину).


Настроить приложение (можно добавлять Thread.sleep(...)) так чтобы оно падало
с OOM примерно через 5 минут после начала работы.

Собрать статистику (количество сборок, время на сборки) по разным GC.

!!! Сделать выводы !!!
ЭТО САМАЯ ВАЖНАЯ ЧАСТЬ РАБОТЫ:
Какой gc лучше и почему?

Выводы оформить в файле Сonclusions.md в корне папки проекта.
Результаты измерений сведите в таблицу.

Попробуйте провести этот эксперимент на небольшом хипе порядка 256Мб, и на максимально возможном, который у вас может быть. 

## Модуль hw05-AOP<a name="Модуль_hw05-AOP"></a>
Автоматическое логирование.
Цель: Понять как реализуется AOP, какие для этого есть технические средства.
Разработайте такой функционал:
метод класса можно пометить самодельной аннотацией @Log, например, так:

class TestLogging {
@Log
public void calculation(int param) {};
}

При вызове этого метода "автомагически" в консоль должны логироваться значения параметров.
Например так.

class Demo {
public void action() {
new TestLogging().calculation(6);
}
}

В консоле дожно быть:
executed method: calculation, param: 6

Обратите внимание: явного вызова логирования быть не должно.

Учтите, что аннотацию можно поставить, например, на такие методы:
public void calculation(int param1)
public void calculation(int param1, int param2)
public void calculation(int param1, int param2, String param3)

P.S.
Выбирайте реализацию с ASM, если действительно этого хотите и уверены в своих силах.
Критерии оценки: Система оценки максимально соответсвует привычной школьной:
3 и больше - задание принято (удовлетворительно).
ниже - задание возвращается на доработку.

## Модуль hw06-ATM<a name="Модуль_hw06-ATM"></a>
Эмулятор банкомата
Цель: Применить на практике принципы SOLID.
Написать эмулятор АТМ (банкомата).

Объект класса АТМ должен уметь:
- принимать банкноты разных номиналов (на каждый номинал должна быть своя ячейка)
- выдавать запрошенную сумму минимальным количеством банкнот или ошибку если сумму нельзя выдать
Это задание не на алгоритмы, а на проектирование.
Поэтому оптимизировать выдачу не надо.
- выдавать сумму остатка денежных средств

В этом задании больше думайте об архитектуре приложения.
Не отвлекайтесь на создание таких объектов как: пользователь, авторизация, клавиатура, дисплей, UI (консольный, Web, Swing), валюта, счет, карта, т.д.
Все это не только не нужно, но и вредно!
Критерии оценки: Система оценки максимально соответсвует привычной школьной:
3 и больше - задание принято (удовлетворительно).
ниже - задание возвращается на доработку.

## Модуль hw07-MessageHandler<a name="Модуль_hw07-MessageHandler"></a>
Домашнее задание
Обработчик сообщений
Цель: Применить на практике шаблоны проектирования.
Реализовать todo из модуля homework.

## Модуль hw08-JsonObjectWriter <a name="Модуль_hw08-JsonObjectWriter"></a>
Cвой json object writer
Цель: Научиться сериализовывать объект в json, попрактиковаться в разборе структуры объекта.
Напишите свой json object writer (object to JSON string) аналогичный gson на основе javax.json.

Gson это делает так:
Gson gson = new Gson();
AnyObject obj = new AnyObject(22, "test", 10);
String json = gson.toJson(obj);

Сделайте так:
MyGson myGson = new MyGson();
AnyObject obj = new AnyObject(22, "test", 10);
String myJson = myGson.toJson(obj);

Должно получиться:
AnyObject obj2 = gson.fromJson(myJson, AnyObject.class);
System.out.println(obj.equals(obj2));

Поддержите:
- примитивные типы
- массивы примитивных типов
- коллекции (interface Collection)
не забываться, что obj может быть null

## Модуль hw09-Jdbc <a name="Модуль_hw09-Jdbc"></a>

Самодельный ORM
Цель: Научиться работать с jdbc. На практике освоить многоуровневую архитектуру приложения.
Работа должна использовать базу данных H2.

Создайте в базе таблицу User с полями:

• id bigint(20) NOT NULL auto_increment
• name varchar(255)
• age int(3)

Создайте свою аннотацию @Id

Создайте класс User (с полями, которые соответствуют таблице, поле id отметьте аннотацией).

Реализуйте интерфейс JdbcMapper<T>, который умеет работать с классами, в которых есть поле с аннотацией @Id.
JdbcMapper<T> должен сохранять объект в базу и читать объект из базы.
Для этого надо реализовать оставшиеся интерфейсы из пакета mapper.
Таким обзазом, получится надстройка над DbExecutor<T>, которая по заданному классу умеет генерировать sql-запросы.
А DbExecutor<T> должен выполнять сгенерированные запросы.

Имя таблицы должно соответствовать имени класса, а поля класса - это колонки в таблице.

Проверьте его работу на классе User.

За основу возьмите класс HomeWork.

Создайте еще одну таблицу Account:
• no bigint(20) NOT NULL auto_increment
• type varchar(255)
• rest number

Создайте для этой таблицы класс Account и проверьте работу JdbcMapper на этом классе. 

## Модуль hw10-Hibernate <a name="Модуль_hw10-Hibernate"></a>

Использование Hibernate
Цель: На практике освоить основы Hibernate.
Понять как аннотации-hibernate влияют на формирование sql-запросов.
Работа должна использовать базу данных H2.

Возьмите за основу предыдущее ДЗ (Самодельный ORM),
используйте предложенный на вебинаре api (пакет api)
и реализуйте функционал сохранения и чтения объекта User через Hibernate.
(Рефлексия больше не нужна)
Конфигурация Hibernate должна быть вынесена в файл.

Добавьте в User поля:
адрес (OneToOne)
class AddressDataSet {
private String street;
}
и телефон (OneToMany)
class PhoneDataSet {
private String number;
}

Разметьте классы таким образом, чтобы при сохранении/чтении объека User каскадно сохранялись/читались вложенные объекты.

ВАЖНО.
1) Hibernate должен создать только три таблицы: для телефонов, адресов и пользователей.
2) При сохранении нового объекта не должно быть update-ов.
Посмотрите в логи и проверьте, что эти два требования выполняются.
Критерии оценки: Система оценки максимально соответсвует привычной школьной:
3 и больше - задание принято (удовлетворительно).
ниже - задание возвращается на доработку.

## Модуль hw11-MyCache]<a name="hw11-MyCache"></a>
Свой cache engine
Цель: Научится применять WeakHashMap,
понять базовый принцип организации кеширования.
Закончите реализацию MyCache из вебинара.
Используйте WeakHashMap для хранения значений.

Добавьте кэширование в DBService из задания про Hibernate ORM или "Самодельный ORM".
Для простоты скопируйте нужные классы в это ДЗ.

Убедитесь, что ваш кэш действительно работает быстрее СУБД и сбрасывается при недостатке памяти. 

### Преподаватели
Сергей Петрелевич<br>
Стрекалов Павел<br>
Александр Оруджев<br>
Вячеслав Лапин<br>
Виталий Куценко<br>
