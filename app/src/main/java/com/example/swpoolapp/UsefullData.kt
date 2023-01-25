package com.example.swpoolapp

public class UsefullData {
    companion object {
        public var serverAddr: String = "http://192.168.0.105"
        public var month = mutableMapOf<String, String>(
            "JANUARY" to "Января",
            "FEBRUARY" to "Февраля",
            "MARCH" to "Марта",
            "APRIL" to "Апреля",
            "MAY" to "Мая",
            "JUNE" to "Июня",
            "JULY" to "Июля",
            "AUGUST" to "Августа",
            "SEPTEMBER" to "Сентября",
            "OCTOBER" to "Октября",
            "NOVEMBER" to "Ноября",
            "DECEMBER" to "Декабря"
        )
        public var days = mutableMapOf<String, String>(
            "MONDAY" to "Понедельник",
            "TUESDAY" to "Вторник",
            "WEDNESDAY" to "Среда",
            "THURSDAY" to "Четверг",
            "FRIDAY" to "Пятница",
            "SATURDAY" to "Суббота",
            "SUNDAY" to "Воскресенье"
        )
    }

}