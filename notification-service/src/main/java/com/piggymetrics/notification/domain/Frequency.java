//알림 빈도를 표현. (daily, weekly...)

/*
작성자 : 이지은
최종 수정 일시 : 2024-12-19, 목, 10:34
수정 내용 : IllegalArgumentException 메시지 구체화, 주석 추가
*/

package com.piggymetrics.notification.domain;

import java.util.stream.Stream;

/**
 * Frequency 열거형 알림 빈도 정의.
 * 각 빈도는 일(day) 단위로 표현, 관련 메서드를 통해 빈도 처리.
 */
public enum Frequency {

	WEEKLY(7), // 주 단위 빈도
	MONTHLY(30), // 월 단위 빈도
	QUARTERLY(90); // 분기 단위 빈도

	private final int days;

	/**
	 * Frequency 생성자.
	 *
	 * @param days 해당 빈도의 일 수
	 */
	Frequency(int days) {
		this.days = days;
	}

	/**
	 * 빈도의 일 수 반환.
	 *
	 * @return 빈도의 일 수
	 */
	public int getDays() {
		return days;
	}

	/**
	 * 주어진 일 수에 해당하는 Frequency 반환.
	 * 일치하는 빈도가 없을 경우 예외를 던짐.
	 *
	 * @param days 빈도의 일 수
	 * @return 주어진 일 수에 해당하는 Frequency
	 * @throws IllegalArgumentException 유효하지 않은 일 수가 주어진 경우
	 */
	public static Frequency withDays(int days) {
		return Stream.of(Frequency.values())
				.filter(f -> f.getDays() == days)
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 일 수: " + days));
	}
}
