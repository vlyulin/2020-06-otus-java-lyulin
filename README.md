<?xml version="1.0" encoding="UTF-8"?>
<module type="JAVA_MODULE" version="4" />

# Курс "Разработчик Java" в OTUS

# Содержание:
* [Студент](#Студент)
* [Модуль hw01-gradle](#Модуль-hw01-gradle)
* [Модуль hw02-DIY-ArrayList](#Модуль-hw02-DIY-ArrayList)

# Студент
ФИО слушателя: Люлин Вадим Евгеньевич
Название курса: Разработчик Java
Группа: 2020-06

##Модуль hw01-gradle
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

##Модуль hw02-DIY-ArrayList
Домашнее задание DIY ArrayList

Цель: изучить как устроена стандартная коллекция ArrayList. Попрактиковаться в создании своей коллекции.
Написать свою реализацию ArrayList на основе массива.
class DIYarrayList<T> implements List<T>{...}

Проверить, что на ней работают методы из java.util.Collections:
Collections.addAll(Collection<? super T> c, T... elements)
Collections.static <T> void copy(List<? super T> dest, List<? extends T> src)
Collections.static <T> void sort(List<T> list, Comparator<? super T> c)

### Преподаватели
Сергей Петрелевич<br>
Стрекалов Павел<br>
Александр Оруджев<br>
Вячеслав Лапин<br>
Виталий Куценко<br>
