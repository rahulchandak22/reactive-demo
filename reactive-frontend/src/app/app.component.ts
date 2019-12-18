import { Component, OnInit } from '@angular/core';
import { MovieService } from './services/movie.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'reactive-frontend';
  movies = [];

  constructor(private service: MovieService){

  }

  ngOnInit() {
    this.getMovies();
  }
  getMovies() {
    this.service.getMovies().subscribe(event => {
      this.movies.push(JSON.parse(event.data));
    })
  }
  addMovie(movieName: string) {
    this.service.addMovie(movieName).subscribe(event => {
      console.log("movie added")
    })
  }
  
}
