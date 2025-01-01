/*
작성자 : 이지은
최종 수정 일시 : 2024-12-19, 목, 14:42
수정 내용 : 주석 추가, 예외 처리 추가
*/

package com.piggymetrics.notification.repository.converter;

import com.piggymetrics.notification.domain.Frequency;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * FrequencyReaderConverter 클래스는 MongoDB에서 Integer 값을 Frequency로 변환.
 * 사용 이유:
 * - MongoDB에서 저장된 빈도 데이터를 도메인 객체로 변환하여 애플리케이션에서 사용.
 */
@Component
public class FrequencyReaderConverter implements Converter<Integer, Frequency> {

	/**
	 * Integer 값을 Frequency로 변환.
	 *
	 * @param days 변환할 Integer 값 (일 수)
	 * @return 변환된 Frequency 객체
	 * @throws IllegalArgumentException 유효하지 않은 일 수가 주어졌을 경우
	 */
	@Override
	public Frequency convert(Integer days) {
		try {
			return Frequency.withDays(days);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("변환할 수 없는 일 수: " + days, e);
		}
	}
}

