package com.piggymetrics.auth.repository;

import com.piggymetrics.auth.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import com.piggymetrics.auth.service.security.MongoUserDetailsService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataMongoTest // MongoDB 관련 빈만 로드
@TestPropertySource(properties = {
		"spring.cloud.config.enabled=false",
		"de.flapdoodle.mongodb.embedded.version=6.0.2"
})
public class UserRepositoryTest {

	@Autowired
	private UserRepository repository;

	@Test
	@DisplayName("사용자 이름으로 사용자 정보 저장 및 조회한다.")
	public void shouldSaveAndFindUserByName() {
		// given
		User user = User.of("name", "password");
		repository.save(user);

		// when
		Optional<User> found = repository.findById(user.getUsername());

		// then
		assertTrue(found.isPresent());
		assertEquals(user.getUsername(), found.get().getUsername());
		assertEquals(user.getPassword(), found.get().getPassword());
	}
}
