/*
작성자 : 이지은
최종 수정 일시 : 2024-12-19, 목, 14:42
수정 내용 : 주석 추가, 예외 처리 추가(null값 처리)
*/

package com.piggymetrics.notification.repository.converter;

import com.piggymetrics.notification.domain.Frequency;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * FrequencyWriterConverter 클래스는 Frequency 객체를 Integer로 변환.
 * 사용 이유:
 * - MongoDB에 Frequency 데이터를 저장하기 위해 Integer 형식으로 변환.
 */
@Component
public class FrequencyWriterConverter implements Converter<Frequency, Integer> {

	/**
	 * Frequency 값을 Integer로 변환.
	 *
	 * @param frequency 변환할 Frequency 객체
	 * @return Frequency에 해당하는 일 수 (Integer 값)
	 * @throws IllegalArgumentException null 값이 주어진 경우
	 */
	@Override
	public Integer convert(Frequency frequency) {
		if (frequency == null) {
			throw new IllegalArgumentException("변환할 Frequency 객체가 null입니다.");
		}
		return frequency.getDays();
	}
}