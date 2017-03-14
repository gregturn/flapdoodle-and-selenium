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

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * @author Greg Turnquist
 */
@Component
public class InitDatabase implements CommandLineRunner {

	private final ImageRepository repository;

	public InitDatabase(ImageRepository repository) {
		this.repository = repository;
	}

	@Override
	public void run(String... args) throws Exception {
		// tag::log[]
		repository
			.deleteAll()
			.log("initDatabase")
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
				).log("initDatabase"))
			.log("initDatabase")
			.then(() -> repository.findAll().collectList())
			.log("initDatabase")
			.then(images -> {
				images.forEach(image ->
					System.out.println(image.toString()));
				return Mono.empty();
			})
			.log("initDatabase")
			.block(Duration.ofSeconds(30));
		// end::log[]
	}
}
