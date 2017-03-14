/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.greglturnquist.learningspringboot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Greg Turnquist
 */
// tag::1[]
@RunWith(SpringRunner.class)
@DataMongoTest
public class EmbeddedImageRepositoryTests {

	@Autowired
	ImageRepository repository;
	// end::1[]

	// tag::2[]
	@Before
	public void setUp() {
		repository
			.deleteAll()
			.then(() -> Mono.when(
				repository.save(
					new Image("1",
						"learning-spring-boot-cover.jpg"))
					.log("initDatabase"),
				repository.save(
					new Image("2",
						"learning-spring-boot-2nd-edition-cover.jpg"))
					.log("initDatabase"),
				repository.save(
					new Image("3",
						"bazinga.png"))
					.log("initDatabase")
			))
			.then(() -> repository.findAll().collectList())
			.then(images -> {
				images.forEach(image ->
					System.out.println(image.toString()));
				return Mono.empty();
			})
			.block(Duration.ofSeconds(30));
	}
	// end::2[]

	// tag::3[]
	@Test
	public void findAllShouldWork() {
		List<Image> images = repository.findAll().collectList().block();

		assertThat(images).hasSize(3);
		assertThat(images)
			.extracting(Image::getName)
			.contains(
				"learning-spring-boot-cover.jpg",
				"learning-spring-boot-2nd-edition-cover.jpg",
				"bazinga.png");
	}
	// end::3[]

	// tag::4[]
	@Test
	public void findByNameShouldWork() {
		Image image = repository.findByName("bazinga.png").block();

		assertThat(image.getName()).isEqualTo("bazinga.png");
		assertThat(image.getId()).isEqualTo("3");
	}
	// end::4[]

}
