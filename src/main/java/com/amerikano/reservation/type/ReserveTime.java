package com.amerikano.reservation.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 예약 시간 상수(가독성을 위해)
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ReserveTime {
    TIME_MIDNIGHT("0"),
    TIME_1("1"),
    TIME_2("2"),
    TIME_3("3"),
    TIME_4("4"),
    TIME_5("5"),
    TIME_6("6"),
    TIME_7("7"),
    TIME_8("8"),
    TIME_9("9"),
    TIME_10("10"),
    TIME_11("11"),
    TIME_NOON("12"),
    TIME_13("13"),
    TIME_14("14"),
    TIME_15("15"),
    TIME_16("16"),
    TIME_17("17"),
    TIME_18("18"),
    TIME_19("19"),
    TIME_20("20"),
    TIME_21("21"),
    TIME_22("22"),
    TIME_23("23");

    private String timeString;
}
