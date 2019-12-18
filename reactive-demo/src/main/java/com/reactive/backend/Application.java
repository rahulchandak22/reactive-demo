package com.reactive.backend;

import com.reactive.backend.entities.Movie;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;



@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

@RestController
@RequestMapping("/")
class SampleMoviesCLR implements CommandLineRunner{

	private final MovieRepository movieRepository;

	@Autowired
	private MongoOperations mongoOperations;

	SampleMoviesCLR(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;
	}

	@GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Movie> getMovie(){
	  return movieRepository.findWithTailableCursorBy();
		//return movieRepository.findAll().delayElements(Duration.ofSeconds(1));
	}

	@PostMapping(value = "movie", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Movie> addMovie(@RequestBody Movie movie){
		return movieRepository.save(movie);
	}
	@Override
	public void run(String... args) throws Exception {

    MongoClient mongoClient = MongoClients.create("mongodb://localhost:63515");
    ReactiveMongoTemplate template = new ReactiveMongoTemplate(mongoClient, "local");
    String collectionName = template.getCollectionName(Movie.class);
    mongoOperations.createCollection(collectionName, CollectionOptions.empty().capped().size(9999999L).maxDocuments(100L));

    Flux.just("Les quatre cents coups", "La haine", "The Godfather", "The Departed ", "Eddie Murphy: Raw ",
				"Batman Begins ", "Gladiator", "Coming to America", "The Bellboy", "Rango", "Transformers", "Limitless")
				.map(title -> new Movie(UUID.randomUUID().toString(), title))
				.flatMap(movieRepository::save).subscribe();


		/*movieRepository.deleteAll()
				.thenMany(movieFlux).subscribe(System.out::println);*/
	}
}

interface MovieRepository extends ReactiveMongoRepository<Movie, String> {
  @Tailable
  Flux<Movie> findWithTailableCursorBy();
}

